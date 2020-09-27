package com.project.p2p.service;

import com.project.p2p.vo.InvestRankVo;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface RedisService {

    /**
     * 存String数据
     * @param key 键
     * @param time 时间
     * @param timeUnit 单位
     * @param value 值
     */
    void set(String key, Object value, int time, TimeUnit timeUnit);

    /**
     * 取String数据
     * @param key 键
     * @return 值
     */
    Object get(String key);

    /**
     * 存投资排行榜z-set数据
     * @param phone 电话号码
     * @param money 金额
     */
    void setInvestZset(String phone, Double money);

    /**
     *取投资排行榜前五名数据
     * @return 投资排行集合
     */
    List<InvestRankVo> getInvestZset();
}
