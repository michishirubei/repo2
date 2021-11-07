package com.bjpowernode.vo;

import java.io.Serializable;

public class InvestTopBean implements Serializable {
    private static final long serialVersionUID = -69123141437411501L;
    private String phone;
    private Double money;

    public InvestTopBean(String phone, Double money) {
        this.phone = phone;
        this.money = money;
    }

    public InvestTopBean() {
    }

    public String getPhone() {
       return  phone.substring(0,3) + "******" + phone.substring(9);
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
