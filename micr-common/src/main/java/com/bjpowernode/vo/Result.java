package com.bjpowernode.vo;

import com.bjpowernode.enums.CodeEnum;

//表示ajax请求的返回结果
public class Result<T> {
    // succ=true， 请求处理成功， false失败
    private boolean succ;
    // 应答代码（错误码）
    private int code;
    // 错误消息
    private String msg;
    // 应答的数据
    private T data;

    public Result() {
    }

    public Result(boolean succ, int code, String msg) {
        this.succ = succ;
        this.code = code;
        this.msg = msg;
        this.data= (T)"";
    }
    //成功的Result
    public static Result ok(){
        Result result = new Result(true,1000,"请求处理成功");
        return result;
    }

    //错误的Result
    public static Result fail(){
        Result result = new Result(false,-1,"请求处理失败");
        return result;
    }

    public void setCodeEnum(CodeEnum codeEnum){
        this.code = codeEnum.getCode();
        this.msg = codeEnum.getMsg();
    }

    public boolean isSucc() {
        return succ;
    }

    public void setSucc(boolean succ) {
        this.succ = succ;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "succ=" + succ +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
