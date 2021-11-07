package com.bjpowernode.pay.export;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.entity.RechargeEntity;
import com.bjpowernode.model.RechargeResult;
import com.bjpowernode.pay.util.HttpUtil;
import com.bjpowernode.pay.util.Pkipair;
import com.bjpowernode.service.bussiness.RechargeService;
import com.bjpowernode.service.pay.ExportKuaiQianService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@DubboService(interfaceClass = ExportKuaiQianService.class,version = "1.0")
public class ExportKuqiQianServiceImpl implements ExportKuaiQianService {


    @DubboReference(interfaceClass = RechargeService.class,version = "1.0")
    private RechargeService rechargeService;

    //处理掉单
    @Override
    public void handlerKQDiaoDan() {
        //查询数据库recharge_record表status = 0的所有记录
        List<RechargeEntity> rechargeList = rechargeService.queryProcessingStatusList();
        for(RechargeEntity recharge: rechargeList){
            queryOrderInfo(recharge.getRechargeNo());
        }
    }


    @Override
    public RechargeResult queryOrderInfo(String orderId) {
        RechargeResult result = new RechargeResult();
        result.setResult(false);

        Map<String, Object> request = new HashMap<String, Object>();
        //固定值：1代表UTF-8;
        String inputCharset = "1";
        //固定值：v2.0 必填
        String version = "v2.0";
        //1代表Md5，2 代表PKI加密方式  必填
        String signType = "2";
        //人民币账号 商户号 membcode+01  必填 ，更换为我们之前充值时使用的商户号
        String merchantAcctId = "1001214035601";
        //固定值：0 按商户订单号单笔查询，1 按交易结束时间批量查询必填
        String queryType = "0";
        //固定值：1	代表简单查询 必填
        String queryMode = "1";
        //数字串，
        String startTime = "";
        //数字串
        String endTime = "";
        String requestPage = "";
        String key = "27YKWKBKHT2IZSQ4";//27YKWKBKHT2IZSQ4

        request.put("inputCharset", inputCharset);
        request.put("version", version);
        request.put("signType", signType);
        request.put("merchantAcctId", merchantAcctId);
        request.put("queryType", queryType);
        request.put("queryMode", queryMode);
        request.put("startTime", startTime);
        request.put("endTime", endTime);
        request.put("requestPage", requestPage);
        request.put("orderId", orderId);

        String message="";
        message = appendParam(message,"inputCharset",inputCharset);
        message = appendParam(message,"version",version);
        message = appendParam(message,"signType",signType);
        message = appendParam(message,"merchantAcctId",merchantAcctId);
        message = appendParam(message,"queryType",queryType);
        message = appendParam(message,"queryMode",queryMode);
        message = appendParam(message,"startTime",startTime);
        message = appendParam(message,"endTime",endTime);
        message = appendParam(message,"requestPage",requestPage);
        message = appendParam(message,"orderId",orderId);
        message = appendParam(message,"key",key);

        Pkipair pki = new Pkipair();
        String sign = pki.signMsg(message);
        //把加入singMsg的参数，必须的
        request.put("signMsg", sign);

        System.out.println("请求json串===" + JSON.toJSONString(request));
        //sandbox提交地址 ： 快钱的查询地址
        String reqUrl = "https://sandbox.99bill.com/gatewayapi/gatewayOrderQuery.do";
        String response = "";

        try {
            response = HttpUtil.doPostJsonRequestByHttps(JSON.toJSONString(request), reqUrl, 3000, 8000);
            System.out.println("返回json串，快钱给的支付结果==="+response);

            //解析json
            if(StringUtils.isNotBlank(response)){
                JSONObject objectOne = JSONObject.parseObject(response);
                if(objectOne != null){
                    Object orderDetail = objectOne.get("orderDetail");
                    if( orderDetail == null ){
                        //没有查询到
                        result.setReason("没有查到到用户的充值记录");
                        result.setCode(30008);
                    } else{
                        //json 数组
                        JSONArray array  = objectOne.getJSONArray("orderDetail");
                        if( array != null && array.size() > 0){
                            // 可以取其一个成员， 做充值的后续处理
                            for(Object o : array){
                                JSONObject jsonObject = (JSONObject)o;

                                jsonObject.getString("orderId");
                                jsonObject.getString("payAmount");
                                jsonObject.getString("payResult");

                                result = rechargeService.doWithKqNotifyService(
                                        jsonObject.getString("orderId"),
                                        jsonObject.getString("payResult"),
                                        jsonObject.getString("payAmount"));
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }



    public  static String appendParam(String result, String paramId, String paramValue) {
        if (result != "") {
            if (paramValue != "" && paramValue != null) {

                result += "&" + paramId + "=" + paramValue;
            }
        } else {

            if (paramValue != "" && paramValue != null ) {
                result = paramId + "=" + paramValue;
            }
        }
        return result;
    }
}
