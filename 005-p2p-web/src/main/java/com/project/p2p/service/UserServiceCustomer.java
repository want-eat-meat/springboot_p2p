package com.project.p2p.service;

import com.project.p2p.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserServiceCustomer {
    /**
     * 查询所有用户的个数
     * @return 用户总数
     */
    Long queryAllCount();

    /**
     * 根据手机号获取验证码
     * @param phone 手机号
     */
    void getCheckCode(String phone);

    /**
     * 注册
     * @param phone 手机号
     * @param password 密码
     * @param code 注册验证码
     * @return 添加的用户
     */
    User register(String phone, String password, String code);

    /**
     * 根据用户信息查询用户
     * @param user 用户对象
     * @return 用户对象
     */
    User checkUser(User user, String code, String captcha);

    /**
     * 实名认证
     * @param user 用户信息
     * @param code 客户端验证码
     * @return 修改后用户信息
     */
    User identity(User user, String code);

    /**
     * 修改用户信息
     * @param user 新的用户信息
     * @return 修改后用户信息
     */
    User modify(User user);

    /**
     * 退出
     * @param uid 用户编号
     */
    void logout(Integer uid);

    /**
     * 判断手机号是否存在
     * @param phone 手机号
     */
    void checkPhoneExist(String phone);
}
