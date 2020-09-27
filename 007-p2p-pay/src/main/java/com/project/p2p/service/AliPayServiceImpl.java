package com.project.p2p.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.project.p2p.constant.AliPayConstants;
import com.project.p2p.pojo.RechargeRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Service(interfaceClass = AliPayService.class, version = "1.0.0", retries = 2)
@Component
public class AliPayServiceImpl implements AliPayService {

    private static AlipayClient alipayClient =  new DefaultAlipayClient(AliPayConstants.URL, AliPayConstants.APPID, AliPayConstants.RSA_PRIVATE_KEY, AliPayConstants.FORMAT, AliPayConstants.CHARSET, AliPayConstants.ALIPAY_PUBLIC_KEY, AliPayConstants.SIGNTYPE);

    /**
     * 发送支付信息到支付宝
     */
    @Override
    public String sendPayMessage(RechargeRecord rechargeRecord) {
        AlipayTradePagePayRequest alipayRequest =  new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl( AliPayConstants.return_url );
        alipayRequest.setNotifyUrl( AliPayConstants.notify_url );
        alipayRequest.setBizContent("{\"out_trade_no\":\""+ rechargeRecord.getRechargeNo() +"\","
                + "\"total_amount\":\""+ rechargeRecord.getRechargeMoney() +"\","
                + "\"subject\":\""+ rechargeRecord.getRechargeDesc() +"\","
                + "\"body\":\"账户充值\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        String form= "" ;
        try  {
            form = alipayClient.pageExecute(alipayRequest).getBody();
        }  catch  (AlipayApiException e) {
            e.printStackTrace();
        }
        return form;
    }

    /**
     *查询订单支付状态
     */
    @Override
    public Boolean checkPayStatus(String outTradeNo) {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();

        request.setBizContent("{\"out_trade_no\":\""+ outTradeNo +"\"}");
        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return false;
        }
        //结果判断
        if(!"10000".equals(response.getCode())){
            return false;
        }
        if(!"TRADE_SUCCESS".equals(response.getTradeStatus())){
            return false;
        }
        return true;
    }
}
