package com.project.p2p.service;

import com.project.p2p.pojo.RechargeRecord;

public interface WxPayService {
    /**
     * 调用统一下单API
     * @param rechargeRecord 订单信息
     * @return 调用结果
     */
    Object sendOrderRequest(RechargeRecord rechargeRecord) throws Exception;
}
