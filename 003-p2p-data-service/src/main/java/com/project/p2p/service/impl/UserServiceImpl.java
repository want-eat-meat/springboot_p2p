package com.project.p2p.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.project.p2p.constant.MyConstants;
import com.project.p2p.mapper.UserMapper;
import com.project.p2p.pojo.User;
import com.project.p2p.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service(interfaceClass = UserService.class, version="1.0.0", retries = 2)
@Component
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 查询用户个数
     */
    @Override
    public Long queryAllCount() {
        Long userNumber = (Long) redisTemplate.opsForValue().get(MyConstants.USER_NUMBER);
        if(!ObjectUtils.allNotNull(userNumber)){
            synchronized (this){
                userNumber = (Long) redisTemplate.opsForValue().get(MyConstants.USER_NUMBER);
                if(!ObjectUtils.allNotNull(userNumber)){
                    userNumber = userMapper.queryAllCount();
                    redisTemplate.opsForValue().set(MyConstants.USER_NUMBER, userNumber, 24, TimeUnit.HOURS);
                }
            }
        }
        return userNumber;
    }
    /**
     * 新增用户
     */
    @Override
    public User add(User user) {
        user.setAddTime(new Date());
        user.setLastLoginTime(user.getAddTime());
        userMapper.insertSelective(user);
        return user;
    }

    /**
     *根据用户信息查询用户
     */
    @Override
    public User queryOne(User user) {
        return userMapper.queryOne(user);
    }

    @Override
    public User remove(Integer id) {
        return null;
    }

    @Override
    public User modify(User user) {
        userMapper.updateByPrimaryKeySelective(user);
        return user;
    }

    @Override
    public User queryOne(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<User> quertAll() {
        return null;
    }
}
