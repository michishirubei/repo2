package com.bjpowernode.service.pay;

import com.bjpowernode.model.RechargeResult;

//支付服务，提供的快钱访问接口
public interface ExportKuaiQianService {

    //在pay调用快钱的支付查询接口，返回的是快钱的json
    RechargeResult queryOrderInfo(String orderId);

    //处理掉单
    void handlerKQDiaoDan();
}
