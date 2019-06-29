package com.yzhg.tool.utils;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.yzhg.tool.R;
import com.yzhg.tool.utils.common.LogUtils;
import com.yzhg.tool.utils.common.SDCardOperation;
import com.yzhg.tool.utils.toast.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * 类 名: Tools
 * 作 者: yzhg
 * 创 建: 2019/6/29 0029
 * 版 本: 4.1.0
 * 历 史: (版本) 作者 时间 注释
 * 描 述:
 */
public class Tools {

    @SuppressLint("StaticFieldLeak")
    private static Context context;


    private Tools() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static void init(Context context) {
        Tools.context = context.getApplicationContext();
    }

    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("请在Application中初始化操作");
    }


    //获取字符串
    public static String getString(int id) {
        return getContext().getResources().getString(id);
    }

    //获取图片资源
    public static Drawable getDrawable(int id) {
        return getContext().getResources().getDrawable(id);
    }

    //获取颜色
    public static int getColor(int id) {
        return getContext().getResources().getColor(id);
    }


    /////////////////////////////DP与PX之间的转换////////////////////////////////////////////////

    /**
     * dp与px之间的转换
     */
    public static int dip2px(float dip) {
        //获取屏幕的密度
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }


    public static float px2dip(float px) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return px / density;
    }

    ////////////////////////////////判断当前SDK版本///////////////////////////////////////////////////

    /**
     * 判断SDK版本
     */
    public static boolean jumpSDK(int SDKVersion) {
        return Build.VERSION.SDK_INT >= SDKVersion;
    }


    /*
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述:  获取当前版本号的方法
     */
    public static String getPackageVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context
                    .getPackageName(), 0);
            String versionName = packageInfo.versionName;
            return versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "获取版本号失败";
        }
    }

    public static int getPackageVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context
                    .getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 密码的md5加密
     */
    public static String encode(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : result) {
                int number = b & 0xff;
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    sb.append("0");
                }
                sb.append(str);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    //////////////////////////////////////////检测微信,QQ是否安装///////////////////////////////////////

    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断qq是否可用
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    /////////////////////////////////检测手机号 , 和邮箱是否符合规范////////////////////////////////

    /**
     * 加载View
     */
    public static View getLayoutID(int layoutId) {
        return LayoutInflater.from(context).inflate(layoutId, null);
    }


    /**
     * 保存图片到相册
     */
    public static boolean saveImage(Context context, View v) {
        try {
            String imagePath = saveImageToGallery(context, getViewBp(v));
            LogUtils.d("------图片保存路径------" + imagePath);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 将View转为BitMap
     *
     * @param v view
     * @return 返回bitmap
     */
    private static Bitmap getViewBp(View v) {
        if (null == v) {
            return null;
        }
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        v.measure(View.MeasureSpec.makeMeasureSpec(v.getWidth(),
                View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(
                v.getHeight(), View.MeasureSpec.EXACTLY));
        v.layout((int) v.getX(), (int) v.getY(),
                (int) v.getX() + v.getMeasuredWidth(),
                (int) v.getY() + v.getMeasuredHeight());
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache(), 0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        v.setDrawingCacheEnabled(false);
        v.destroyDrawingCache();
        return b;
    }

    /**
     * 保存图片
     *
     * @param context
     * @param bmp
     * @return
     * @throws Exception
     */
    public static String saveImageToGallery(Context context, Bitmap bmp) throws Exception {
        File appDir = SDCardOperation.ExistSDCardMkdirs("JDS");
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        FileOutputStream fos = new FileOutputStream(file);
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse
                ("file://" + file.getAbsolutePath())));
        return file.getAbsolutePath();
    }


    /*判断集合是否为空*/
    public static boolean isEmpty(List mList) {
        return mList == null || mList.size() == 0;
    }
    /**
     * 打开外部浏览器下载APP
     *
     * @param context
     * @param url
     */
    public static void openBrowser(Context context, String url) {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
        // 官方解释 : Name of the component implementing an activity that can display the intent
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            final ComponentName componentName = intent.resolveActivity(context.getPackageManager());
            // 打印Log   ComponentName到底是什么
            context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
        } else {
            Toast.makeText(context.getApplicationContext(), "请下载浏览器", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 打开权限设置页面
     */
    public static void openSettingPage(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 控制布局显示与隐藏
     */
    public static void setViewGone(int visibility, View... views) {
        for (View view : views) {
            view.setVisibility(visibility);
        }
    }

    /*
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述:
     *     复制到建忒版
     */
    public static void copyToClipboard(Context context, String text, int msg) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText("text", text));
        ToastUtils.show(msg);
    }

    //打开QQ,并打开临时会话
    public static void openQQ(Context context, String qqCode) {
        String url11 = "mqqwpa://im/chat?chat_type=wpa&uin=" + qqCode + "&version=1";
        if (Tools.isQQClientAvailable(context)) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url11)));
        } else {
            ToastUtils.show("未检测到QQ,或者版本不兼容");
        }
    }

    /**
     * 调用拨号界面
     *
     * @param phone 电话号码
     */
    public static void callPhone(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void openWx(Context context) {
        Intent intent = new Intent();
        ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cmp);
        context.startActivity(intent);
    }
}










