package com.project.p2p.service;

import com.project.p2p.pojo.RechargeRecord;
import com.project.p2p.utils.Page;

public interface RechargeServiceCustomer {
    /**
     * 根据用户id分页查询充值记录
     * @param uid 用户id
     * @param page 分页信息
     * @return 分页结果
     */
    Page queryListByUid(Integer uid, Page page);
    /**
     * 创建订单
     * @param uid 用户id
     * @param money 金额
     * @return 订单
     */
    RechargeRecord createRechargeRecord(Integer uid, Double money);

    /**
     * 根据订单号获取订单信息
     * @param rechargeNo 订单号
     * @return 订单信息
     */
    RechargeRecord queryByRechargeNo(String rechargeNo);

    /**
     * 修改订单信息
     * @param rechargeRecord 订单信息
     */
    void modify(RechargeRecord rechargeRecord);
}
