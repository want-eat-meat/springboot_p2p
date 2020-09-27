package com.project.p2p.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.project.p2p.constant.MyConstants;
import com.project.p2p.service.RedisService;
import com.project.p2p.vo.InvestRankVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service(interfaceClass = RedisService.class, version="1.0.0", retries = 2)
@Component
public class RedisServiceImpl implements RedisService {
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 存String数据
     */
    @Override
    public void set(String key, Object value, int time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    /**
     * 取String数据
     */
    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 存投资排行榜z-set数据
     */
    @Override
    public void setInvestZset(String phone, Double money) {
        redisTemplate.opsForZSet().incrementScore(MyConstants.INVEST_RANK, phone, money);
    }

    /**
     *取投资排行榜前五名数据
     */
    @Override
    public List<InvestRankVo> getInvestZset() {
        List<InvestRankVo> investRankVos = new ArrayList<>();
        Set<ZSetOperations.TypedTuple> set = redisTemplate.opsForZSet().reverseRangeWithScores(MyConstants.INVEST_RANK, 0, 4);
        Iterator<ZSetOperations.TypedTuple> iterator = set.iterator();
        while(iterator.hasNext()){
            InvestRankVo investRankVo = new InvestRankVo();
            ZSetOperations.TypedTuple next = iterator.next();
            investRankVo.setPhone(next.getValue().toString());
            investRankVo.setMoney(next.getScore());
            investRankVos.add(investRankVo);
        }
        return investRankVos;
    }
}
