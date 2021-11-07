package com.bjpowernode.model;

import com.bjpowernode.entity.ProductEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class IndexData implements Serializable {
    private static final long serialVersionUID = 5491946301905957799L;

    private Integer registerUsers;
    private BigDecimal sumBidMoney;
    private BigDecimal avgRate;
    private List<ProductEntity> xinList;
    private List<ProductEntity> youList;
    private List<ProductEntity> sanList;


    public IndexData() {
    }

    public IndexData(Integer registerUsers,
                     BigDecimal sumBidMoney,
                     BigDecimal avgRate,
                     List<ProductEntity> xinList,
                     List<ProductEntity> youList,
                     List<ProductEntity> sanList) {
        this.registerUsers = registerUsers;
        this.sumBidMoney = sumBidMoney;
        this.avgRate = avgRate;
        this.xinList = xinList;
        this.youList = youList;
        this.sanList = sanList;
    }

    public Integer getRegisterUsers() {
        return registerUsers;
    }

    public void setRegisterUsers(Integer registerUsers) {
        this.registerUsers = registerUsers;
    }

    public BigDecimal getSumBidMoney() {
        return sumBidMoney;
    }

    public void setSumBidMoney(BigDecimal sumBidMoney) {
        this.sumBidMoney = sumBidMoney;
    }

    public BigDecimal getAvgRate() {
        return avgRate;
    }

    public void setAvgRate(BigDecimal avgRate) {
        this.avgRate = avgRate;
    }

    public List<ProductEntity> getXinList() {
        return xinList;
    }

    public void setXinList(List<ProductEntity> xinList) {
        this.xinList = xinList;
    }

    public List<ProductEntity> getYouList() {
        return youList;
    }

    public void setYouList(List<ProductEntity> youList) {
        this.youList = youList;
    }

    public List<ProductEntity> getSanList() {
        return sanList;
    }

    public void setSanList(List<ProductEntity> sanList) {
        this.sanList = sanList;
    }
}
