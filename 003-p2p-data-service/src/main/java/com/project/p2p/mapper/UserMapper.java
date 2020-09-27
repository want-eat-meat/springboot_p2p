package com.project.p2p.mapper;

import com.project.p2p.pojo.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 查询用户个数
     * @return 用户个数
     */
    Long queryAllCount();

    /**
     * 根据用户信息获取用户
     * @param user 用户信息
     * @return 用户对象
     */
    User queryOne(User user);
}