package com.project.p2p.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.project.p2p.exception.ResultException;
import com.project.p2p.pojo.BidInfo;
import com.project.p2p.pojo.FinanceAccount;
import com.project.p2p.pojo.LoanInfo;
import com.project.p2p.pojo.User;
import com.project.p2p.service.*;
import com.project.p2p.utils.Page;
import com.project.p2p.vo.InvestRankVo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BidInfoServiceCustomerImpl implements BidInfoServiceCustomer {
    @Reference(interfaceClass = BidInfoService.class, version = "1.0.0", retries = 2, check = false)
    private BidInfoService bidInfoService;
    @Reference(interfaceClass = UserService.class, version = "1.0.0", retries = 2, check = false)
    private UserService userService;
    @Reference(interfaceClass = LoanInfoService.class, version = "1.0.0", retries = 2, check = false)
    private LoanInfoService loanInfoService;
    @Reference(interfaceClass = FinanceAccountService.class, version = "1.0.0", retries = 2, check = false)
    private FinanceAccountService financeAccountService;
    @Reference(interfaceClass = RedisService.class, version = "1.0.0", retries = 2, check = false)
    private RedisService redisService;


    //查询总成交额
    @Override
    public Double queryAllCountMoney() {
        return bidInfoService.queryAllCountMoney();
    }

    /**
     * 根据产品id获取投资信息
     */
    @Override
    public Page queryListByPid(Integer id, Page page) {
        //获取分页数据
        List<BidInfo> bidInfos = bidInfoService.queryListByPid(id, page.getStart(), page.getCount());
        if(bidInfos.size() != 0) {
            page.setData(bidInfos);
        }else{
            page.setData(null);
        }
        //获取总数量
        Integer total = bidInfoService.queryCountByPid(id);
        page.setTotal(total);
        //替换手机号中间四位为*号
        for(BidInfo bidInfo : bidInfos){
            String phone =  bidInfo.getUser().getPhone();
            phone = phone.substring(0, 3) + "******" + phone.substring(9);
            bidInfo.getUser().setPhone(phone);
        }
        return page;
    }
    /**
     *根据用户id获取投资信息
     */
    @Override
    public Page queryListByUid(Integer id, Page page) {
        List<BidInfo> bidInfos = bidInfoService.queryListByUid(id, page.getStart(), page.getCount());
        page.setData(bidInfos);
        Integer total = bidInfoService.queryCountByUid(id);
        page.setTotal(total);
        return page;
    }

    /**
     * 创建投资
     */
    @Override
    public void invest(User user, Integer loanId, Double money) {
        //获取用户余额，判断余额是否充足
        FinanceAccount financeAccount = financeAccountService.queryByUid(user.getId());
        if(financeAccount == null || financeAccount.getAvailableMoney() < money){
            throw new ResultException("账户余额不足");
        }
        //获取产品信息
        LoanInfo loanInfo = loanInfoService.queryOne(loanId);
        //判断投资金额是否在产品允许范围内
        if(money % 100 != 0){
            throw new ResultException("投资金额需为100的倍数");
        }
        if(money < loanInfo.getBidMinLimit() || money > loanInfo.getLeftProductMoney() || money > loanInfo.getBidMaxLimit()){
            throw new ResultException("投资金额超出范围");
        }
        //判断投资完后产品状态是否变化
        loanInfo.setLeftProductMoney(loanInfo.getLeftProductMoney() - money);
        if(loanInfo.getLeftProductMoney() == 0) {
            //如果变化，修改产品信息中的状态和投资满额时间
            loanInfo.setProductStatus(1);
            loanInfo.setProductFullTime(new Date());
        }
        String result = loanInfoService.submitInvest(loanInfo, user.getId(), money);
        if("fail".equals(result)){
            throw new ResultException("投资失败");
        }
        //更新redis中的投资信息
        redisService.setInvestZset(user.getPhone(), money);
    }


    /**
     * 获取投资排行列表
     */
    @Override
    public List<InvestRankVo> getInvestRank() {
        List<InvestRankVo> investZset = redisService.getInvestZset();
        if(investZset.size() == 0){
            synchronized (this) {
                investZset = redisService.getInvestZset();
                if(investZset.size() == 0) {
                    List<InvestRankVo> investRankVos = bidInfoService.getInvestList();
                    for (InvestRankVo investRankVo : investRankVos) {
                        redisService.setInvestZset(investRankVo.getPhone(), investRankVo.getMoney());
                    }
                    investZset = redisService.getInvestZset();
                }
            }

        }
        for(InvestRankVo investRankVo : investZset){
            String phone = investRankVo.getPhone();
            investRankVo.setPhone(phone.substring(0, 3) + "******" + phone.substring(9));
        }
        return investZset;
    }
}
