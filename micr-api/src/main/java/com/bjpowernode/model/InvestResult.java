package com.bjpowernode.model;

import java.io.Serializable;

/**
 *  投资结果
 */
public class InvestResult implements Serializable {
    private static final long serialVersionUID = -1964101466810215406L;
    private boolean result; //true成功，false失败
    private String reason;  // 失败原因
    private int code;       //失败的代码

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
