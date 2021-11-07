package com.bjpowernode.service.bussiness;

import com.bjpowernode.entity.RechargeEntity;
import com.bjpowernode.model.RechargeResult;

import java.util.List;

//充值服务
public interface RechargeService {
    /***********用户的充值记录***************/
    List<RechargeEntity> queryRechageListByUserId(Integer userId,
                                                  Integer pageNo,
                                                  Integer pageSize);


    /***********用户的充值记录总数***************/
    Integer queryRechargeRecordNumByUserId(Integer userId);

    /***********创建充值记录***************/
    boolean addRecharge(RechargeEntity entity);


    /***********完成快钱异步通知业务***************/
    RechargeResult doWithKqNotifyService(String orderId, String payResult, String payAmount);

    /***********根据充值订单号，查询充值记录***************/
    RechargeEntity queryRechargeByRechargeNo(String orderId);

    /***********状态是充值中的记录***************/
    List<RechargeEntity> queryProcessingStatusList();
}
