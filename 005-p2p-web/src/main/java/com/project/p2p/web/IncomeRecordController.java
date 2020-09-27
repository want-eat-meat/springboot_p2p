package com.project.p2p.web;

import com.project.p2p.service.IncomeServiceCustomer;
import com.project.p2p.utils.Page;
import com.project.p2p.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("income")
public class IncomeRecordController {

    @Autowired
    private IncomeServiceCustomer incomeServiceCustomer;
    @GetMapping
    public String income(){
        return "myIncome";
    }

    @GetMapping("/{start}/{uid}")
    public @ResponseBody Result listIncome(@PathVariable("start")Integer start,
                                           @PathVariable("uid") Integer uid) {
        Page page = new Page();
        page.setStart(start);
        page.setCount(5);
        page = incomeServiceCustomer.queryListByUid(uid, page);
        return Result.success(page);
    }

}
