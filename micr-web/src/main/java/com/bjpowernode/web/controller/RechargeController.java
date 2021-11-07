package com.bjpowernode.web.controller;

import com.bjpowernode.consts.YLBConsts;
import com.bjpowernode.consts.YLBKey;
import com.bjpowernode.entity.RechargeEntity;
import com.bjpowernode.entity.UserEntity;
import com.bjpowernode.model.RechargeResult;
import com.bjpowernode.util.YLBUtil;
import com.bjpowernode.vo.Page;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class RechargeController extends BaseController {

    @Value("${payservice.kuaiqian.url}")
    private String payServiceKuaiQianUrl;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**********进入到充值主页面****************/
    @GetMapping("/recharge/page/index")
    public String rechagePageIndex(Model model){
        //生成充值的流水号，订单号orderId
        //orderId: 使用时间戳 + 唯一自增值
        String orderId = generateOrderId();
        //存储orderId到redis
        stringRedisTemplate.opsForValue().set(orderId,orderId);
        model.addAttribute("orderId",orderId);
        model.addAttribute("kqUrl",payServiceKuaiQianUrl);
        return "toRecharge";
    }

    //确定充值的结果
    //action="finish" | "error" | "close"
    @GetMapping("/recharge/result")
    public String rechargeResult(String action, String orderId,Model model){
        System.out.println("/recharge/result:"+action+","+orderId);

        String forward = "";
        //1.查询自己的数据库
        RechargeEntity recharge = rechargeService.queryRechargeByRechargeNo(orderId);
        if( recharge != null ){
            Integer status  = recharge.getRechargeStatus();
            if( status == YLBConsts.RECHAGE_STATUS_SUCCESS ){
                //充值成功
                forward = "forward:/user/page/mycenter";
            } else if( status == YLBConsts.RECHAGE_STATUS_FAILURE) {
                //充值失败，等其他情况
                forward = "forward:/recharge/page/index";
            } else {
                //需要调用快钱的查询接口，才能确定充值结果
                RechargeResult result = kuaiQianService.queryOrderInfo(orderId);
                if( result.getCode() == 30000 ){
                    //用户充值成功
                    forward = "forward:/user/page/mycenter";
                } else {
                    //充值失败，等其他情况
                    forward = "forward:/recharge/page/index";
                }
            }
        }
        return forward;
    }


    /**********进入到更多充值页面****************/
    @GetMapping("/recharge/more/list")
    public String moreRecharge(Integer pageNo,HttpSession session, Model model){

        UserEntity user = (UserEntity) session.getAttribute(YLBConsts.SESSION_USER);
        //查询充值记录
        pageNo  = YLBUtil.pageNo(pageNo);
        List<RechargeEntity> rechargeList  = rechargeService.queryRechageListByUserId(
                                 user.getId(),pageNo,YLBConsts.PAGE_SIZE_MYCENTER);

        //记录数量
        int totalRecords = rechargeService.queryRechargeRecordNumByUserId(user.getId());
        Page page = new Page(pageNo,YLBConsts.PAGE_SIZE_MYCENTER,totalRecords);


        model.addAttribute("rechargeList",rechargeList);
        model.addAttribute("page",page);

        return "myRecharge";

    }


    //生成orderId  //orderId: 使用时间戳 + 唯一自增值
    private String generateOrderId(){
       return  "KQ"+DateFormatUtils.format(new Date(),"yyyyMMddHHmmssSSS")
                +stringRedisTemplate.opsForValue().increment(YLBKey.KQ_ORDERID_SEQ);
    }
}
