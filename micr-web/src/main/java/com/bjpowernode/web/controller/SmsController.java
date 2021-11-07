package com.bjpowernode.web.controller;

import com.bjpowernode.consts.YLBConsts;
import com.bjpowernode.util.YLBUtil;
import com.bjpowernode.vo.Result;
import com.bjpowernode.web.service.SmsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class SmsController extends BaseController{

    @Resource
    private SmsService smsService;

    /********接收发送短信的请求 ***************/
    @GetMapping("/sms/sendCode")
    public Result receRequestSendSmsCode(@RequestParam("phone") String phone){
        Result<String> result  = Result.fail();

        if(YLBUtil.checkPhone(phone)){
            boolean send = smsService.sendSms(YLBConsts.ACTION_REGIST,phone);
            if( send ){
                result = Result.ok();
            }
        }
        return result;
    }
}
