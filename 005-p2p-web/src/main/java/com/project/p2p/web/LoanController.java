package com.project.p2p.web;

import com.project.p2p.constant.MyConstants;
import com.project.p2p.enums.LoanTypeEnum;
import com.project.p2p.pojo.BidInfo;
import com.project.p2p.pojo.FinanceAccount;
import com.project.p2p.pojo.LoanInfo;
import com.project.p2p.pojo.User;
import com.project.p2p.service.BidInfoServiceCustomer;
import com.project.p2p.service.FinanceServiceCustomer;
import com.project.p2p.service.LoanInfoServiceCustomer;
import com.project.p2p.utils.Page;
import com.project.p2p.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/loan")
public class LoanController {

    @Autowired
    LoanInfoServiceCustomer loanInfoServiceCustomer;
    @Autowired
    BidInfoServiceCustomer bidInfoServiceCustomer;
    @Autowired
    FinanceServiceCustomer financeServiceCustomer;


    @GetMapping("/{start}/{type}")
    public @ResponseBody Result listByTypeAndCount(@PathVariable("start") Integer start, @PathVariable("type")Integer type, HttpServletRequest request){
        Integer totalPage = (Integer) request.getSession().getAttribute(MyConstants.TOTAL_PAGE);
        if(totalPage == null){
            totalPage = 0;
        }
        Page page = new Page();
        if(start >= 0 || start <= totalPage) {
            page.setStart(start);
            page = loanInfoServiceCustomer.listByPageAndType(page, LoanTypeEnum.createLoanTypeEnum(type));
            request.getSession().setAttribute(MyConstants.TOTAL_PAGE, page.getTotalPage());
        }
        return Result.success(page);
    }

    /**
     * 跳转产品详情页
     */
    @RequestMapping("/{id}")
    public String loanInfo(@PathVariable("id") Integer id, Model model, HttpServletRequest request){
        //根据id获取产品信息
        LoanInfo loanInfo = loanInfoServiceCustomer.queryById(id);
        model.addAttribute("loanInfo", loanInfo);
        //根据用户id查询余额
        User user = (User) request.getSession().getAttribute(MyConstants.SESSION_USER);
        if(user != null){
           FinanceAccount finance = financeServiceCustomer.getFinanceByUid(user.getId());
           model.addAttribute("money", finance == null ? 0 : finance.getAvailableMoney());
       }
        return "loanInfo";
    }

    /**
     *查询产品投资列表
     */
    @GetMapping("/bid/{pid}/{start}")
    public @ResponseBody Result getBidList(@PathVariable("pid") int pid,
                             @PathVariable("start") int start){
        Page page = new Page();
        page.setStart(start);
        page = bidInfoServiceCustomer.queryListByPid(pid, page);
        return Result.success(page);
    }
}
