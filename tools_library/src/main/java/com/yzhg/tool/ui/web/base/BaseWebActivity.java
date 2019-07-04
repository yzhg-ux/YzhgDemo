package com.yzhg.tool.ui.web.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yzhg.tool.utils.common.ActivitysManager;
import com.yzhg.tool.utils.common.PermissionTools;
import com.yzhg.tool.utils.toast.ToastUtils;


/**
 * 类 名:
 * 作 者: yzhg
 * 创 建: 2019/3/22 0022
 * 版 本: 1.0
 * 历 史: (版本) 作者 时间 注释
 * 描 述:
 */
public class BaseWebActivity extends AppCompatActivity {

    private ActivitysManager activitysManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitysManager = ActivitysManager.getInstance();
        activitysManager.addActivity(this);
    }

    protected void cameraReadWriteStorage() {

    }

    protected void camera() {

    }

    protected void chooseImage() {

    }

    protected void chooseVideo() {

    }

    protected void chooseAudio() {

    }

    protected void requestPermission(final int type, String... permissions) {
        new PermissionTools.Builder().setPermissionListener(new PermissionTools.Builder.PermissionListener() {
            @Override
            public void permissionSuccess() {
                //权限申请成功
                if (type == BaseWebViewActivity.permission_CAMERA_READ_WRITE_EXTERNAL_STORAGE) {
                    cameraReadWriteStorage();
                } else if (type == BaseWebViewActivity.permission_CAMERA) {
                    camera();
                } else if (type == BaseWebViewActivity.permission_IMAGE) {
                    chooseImage();
                } else if (type == BaseWebViewActivity.permission_VIDEO) {
                    chooseVideo();
                } else if (type == BaseWebViewActivity.permission_AUDIO) {
                    chooseAudio();
                }
            }

            @Override
            public void permissionFile() {
                //权限申请失败
                ToastUtils.show("权限获取失败！部分功能无法使用");
            }
        }).morePermission(this, permissions);
    }
}













