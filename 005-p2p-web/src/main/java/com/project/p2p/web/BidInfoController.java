package com.project.p2p.web;

import com.project.p2p.constant.MyConstants;
import com.project.p2p.pojo.User;
import com.project.p2p.service.BidInfoServiceCustomer;
import com.project.p2p.utils.Page;
import com.project.p2p.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
@RequestMapping("bidinfo")
public class BidInfoController {
    @Autowired
    private BidInfoServiceCustomer bidInfoServiceCustomer;

    @GetMapping()
    public String invest(){
        return "myInvest";
    }

    @GetMapping("/{start}/{uid}")
    public @ResponseBody
    Result listIncome(@PathVariable("start")Integer start,
                      @PathVariable("uid") Integer uid) {
        Page page = new Page();
        page.setStart(start);
        page.setCount(5);
        page = bidInfoServiceCustomer.queryListByUid(uid, page);
        return Result.success(page);
    }

    /**
     *投资
     */
    @PostMapping("/{loanId}/{money}")
    public @ResponseBody Result invest(@PathVariable("loanId") Integer loanId,
                         @PathVariable("money") Double money,
                         HttpServletRequest request){
        //获取用户
        User user = (User) request.getSession().getAttribute(MyConstants.SESSION_USER);
        if(user.getIdCard() == null){
            return Result.fail(1001, "请先完成实名认证");
        }
        bidInfoServiceCustomer.invest(user, loanId, money);
        return Result.success();
    }
}
