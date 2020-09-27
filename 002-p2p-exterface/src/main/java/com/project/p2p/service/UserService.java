package com.project.p2p.service;

import com.project.p2p.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserService extends BaseService<User>{
    /**
     * 查询所有用户的个数
     * @return 用户总数
     */
    Long queryAllCount();

    /**
     * 根据用户信息查询用户
     * @param user 用户信息
     * @return 用户对象
     */
    User queryOne(User user);
}
