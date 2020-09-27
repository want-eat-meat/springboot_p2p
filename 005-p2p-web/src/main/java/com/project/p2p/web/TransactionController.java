package com.project.p2p.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alipay.api.internal.util.AlipaySignature;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.project.p2p.constant.AliPayConstants;
import com.project.p2p.constant.MyConstants;
import com.project.p2p.pojo.FinanceAccount;
import com.project.p2p.pojo.RechargeRecord;
import com.project.p2p.pojo.User;
import com.project.p2p.service.AliPayService;
import com.project.p2p.service.FinanceServiceCustomer;
import com.project.p2p.service.RechargeServiceCustomer;
import com.project.p2p.service.WxPayService;
import com.project.p2p.utils.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping("transaction")
public class TransactionController {
    @Autowired
    private FinanceServiceCustomer financeServiceCustomer;
    @Autowired
    private RechargeServiceCustomer rechargeServiceCustomer;
    @Reference(interfaceClass = AliPayService.class, version = "1.0.0", check = false, retries = 2)
    private AliPayService aliPayService;
    @Reference(interfaceClass = WxPayService.class, version = "1.0.0", check = false, retries = 2)
    private WxPayService wxPayService;

    @RequestMapping("toRechargeBack")
    public String toRechargeBack(){
        return "toRechargeBack";
    }

    /**
     * 查询账户余额
     */
    @RequestMapping("/getMoney")
    public @ResponseBody
    Result getMoney(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute(MyConstants.SESSION_USER);
        FinanceAccount financeAccount = financeServiceCustomer.getFinanceByUid(user.getId());
        return Result.success(financeAccount == null ? 0 : financeAccount.getAvailableMoney());
    }

    /**
     *支付宝支付
     */
    @RequestMapping("aliPay")
    public @ResponseBody String aliPay(HttpServletRequest request, Double rechargeMoney){
        User user = (User) request.getSession().getAttribute(MyConstants.SESSION_USER);
        //创建订单信息
        RechargeRecord rechargeRecord = rechargeServiceCustomer.createRechargeRecord(user.getId(), rechargeMoney);
        //调用支付宝支付类
        return rechargeRecord == null ? "创建订单失败" : aliPayService.sendPayMessage(rechargeRecord);
    }

    /**
     * 支付宝同步通知页面
     */
    @RequestMapping("alipayReturnNotice")
    public String alipayReturnNotice(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        //获取返回的信息
        Map<String,String> params = new HashMap<>();
        Map<String,String[]> requestParams = request.getParameterMap();
        //读取参数
        getParameters(params, requestParams);
        //验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params, AliPayConstants.ALIPAY_PUBLIC_KEY, AliPayConstants.CHARSET, AliPayConstants.SIGNTYPE);
        //验签成功
        if(signVerified) {
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
            //付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");
            // 修改订单状态为支付成功
            RechargeRecord rechargeRecord = rechargeServiceCustomer.queryByRechargeNo(out_trade_no);
            //如果定时器修改成功
            if(StringUtils.equals(rechargeRecord.getRechargeStatus(), "1")){
                return "toRechargeBack";
            }
            //获取订单支付状态
            if(aliPayService.checkPayStatus(out_trade_no)) {
                //成功，修改充值状态为1(充值成功)
                rechargeRecord.setRechargeStatus("1");
                //修改充值记录状态
                rechargeServiceCustomer.modify(rechargeRecord);
                //修改账户余额
                financeServiceCustomer.addFinance(rechargeRecord.getUid(), Double.parseDouble(total_amount));
            }else{
                //失败，修改充值状态为2(充值失败)
                rechargeRecord.setRechargeStatus("2");
                //修改充值记录状态
                rechargeServiceCustomer.modify(rechargeRecord);
                model.addAttribute("trade_msg", "充值失败");
            }

        }else{
            model.addAttribute("trade_msg", "充值失败");
        }
        return "toRechargeBack";
    }

    /**
     * 从请求中的参数Map中读取数据
     */
    public void getParameters(Map<String, String> params, Map<String, String[]> requestParams) throws UnsupportedEncodingException {
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
    }

    /**
     * 微信支付
     */
    @RequestMapping("weixinPay")
    public String weixinPay(HttpServletRequest request, Double rechargeMoney, Model model){
        User user = (User) request.getSession().getAttribute(MyConstants.SESSION_USER);
        //创建订单信息
        RechargeRecord rechargeRecord = rechargeServiceCustomer.createRechargeRecord(user.getId(), rechargeMoney);
        model.addAttribute("rechargeRecord", rechargeRecord);
        return "wxPay";
    }

    @RequestMapping("code")
    public void wxCode(@RequestParam(value = "rechargeNo", required = true)String rechargeNo,
                       HttpServletRequest request,
                       HttpServletResponse response) throws Exception {
        RechargeRecord rechargeRecord = rechargeServiceCustomer.queryByRechargeNo(rechargeNo);
        Map<String, String> resultMap = (Map<String, String>) wxPayService.sendOrderRequest(rechargeRecord);
        if(!"SUCCESS".equals(resultMap.get("return_code"))){
            response.sendRedirect(request.getContextPath() + "/toRechargeBack");
        }
        if(!"SUCCESS".equals(resultMap.get("result_code"))){
            response.sendRedirect(request.getContextPath() + "/toRechargeBack");
        }
        String codeUrl = resultMap.get("code_url");
        //创建二维码
        BitMatrix bitMatrix = new MultiFormatWriter().encode(codeUrl, BarcodeFormat.QR_CODE, 300, 300);
        MatrixToImageWriter.writeToStream(bitMatrix, "jpg", response.getOutputStream());
        response.getOutputStream().close();
    }
}
