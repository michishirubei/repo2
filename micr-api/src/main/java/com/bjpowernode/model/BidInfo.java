package com.bjpowernode.model;

import com.bjpowernode.entity.BidEntity;

public class BidInfo extends BidEntity {
    private String phone;
    private String productName;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
