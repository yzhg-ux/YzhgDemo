package com.yzhg.tool.base.mvp.base;

import android.content.Context;

import com.uber.autodispose.AutoDisposeConverter;

/**
 * 类 名: BaseView
 * 作 者: yzhg
 * 创 建: 2019/4/15 0015
 * 版 本: 1.0
 * 历 史: (版本) 作者 时间 注释
 * 描 述:  MVP V层基类 所有View层均继承于此
 */
public interface BaseView {

    /*获取上下文*/
    Context getContext();

    /*绑定生命周期*/
    <T> AutoDisposeConverter<T> bindAutoDispose();
}
