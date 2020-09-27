package com.project.p2p.service;

import com.project.p2p.pojo.BidInfo;
import com.project.p2p.vo.InvestRankVo;

import java.util.List;
import java.util.Map;

public interface BidInfoService extends BaseService<BidInfo> {

    /**
     * 查询累计成交额
     * @return 总金额
     */
    Double queryAllCountMoney();

    /**
     * 根据产品id获取投资信息
     * @param loanId 产品id
     * @param start 起始下标
     * @param count 个数
     * @return 投资信息
     */
    List<BidInfo> queryListByPid(Integer loanId, Integer start, Integer count);

    /**
     * 根据产品id获取投资数量
     * @param pid 产品id
     * @return 投资数量
     */
    Integer queryCountByPid(Integer pid);

    /**
     * 根据用户id获取投资信息
     * @param uid 用户id
     * @param start 起始下标
     * @param count 个数
     * @return 投资信息
     */
    List<BidInfo> queryListByUid(Integer uid, Integer start, Integer count);

    /**
     * 根据用户id获取投资数量
     * @param uid 用户id
     * @return 投资数量
     */
    Integer queryCountByUid(Integer uid);

    /**
     * 获取投资金额列表
     * @return 投资金额集合
     */
    List<InvestRankVo> getInvestList();
}
