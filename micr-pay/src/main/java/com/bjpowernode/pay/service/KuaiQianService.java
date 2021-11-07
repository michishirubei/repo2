package com.bjpowernode.pay.service;

import com.bjpowernode.model.RechargeResult;
import com.bjpowernode.pay.util.Pkipair;
import com.bjpowernode.service.bussiness.RechargeService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class KuaiQianService {

    @DubboReference(interfaceClass = RechargeService.class,version = "1.0")
    private RechargeService rechargeService;

    //获取支付接口需要的参数
    //Map: 保存form上需要的所有参数值
    public Map<String, String> getFormData(String userId, String phone, String ylbOrderId, String yuan) {
        //人民币网关账号，该账号为11位人民币网关商户编号+01,该参数必填。
        String merchantAcctId = "1001214035601";//
        //编码方式，1代表 UTF-8; 2 代表 GBK; 3代表 GB2312 默认为1,该参数必填。
        String inputCharset = "1";
        //接收支付结果的页面地址，该参数一般置为空即可。
        String pageUrl = "";
        //服务器接收支付结果的后台地址，该参数务必填写，不能为空。
        String bgUrl = "http://47.113.198.114:9999/pay/kq/notify";
        //网关版本，固定值：v2.0,该参数必填。
        String version = "v2.0";
        //语言种类，1代表中文显示，2代表英文显示。默认为1,该参数必填。
        String language = "1";
        //签名类型,该值为4，代表PKI加密方式,该参数必填。
        String signType = "4";
        //支付人姓名,可以为空。
        String payerName = "";
        //支付人联系类型，1 代表电子邮件方式；2 代表手机联系方式。可以为空。
        String payerContactType = "2";
        //支付人联系方式，与payerContactType设置对应，payerContactType为1，则填写邮箱地址；payerContactType为2，则填写手机号码。可以为空。
        String payerContact = phone;
        //指定付款人，可以为空
        String payerIdType = "3";
        //付款人标识，可以为空
        String payerId = userId;
        //付款人IP，可以为空
        String payerIP = "";
        //商户订单号，以下采用时间来定义订单号，商户可以根据自己订单号的定义规则来定义该值，不能为空。
        String orderId = ylbOrderId;
        //订单金额，金额以“分”为单位，商户测试以1分测试即可，切勿以大金额测试。该参数必填。
        String orderAmount = new BigDecimal(yuan).multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString();
        //订单提交时间，格式：yyyyMMddHHmmss，如：20071117020101，不能为空。
        String orderTime = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        //快钱时间戳，格式：yyyyMMddHHmmss，如：20071117020101， 可以为空
        String orderTimestamp = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        ;
        //商品名称，可以为空。
        String productName = "Apple";
        //商品数量，可以为空。
        String productNum = "1";
        //商品代码，可以为空。
        String productId = "10000";
        //商品描述，可以为空。
        String productDesc = "Apple";
        //扩展字段1，商户可以传递自己需要的参数，支付完快钱会原值返回，可以为空。
        String ext1 = "pjpowernode";
        //扩展自段2，商户可以传递自己需要的参数，支付完快钱会原值返回，可以为空。
        String ext2 = "ylb";
        //支付方式，一般为00，代表所有的支付方式。如果是银行直连商户，该值为10-1或10-2，必填。
        String payType = "00";
        //银行代码，如果payType为00，该值可以为空；如果payType为10-1或10-2，该值必须填写，具体请参考银行列表。
        String bankId = "";
        //同一订单禁止重复提交标志，实物购物车填1，虚拟产品用0。1代表只能提交一次，0代表在支付不成功情况下可以再提交。可为空。
        String redoFlag = "0";
        //快钱合作伙伴的帐户号，即商户编号，可为空。
        String pid = "";

        //创建Map，保存参数
        Map<String, String> param = new HashMap<>();
        // signMsg 签名字符串 不可空，生成加密签名串
        String signMsgVal = "";
        signMsgVal = appendParam(signMsgVal, "inputCharset", inputCharset, param);
        signMsgVal = appendParam(signMsgVal, "pageUrl", pageUrl, param);
        signMsgVal = appendParam(signMsgVal, "bgUrl", bgUrl, param);
        signMsgVal = appendParam(signMsgVal, "version", version, param);
        signMsgVal = appendParam(signMsgVal, "language", language, param);
        signMsgVal = appendParam(signMsgVal, "signType", signType, param);
        signMsgVal = appendParam(signMsgVal, "merchantAcctId", merchantAcctId, param);
        signMsgVal = appendParam(signMsgVal, "payerName", payerName, param);
        signMsgVal = appendParam(signMsgVal, "payerContactType", payerContactType, param);
        signMsgVal = appendParam(signMsgVal, "payerContact", payerContact, param);
        signMsgVal = appendParam(signMsgVal, "payerIdType", payerIdType, param);
        signMsgVal = appendParam(signMsgVal, "payerId", payerId, param);
        signMsgVal = appendParam(signMsgVal, "payerIP", payerIP, param);
        signMsgVal = appendParam(signMsgVal, "orderId", orderId, param);
        signMsgVal = appendParam(signMsgVal, "orderAmount", orderAmount, param);
        signMsgVal = appendParam(signMsgVal, "orderTime", orderTime, param);
        signMsgVal = appendParam(signMsgVal, "orderTimestamp", orderTimestamp, param);
        signMsgVal = appendParam(signMsgVal, "productName", productName, param);
        signMsgVal = appendParam(signMsgVal, "productNum", productNum, param);
        signMsgVal = appendParam(signMsgVal, "productId", productId, param);
        signMsgVal = appendParam(signMsgVal, "productDesc", productDesc, param);
        signMsgVal = appendParam(signMsgVal, "ext1", ext1, param);
        signMsgVal = appendParam(signMsgVal, "ext2", ext2, param);
        signMsgVal = appendParam(signMsgVal, "payType", payType, param);
        signMsgVal = appendParam(signMsgVal, "bankId", bankId, param);
        signMsgVal = appendParam(signMsgVal, "redoFlag", redoFlag, param);
        signMsgVal = appendParam(signMsgVal, "pid", pid, param);

        System.out.println("商户发起请求， 要签名的原始数据=" + signMsgVal);
        Pkipair pki = new Pkipair();
        //签名值，需要发送给快钱
        String signMsg = pki.signMsg(signMsgVal);
        param.put("signMsg",signMsg);

        return param;
    }


    //处理快钱的异步通知
    public RechargeResult handlerKqNotify(HttpServletRequest request){

        RechargeResult result = new RechargeResult();
        String merchantAcctId = request.getParameter("merchantAcctId");
        String version = request.getParameter("version");
        String language = request.getParameter("language");
        String signType = request.getParameter("signType");
        String payType = request.getParameter("payType");
        String bankId = request.getParameter("bankId");
        String orderId = request.getParameter("orderId");
        String orderTime = request.getParameter("orderTime");
        String orderAmount = request.getParameter("orderAmount");

        String bindCard = request.getParameter("bindCard");
        if(request.getParameter("bindCard")!=null){
            bindCard = request.getParameter("bindCard");
        }
        String bindMobile="";
        if(request.getParameter("bindMobile")!=null){
            bindMobile = request.getParameter("bindMobile");
        }

        String dealId = request.getParameter("dealId");
        String bankDealId = request.getParameter("bankDealId");
        String dealTime = request.getParameter("dealTime");
        String payAmount = request.getParameter("payAmount");
        String fee = request.getParameter("fee");
        String ext1 = request.getParameter("ext1");
        String ext2 = request.getParameter("ext2");
        String payResult = request.getParameter("payResult");
        String aggregatePay = request.getParameter("aggregatePay");
        String errCode = request.getParameter("errCode");
        String signMsg = request.getParameter("signMsg");
        String merchantSignMsgVal = "";
        merchantSignMsgVal = appendParam(merchantSignMsgVal,"merchantAcctId", merchantAcctId,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "version",version,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "language",language,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "signType",signType,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "payType",payType,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "bankId",bankId,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "orderId",orderId,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "orderTime",orderTime,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "orderAmount",orderAmount,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "bindCard",bindCard,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "bindMobile",bindMobile,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "dealId",dealId,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "bankDealId",bankDealId,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "dealTime",dealTime,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "payAmount",payAmount,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "fee", fee,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "ext1", ext1,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "ext2", ext2,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "payResult",payResult,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "aggregatePay",aggregatePay,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "errCode",errCode,null);

        Pkipair pki = new Pkipair();
        //验证签名 flag=true 是通过， 其他没有通过
        boolean flag = pki.enCodeByCer(merchantSignMsgVal, signMsg);

        if(flag ){
            //商家可以接收收据，处理业务
            result  = rechargeService.doWithKqNotifyService(orderId,payResult,payAmount);
        } else {
            //验签失败 ，记录日志，或者更新数据库。
            //使用logback 记录日志
            result.setResult(false);
            result.setCode(30006);
            result.setReason(orderId+"，验签失败");
        }

        return result;
    }


    public String appendParam(String returns, String paramId, String paramValue, Map<String, String> param) {
        if (returns != "") {
            if (paramValue != "" && paramValue != null) {
                returns += "&" + paramId + "=" + paramValue;
                if (param != null) {
                    param.put(paramId, paramValue);
                }
            }
        } else {
            if (paramValue != "" && paramValue != null) {
                returns = paramId + "=" + paramValue;
                if (param != null) {
                    param.put(paramId, paramValue);
                }
            }
        }
        return returns;
    }
}
