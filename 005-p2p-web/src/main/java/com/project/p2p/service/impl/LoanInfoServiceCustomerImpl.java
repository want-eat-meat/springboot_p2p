package com.project.p2p.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.project.p2p.enums.LoanTypeEnum;
import com.project.p2p.pojo.LoanInfo;
import com.project.p2p.service.LoanInfoService;
import com.project.p2p.service.LoanInfoServiceCustomer;
import com.project.p2p.service.RedisService;
import com.project.p2p.utils.Page;
import com.project.p2p.vo.InvestRankVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanInfoServiceCustomerImpl implements LoanInfoServiceCustomer {

    @Reference(interfaceClass = LoanInfoService.class, version = "1.0.0", check = false, retries = 2)
    private LoanInfoService loanInfoService;

    /**
     * 查询历史年化收益率
     */
    @Override
    public Double queryHistoryAvgRate() {
        Double rate = loanInfoService.queryHistoryAvgRate();
        return Math.round(rate * 100) / 100d;
    }

    /**
     * 根据种类查询指定位置和条数的理财产品
     */
    @Override
    public List<LoanInfo> listByTypeAndCount(Integer start, Integer count, LoanTypeEnum type) {
        return loanInfoService.listByTypeAndCount(start, count, type);
    }

    /**
     * 根据类型分页查询理财产品
     */
    @Override
    public Page listByPageAndType(Page page, LoanTypeEnum loanTypeEnum) {
        //根据分页信息获取产品列表
        List<LoanInfo> loanInfos = loanInfoService.listByTypeAndCount(page.getStart(), page.getCount(), loanTypeEnum);
        page.setData(loanInfos);
        //获取数据总条数
        Integer total = loanInfoService.queryAllCountByType(loanTypeEnum);
        page.setTotal(total);
        return page;
    }

    /**
     * 根据id查询
     */
    @Override
    public LoanInfo queryById(int id) {
       return loanInfoService.queryOne(id);

    }

}
