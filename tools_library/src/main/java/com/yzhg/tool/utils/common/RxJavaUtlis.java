package com.yzhg.tool.utils.common;

import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * description (RxJava封装的工具类)
 * Created by yzhg on 2017/9/21.
 */

public class RxJavaUtlis {

    public static <T> ObservableTransformer<T, T> setThread() {
        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> FlowableTransformer<T, T> setFlowable() {
        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
