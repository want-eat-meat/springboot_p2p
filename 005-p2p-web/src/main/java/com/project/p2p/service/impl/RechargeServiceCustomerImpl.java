package com.project.p2p.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.project.p2p.pojo.RechargeRecord;
import com.project.p2p.service.RechargeService;
import com.project.p2p.service.RechargeServiceCustomer;
import com.project.p2p.utils.Page;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class RechargeServiceCustomerImpl implements RechargeServiceCustomer {

    @Reference(interfaceClass = RechargeService.class, version = "1.0.0", check = false, retries = 2)
    private RechargeService rechargeService;

    /**
     * 根据用户id分页查询充值记录
     */
    @Override
    public Page queryListByUid(Integer uid, Page page) {
        List<RechargeRecord> rechargeRecords = rechargeService.queryListByUid(uid, page.getStart(), page.getCount());
        page.setData(rechargeRecords);
        Integer total = rechargeService.queryCountAllByUid(uid);
        page.setTotal(total);
        return page;
    }

    /**
     * 创建订单
     */
    @Override
    public RechargeRecord createRechargeRecord(Integer uid, Double money) {
        //创建订单信息
        RechargeRecord rechargeRecord = new RechargeRecord();
        rechargeRecord.setUid(uid);
        rechargeRecord.setRechargeMoney(money);
        rechargeRecord.setRechargeDesc("账户充值");
        rechargeRecord.setRechargeTime(new Date());
        rechargeRecord.setRechargeStatus("0");
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssSSS");
        String rechargeNo = format.format(new Date());
        rechargeRecord.setRechargeNo(rechargeNo + uid);
        return rechargeService.add(rechargeRecord);
    }

    /**
     * 根据订单号获取订单信息
     */
    @Override
    public RechargeRecord queryByRechargeNo(String rechargeNo) {
        return rechargeService.queryByRechargeNo(rechargeNo);
    }

    @Override
    public void modify(RechargeRecord rechargeRecord) {
        rechargeService.modify(rechargeRecord);
    }
}
