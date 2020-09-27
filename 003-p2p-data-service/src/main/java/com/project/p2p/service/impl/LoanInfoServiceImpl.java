package com.project.p2p.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.project.p2p.constant.MyConstants;
import com.project.p2p.enums.LoanTypeEnum;
import com.project.p2p.mapper.BidInfoMapper;
import com.project.p2p.mapper.FinanceAccountMapper;
import com.project.p2p.mapper.LoanInfoMapper;
import com.project.p2p.pojo.BidInfo;
import com.project.p2p.pojo.FinanceAccount;
import com.project.p2p.pojo.LoanInfo;
import com.project.p2p.service.LoanInfoService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service(interfaceClass = LoanInfoService.class, version = "1.0.0", retries = 2)
@Component
@Transactional
public class LoanInfoServiceImpl implements LoanInfoService {

    @Autowired
    private LoanInfoMapper loanInfoMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private FinanceAccountMapper financeAccountMapper;
    @Autowired
    private BidInfoMapper bidInfoMapper;

    /**
     * 查询历史年化收益率
     */
    @Override
    public Double queryHistoryAvgRate() {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        Double rate = (Double) redisTemplate.opsForValue().get(MyConstants.HISTORY_AVG_RATE);
        //如果redis中没有
        if(!ObjectUtils.allNotNull(rate)){
            synchronized (this){
                //再次确定redis中有没有
                rate = (Double) redisTemplate.opsForValue().get(MyConstants.HISTORY_AVG_RATE);
                if(!ObjectUtils.allNotNull(rate)){
                    rate = loanInfoMapper.selectHistoryAvgRate();
                    redisTemplate.opsForValue().set(MyConstants.HISTORY_AVG_RATE, rate, 24, TimeUnit.HOURS);
                }
            }
        }
        return rate;
    }

    /**
     * 根据种类查询指定位置和数量的理财产品
     */
    @Override
    public List<LoanInfo> listByTypeAndCount(Integer start, Integer count, LoanTypeEnum type) {
        if(type == null){
            return loanInfoMapper.selectByTypeAndCount(start, count, null);
        }
        return loanInfoMapper.selectByTypeAndCount(start, count, type.getType());
    }

    /**
     * 查询该类型下所有数据条数
     * @return 产品条数
     */
    @Override
    public Integer queryAllCountByType(LoanTypeEnum type) {
        if(type == null){
            return loanInfoMapper.selectAllCountByType(null);
        }
        return loanInfoMapper.selectAllCountByType(type.getType());
    }

    /**
     * 提交投资
     * @param  loanInfo 产品信息
     * @param uid 用户id
     * @param money 金额
     */
    @Override
    public String submitInvest(LoanInfo loanInfo, Integer uid, Double money) {
        //修改产品信息
        Integer result = loanInfoMapper.updateByPrimaryKeyLimitVersion(loanInfo);
        if(result == 0){
            return "fail";
        }
        //扣减用户余额
        financeAccountMapper.updateFinanceByUid(uid, money);
        //添加投资记录
        BidInfo bidInfo = new BidInfo();
        bidInfo.setUid(uid);
        bidInfo.setLoanId(loanInfo.getId());
        bidInfo.setBidMoney(Double.parseDouble("" + money));
        bidInfo.setBidStatus(1);
        bidInfo.setBidTime(new Date());
        bidInfoMapper.insertSelective(bidInfo);
        return "success";
    }

    @Override
    public LoanInfo add(LoanInfo loanInfo) {
        return null;
    }

    @Override
    public LoanInfo remove(Integer id) {
        return null;
    }

    @Override
    public LoanInfo modify(LoanInfo loanInfo) {
        loanInfoMapper.updateByPrimaryKeySelective(loanInfo);
        return loanInfo;
    }

    @Override
    public LoanInfo queryOne(Integer id) {
        return loanInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<LoanInfo> quertAll() {
        return null;
    }
}
