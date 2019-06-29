package com.yzhg.tool.http.http;

import com.google.gson.JsonParseException;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.yzhg.common.R;
import com.yzhg.common.utils.common.LogUtils;
import com.yzhg.common.utils.common.Utils;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 类 名: BaseResponseObserver
 * 作 者: yzhg
 * 创 建: 2019/5/30 0030
 * 版 本: 4.1.0
 * 历 史: (版本) 作者 时间 注释
 * 描 述:
 */
public abstract class BaseResponseObserver <T extends BaseResponse> implements Observer<T> {

    /**
     * 一个参数的构造方法
     */
    public BaseResponseObserver() {
    }



    @Override
    public void onSubscribe(Disposable d) {

    }

    /**
     * 网络数据加载成功后,会走到这个方法中
     *
     * @param response 泛型
     *                 <p>
     *                 逻辑判断:
     *                 如果 ErrorCode 请求码  为20000 则说明请求网络成功
     *                 否则表示请求网络失败 ,需要处理网络失败的状况
     */
    @Override
    public void onNext(T response) {  //请求成功额方法
        LogUtils.d("----登录请求成功response " + response.toString());
        if (10000 == response.getErrcode()) {
            onSuccess(response);
        } else {
            onFail(response);
        }
    }

    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 请求失败的方法
     */
    private void onFail(T response) {
        onDefeated(response.getErrmsg(), response.getErrcode());
    }

    /**
     * 抽象方法,当前网络数据请求成功,成功的数据将由子类去统一处理
     *
     * @param response 获取到成功后的数据
     */
    protected abstract void onSuccess(T response);

    /**
     * 抽象方法,网络数据加载失败的将错误信息传递到页面
     *
     * @param string     错误信息
     * @param errorIndex 错误码
     */
    protected abstract void onDefeated(String string, int errorIndex);

    @Override
    public void onError(Throwable e) {
        try {
            String errorString = e.getMessage().toString();
            LogUtils.d("------异常错误" + errorString);
            if (e instanceof HttpException) {   //HTTP  错误
                onException(ExceptionReason.BAD_NETWORK, errorString);
            } else if (e instanceof ConnectException || e instanceof UnknownHostException) {  //连接错误
                onException(ExceptionReason.CONNECT_ERROR, errorString);
            } else if (e instanceof InterruptedIOException) {  //连接超时
                onException(ExceptionReason.CONNECT_TIMEOUT, errorString);
            } else if (e instanceof JsonParseException
                    || e instanceof JSONException
                    || e instanceof ParseException) {  //解析异常
                onException(ExceptionReason.PARSE_ERROR, errorString);
            } else {
                onException(ExceptionReason.UNKNOWN_ERROR, errorString);  //未知错误
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    @Override
    public void onComplete() {

    }

    /**
     * 错误异常处理
     *
     * @param reason 异常
     */
    private void onException(ExceptionReason reason, String error) {
        switch (reason) {
            case CONNECT_ERROR:
                onDefeated(Utils.getString(R.string.net_connect_error), 1001);
                LogUtils.d(Utils.getString(R.string.net_connect_error));
                break;
            case CONNECT_TIMEOUT:
                onDefeated(Utils.getString(R.string.net_connect_timeOut), 1002);
                LogUtils.d(Utils.getString(R.string.net_connect_timeOut));
                break;
            case BAD_NETWORK:
                onDefeated(Utils.getString(R.string.net_bad_netWork), 1003);
                LogUtils.d(Utils.getString(R.string.net_bad_netWork));
                break;
            case PARSE_ERROR:
                onDefeated(Utils.getString(R.string.net_parse_error), 1004);
                LogUtils.d(Utils.getString(R.string.net_parse_error));
                break;
            case UNKNOWN_ERROR:
            default:
                onDefeated(Utils.getString(R.string.net_unKnown_error), 1000);
                LogUtils.d(Utils.getString(R.string.net_unKnown_error));
                break;
        }
    }

    /**
     * 采用枚举
     */
    public enum ExceptionReason {
        /**
         * 解析数据失败
         */
        PARSE_ERROR,
        /**
         * 网络问题
         */
        BAD_NETWORK,
        /**
         * 连接错误
         */
        CONNECT_ERROR,
        /**
         * 连接超时
         */
        CONNECT_TIMEOUT,
        /**
         * 未知错误
         */
        UNKNOWN_ERROR,
    }

}
