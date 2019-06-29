package com.yzhg.tool.utils.common;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by $(剪刀手--yzhg) on 2018/8/13 0013.
 * 用一句话描述该类的用处:
 * <p>
 * 权限申请
 */
public class PermissionTools {
    public static final int ALL_PERMISSION_CODE = 10000;
    public static final int READ_CONTACTS_CODE = 10001;
    public static final int CALL_PHONE_CODE = 10002;
    public static final int READ_CALENDAR_CODE = 10003;
    public static final int CAMERA_CODE = 10004;
    public static final int BODY_SENSORS_CODE = 10005;
    public static final int ACCESS_FINE_LOCATION_CODE = 10006;
    public static final int WRITE_EXTERNAL_STORAGE_CODE = 10007;

    public static final int READ_EXTERNAL_STORAGE_CODE = 10008;
    public static final int RECORD_AUDIO_CODE = 10009;
    public static final int SEND_SMS_CODE = 10010;
    public static final int READ_CALL_LOG_CODE = 10011;
    public static final int WRITE_SETTINGS_STORAGE_CODE = 10012;


    public static final String READ_CONTACTS = Manifest.permission.READ_CONTACTS;  //允许程序访问联系人通讯录信息
    public static final String CALL_PHONE = Manifest.permission.CALL_PHONE;  //允许程序从非系统拨号器里拨打电话
    public static final String READ_CALENDAR = Manifest.permission.READ_CALENDAR;  //允许程序读取用户的日志信息
    public static final String CAMERA = Manifest.permission.CAMERA;  //允许程序访问摄像头进行拍照
    public static final String BODY_SENSORS = Manifest.permission.BODY_SENSORS;  //感应器权限
    public static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;// 允许程序通过GPS芯片接收卫星的定位信息
    public static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE; //SD卡读写权限
    public static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE; //SD卡读取权限
    public static final String WRITE_SETTINGS_STORAGE = Manifest.permission.WRITE_SETTINGS; //SD卡读取权限
    public static final String RECORD_AUDIO = Manifest.permission.RECORD_AUDIO; // 允许程序录制声音通过手机或耳机的麦克
    public static final String SEND_SMS = Manifest.permission.SEND_SMS;   //允许程序发送短信
    public static final String READ_SMS = Manifest.permission.READ_SMS;   //允许程序发送短信
    public static final String READ_CALL_LOG = Manifest.permission.READ_CALL_LOG;   //获取手机通讯录
    //
    public static String[] permission = {
            /*允许获取手机通讯录*/
            READ_CONTACTS,
            READ_CALL_LOG,
            /*直接拨打电话*/
            CALL_PHONE,
            /*获取手机位置*/
            ACCESS_FINE_LOCATION,
            /*获取手机SD卡权限*/
            WRITE_EXTERNAL_STORAGE
    };


    /**
     * 使用Builder设计模式,打造6.0动态权限申请
     */
    public static class Builder {
        /*在Fragment中申请权限时,需要和Activity区别开来*/
        private Fragment mFragment = null;
        private PermissionListener mListener;

        /**
         * 如果是Fragment 需要设置此方法,
         *
         * @param mFragment , Fragment实体
         * @return
         */
        public Builder setFragmenty(Fragment mFragment) {
            this.mFragment = mFragment;
            return this;
        }

        /**
         * 单个权限申请,
         *
         * @param context    上下文
         * @param permission 单个权限
         * @param ResultCode 权限码
         */
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void applyPermission(final Activity context, String permission, int ResultCode) {
            /**
             * 检查单个权限,
             *  返回结果
             *  1. PackageManager.PERMISSION_DENIED
             *  2. PackageManager.PERMISSION_GRANTED
             *  如果返回为-1 (1) 则 需要申请权限
             */
            int resultCode = ContextCompat.checkSelfPermission(context, permission);
            if (PackageManager.PERMISSION_DENIED == resultCode) {
                /**
                 * 判断用户是否选择了不在提示,如果用户选择了不在提示
                 * 则需要用户跳转到设置页面进行授权
                 */
                applySinglePermission(context, permission, ResultCode);
            } else if (PackageManager.PERMISSION_GRANTED == resultCode) {
                if (mListener != null) {
                    mListener.permissionSuccess();
                } else {
                    throw new NullPointerException("pListener为空,请检查mListener是否已经申请");
                }
            } else {
                throw new RuntimeException("自定义运行时异常,此异常不会出现,一旦出现则官方API出现错误");
            }
        }

