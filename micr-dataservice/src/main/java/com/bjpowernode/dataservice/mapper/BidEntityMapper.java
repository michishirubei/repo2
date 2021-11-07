package com.bjpowernode.dataservice.mapper;

import com.bjpowernode.entity.BidEntity;
import com.bjpowernode.model.BidInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BidEntityMapper {

    /*********累计成交金额********************/
    BigDecimal selectSumBidMoney();

    /*********产品最近的3条投资记录********************/
    List<BidInfo> selectBidsByProductId(@Param("productId") Integer productId);

    /*********某个用户的投资记录********************/
    List<BidInfo> selectBidsByUserId(@Param("uid") Integer userId,
                                     @Param("offset") Integer offset,
                                     @Param("rows") Integer rows);

    /*********某个用户的投资记录总数********************/
    int selectBidsRecordNums(@Param("uid") Integer userId);

    /*********某个用户的投资记录总数********************/
    int insert(BidEntity record);

    int deleteByPrimaryKey(Integer id);



    int insertSelective(BidEntity record);

    BidEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BidEntity record);

    int updateByPrimaryKey(BidEntity record);


    /********某个产品的投资记录**************/
    List<BidEntity> selectByProductId(@Param("productId") Integer id);
}