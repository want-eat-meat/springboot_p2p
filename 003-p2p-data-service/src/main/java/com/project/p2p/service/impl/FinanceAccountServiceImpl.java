package com.project.p2p.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.project.p2p.mapper.FinanceAccountMapper;
import com.project.p2p.pojo.FinanceAccount;
import com.project.p2p.service.FinanceAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service(interfaceClass = FinanceAccountService.class, version="1.0.0", retries = 2)
@Component
@Transactional
public class FinanceAccountServiceImpl implements FinanceAccountService {
    @Autowired
    private FinanceAccountMapper financeAccountMapper;
    @Override
    public FinanceAccount add(FinanceAccount financeAccount) {
        financeAccountMapper.insertSelective(financeAccount);
        return financeAccount;
    }

    @Override
    public FinanceAccount remove(Integer id) {
        return null;
    }

    @Override
    public FinanceAccount modify(FinanceAccount financeAccount) {
        financeAccountMapper.updateByPrimaryKeySelective(financeAccount);
        return financeAccount;
    }

    @Override
    public FinanceAccount queryOne(Integer id) {
        return null;
    }

    @Override
    public List<FinanceAccount> quertAll() {
        return null;
    }

    /**
     * 根据用户id获取账户信息
     */
    @Override
    public FinanceAccount queryByUid(Integer uid) {
        return financeAccountMapper.selectByUid(uid);
    }

    /**
     * 修改账户余额
     */
    @Override
    public void addFinance(Integer uid, Double money) {
        financeAccountMapper.updateFinanceByUid(uid, money);
    }
}
