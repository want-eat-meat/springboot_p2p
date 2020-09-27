package com.project.p2p.service;

import com.project.p2p.enums.LoanTypeEnum;
import com.project.p2p.pojo.FinanceAccount;
import com.project.p2p.pojo.LoanInfo;

import java.util.List;

public interface LoanInfoService extends BaseService<LoanInfo>{
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
     * 查询该类型下所有数据条数
     * @param type 产品类型
     * @return 产品条数
     */
    Integer queryAllCountByType(LoanTypeEnum type);

    /**
     *提交投资
     * @param  loanInfo 产品信息
     * @param uid 用户id
     * @param money 金额
     * @return 结果
     */
    String submitInvest(LoanInfo loanInfo, Integer uid, Double money);
}
