package com.yzhg.tool.base.mvp.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;

import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;
import com.yzhg.common.base.activity.BasicActivity;
import com.yzhg.common.base.mvp.base.BasePresenterImpl;
import com.yzhg.common.base.mvp.base.BaseView;

import java.lang.reflect.ParameterizedType;


/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public abstract class MVPBaseActivity<V extends BaseView, T extends BasePresenterImpl<V>> extends BasicActivity implements BaseView {

    public T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = getInstance(this, 1);
        mPresenter.attachView((V) this);
    }


    @Override
    public Context getContext() {
        return this;
    }

    public <T> T getInstance(Object o, int i) {
        try {
            return ((Class<T>) ((ParameterizedType) (o.getClass().getGenericSuperclass())).getActualTypeArguments()[i]).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
    }

    /*绑定生命周期*/
    @Override
    public <T> AutoDisposeConverter<T> bindAutoDispose() {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY));
    }
}










