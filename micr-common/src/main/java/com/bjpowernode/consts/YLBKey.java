package com.bjpowernode.consts;

//记录项目中redis的key
public class YLBKey {
    /**
     * redis的key命名： 见名知其意
     * 规则命名：  类别:子类别:项目
     * 注册验证码： USER:REG:SMS:136000000
     */
    //注册验证码 USER:REG:SMS:手机号
    public static final String USER_REGIST_SMSCODE="USER:REG:SMS:";
    //用户的实名认证次数
    public static final String USER_REAL_NAME = "USER:REALNAME:TIMES:";
    //投资排行榜
    public static final String INVEST_TOP_LIST = "INVEST:TOP:LIST" ;
    //注册总数量
    public static final Object USER_REGIST_COUNTS = "USER:REGIST:COUNTS";
    //快钱序列id
    public static final String KQ_ORDERID_SEQ = "RECHARGE:KQ:SEQ";
}
