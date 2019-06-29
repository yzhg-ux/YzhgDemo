package com.yzhg.tool.utils.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.yzhg.tool.R;


/**
 * 作 者: yzhg
 * 历 史: (版本) 1.0
 * 描 述: 倒计时控件
 */
public class CountDomeTime {

    private static Animation animation;

    public static CountDomeTime createTime(Context context) {
        if (animation == null)
            animation = AnimationUtils.loadAnimation(context, R.anim.animation_text);
        return new CountDomeTime();
    }

    @SuppressLint("SetTextI18n")
    public CountDownTimer countDomeTimer(final TextView view, final View view1, final View view2) {
        return new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long l = millisUntilFinished / 1000;
                view.setText("剩余" + ((l < 10) ? "0" + l : String.valueOf(l)) + "秒");
            }

            @Override
            public void onFinish() {
                view.setEnabled(false);
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.GONE);
            }
        };
    }
}
