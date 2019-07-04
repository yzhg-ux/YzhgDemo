package com.yzhg.tool.ui.web;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.yzhg.tool.R;
import com.yzhg.tool.ui.web.base.BaseWebViewActivity;

/**
 * 类 名: CommonWebViewActivity
 * 作 者: yzhg
 * 创 建: 2019/3/22 0022
 * 版 本: 1.0
 * 历 史: (版本) 作者 时间 注释
 * 描 述:
 */
public class CommonWebViewActivity extends BaseWebViewActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_webview);
        initViews();
    }

    protected RelativeLayout rl_loading_faild;

    private void initViews() {
        rl_loading_faild = findViewById(R.id.rl_loading_faild);
        rl_loading_faild.setOnClickListener(v -> againLoad());
    }


    protected void againLoad() {
        if (webView != null) {
            webView.setVisibility(View.VISIBLE);
        }
        rl_loading_faild.setVisibility(View.GONE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (islandport) {
                if (chromeClient != null) {
                    chromeClient.onHideCustomView();
                }
            } else {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
