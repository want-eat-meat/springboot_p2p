package com.project.p2p.timer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.project.p2p.pojo.RechargeRecord;
import com.project.p2p.service.AliPayService;
import com.project.p2p.service.FinanceAccountService;
import com.project.p2p.service.RechargeService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RechargeCheckTimer {

    @Reference(interfaceClass = AliPayService.class, version = "1.0.0", check = false, retries = 2)
    private AliPayService aliPayService;

    @Reference(interfaceClass = RechargeService.class, version = "1.0.0", check = false, retries = 2)
    private RechargeService rechargeService;

    @Reference(interfaceClass = FinanceAccountService.class, version = "1.0.0", check = false, retries = 2)
    private FinanceAccountService financeAccountService;


    @Scheduled(cron="0 0/30 * * * ?")
    public void checkRecharge(){
        System.out.println("充值订单检查计时器start");
        //查询所有状态为0的订单
        List<RechargeRecord> rechargeRecords = rechargeService.queryListByStatus("0");
        if(rechargeRecords == null || rechargeRecords.size() == 0){
            return;
        }
        for(RechargeRecord rechargeRecord : rechargeRecords){
            if(aliPayService.checkPayStatus(rechargeRecord.getRechargeNo())){
                //成功，修改充值状态为1(充值成功)
                rechargeRecord.setRechargeStatus("1");
                //修改充值记录状态
                rechargeService.modify(rechargeRecord);
                //修改账户余额
                financeAccountService.addFinance(rechargeRecord.getUid(), rechargeRecord.getRechargeMoney());
            }
        }
        System.out.println("充值订单检查计时器over");
    }
}
