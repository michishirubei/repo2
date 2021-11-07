package com.bjpowernode.web.controller;

import com.bjpowernode.model.IndexData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController extends BaseController {

    private Logger log = LoggerFactory.getLogger(IndexController.class);

    /*************首页******************/
    @GetMapping({"/index","/",""})
    public String index(Model model){
        log.debug("用户进入到首页");
        //调用远程服务，才能获取注册用户数量
        int registerUsers = userService.queryRegisterUsers();

        //累计成交金额
        //BigDecimal sumBidMoney = investService.querySumInvestMoney();

        //收益率平均值
        //BigDecimal avgRate = productService.queryAvgRate();

        //查询新手宝一个产品
        //List<ProductEntity> xinProductList = productService
        //        .queryProductListPage(YLBConsts.PRODUCT_TYPE_XINSHOUBAO,1,1);


        //查询优选类产品
        //List<ProductEntity> youProductList = productService
        //        .queryProductListPage(YLBConsts.PRODUCT_TYPE_YOUXUAN,1,4);

        //散标类产品
        //List<ProductEntity> sanProductList = productService
        //        .queryProductListPage(YLBConsts.PRODUCT_TYPE_SANBIAO,1,8);

        //调用一次提供者，获取首页的6项数据
        IndexData indexData = productService.queryIndexData();
        //保存数据
        model.addAttribute("avgRate",indexData.getAvgRate());
        model.addAttribute("sumBidMoney",indexData.getSumBidMoney());
        model.addAttribute("registerUsers",indexData.getRegisterUsers());

        //产品
        model.addAttribute("xinProductList",indexData.getXinList());
        model.addAttribute("youProductList",indexData.getYouList());
        model.addAttribute("sanProductList",indexData.getSanList());

        log.debug("首页数据信息|"+ indexData.getAvgRate()+"|"+indexData.getSumBidMoney()+"|"+indexData.getRegisterUsers() );

        return "index";
    }
}
