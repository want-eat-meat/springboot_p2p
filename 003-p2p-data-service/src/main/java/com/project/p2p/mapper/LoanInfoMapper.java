package com.project.p2p.mapper;

import com.project.p2p.pojo.LoanInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LoanInfo record);

    int insertSelective(LoanInfo record);

    LoanInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LoanInfo record);

    int updateByPrimaryKey(LoanInfo record);

    /**
     * 查询历史年化收益率
     * @return 收益率
     */
    Double selectHistoryAvgRate();
    /**
     * 根据种类查询指定位置和条数的理财产品
     * @param start 开始位置 可以为null
     * @param count 条数 可以为和start一起为null
     * @param type 种类 0：新手宝 1：优选产品 2：散标产品 可以为null
     * @return
     */
    List<LoanInfo> selectByTypeAndCount(Integer start, Integer count, Integer type);

    /**
     * 查询该类型下所有数据条数
     * @param type 产品类型
     * @return 产品条数
     */
    Integer selectAllCountByType(Integer type);

    /**
     * 乐观锁修改产品信息
     * @param loanInfo 产品信息
     * @return 受影响条数
     */
    Integer updateByPrimaryKeyLimitVersion(LoanInfo loanInfo);

    /**
     * 根据产品状态获取产品列表
     * @param status 产品状态
     * @return 产品列表
     */
    List<LoanInfo> selectListByStatus(Integer status);

    /**
     * 根据主键集合修改状态
     * @param loanIds 注件集合
     */
    void updateStatusByPrimaryKeys(@Param("loanIds") List<Integer> loanIds, Integer status);
}