package com.project.p2p.web;

import com.project.p2p.enums.LoanTypeEnum;
import com.project.p2p.pojo.*;
import com.project.p2p.service.*;
import com.project.p2p.utils.Page;
import com.project.p2p.vo.InvestRankVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author zhanhxin
 */
@Controller
public class IndexController {

    @Autowired
    private LoanInfoServiceCustomer loanInfoService;
    @Autowired
    private UserServiceCustomer userService;
    @Autowired
    private BidInfoServiceCustomer bidInfoService;
    @Autowired
    private RechargeServiceCustomer rechargeService;
    @Autowired
    private IncomeServiceCustomer incomeService;
    @Autowired
    private FinanceServiceCustomer financeServiceCustomer;

    /**
     * 跳转首页
     */
    @RequestMapping
    public String index(Model model){
        //查询历史年化收益率
        Double historyAvgRate = loanInfoService.queryHistoryAvgRate();
        model.addAttribute("historyAvgRate", historyAvgRate);
        //查询平台用户数
        Long allUserCount = userService.queryAllCount();
        model.addAttribute("allUserCount", allUserCount);
        //查询累计交易额
        Double allCountMoney = bidInfoService.queryAllCountMoney();
        model.addAttribute("allCountMoney", allCountMoney);
        //查询最新的一条新手宝
        List<LoanInfo> noviceLoans = loanInfoService.listByTypeAndCount(0, 1, LoanTypeEnum.NOVICE);
        model.addAttribute("noviceLoans", noviceLoans);
        //查询最新的四条优选产品
        List<LoanInfo> optLoans = loanInfoService.listByTypeAndCount(0, 4, LoanTypeEnum.OPTIMIZATION);
        model.addAttribute("optLoans", optLoans);
        //查询最新的八条散标产品
        List<LoanInfo> scatterLoans = loanInfoService.listByTypeAndCount(0, 8, LoanTypeEnum.SCATTER);
        model.addAttribute("scatterLoans", scatterLoans);
        return "index";
    }

    /**
     * 跳转产品列表页
     * @param pType 产品类型
     */
    @RequestMapping("loan")
    public String showOptLoans(@RequestParam(value = "ptype",required = false) Integer pType, Model model){
        model.addAttribute("pType", pType);
        List<InvestRankVo> investRankVos = bidInfoService.getInvestRank();
        model.addAttribute("investRank", investRankVos);
        return "loan";
    }

    /**
     *跳转个人中心页
     */
    @RequestMapping("/myCenter/{id}")
    public String myCenter(@PathVariable("id") Integer id, Model model){
        Page page = new Page();
        page.setStart(0);
        page.setCount(5);
        //查询账户余额
        FinanceAccount financeByUid = financeServiceCustomer.getFinanceByUid(id);
        model.addAttribute("money", financeByUid == null ? 0 : financeByUid.getAvailableMoney());
        //查询最近的5条投资
        List<BidInfo> bidInfos = (List<BidInfo>) bidInfoService.queryListByUid(id, page).getData();
        model.addAttribute("bidInfos", bidInfos);
        //查询最近的5条充值
        List<RechargeRecord> rechargeRecords = (List<RechargeRecord>) rechargeService.queryListByUid(id, page).getData();
        model.addAttribute("rechargeRecords", rechargeRecords);
        //查询最近的5条收益
        List<IncomeRecord> incomeRecords = (List<IncomeRecord>) incomeService.queryListByUid(id, page).getData();
        model.addAttribute("incomeRecords", incomeRecords);
        return "myCenter";
    }

    /**
     * 跳转充值页面
     */
    @RequestMapping("/toRecharge")
    public String toRecharge(){
        return "/toRecharge";
    }
}
