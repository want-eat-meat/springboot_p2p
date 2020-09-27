package com.project.p2p.mapper;

import com.project.p2p.pojo.BidInfo;
import com.project.p2p.vo.InvestRankVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BidInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BidInfo record);

    int insertSelective(BidInfo record);

    BidInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BidInfo record);

    int updateByPrimaryKey(BidInfo record);

    /**
     * 查询总成交额
     * @return 总金额
     */
    Double queryAllCountMoney();

    /**
     * 根据产品id获取投资信息
     * @param loanId 产品id
     * @return 投资人手机号：phone 投资金额：money 投资时间：time
     */
    List<BidInfo> selectListByPid(Integer loanId, Integer start, Integer count);

    /**
     * 根据产品id获取投资数量
     * @param pid 产品id
     * @return 投资数量
     */
    Integer selectCountByPid(Integer pid);
    /**
     * 根据产品id获取投资数量
     * @param uid 产品id
     * @return 投资数量
     */
    Integer selectCountByUid(Integer uid);

    /**
     * 根据用户id获取投资信息
     * @param uid 用户id
     * @param start 起始下标
     * @param count 个数
     * @return 投资信息
     */
    List<BidInfo> selectListByUid(Integer uid, Integer start, Integer count);

    /**
     * 获取投资金额列表
     * @return 投资金额集合
     */
    List<InvestRankVo> selectInvestList();

    /**
     * 根据产品id集合查询投资记录集合
     * @param loanIds 产品id集合
     * @return 投资记录集合
     */
    List<BidInfo> selectListByLoanIds(@Param("loanIds") List<Integer> loanIds);
}