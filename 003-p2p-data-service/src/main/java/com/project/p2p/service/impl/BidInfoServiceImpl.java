package com.project.p2p.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.project.p2p.constant.MyConstants;
import com.project.p2p.mapper.BidInfoMapper;
import com.project.p2p.pojo.BidInfo;
import com.project.p2p.service.BidInfoService;
import com.project.p2p.vo.InvestRankVo;
import javafx.scene.media.VideoTrack;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service(interfaceClass = BidInfoService.class, version = "1.0.0", retries = 2)
@Component
@Transactional
public class BidInfoServiceImpl implements BidInfoService {

    @Autowired
    private BidInfoMapper bidInfoMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询总成交额
     */
    @Override
    public Double queryAllCountMoney() {
        Double allCountMoney = (Double) redisTemplate.opsForValue().get(MyConstants.ALL_COUNT_MONEY);
        //如果redis中没有
        if(!ObjectUtils.allNotNull(allCountMoney)){
            synchronized (this){
                //再次确定redis中有没有
                allCountMoney = (Double) redisTemplate.opsForValue().get(MyConstants.ALL_COUNT_MONEY);
                if(!ObjectUtils.allNotNull(allCountMoney)){
                    allCountMoney = bidInfoMapper.queryAllCountMoney();
                    redisTemplate.opsForValue().set(MyConstants.ALL_COUNT_MONEY, allCountMoney, 24, TimeUnit.HOURS);
                }
            }
        }
        return allCountMoney;
    }

    /**
     * 根据产品id获取投资信息
     */
    @Override
    public List<BidInfo> queryListByPid(Integer loanId, Integer start, Integer count) {
        return bidInfoMapper.selectListByPid(loanId, start, count);
    }

    /**
     *根据产品id获取投资数量
     */
    @Override
    public Integer queryCountByPid(Integer pid) {
        return bidInfoMapper.selectCountByPid(pid);
    }

    /**
     *根据产品id获取投资数量
     */
    @Override
    public Integer queryCountByUid(Integer uid) {
        return bidInfoMapper.selectCountByUid(uid);
    }

    /**
     * 获取投资金额列表
     * @return 投资记录集合
     */
    @Override
    public List<InvestRankVo> getInvestList() {
        return bidInfoMapper.selectInvestList();
    }

    /**
     * 根据用户id获取投资信息
     */
    @Override
    public List<BidInfo> queryListByUid(Integer uid, Integer start, Integer count) {
        return bidInfoMapper.selectListByUid(uid, start, count);
    }

    @Override
    public BidInfo add(BidInfo bidInfo) {
        bidInfoMapper.insertSelective(bidInfo);
        return bidInfo;
    }

    @Override
    public BidInfo remove(Integer id) {
        return null;
    }

    @Override
    public BidInfo modify(BidInfo bidInfo) {
        return null;
    }

    @Override
    public BidInfo queryOne(Integer id) {
        return null;
    }

    @Override
    public List<BidInfo> quertAll() {
        return null;
    }
}
