package com.bjpowernode.dataservice.mapper;

import com.bjpowernode.entity.RechargeEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RechargeEntityMapper {

    /*********查询用户的充值记录****************/
    List<RechargeEntity> selectRechargeListByUserId(@Param("uid") Integer userId,
                                                    @Param("offset") Integer offSet,
                                                    @Param("rows") Integer rows);


    /*********查询用户的充值记录总数****************/
    int selectRechargeRecordNum(@Param("uid") Integer userId);

    /*********根据订单号查询订单记录****************/
    RechargeEntity selectByRechargeNo(@Param("rechargeNo") String orderId);

    /*********根据主键更新状态值****************/
    int updateStatusByPrimaryKey(@Param("id") Integer id, @Param("newStatus") Integer newStatus);

    /*********充值充值中的记录****************/
    List<RechargeEntity> selectInProcessing();

    int deleteByPrimaryKey(Integer id);

    int insert(RechargeEntity record);

    int insertSelective(RechargeEntity record);

    RechargeEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RechargeEntity record);

    int updateByPrimaryKey(RechargeEntity record);



}