package com.project.p2p.web;

import com.project.p2p.constant.MyConstants;
import com.project.p2p.pojo.User;
import com.project.p2p.service.BidInfoServiceCustomer;
import com.project.p2p.service.UserServiceCustomer;
import com.project.p2p.utils.CookieUtil;
import com.project.p2p.utils.FastDFSClient;
import com.project.p2p.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserServiceCustomer userServiceCustomer;
    @Autowired
    private BidInfoServiceCustomer bidInfoServiceCustomer;
    @Value("${filePath}")
    private String filePath;

    /**
     * 发送手机验证码
     * @param phone 手机号
     */
    @GetMapping("/register/{phone}")
    public @ResponseBody Result getCheckCode(@PathVariable("phone") String phone, HttpServletRequest request){
        //根据手机号获取验证码
        userServiceCustomer.checkPhoneExist(phone);
        userServiceCustomer.getCheckCode(phone);
        return Result.success();
    }

    /**
     * 注册
     * @param phone 手机号
     * @param password 密码
     * @param code 验证码
     */
    @PutMapping("/register/{phone}/{password}/{code}")
    public @ResponseBody Result register(@PathVariable("phone")String phone,
                                        @PathVariable("password") String password,
                                        @PathVariable("code") String code,
                                         HttpServletRequest request,
                                         HttpServletResponse response) throws Exception {
        //添加用户
        User user = userServiceCustomer.register(phone, password, code);
        //将用户放入session域中保存
        //更新信息
        response.addCookie(CookieUtil.createTokenCookie(user));
        request.getSession().setAttribute(MyConstants.SESSION_USER, user);
        return Result.success();
    }

    /**
     *登录
     */
    @PostMapping("/login/{code}")
    public @ResponseBody Result login(@RequestBody User user,
                                      @PathVariable("code")String code,
                                      @RequestParam(required = false) String returnURL,
                                      HttpServletRequest request,  HttpServletResponse response) throws Throwable {
        //获取图片验证码
        String captcha = (String) request.getSession().getAttribute(MyConstants.CAPTCHA);
        //登录
        User resultUser = userServiceCustomer.checkUser(user, code, captcha);
        //更新信息
        response.addCookie(CookieUtil.createTokenCookie(resultUser));
        request.getSession().setAttribute(MyConstants.SESSION_USER, resultUser);
        return Result.success(returnURL);
    }

    /**
     *实名认证
     */
    @PostMapping("/identity/{code}")
    public @ResponseBody Result identity(@PathVariable("code")String code,
                                         @RequestBody User user){
        userServiceCustomer.identity(user, code);
        return Result.success();
    }

    /**
     *设置头像
     */
    @RequestMapping("/setHeadImg")
    public @ResponseBody Result setHeadImg(MultipartFile file, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute(MyConstants.SESSION_USER);
        try {
            FastDFSClient fastDFSClient = new FastDFSClient("fastdfs.conf");
            //获取文件后缀名
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            //保存到文件系统中
            String path = fastDFSClient.uploadFile(file.getBytes(), suffix);
            //保存到数据库中
            user.setHeaderImage(filePath + path);
            userServiceCustomer.modify(user);
        } catch (Exception e) {
            return Result.fail(500, "头像上传失败");
        }
        return Result.success();
    }

    /**
     * 跳转登录界面
     */
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String loginPage(@RequestParam(required = false) String returnURL, Model model, HttpServletRequest request){
        request.getSession().removeAttribute(MyConstants.SESSION_USER);
        //查询平台用户数
        Long allUserCount = userServiceCustomer.queryAllCount();
        model.addAttribute("allUserCount", allUserCount);
        //查询累计交易额
        Double allCountMoney = bidInfoServiceCustomer.queryAllCountMoney();
        model.addAttribute("allCountMoney", allCountMoney);
        //返回路径
        model.addAttribute("returnURL", returnURL);
        return "login";
    }

    /**
     * 跳转注册页面
     */
    @RequestMapping("register")
    public String register(){
        return "register";
    }

    /**
     * 跳转实名验证界面
     */
    @RequestMapping("/realName")
    public String realName(){
        return "realName";
    }

    /**
     * 退出登录
     */
    @RequestMapping("/logout/{uid}")
    public String logout(@PathVariable("uid") Integer uid, HttpServletRequest request, HttpServletResponse response){
        userServiceCustomer.logout(uid);
        Cookie cookie = CookieUtil.removeTokenCookie();
        //移除cookie和session中的信息
        response.addCookie(cookie);
        request.getSession().removeAttribute(MyConstants.SESSION_USER);
        return "redirect:/";
    }

    /**
     * 跳转上传头像界面
     */
    @RequestMapping("/uploadHeader")
    public String uploadHeader(){
        return "headImg";
    }

}
