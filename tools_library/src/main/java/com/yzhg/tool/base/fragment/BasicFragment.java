package com.yzhg.tool.base.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.yzhg.tool.R;
import com.yzhg.tool.utils.common.CustomDialog;
import com.yzhg.tool.utils.common.SPUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 类 名: BasicFragment.
 * 作 者: yzhg
 * 创 建: 2018/9/1/001.
 * 版 本: 1.0
 * 历 史: (版本) 作者 时间 注释
 * 描 述:
 */
public abstract class BasicFragment extends Fragment {

    protected View mRootView;
    protected Activity context;
    private CustomDialog customDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);

        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(mRootView);
    }

    protected void showBaseLoadingDialog(String msg) {
        showDialog(msg);
    }

    private void showDialog(String msg) {
        /*初始化加载框*/
        customDialog = new CustomDialog.Builder(getActivity())
                .setView(R.layout.load_dialog)
                .setStyle(R.style.MyDialogTheme)
                .setAnimation(R.style.dialogAnimation)
                .setText(R.id.tv_load_dialog, msg)
                .setClearShade(true)
                .setGravity(Gravity.CENTER)
                .build();
        customDialog.show();
    }

    protected void closeBaseLoadingDialog() {
        if (customDialog != null && customDialog.isShowing()) {
            customDialog.dismiss();
        }
    }

    protected void init(View view) {
        context = getActivity();
    }

    protected abstract int getLayoutId();

}
