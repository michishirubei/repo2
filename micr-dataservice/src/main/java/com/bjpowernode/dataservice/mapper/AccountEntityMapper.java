package com.bjpowernode.dataservice.mapper;

import com.bjpowernode.entity.AccountEntity;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface AccountEntityMapper {
    /******使用手机号查询资金*******/
    AccountEntity selectByUid(@Param("uid") Integer uid);

    /******给一条资金账户上锁*******/
    AccountEntity selectForUpdate(@Param("uid") Integer userId);


    /*********投资更新账户金额**************/
    int updateAvailableMoneyByInvest(@Param("uid") Integer userId, @Param("bidMoney") BigDecimal bidMoney);

    /*********收益返还，更新资金**************/
    int updateAvailableMoneyByIncomeBack(@Param("uid") Integer uid,
                                         @Param("bidMoney") BigDecimal bidMoney,
                                         @Param("incomeMoney") BigDecimal incomeMoney);

    /*********充值，更新资金**************/
    int updateAvailableMoneyByRecharge(@Param("uid") Integer uid, @Param("rechargeMoney") BigDecimal rechargeMoney);

    int deleteByPrimaryKey(Integer id);

    int insert(AccountEntity record);

    int insertSelective(AccountEntity record);

    AccountEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AccountEntity record);

    int updateByPrimaryKey(AccountEntity record);



}