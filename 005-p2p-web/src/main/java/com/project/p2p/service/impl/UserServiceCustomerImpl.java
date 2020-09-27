package com.project.p2p.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.project.p2p.constant.MyConstants;
import com.project.p2p.exception.ResultException;
import com.project.p2p.pojo.FinanceAccount;
import com.project.p2p.pojo.User;
import com.project.p2p.service.FinanceAccountService;
import com.project.p2p.service.RedisService;
import com.project.p2p.service.UserService;
import com.project.p2p.service.UserServiceCustomer;
import com.project.p2p.utils.MD5Util;
import com.project.p2p.utils.PhoneCheckUtil;
import jdk.internal.dynalink.support.RuntimeContextLinkRequestImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceCustomerImpl implements UserServiceCustomer {
    /**
     * 正则表达式
     */
    private static Pattern PHONE_PATTERN = Pattern.compile("^1[3456789]\\d{9}$");
    private static Pattern PASSWORD_PATTERN = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$");
    private static Pattern CODE_PATTERN = Pattern.compile("^[0-9]{6}$");
    private static Pattern NAME_PATTERN = Pattern.compile("^[\\u4E00-\\u9FA5\\uf900-\\ufa2d·s]{2,20}$");
    private static Pattern IDCARD_PATTERN = Pattern.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$");
    private static Pattern CAPTCHA_PATTERN = Pattern.compile("^[0-9A-Za-z]{4}$");

    @Reference(interfaceClass = UserService.class, version="1.0.0", check = false, retries = 2)
    private UserService userService;
    @Reference(interfaceClass = FinanceAccountService.class, version = "1.0.0", check = false, retries = 2)
    private FinanceAccountService financeAccountService;
    @Reference(interfaceClass = RedisService.class, version="1.0.0", check = false, retries = 2)
    private RedisService redisService;

    /**
     * 查询用户个数
     */
    @Override
    public Long queryAllCount() {
        return userService.queryAllCount();
    }

    /**
     * 判断手机号是否存在
     */
    @Override
    public void checkPhoneExist(String phone) {
        User user = new User();
        user.setPhone(phone);
        user = userService.queryOne(user);
        if(user != null){
            throw new ResultException(1001, "手机号已被注册");
        }
    }

    /**
     * 根据手机号获取验证码
     */
    @Override
    public void getCheckCode(String phone) {
        //验证手机号格式
        Matcher matcher = PHONE_PATTERN.matcher(phone);
        if(!matcher.matches()){
            throw new ResultException(1001, "请输入正确的手机号");
        }
        try {
            //获取验证码
            //String result = PhoneCheckUtil.SendCode(phone);
            String result = sendCode(phone);
            //结果处理
            Map<String, Object> resultMap = (Map<String, Object>) JSONObject.parse(result);
            if(!resultMap.get("code").equals("200")){
                throw new ResultException("发送失败");
            }
            String code = (String) resultMap.get("obj");
            System.out.println(code);
            //验证码存入redis中
            redisService.set(phone, code, 5, TimeUnit.MINUTES);
        } catch (Exception e) {
            throw new ResultException(1003, "发送失败，请检查手机号");
        }
    }

    /**
     * 注册
     */
    @Override
    public User register(String phone, String password, String code) {
        //redis中获取验证码
        String checkCode = (String) redisService.get(phone);
        //验证码是否未空
        Matcher matcher = CODE_PATTERN.matcher(code);
        if(!matcher.matches()){
            throw new ResultException(1003, "验证码为6位数字");
        }
        //配对验证码是否为空
        if(!ObjectUtils.allNotNull(checkCode)){
            throw new ResultException(1003, "验证码已过期或未发送验证码");
        }
        //验证码是否正确
        if(!code.equals(checkCode)){
            throw new ResultException(1003, "验证码不正确");
        }
        //判断手机号格式
        Matcher phoneMatcher = PHONE_PATTERN.matcher(phone);
        if(!phoneMatcher.matches()){
            throw new ResultException(1001, "请输入正确的手机号");
        }
        //判断密码格式
        Matcher pwdMatcher = PASSWORD_PATTERN.matcher(password);
        if(!pwdMatcher.matches()){
            throw new ResultException(1002, "密码必须由数字和字母组成");
        }
        //创建用户
        User newUser = new User();
        newUser.setPhone(phone);

        //判断手机号是否存在
        User user = userService.queryOne(newUser);
        if(user != null){
            throw new ResultException(1001, "手机号已被注册");
        }
        //使用md5对密码加密
        newUser.setLoginPassword(MD5Util.getMD5(password));
        //添加最近登录时间
        newUser.setLastLoginTime(new Date());
        //添加用户
        newUser = userService.add(newUser);
        //创建账户
        FinanceAccount financeAccount = new FinanceAccount();
        financeAccount.setUid(newUser.getId());
        financeAccount.setAvailableMoney(888d);
        financeAccountService.add(financeAccount);
        //将用户信息存储到redis中
        redisService.set(MyConstants.SESSION_USER + newUser.getId(), newUser, 30, TimeUnit.MINUTES);
        return newUser;
    }

    /**
     *登录验证
     */
    @Override
    public User checkUser(User user, String code, String captcha) {
        //检查手机号和密码是否为空
        if(!ObjectUtils.allNotNull(user.getPhone(), user.getLoginPassword())){
            throw new ResultException("手机号或密码为空");
        }
        //检查验证码是否为空
        if(code == null || "".equals(code)){
            throw new ResultException("验证码为空");
        }
        //检查验证码是否正确
        if(!captcha.equalsIgnoreCase(code)){
            throw new ResultException("验证码不正确");
        }
        //检查是否存在
        User resultUser = userService.queryOne(user);
        if(resultUser == null){
            throw new ResultException("手机号或密码错误");
        }
        //添加最近登录时间
        resultUser.setLastLoginTime(new Date());
        //修改用户信息
        new Thread(()->{
            userService.modify(resultUser);
        }).start();
        //将用户信息存储到redis中
        redisService.set(MyConstants.SESSION_USER + resultUser.getId(), resultUser, 30, TimeUnit.MINUTES);
        return resultUser;
    }

    /**
     *实名验证
     */
    @Override
    public User identity(User user, String code) {
        //获取redis中的验证码
        String baseCode = (String) redisService.get(user.getPhone());
        //判断验证码格式
        Matcher matcher = CODE_PATTERN.matcher(code);
        if(!matcher.matches()){
            throw new ResultException(1003, "验证码格式错误");
        }
        if(baseCode == null){
            throw new ResultException(1003, "未发送验证码");
        }
        //判断验证码是否正确
        if(!code.equalsIgnoreCase(baseCode)){
            throw new ResultException(1003, "验证码错误");
        }
        //判断姓名格式是否正确
        matcher = NAME_PATTERN.matcher(user.getName());
        if(!matcher.matches()){
            throw new ResultException(1001, "姓名格式错误");
        }
        //判断身份证号格式是否正确
        matcher =IDCARD_PATTERN.matcher(user.getIdCard());
        if(!matcher.matches()){
            throw new ResultException(1002, "身份证号格式错误");
        }
        //判断身份证是否存在
        User checkUser = new User();
        checkUser.setIdCard(user.getIdCard());
        User result = userService.queryOne(checkUser);
        if(result != null){
            throw new ResultException(1002, "身份证号已被使用");
        }
        //修改登录时间
        user.setLastLoginTime(new Date());
        //修改用户信息
        user = userService.modify(user);
        //将用户信息存储到redis中
        redisService.set(MyConstants.SESSION_USER + user.getId(), user, 30, TimeUnit.MINUTES);
        return user;
    }

    @Override
    public User modify(User user) {
        user = userService.modify(user);
        //将用户信息存储到redis中
        redisService.set(MyConstants.SESSION_USER + user.getId(), user, 30, TimeUnit.MINUTES);
        return user;
    }

    /**
     * 模拟短信发送
     */
    private String sendCode(String phone){
        StringBuilder code = new StringBuilder(6);
        for(int i = 0; i < 6; i++){
            code.append((int)(Math.random() * 10));
        }
        return "{\"code\":\"200\",\"msg\":\"success\",\"obj\":\"" + code +"\"}";

    }

    /**
     * 退出
     */
    @Override
    public void logout(Integer uid) {
        redisService.set(MyConstants.SESSION_USER + uid, null, 1, TimeUnit.SECONDS);
    }
}
