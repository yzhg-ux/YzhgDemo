package com.yzhg.tool.http.tools;



import com.yzhg.tool.utils.common.LogUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * author ${水木科技 - yzhg} on 2018/3/15.
 * <p>
 * describe:
 *   日志拦截器，用于获取网络日志信息
 *
 */

public class ResponseLogInterceptor implements Interceptor {

    /**
     * 获取网络数据是，找到此信息即可获知，
     */
    private static final String TAG = "------  OkHttp  ------";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        LogUtils.i(TAG, "url     =  : " + request.url());
        LogUtils.i(TAG, "method  =  : " + request.method());
        LogUtils.i(TAG, "headers =  : " + request.headers());
        LogUtils.i(TAG, "body    =  : " + request.body());
        LogUtils.i(TAG, "code     =  : " + response.code());
        LogUtils.i(TAG, "message  =  : " + response.message());
        LogUtils.i(TAG, "protocol =  : " + response.protocol());
        if (response.body() != null && response.body().contentType() != null) {
            MediaType mediaType = response.body().contentType();
            String string = response.body().string();
            LogUtils.i(TAG, "mediaType =  :  " + mediaType.toString());
            LogUtils.i(TAG, "string    =  : " + string);
            ResponseBody responseBody = ResponseBody.create(mediaType, string);
            return response.newBuilder().body(responseBody).build();
        } else {
            return response;
        }
    }
}
