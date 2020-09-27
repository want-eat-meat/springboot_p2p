package com.project.p2p.mapper;

import com.project.p2p.pojo.FinanceAccount;
import org.springframework.stereotype.Repository;

@Repository
public interface FinanceAccountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FinanceAccount record);

    int insertSelective(FinanceAccount record);

    FinanceAccount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FinanceAccount record);

    int updateByPrimaryKey(FinanceAccount record);

    /**
     * 根据用户id获取账户信息
     * @param uid 用户id
     * @return 账户信息
     */
    FinanceAccount selectByUid(Integer uid);

    /**
     * 修改账户余额
     * @param uid 用户id
     * @param money 金额
     */
    void updateFinanceByUid(Integer uid, Double money);
}