package com.yzhg.tool.http.http;

/**
 * Created by $(剪刀手--yzhg) on 2018/5/30 0030.
 * 用一句话描述该类的用处:  网络请求的封装
 */
public class BaseResponse<T> {
    /*返回码*/
    private int errcode;
    /*错误信息*/
    private String errmsg;
    /*结果集*/
    private T data;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
