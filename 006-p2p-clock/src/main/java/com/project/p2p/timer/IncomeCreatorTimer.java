package com.project.p2p.timer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.project.p2p.service.AliPayService;
import com.project.p2p.service.IncomeService;
import com.project.p2p.service.RechargeService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class IncomeCreatorTimer {
    @Reference(interfaceClass = IncomeService.class, version="1.0.0", check = false, retries = 2)
    private IncomeService incomeService;
    @Reference(interfaceClass = RechargeService.class, version="1.0.0", check = false, retries = 2)
    private RechargeService rechargeService;
    /**
     * 生成收益计划,每天10点执行一次
     */
    @Scheduled(cron="0 0 10 * * ?")
    public void createIncome(){
        System.out.println("收益生成计时器start");
        incomeService.createIncome();
        System.out.println("收益生成计时器over");
    }

    /**
     * 结算收益,每天10点执行一次
     */
    @Scheduled(cron = "0 0 10 * * ?")
    public void settleIncome(){
        System.out.println("收益结算计时器start");
        incomeService.settleIncome();
        System.out.println("收益结算计时器over");
    }
}
