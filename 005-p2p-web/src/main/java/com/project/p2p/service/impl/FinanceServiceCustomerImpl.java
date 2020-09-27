package com.project.p2p.service.impl;


import com.alibaba.dubbo.config.annotation.Reference;
import com.project.p2p.pojo.FinanceAccount;
import com.project.p2p.service.FinanceAccountService;
import com.project.p2p.service.FinanceServiceCustomer;
import org.springframework.stereotype.Service;

@Service
public class FinanceServiceCustomerImpl implements FinanceServiceCustomer {

    @Reference(interfaceClass = FinanceAccountService.class, version="1.0.0", check = false, retries = 2)
    private FinanceAccountService financeAccountService;
    /**
     * 根据用户id获取账户信息
     */
    @Override
    public FinanceAccount getFinanceByUid(Integer uid) {
        return financeAccountService.queryByUid(uid);
    }

    /**
     * 修改账户余额
     */
    @Override
    public void addFinance(Integer uid, Double money) {
        //先判断是否有账户

        //没有创建账户

        //修改
        financeAccountService.addFinance(uid, money);
    }
}
