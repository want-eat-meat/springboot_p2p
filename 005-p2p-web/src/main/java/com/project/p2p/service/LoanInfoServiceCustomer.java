package com.project.p2p.service;

import com.project.p2p.enums.LoanTypeEnum;
import com.project.p2p.pojo.LoanInfo;
import com.project.p2p.utils.Page;
import com.project.p2p.vo.InvestRankVo;

import java.util.List;

public interface LoanInfoServiceCustomer {
    /**
     * 查询历史年化收益率
     * @return 历史年化收益率
     */
    Double queryHistoryAvgRate();

    /**
     * 根据种类查询指定位置和条数的理财产品
     * @param start 开始位置 可以为null
     * @param count 条数 可以为和start一起为null
     * @param type 种类 0：新手宝 1：优选产品 2：散标产品 可以为null
     * @return 产品列表
     */
    List<LoanInfo> listByTypeAndCount(Integer start, Integer count, LoanTypeEnum type);

    /**
     * 根据产品类型分页查询理财产品
     * @param page 分页信息
     * @param loanTypeEnum 产品类型
     * @return 分页结果
     */
    Page listByPageAndType(Page page, LoanTypeEnum loanTypeEnum);

    /**
     * 根据id获取产品信息
     * @param id 产品id
     * @return 产品
     */
    LoanInfo queryById(int id);
}
