package com.bjpowernode.pay.controller;

import com.bjpowernode.consts.YLBConsts;
import com.bjpowernode.entity.RechargeEntity;
import com.bjpowernode.model.RechargeResult;
import com.bjpowernode.pay.service.KuaiQianService;
import com.bjpowernode.service.bussiness.RechargeService;
import com.bjpowernode.service.pay.ExportKuaiQianService;
import com.bjpowernode.util.YLBUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

@Controller
public class KuaiQianController {

    @Resource
    private KuaiQianService kuaiQianService;

    @DubboReference(interfaceClass = RechargeService.class,version = "1.0")
    private RechargeService rechargeService;

    /*********接收来自web服务的请求*****************/
    @GetMapping("/kq/recvweb")
    public String receiveFromWeb(String userId,
                                 String phone,
                                 String orderId,
                                 @RequestParam("rechargeMoney") String yuan,
                                 Model model){

        System.out.println("/kq/recvweb:"+userId+","+phone+","+orderId+","+yuan);
        if(StringUtils.isAnyBlank( userId,phone,orderId,yuan)){
            model.addAttribute("msg","请求收据有误");
            return "err";
        } else if(!YLBUtil.checkPhone(phone)){
            model.addAttribute("msg","手机号格式不正确");
            return "err";
        } else if( Double.parseDouble(yuan) < 0 ) {
            model.addAttribute("msg","金额不正确");
            return "err";
        } else {
            try{
                //获取数据
                Map<String,String> map = kuaiQianService.getFormData(userId,phone,orderId,yuan);

                //创建充值记录
                RechargeEntity entity = new RechargeEntity();
                entity.setChannel("kq");
                entity.setRechargeDesc("支付充值");
                entity.setRechargeMoney(new BigDecimal(yuan));
                entity.setRechargeNo(orderId);
                entity.setRechargeStatus(YLBConsts.RECHAGE_STATUS_PROCESSING);
                entity.setRechargeTime(new Date());
                entity.setUid(Integer.parseInt(userId));
                rechargeService.addRecharge(entity);

                model.addAllAttributes(map);
            }catch (Exception e){
                e.printStackTrace();
                model.addAttribute("msg","业务处理有错误");
                return "err";
            }


        }

        return "kqForm";
    }

    /*************接收快钱异步通知******************/
    @GetMapping("/kq/notify")
    @ResponseBody
    public String handlerKuaiQianNotify(HttpServletRequest request){

        String name = "";
        StringBuffer buffer = new StringBuffer("");
        Enumeration<String> parameterNames = request.getParameterNames();
        while( parameterNames.hasMoreElements()){
            name = parameterNames.nextElement();
            buffer.append(name).append("=").append(request.getParameter(name)).append("&");
        }
        System.out.println("buffer="+buffer.toString());
        RechargeResult result = kuaiQianService.handlerKqNotify(request);
        //不需要快钱重复发送异步通知， 所以接收到通知后，返回result为1,
        //redirecturl需要使用外网地址
        return "<result>1</result><redirecturl>http://localhost:8000/ylb/user/page/mycenter</redirecturl>";
    }


    @Resource
    private ExportKuaiQianService service;
    @GetMapping("/test/query")
    @ResponseBody
    public String testQuery(String orderId){
        service.queryOrderInfo(orderId);
        return "Ok";

    }
}
