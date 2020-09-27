package com.project.p2p.service;

import com.project.p2p.pojo.RechargeRecord;

public interface AliPayService {
    /**
     * 发送支付信息到支付宝
     * @param rechargeRecord 订单信息
     * @return 支付页面
     */
    String sendPayMessage(RechargeRecord rechargeRecord);

    /**
     * 检查订单支付状态
     * @param outTradeNo 订单号
     */
    Boolean checkPayStatus(String outTradeNo);

}