        /**
         * 申请单个权限
         * 判断mFragment是否为空,则判断是在Activity和Fragment中
         */
        @RequiresApi(api = Build.VERSION_CODES.M)
        private void applySinglePermission(Activity context, String permission, int RequestCode) {
            if (mFragment == null) {
                ActivityCompat.requestPermissions(context, new String[]{permission}, RequestCode);
            } else {
                mFragment.requestPermissions(new String[]{permission}, RequestCode);
            }
        }

        /**
         * 申请多个权限调用的方法,这里只在开屏页申请一次,不检查是否点击过拒绝
         */
        public void morePermission(Activity context, String[] permissions) {
            List<String> mPermissionList = new ArrayList<>();
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                    mPermissionList.add(permission);
                }
            }
            if (mPermissionList.isEmpty()) {
                /*都已经授权了,通过接口返回*/
                if (mListener != null) {
                    mListener.permissionSuccess();
                }
            } else {
                String[] noPermission = mPermissionList.toArray(new String[mPermissionList.size()]);
                ActivityCompat.requestPermissions(context, noPermission, ALL_PERMISSION_CODE);
                if (mListener != null) {
                    mListener.permissionFile();
                }
            }
        }

        public Builder setPermissionListener(PermissionListener mListener) {
            this.mListener = mListener;
            return this;
        }

        /**
         * 通过接口回调的方式,将回调发送到使用方
         */
        public interface PermissionListener {
            //获取权限成功
            void permissionSuccess();

            //获取权限失败
            void permissionFile();
        }
    }

    public static void onRequestAllPermissionsResult(Activity context, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int i = 0; i < grantResults.length; ++i) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(context, permissions[i])) {
                    new AlertDialog
                            .Builder(context)
                            .setMessage("获取" + resultCode(requestCode) + "失败,将导致部分功能无法正常使用，需要到设置页面手动授权")
                            .setPositiveButton("去授权", (dialog, which) -> {
                                //引导用户至设置页手动授权
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", context.getApplicationContext().getPackageName(), null);
                                intent.setData(uri);
                                context.startActivity(intent);
                            })
                            /*权限授权失败*/
                            .setNegativeButton("取消", (dialog, which) -> {
                                if (mListener != null) mListener.permissionFile(-2);
                            })
                            /*权限授权失败*/
                            .setOnCancelListener(dialog -> {
                                if (mListener != null) mListener.permissionFile(-2);
                            })
                            .setCancelable(false)
                            .show();
                } else {
                    //授权失败,用户没有选中不在提示
                    if (mListener != null) mListener.permissionFile(-1);
                }
                break;
            } else {
                if (mListener != null) mListener.permissionSuccess();
            }
        }
    }


    private static PermissionListener mListener;

    public static void setPermissionListener(PermissionListener mListener) {
        PermissionTools.mListener = mListener;
    }

    public interface PermissionListener {
        //获取权限失败
        void permissionFile(int fileIndex);

        void permissionSuccess();
    }

    public static String resultCode(int requestCode) {
        String permissionTest = "";
        switch (requestCode) {
            case READ_CONTACTS_CODE:
                permissionTest = "通讯录信息权限";
                break;
            case CALL_PHONE_CODE:
                permissionTest = "直接拨打电话权限";
                break;
            case READ_CALENDAR_CODE:
                permissionTest = "读取日志信息权限";
                break;
            case CAMERA_CODE:
                permissionTest = "访问摄像头权限";
                break;
            case BODY_SENSORS_CODE:
                permissionTest = "获取感应器权限";
                break;
            case ACCESS_FINE_LOCATION_CODE:
                permissionTest = "手机当前定位权限";
                break;
            case WRITE_EXTERNAL_STORAGE_CODE:
                permissionTest = "SD卡写入权限";
                break;
            case READ_EXTERNAL_STORAGE_CODE:
                permissionTest = "SD卡读取权限";
                break;
            case RECORD_AUDIO_CODE:
                permissionTest = "访问麦克权限";
                break;
            case SEND_SMS_CODE:
                permissionTest = "发送短信权限";
                break;
            case READ_CALL_LOG_CODE:
                permissionTest = "手机通讯录权限";
                break;
        }
        return permissionTest;
    }

}
