package com.project.p2p.aop;

import com.alibaba.dubbo.config.annotation.Reference;
import com.project.p2p.constant.MyConstants;
import com.project.p2p.pojo.User;
import com.project.p2p.service.RedisService;
import com.project.p2p.utils.CookieUtil;
import com.project.p2p.utils.TokenUtil;
import com.project.p2p.utils.CheckPassMethods;
import com.project.p2p.utils.Result;
import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class LoginCheckAop {

    @Reference(interfaceClass = RedisService.class, version="1.0.0", check = false, retries = 2)
    private RedisService redisService;

    @Pointcut("execution(public * com.project.p2p.web.*.*(..)))")
    public void webPointCut(){

    }

    @Around("webPointCut()")
    public Object checkToken(ProceedingJoinPoint joinPoint) throws Throwable{
        //获取方法参数
        Object[] args = joinPoint.getArgs();

        //获取request和response对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response= attributes.getResponse();

        //获取token
        String token = getToken(request.getCookies());
        //判断token是否为空
        if(token == null){
            String name = ((MethodSignature)joinPoint.getSignature()).getMethod().getName();
            //判断是否不需要登录
            if(CheckPassMethods.check(name)){
                return joinPoint.proceed(args);
            }else{
                request.getSession().removeAttribute(MyConstants.SESSION_USER);
                return turnLogin(joinPoint, request);
            }
        }

        //解析token,超时返回登录界面
        User user;
        try{
            user = TokenUtil.verifyUser(token);
        }catch (RuntimeException e){
            return turnLogin(joinPoint, request);
        }

        //获取redis中的用户对象,并判断是否存在
        User redisUser = (User) redisService.get(MyConstants.SESSION_USER + user.getId());
        if (!ObjectUtils.allNotNull(redisUser)) {
            //移除token
            response.addCookie(CookieUtil.removeTokenCookie());
            return turnLogin(joinPoint, request);
        }
        //信息是否一致
        if(!user.getId().equals(redisUser.getId())
                || !user.getPhone().equals(redisUser.getPhone())){
            return turnLogin(joinPoint, request);
        }
        //向session中添加user数据，用于方法使用
        request.getSession().setAttribute("User", redisUser);
        //token正常,执行方法
        Object result = joinPoint.proceed(args);
        //更新信息
        //1、再次获取redis中的用户信息
        User newUser = (User)redisService.get(MyConstants.SESSION_USER + user.getId());
        if (ObjectUtils.allNotNull(newUser)) {
            //2、获取token，并存入cookie中
            response.addCookie(CookieUtil.createTokenCookie(newUser));
            //3、user放回redis中
            redisService.set(MyConstants.SESSION_USER + user.getId(), newUser, 30, TimeUnit.MINUTES);
            //4、user放入session中
            request.getSession().setAttribute("User", newUser);
        }
        return result;
    }
    /**
     * 从cookie中获取token
     * @param cookies cookie数组
     * @return token字符串
     */
    private String getToken(Cookie[] cookies){
        if(cookies == null){
            return null;
        }
        for(Cookie cookie : cookies){
            if(MyConstants.TOKEN.equals(cookie.getName())){
                return cookie.getValue();
            }
        }
        return  null;
    }

    /**
     * 跳转登录界面
     * @param point 切点方法
     * @return 登录请求或错误结果
     */
    private Object turnLogin(ProceedingJoinPoint point, HttpServletRequest request){
        //获取signature 该注解作用在方法上，强转为 MethodSignature
        MethodSignature signature = (MethodSignature) point.getSignature();
        //获取返回值类型
        String typeName =  signature.getReturnType().getTypeName();
        if(typeName.contains("String")){
            String returnURL = request.getRequestURL() + "?" + request.getQueryString();
            return "redirect:/user/login?returnURL=" + returnURL;
        }else{
            return Result.fail(401, "未登录或登录状态过期");
        }
    }
}
