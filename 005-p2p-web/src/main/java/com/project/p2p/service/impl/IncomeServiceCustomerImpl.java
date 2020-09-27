package com.project.p2p.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.project.p2p.pojo.IncomeRecord;
import com.project.p2p.service.IncomeService;
import com.project.p2p.service.IncomeServiceCustomer;
import com.project.p2p.utils.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncomeServiceCustomerImpl implements IncomeServiceCustomer {
    @Reference(interfaceClass = IncomeService.class, version = "1.0.0", retries = 2)
    private IncomeService incomeService;

    /**
     * 根据用户id分页查询收入信息
     */
    @Override
    public Page queryListByUid(Integer uid, Page page) {
        List<IncomeRecord> incomeRecords = incomeService.queryListByUid(uid, page.getStart(), page.getCount());
        page.setData(incomeRecords);
        Integer total = incomeService.queryCountByUid(uid);
        page.setTotal(total);
        return page;
    }
}
