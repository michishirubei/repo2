package com.bjpowernode.service.bussiness;

import com.bjpowernode.model.BidInfo;
import com.bjpowernode.model.InvestResult;

import java.math.BigDecimal;
import java.util.List;

public interface InvestService {

    /***********累计的投资金额和*****************/
    BigDecimal querySumInvestMoney();

    /***********某个产品的最近3条投资记录*****************/
    List<BidInfo> queryBidListByProudctId(Integer productId);

    /***********查询某个用户的投资记录列表*****************/
    List<BidInfo> queryBidListByUserId(Integer userId,Integer pageNo,Integer pageSize);

    /***********查询某个用户的投资记录总数*****************/
    Integer staticsBidRecordNumsByUserId(Integer userId);

    /***********查询某个用户的投资记录总数*****************/
    InvestResult invest(Integer userId, Integer productId, BigDecimal bidMoney);
}
