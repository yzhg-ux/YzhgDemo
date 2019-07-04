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
        setMaxAspect();
        View contentView = getLayoutInflater().inflate(getLayoutId(), null);
        setContentView(contentView);
        if (setStatusBarLucency()) {
            setStatusBarFullTransparent();
            setFitSystemWindow(false);
        }
        activitysManager = ActivitysManager.getInstance();
        activitysManager.addActivity(this);
    }



    @Override
    protected void onResume() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            boolean result = fixOrientation();
        }
        super.onResume();
    }

    private boolean isTranslucentOrFloating() {
        boolean isTranslucentOrFloating = false;
        try {
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray ta = obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean) m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

    private boolean fixOrientation() {
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo) field.get(this);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /*
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 设置全屏标题栏透明
     */
    public boolean setStatusBarLucency() {
        return true;
    }

    /**
     * 如果需要内容紧贴着StatusBar
     * 应该在对应的xml布局文件中，设置根布局fitsSystemWindows=true。
     */
    private View contentViewGroup;

    protected void setFitSystemWindow(boolean fitSystemWindow) {
        if (contentViewGroup == null) {
            contentViewGroup = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        }
        contentViewGroup.setFitsSystemWindows(fitSystemWindow);
    }

    /**
     * 全透状态栏
     */
    protected void setStatusBarFullTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void KeyBoardCancle() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        if (startActivitySelfCheck(intent)) {
            super.startActivityForResult(intent, requestCode, options);
        }
    }


    private String mActivityJumpTag;
    private long mActivityJumpTime;

    /*检查当前 Activity 是否重复跳转了，不需要检查则重写此方法并返回 true 即可*/
    protected boolean startActivitySelfCheck(Intent intent) {
        // 默认检查通过
        boolean result = true;
        // 标记对象
        String tag;
        if (intent.getComponent() != null) {
            tag = intent.getComponent().getClassName();
        } else if (intent.getAction() != null) { // 隐式跳转
            tag = intent.getAction();
        } else {
            return result;
        }
        if (tag.equals(mActivityJumpTag) && mActivityJumpTime >= SystemClock.uptimeMillis() - 500) {
            // 检查不通过
            result = false;
        }
        // 记录启动标记和时间
        mActivityJumpTag = tag;
        mActivityJumpTime = SystemClock.uptimeMillis();
        return result;
    }

    /**
     * 作 者: yzhg
     * 创 建: ${DATE}.
     * 版 本: 1.0
     * 描 述: 适配全面屏手机
     */
    public void setMaxAspect() {
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (applicationInfo == null) {
            throw new IllegalArgumentException(" get application info = null, has no meta data! ");
        }
        applicationInfo.metaData.putString("android.max_aspect", "2.3");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mContent != null) mContent = null;
        if (showLoadding != null && showLoadding.isShowing()) showLoadding.dismiss();
        activitysManager.removeActivity(this);
    }
}
