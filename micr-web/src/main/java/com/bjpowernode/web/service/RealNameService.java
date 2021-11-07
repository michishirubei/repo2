package com.bjpowernode.web.service;

import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.consts.YLBKey;
import com.bjpowernode.entity.UserEntity;
import com.bjpowernode.service.user.UserService;
import com.bjpowernode.util.HttpClientUtils;
import com.bjpowernode.web.settings.JdwxRealNameConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class RealNameService {

    @Resource
    private JdwxRealNameConfig realNameConfig;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @DubboReference(version = "1.0")
    private UserService userService;

    //做实名认证
    public UserEntity handlerRealName(String phone,String name,String card){
        UserEntity user = null;
        boolean checkResult  = false;
        //调用第三方接口
        Map<String, String> params = new HashMap<>();
        params.put("cardNo",card);
        params.put("realName",name);
        params.put("appkey",realNameConfig.getAppkey());
        try {
            String strResult = HttpClientUtils.doGet(realNameConfig.getUrl(),params);
            System.out.println("实名认证，返回的json="+strResult);

            strResult="{\n" +
                    "    \"code\": \"10000\",\n" +
                    "    \"charge\": false,\n" +
                    "    \"remain\": 1305,\n" +
                    "    \"msg\": \"查询成功\",\n" +
                    "    \"result\": {\n" +
                    "        \"error_code\": 0,\n" +
                    "        \"reason\": \"成功\",\n" +
                    "        \"result\": {\n" +
                    "            \"realname\": \""+name+"\",\n" +
                    "            \"idcard\": \""+card+"\",\n" +
                    "            \"isok\": true\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";

            //解析json
            if(StringUtils.isNotBlank(strResult)){
                JSONObject json = JSONObject.parseObject(strResult);
                if( "10000".equals(json.getString("code"))){
                    //第一级的result
                    JSONObject oneJson = json.getJSONObject("result");
                    if( oneJson != null){
                        //第二级的result
                        JSONObject twoJson = oneJson.getJSONObject("result");
                        if( twoJson != null){
                            //认证结果
                            checkResult =  twoJson.getBoolean("isok");
                        }
                    }
                }
            }
        } catch (Exception e) {
            checkResult = false;
            e.printStackTrace();
        }

        //根据认证结果，是否更新数据库数据
        if( checkResult ){
            //调用数据服务，更新u_user的数据
            user = userService.modifyUser(phone,name,card);
        }
        return user;
    }


    //记录认证次数
    public int checkRealNameTimes(String phone){
        Integer numTimes = 0;
        String key = YLBKey.USER_REAL_NAME+phone;
        if(stringRedisTemplate.hasKey(key)){
            String times =  stringRedisTemplate.opsForValue().get(key);
            numTimes = Integer.valueOf(times) + 1;
            stringRedisTemplate.opsForValue().set(key, String.valueOf(numTimes));
        } else {
            numTimes = 1;
            stringRedisTemplate.opsForValue().set(key,String.valueOf(numTimes));
        }
        return numTimes;
    }
}
