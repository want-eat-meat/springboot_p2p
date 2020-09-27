package com.project.p2p.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.project.p2p.mapper.RechargeRecordMapper;
import com.project.p2p.pojo.RechargeRecord;
import com.project.p2p.service.AliPayService;
import com.project.p2p.service.FinanceAccountService;
import com.project.p2p.service.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service(interfaceClass = RechargeService.class, version="1.0.0", retries = 2)
@Component
@Transactional
public class RechargeServiceImpl implements RechargeService {

    @Autowired
    private RechargeRecordMapper rechargeRecordMapper;
    @Reference(interfaceClass = AliPayService.class, version = "1.0.0", check = false, retries = 2)
    private AliPayService aliPayService;
    @Autowired
    FinanceAccountService financeAccountService;


    /**
     * 根据用户id分页查询充值记录
     */
    @Override
    public List<RechargeRecord> queryListByUid(Integer uid, Integer start, Integer count) {
        return rechargeRecordMapper.selectListByUid(uid, start, count);
    }

    /**
     *根据用户id查询总记录数
     */
    @Override
    public Integer queryCountAllByUid(Integer uid) {
        return rechargeRecordMapper.selectCountAllByUid(uid);
    }

    /**
     *根据充值订单号查询充值订单
     */
    @Override
    public RechargeRecord queryByRechargeNo(String rechargeNo) {
        return rechargeRecordMapper.queryByRechargeNo(rechargeNo);
    }

    /**
     * 根据充值状态查询充值订单
     */
    @Override
    public List<RechargeRecord> queryListByStatus(String status) {
        return rechargeRecordMapper.selectListByStatus(status);
    }

    @Override
    public RechargeRecord add(RechargeRecord rechargeRecord) {
        int result = rechargeRecordMapper.insertSelective(rechargeRecord);
        return result == 1 ? rechargeRecord : null;
    }

    @Override
    public RechargeRecord remove(Integer id) {
        return null;
    }

    @Override
    public RechargeRecord modify(RechargeRecord rechargeRecord) {
        rechargeRecordMapper.updateByPrimaryKeySelective(rechargeRecord);
        return rechargeRecord;
    }

    @Override
    public RechargeRecord queryOne(Integer id) {
        return null;
    }

    @Override
    public List<RechargeRecord> quertAll() {
        return null;
    }
}
