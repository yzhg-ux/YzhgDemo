package com.yzhg.tool.utils.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;



/**
 * 作者 剪刀手 , 时间 2018/1/4.
 * <p>
 * 警句:真英雄大勇无谓,好风景总在险峰
 * 概括(一句话总结该类用法)
 */

public class CustomDialog extends Dialog {


    private Context context;
    private int height, width;
    private boolean cancelTouchOut;
    private View view;
    private int animations;
    private int gravity = Gravity.CENTER; //设置显示的位置,默认剧中
    private boolean showAddress;  //是否设置dialog现实在底部,底部距离为0,宽度与屏幕宽度相同
    private boolean setCancelAble = false;  //设置是否屏蔽返回
    private boolean clearShade = false;

    private CustomDialog(Builder builder) {
        super(builder.context);
        context = builder.context;
        cancelTouchOut = builder.cancelTouchOut;
        animations = builder.animations;
        view = builder.view;
        gravity = builder.gravity;
        showAddress = builder.showAddress;
        setCancelAble = builder.setCancelAble;
        clearShade = builder.clearShade;
    }

    private CustomDialog(Builder builder, int resStyle) {
        super(builder.context, resStyle);
        context = builder.context;
        cancelTouchOut = builder.cancelTouchOut;
        animations = builder.animations;
        view = builder.view;
        gravity = builder.gravity;
        showAddress = builder.showAddress;
        setCancelAble = builder.setCancelAble;
        clearShade = builder.clearShade;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(view);
        setCanceledOnTouchOutside(cancelTouchOut);
        setCancelable(setCancelAble);
        Window window = getWindow();
        window.setWindowAnimations(animations);
        WindowManager.LayoutParams wlp = window.getAttributes();
        if (showAddress) {
            window.setBackgroundDrawableResource(android.R.color.transparent);  //设置背景,如果不设置,一下设置无效
            Display Dd = window.getWindowManager().getDefaultDisplay();  //获取屏幕状态
            wlp.width = Dd.getWidth();
            wlp.y = 0;
        } else {
            wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        }
        if (clearShade) {
            window.setDimAmount(0f);
        }
        wlp.gravity = gravity;
        window.setAttributes(wlp);
    }

    public static final class Builder {
        private Context context;
        private boolean cancelTouchOut = true;
        private boolean setCancelAble = true;  //设置是否屏蔽返回
        private boolean clearShade = false;  //设置阴影
        private View view;
        private int dialogStyle = -1;
        private int animations;
        private int gravity;
        private boolean showAddress = false;

        /*设置上下文对象*/
        public Builder(Context context) {
            this.context = context;
        }

        /*设置布局*/
        public Builder setView(int dialogLayout) {
            view = LayoutInflater.from(context).inflate(dialogLayout, null);
            return this;
        }

        public Builder setView(View dialogLayout) {
            view = dialogLayout;
            return this;
        }


        /*设置Dialog弹框样式*/
        public Builder setStyle(int resStyle) {
            this.dialogStyle = resStyle;
            return this;
        }

        /*设置dialog弹框动画*/
        public Builder setAnimation(int animations) {
            this.animations = animations;
            return this;
        }

        /*设置dialog是否显示在底部,距离底部边距为0,宽度显示为屏幕宽度*/
        public Builder setShowBottom(boolean showAddress) {
            this.showAddress = showAddress;
            return this;
        }

        /*设置点击dialog外面是否关闭Dialog*/
        public Builder setCancelTouchOut(boolean val) {
            cancelTouchOut = val;
            return this;
        }

        /*设置是否屏蔽返回*/
        public Builder setCancelAble(boolean setCancelAble) {
            this.setCancelAble = setCancelAble;
            return this;
        }

        public Builder setClearShade(boolean clearShade) {
            this.clearShade = clearShade;
            return this;
        }

        /*设置dialog显示的位置*/
        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        /*设置dialog的点击事件*/
        public Builder setViewOnClick(int viewRes, View.OnClickListener listener) {
            view.findViewById(viewRes).setOnClickListener(listener);
            return this;
        }

        /*设置dialog的点击事件*/
        public Builder setViewVisibility(int viewRes, int visibility) {
            view.findViewById(viewRes).setVisibility(visibility);
            return this;
        }

        public Builder setText(int viewRes, String text) {
            ((TextView) view.findViewById(viewRes)).setText(text);
            return this;
        }

        public Builder setText(int viewRes, SpannableStringBuilder text) {
            ((TextView) view.findViewById(viewRes)).setText(text);
            return this;
        }

        public Builder setIntText(int viewRes, int text) {
            ((TextView) view.findViewById(viewRes)).setText(context.getResources().getString(text));
            return this;
        }

        public Builder setHtmlText(int viewRes, String text) {
            ((TextView) view.findViewById(viewRes)).setText(Html.fromHtml(text));
            return this;
        }

        /*创建Dialog弹框*/
        public CustomDialog build() {
            if (dialogStyle != -1) {
                return new CustomDialog(this, dialogStyle);
            } else {
                return new CustomDialog(this);
            }
        }
    }
}
