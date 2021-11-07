package com.bjpowernode.enums;

public enum  CodeEnum {
    PHONE_BLANK_ERR(1001,"手机号是空的"),
    PHONE_FORMAT_ERR(1002,"手机号格式不正确"),
    PHONE_HAS_REGISTED_ERR(1003,"手机号已经注册，请更换手机号"),
    PARAMETER_BLANK_ERROR(1004,"请求参数是空"),
    PARAMETER_PASSWORD_ERROR(1005,"请求手机号或者密码无效"),
    PARAMETER_SMSCODE_ERROR(1006,"短信验证码无效"),
    REALNAME_NAME_ERROR(1007,"姓名不是中文"),
    REALNAME_IDCARD_ERROR(1008,"身份证号不正确"),
    REALNAME_PHONE_ERROR(1009,"不是登录或者注册的手机号"),
    REALNAME_CHECKED_ERROR(1010,"已经做过实名认证"),
    REALNAME_TIMES_ERROR(1011,"实名认证超过次数了，联系客服"),
    INVEST_MONEY_100_ERROR(1013,"投资金额不是100的整数倍"),
    INVEST_ERROR(1014,"投资失败："),
    LOGIN_REQUIED_ERROR(1015,"需要重新登录"),
    ;
    private int code;
    private String msg;

    CodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
