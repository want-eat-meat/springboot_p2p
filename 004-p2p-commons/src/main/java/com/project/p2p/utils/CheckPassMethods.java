package com.project.p2p.utils;

public class CheckPassMethods {
    private final static  String[] METHODS = {
            "index",
            "login",
            "loginPage",
            "handleCaptchaRequest",
            "showOptLoans",
            "listByTypeAndCount",
            "loanInfo",
            "getBidList",
            "register",
            "getCheckCode",
            "checkPhoneExist"
    };

    public static boolean check(String name){
        for(String method : METHODS){
            if(method.equals(name)){
                return true;
            }
        }
        return false;
    }
}
