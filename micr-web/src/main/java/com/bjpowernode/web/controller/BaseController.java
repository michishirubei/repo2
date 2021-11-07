package com.bjpowernode.web.controller;

import com.bjpowernode.service.bussiness.InvestService;
import com.bjpowernode.service.bussiness.ProductService;
import com.bjpowernode.service.bussiness.RechargeService;
import com.bjpowernode.service.pay.ExportKuaiQianService;
import com.bjpowernode.service.user.AccountService;
import com.bjpowernode.service.user.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

public class BaseController {

    //引用远程服务
    @DubboReference(interfaceClass = UserService.class,version = "1.0")
    protected UserService userService;

    @DubboReference(interfaceClass = InvestService.class,version = "1.0")
    protected InvestService investService;

    @DubboReference(interfaceClass = ProductService.class,version = "1.0")
    protected ProductService productService;

    //资金账户的服务接口
    @DubboReference(interfaceClass = AccountService.class,version = "1.0")
    protected AccountService accountService;

    //充值服务
    @DubboReference(interfaceClass = RechargeService.class,version = "1.0")
    protected RechargeService rechargeService;

    //pay服务中的快钱的接口
    @DubboReference(interfaceClass = ExportKuaiQianService.class,version = "1.0")
    protected ExportKuaiQianService kuaiQianService;


    @Resource
    protected StringRedisTemplate stringRedisTemplate;
}
