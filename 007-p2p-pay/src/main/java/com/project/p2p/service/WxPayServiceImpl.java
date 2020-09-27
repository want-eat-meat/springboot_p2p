package com.project.p2p.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.project.p2p.pojo.RechargeRecord;
import com.project.p2p.utils.HttpClientUtils;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service(interfaceClass = WxPayService.class, version = "1.0.0", retries = 2)
@Component
public class WxPayServiceImpl implements WxPayService {

    @Override
    public Object sendOrderRequest(RechargeRecord rechargeRecord) throws Exception {
        //设置参数
        Map<String, String> map = new HashMap<>();
        map.put("appid", "wx8a3fcf509313fd74");
        map.put("mch_id", "1361137902");
        map.put("nonce_str", WXPayUtil.generateNonceStr());
        map.put("body", rechargeRecord.getRechargeDesc());
        map.put("out_trade_no", rechargeRecord.getRechargeNo());
        BigDecimal decimal = new BigDecimal(rechargeRecord.getRechargeMoney());
        String  money= decimal.multiply(new BigDecimal(100)).toString();
        map.put("total_fee", money);
        map.put("spbill_create_ip", "127.0.0.1");
        map.put("notify_url", "http://localhost:9090/pay/wxpay");
        map.put("trade_type", "NATIVE");
        map.put("product_id", rechargeRecord.getRechargeDesc());
        map.put("sign", WXPayUtil.generateSignature(map, "367151c5fd0d50f1e34a68a802d6bbca"));
        //转化为xml类型数据
        String data = WXPayUtil.mapToXml(map);
        //发送httpclient请求
        String result = HttpClientUtils.doPostByXml("https://api.mch.weixin.qq.com/pay/unifiedorder", data);
        return WXPayUtil.xmlToMap(result);
    }
}
