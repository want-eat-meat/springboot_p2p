package com.project.p2p.vo;

import java.io.Serializable;

public class InvestRankVo implements Serializable {

    private String phone;

    private Double money;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }
}
