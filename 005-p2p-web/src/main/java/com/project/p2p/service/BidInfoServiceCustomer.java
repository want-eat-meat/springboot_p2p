package com.project.p2p.service;

import com.project.p2p.pojo.BidInfo;
import com.project.p2p.pojo.User;
import com.project.p2p.utils.Page;
import com.project.p2p.vo.InvestRankVo;

import java.util.List;
import java.util.Map;

public interface BidInfoServiceCustomer {
    /**
     * 查询累计成交额
     * @return 总金额
     */
    Double queryAllCountMoney();

    /**
     * 根据产品id获取投资信息
     * @param id 产品id
     * @param  page 分页信息
     * @return 分页结果
     */
    Page queryListByPid(Integer id, Page page);

    /**
     * 根据用户id获取投资信息
     * @param id 用户id
     * @param page 分页信息
     * @return 产品列表
     */
    Page queryListByUid(Integer id, Page page);

    /**
     * 创建投资
     * @param user 用户id
     * @param loanId 产品id
     * @param money 投资金额
     */
    void invest(User user, Integer loanId, Double money);

    /**
     * 获取投资排行榜
     * @return 投资排行集合
     */
    List<InvestRankVo> getInvestRank();
}
