package com.project.p2p.service;

import com.project.p2p.pojo.FinanceAccount;

public interface FinanceAccountService extends BaseService<FinanceAccount> {
    /**
     * 根据用户id获取账户信息
     * @param uid 用户id
     * @return 账户信息
     */
    FinanceAccount queryByUid(Integer uid);

    /**
     * 修改账户余额
     * @param uid 用户id
     * @param money 金额
     */
    void addFinance(Integer uid, Double money);
}
