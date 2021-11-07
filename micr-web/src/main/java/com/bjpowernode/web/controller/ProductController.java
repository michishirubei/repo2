package com.bjpowernode.web.controller;

import com.bjpowernode.consts.YLBConsts;
import com.bjpowernode.consts.YLBKey;
import com.bjpowernode.entity.AccountEntity;
import com.bjpowernode.entity.ProductEntity;
import com.bjpowernode.entity.UserEntity;
import com.bjpowernode.model.BidInfo;
import com.bjpowernode.util.YLBUtil;
import com.bjpowernode.vo.InvestTopBean;
import com.bjpowernode.vo.Page;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class ProductController extends BaseController {



    /******单个产品详情页面**********/
    @GetMapping("/product/info")
    public String pageProductInfo(@RequestParam("pid") Integer productId,
                                  Model model,
                                  HttpSession session){

        //参数检查
        if( productId != null && productId > 0){
            //查询产品
            ProductEntity product = productService.queryProductById(productId);
            if( product != null){
                //查询用户的资金
                UserEntity user = (UserEntity) session.getAttribute(YLBConsts.SESSION_USER);
                if( user != null ){
                    //根据userId查询资金
                    AccountEntity account = accountService.queryByUid(user.getId());
                    if(account != null){
                        model.addAttribute("accountMoney",account.getAvailableMoney());
                    }
                }


                //产品的相关投资记录
                List<BidInfo> bidList = investService.queryBidListByProudctId(productId);
                model.addAttribute("bidList",bidList);

                //把产品对象放入model
                model.addAttribute("product",product);
                return "productInfo";
            } else {
                model.addAttribute("error","此产品不存在");
                return "tip";
            }
        } else {
            model.addAttribute("error","此产品不存在");
            return "tip";
        }

    }


    /******分页查询产品页面**********/
    @GetMapping("/product/list")
    public String pageProducts(@RequestParam("ptype") Integer productType,
                               @RequestParam(value = "pageNo",required = false,defaultValue = "1") Integer pageNo,
                               Model model){

        //做参数检查
        if(YLBUtil.typeInRange(productType)){
            pageNo = YLBUtil.pageNo(pageNo);
            //按类型分页查询产品
            List<ProductEntity> productList = productService.queryProductListPage(
                    productType,pageNo, YLBConsts.PAGE_SIZE);
            model.addAttribute("productList",productList);

            //计算分页Page
            Integer totalRecord = productService.queryTotalRecordByProuductType(productType);
            Page page = new Page(pageNo,YLBConsts.PAGE_SIZE,totalRecord);
            model.addAttribute("page",page);

            //@TODO 投资排行榜
            Set<ZSetOperations.TypedTuple<String>> sets = stringRedisTemplate.opsForZSet()
                                        .reverseRangeWithScores(YLBKey.INVEST_TOP_LIST, 0, 4);

            List<InvestTopBean> topList  = new ArrayList<>();
            sets.forEach( s -> {
                 topList.add( new InvestTopBean(s.getValue(),s.getScore()));
            });
            model.addAttribute("topList",topList);


            return "products";

        } else {
            model.addAttribute("error","无此产品类型");
            return "tip";
        }

    }
}
