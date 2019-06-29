package com.yzhg.tool.base.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by $(剪刀手--yzhg) on 2018/4/9 0009.
 * 用一句话描述: 懒加载APP,用于
 */

public abstract class LazyLoadFragment extends BasicFragment {

    private boolean isFirstLoad = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(getLayoutId(), container, false);
        context = getActivity();
        isFirstLoad = true;//视图创建完成，将变量置为true
        return view;
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
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isFirstLoad && isVisibleToUser) {//视图变为可见并且是第一次加载
            onLazyLoad();
            isFirstLoad = false;
        }
    }


    /**
     * 页面跳转
     */
    public void startActivity(Class<?> clazz) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }


    //数据加载接口，留给子类实现
    protected abstract void onLazyLoad();

    //初始化视图接口，子类必须实现
    //public abstract View initView(LayoutInflater inflater, @Nullable ViewGroup container);
    protected abstract int getLayoutId();
}
