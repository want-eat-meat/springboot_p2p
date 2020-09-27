package com.project.p2p.service;

import com.project.p2p.pojo.IncomeRecord;

import java.util.List;

public interface IncomeService extends BaseService {
    /**
     * 根据用户id分页查询收入信息
     * @param uid 用户id
     * @param start 开始下标
     * @param count 分页结果
     * @return 分页结果
     */
    List<IncomeRecord> queryListByUid(Integer uid, Integer start, Integer count);

    /**
     * 根据用户id查询收入数
     * @param uid 用户id
     * @return 收入数
     */
    Integer queryCountByUid(Integer uid);

    /**
     * 创建收益计划
     */
    void createIncome();

    /**
     * 结算收益
     */
    void settleIncome();
}
