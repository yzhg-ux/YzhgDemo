package com.yzhg.tool.base.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import com.yzhg.tool.R;
import com.yzhg.tool.utils.common.ActivitysManager;
import com.yzhg.tool.utils.common.CustomDialog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * 类 名: .
 * 作 者: yzhg
 * 创 建: 2018/9/2/002.
 * 版 本: 1.0
 * 历 史: (版本) 作者 时间 注释
 * 描 述:
 */
public abstract class BasicActivity extends AppCompatActivity {

    protected Activity mContent;
    protected CustomDialog showLoadding;

    private ActivitysManager activitysManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = getLayoutInflater().inflate(getLayoutId(), null);
        setContentView(contentView);
        activitysManager = ActivitysManager.getInstance();
        activitysManager.addActivity(this);
    }

    protected abstract int getLayoutId();

    protected void showBaseLoadingDialog(String msg) {
        showDialog(msg);
    }

    protected void hideBaseLoadingDialog() {
        if (showLoadding != null && showLoadding.isShowing()) showLoadding.dismiss();
    }

    private void showDialog(String msg) {
        /*初始化加载框*/
        showLoadding = new CustomDialog.Builder(this)
                .setView(R.layout.load_dialog)
                .setStyle(R.style.MyDialogTheme)
                .setAnimation(R.style.dialogAnimation)
                .setText(R.id.tv_load_dialog, msg)
                .setClearShade(true)
                .setCancelTouchOut(false)
                .setGravity(Gravity.CENTER)
                .build();
        showLoadding.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mContent != null) mContent = null;
        if (showLoadding != null && showLoadding.isShowing()) showLoadding.dismiss();
        activitysManager.removeActivity(this);
    }
}
