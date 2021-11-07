package com.bjpowernode.consts;

public class YLBConsts {

    /***********项目中的名词******************/
    //注册操作
    public static final String ACTION_REGIST="regist";

    /***********会话中的key******************/
    public static final String SESSION_USER="ylb_session_user";

    /***********分页******************/
    public  static  final Integer PAGE_SIZE= 9 ;
    //用户中心，分页中，默认是 6 条记录
    public  static  final Integer PAGE_SIZE_MYCENTER = 6 ;

    /***********产品类型******************/
    //新手宝
    public static final Integer PRODUCT_TYPE_XINSHOUBAO= 0;
    //优选
    public static final Integer PRODUCT_TYPE_YOUXUAN= 1;
    //散标
    public static final Integer PRODUCT_TYPE_SANBIAO= 2;


    /***********bid_info表 bid_status的值******************/
    //投资成功
    public static final Integer BID_STATUS_SUCCESS = 1;
    //投资失败
    public static final Integer BID_STATUS_FAILURE = 2;


    /***********product_info表 product_status的值******************/
    //产品未满标
    public static final Integer PRODUCT_STATUS_NONE_MANBIAO = 0;
    //满标
    public static final Integer PRODUCT_STATUS_MANBIAO = 1;
    //满标生成收益计划
    public static final Integer PRODUCT_STATUS_PLAN = 2;

    /***********income_record 表 income_status的值 收益状态（0未返，1已返 ******************/
    //未返
    public static final Integer INCOME_STATUS_PLAN = 0;
    //已返
    public static final Integer INCOME_STATUS_BACK = 1;

    /***********recharge_record 表 recharge_status的值 0:充值中， 1：成功；2：失败 ******************/
    //充值中
    public static final Integer RECHAGE_STATUS_PROCESSING = 0;
    //充值成功
    public static final Integer RECHAGE_STATUS_SUCCESS = 1;
    //充值失败
    public static final Integer RECHAGE_STATUS_FAILURE = 2;
}
