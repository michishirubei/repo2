package com.bjpowernode.web.service;

import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.consts.YLBConsts;
import com.bjpowernode.consts.YLBKey;
import com.bjpowernode.web.settings.JdwxSmsConfig;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

//处理短信相关的业务类
@Service
public class SmsService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private JdwxSmsConfig smsConfig;


    /**
     * @param action 行为（在什么业务发短信）
     * @param phone  手机号
     */
    public boolean sendSms(String action,String phone){

        boolean result = false;
        String content = "";
        String code = "";

        if(YLBConsts.ACTION_REGIST.equals(action)){
            code = RandomStringUtils.randomNumeric(6);
            //获取注册，发送短信内容
            content =  String.format( smsConfig.getContent(),code);
        } else if("login".equals(action)){
            //登录的短信内容
        }

        //调用第三方接口
        String responseText = invokeJdwxApi(phone,content);

        responseText="{\n" +
                "    \"code\": \"10000\",\n" +
                "    \"charge\": false,\n" +
                "    \"remain\": 1305,\n" +
                "    \"msg\": \"查询成功\",\n" +
                "    \"result\": {\n" +
                "        \"ReturnStatus\": \"Success\",\n" +
                "        \"Message\": \"ok\",\n" +
                "        \"RemainPoint\": 420842,\n" +
                "        \"TaskID\": 18424321,\n" +
                "        \"SuccessCounts\": 1\n" +
                "    }\n" +
                "}";

        if( !responseText.equals("") ){
            //使用fastjson 处理数据
            JSONObject jsonObject = JSONObject.parseObject(responseText);
            if( "10000".equals(jsonObject.getString("code"))){
                //获取result节点
                JSONObject resultJsonObject = jsonObject.getJSONObject("result");
                if( resultJsonObject != null ){
                   if("Success".equalsIgnoreCase(resultJsonObject.getString("ReturnStatus"))){
                       result = true;
                   }
                   // @TODO : 记录日志， 短信发送的细节， code，msg，ReturnStatus， DEBUG
                }
            }
        }
        //把短信验证码，保存到redis，设置有效时间
        if(YLBConsts.ACTION_REGIST.equals(action) && result){
            //注册验证码，需要3分钟
            String key = YLBKey.USER_REGIST_SMSCODE + phone;
            stringRedisTemplate.opsForValue().set(key,code,3, TimeUnit.MINUTES);
        }

        System.out.println("发送的短信验证码内容："+code);
        return result;
    }

    /**
     * @param phone   收取短信的手机号
     * @param content 短信的内容
     * @return
     */
    private String invokeJdwxApi(String phone, String content) {
        CloseableHttpClient client = HttpClients.createDefault();

        String url = smsConfig.getUrl()+"?mobile="+phone+"&content="+content+"&appkey="+smsConfig.getAppkey();
        HttpGet get  = new HttpGet(url);
        String responseText = "";
        try{
            CloseableHttpResponse response = client.execute(get);
            if( response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                responseText = EntityUtils.toString(response.getEntity());
            }
        }catch (Exception e){
            e.printStackTrace();
            responseText = "";
        }
        return responseText;
    }

    /****************判断短信验证码*************************/
    public boolean checkAuthCode(String phone,String code){
        String key = YLBKey.USER_REGIST_SMSCODE+phone;
        if( stringRedisTemplate.hasKey(key)){
            String redisSmsCode = stringRedisTemplate.opsForValue().get(key);
            if( code.equalsIgnoreCase(redisSmsCode)){
                return true;
            }
        }
        return false;
    }
}
