package com.yzhg.tool.http.observer;

import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.yzhg.tool.R;
import com.yzhg.tool.utils.Tools;
import com.yzhg.tool.utils.common.LogUtils;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * 类 名: NewBasicObserver
 * 作 者: yzhg
 * 创 建: 2018/10/15 0015
 * 版 本: 1.0
 * 历 史: (版本) 作者 时间 注释
 * 描 述: 封装的 Observer
 */
public abstract class NewBasicObserver<T> implements Observer<T> {

    private final static String TAG = NewBasicObserver.class.getSimpleName();

    @Override
    public void onSubscribe(Disposable disposable) {

    }

    @Override
    public void onNext(T result) {

    }

    @Override
    public void onError(Throwable e) {
        String errorString = e.getMessage();  //错误日志信息
        LogUtils.d("错误信息" + errorString);
        if (e instanceof HttpException) {   //HTTP  错误
            onException(ExceptionReason.BAD_NETWORK);
        } else if (e instanceof ConnectException || e instanceof UnknownHostException) {  //连接错误
            onException(ExceptionReason.CONNECT_ERROR);
        } else if (e instanceof InterruptedIOException) {  //连接超时
            onException(ExceptionReason.CONNECT_TIMEOUT);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {  //解析异常
            onException(ExceptionReason.PARSE_ERROR);
        } else {
            onException(ExceptionReason.UNKNOWN_ERROR);  //未知错误
        }
    }

    private void onException(ExceptionReason reason) {
        switch (reason) {
            case CONNECT_ERROR:
                onDefeated(Tools.getString(R.string.net_connect_error), 1111);
                break;
            case CONNECT_TIMEOUT:
                onDefeated(Tools.getString(R.string.net_connect_timeOut), 1112);
                break;
            case BAD_NETWORK:
                onDefeated(Tools.getString(R.string.net_bad_netWork), 1113);
                break;
            case PARSE_ERROR:
                onDefeated(Tools.getString(R.string.net_parse_error), 1114);
                break;
            case UNKNOWN_ERROR:
            default:
                onDefeated(Tools.getString(R.string.net_unKnown_error), 1000);
                break;
        }
    }

    public void onDefeated(String string, int code) {

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

    @Override
    public void onComplete() {

    }
}
