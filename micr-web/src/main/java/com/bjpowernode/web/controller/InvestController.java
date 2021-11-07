package com.bjpowernode.web.controller;

import com.bjpowernode.consts.YLBConsts;
import com.bjpowernode.consts.YLBKey;
import com.bjpowernode.entity.UserEntity;
import com.bjpowernode.enums.CodeEnum;
import com.bjpowernode.model.BidInfo;
import com.bjpowernode.model.InvestResult;
import com.bjpowernode.util.YLBUtil;
import com.bjpowernode.vo.Page;
import com.bjpowernode.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

//有关投资的操作
@Controller
@RequestMapping("/invest")
public class InvestController extends BaseController {

    private Logger log = LoggerFactory.getLogger(InvestController.class);

    //@PostMapping("/product")
    @RequestMapping("/product")
    @ResponseBody
    public Result investProduct(Integer productId ,
                                BigDecimal bidMoney,
                                HttpSession session){

        log.debug("用户投资|"+ productId+"|"+bidMoney);
        StringBuilder builder = new StringBuilder("");
        Result<String> result = Result.fail();

        if( productId == null || productId < 1 ){
            result.setCodeEnum(CodeEnum.PARAMETER_BLANK_ERROR);
        } else if( bidMoney == null || bidMoney.intValue() % 100 !=0 ){
            result.setCodeEnum(CodeEnum.INVEST_MONEY_100_ERROR);
        } else {
            //投资，调用数据服务的一个方法
            UserEntity user = (UserEntity) session.getAttribute(YLBConsts.SESSION_USER);

            builder.append("UserId:"+user.getId());

            InvestResult investResult =  investService.invest(user.getId(),productId,bidMoney);
            if( investResult.getResult() == true ){
                result = Result.ok();
                result.setMsg(investResult.getReason());

                //更新投资排行榜
                stringRedisTemplate.opsForZSet()
                        .incrementScore(YLBKey.INVEST_TOP_LIST, user.getPhone(), bidMoney.doubleValue());


            } else {
                result.setCode(CodeEnum.INVEST_ERROR.getCode());
                result.setMsg(investResult.getReason());
            }
            builder.append("|result:"+result.getCode()+"|msg:"+result.getMsg());
        }
        log.info("用户投资信息|"+"ProductId:"+productId+"|bidMoney:"+bidMoney+"|"+builder.toString());
        return result;

    }

    /************更多投资记录， 分页查看投资*****************/
    @GetMapping("/more/list")
    public String moreInvestList(@RequestParam(value = "pageNo",required = false,defaultValue = "1") Integer pageNo,
                                 HttpSession session,
                                 Model model){

        //分页查询投资记录
        UserEntity user = (UserEntity) session.getAttribute(YLBConsts.SESSION_USER);

        pageNo = YLBUtil.pageNo(pageNo);
        List<BidInfo> bidInfoList = investService.queryBidListByUserId(
                                user.getId(),pageNo, YLBConsts.PAGE_SIZE_MYCENTER);

        //创建Page分页对象
        int totalRecord = investService.staticsBidRecordNumsByUserId(user.getId());
        Page page = new Page(pageNo,YLBConsts.PAGE_SIZE_MYCENTER,totalRecord);

        model.addAttribute("page",page);
        model.addAttribute("bidInfoList",bidInfoList);
        return "myInvest";
    }
}
