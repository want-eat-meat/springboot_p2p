package com.project.p2p.utils;

import com.project.p2p.constant.MyConstants;
import com.project.p2p.pojo.User;

import javax.servlet.http.Cookie;

public class CookieUtil {

    /**
     * 创建用户token并创建cookie
     * @param resultUser 用户信息
     * @return cookie
     */
    public static Cookie createTokenCookie(User resultUser) throws Exception {
        //1、获取token并存放到cookie中
        String token = TokenUtil.createToken(resultUser, MyConstants.LOGIN_STATE_TIME);
        //2、token存放到cookie中
        Cookie cookie = new Cookie(MyConstants.TOKEN, token);
        cookie.setMaxAge(MyConstants.LOGIN_STATE_TIME);
        cookie.setPath("/");
        return cookie;
    }

    public static Cookie removeTokenCookie(){
        Cookie cookie = new Cookie(MyConstants.TOKEN, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }
}
