package com.yzhg.tool.base.mvp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;
import com.yzhg.tool.base.fragment.BasicFragment;
import com.yzhg.tool.base.mvp.base.BasePresenterImpl;
import com.yzhg.tool.base.mvp.base.BaseView;

import java.lang.reflect.ParameterizedType;

/**
 * 类 名: MVPLazyLoadFragment
 * 作 者: yzhg
 * 创 建: 2019/4/15 0015
 * 版 本: 1.0
 * 历 史: (版本) 作者 时间 注释
 * 描 述:
 */
public abstract class MVPLazyLoadFragment<V extends BaseView, T extends BasePresenterImpl<V>> extends BasicFragment implements BaseView{

    private boolean isFirstLoad = false;

    public T mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = getInstance(this, 1);
        mPresenter.attachView((V) this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        isFirstLoad = true;//视图创建完成，将变量置为true
        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getUserVisibleHint()) {//如果Fragment可见进行数据加载
            onLazyLoad();
            isFirstLoad = false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isFirstLoad = false;//视图销毁将变量置为false
        if (mPresenter != null)
            mPresenter.detachView();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isFirstLoad && isVisibleToUser) {//视图变为可见并且是第一次加载
            onLazyLoad();
            isFirstLoad = false;
        }
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    //数据加载接口，留给子类实现
    protected abstract void onLazyLoad();

    public <T> T getInstance(Object o, int i) {
        try {
            return ((Class<T>) ((ParameterizedType) (o.getClass().getGenericSuperclass())).getActualTypeArguments()[i]).newInstance();
        } catch (Fragment.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /*绑定生命周期*/
    @Override
    public <T> AutoDisposeConverter<T> bindAutoDispose() {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY));
    }
}
