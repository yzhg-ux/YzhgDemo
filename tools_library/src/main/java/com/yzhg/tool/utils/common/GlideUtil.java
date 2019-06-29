package com.yzhg.tool.utils.common;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

/**
 * Created by $(剪刀手--yzhg) on 2018/5/31 0031.
 * 用一句话描述该类的用处:
 */
public class GlideUtil {

    /**
     * Glide特点
     * 使用简单
     * 可配置度高，自适应程度高
     * 支持常见图片格式 Jpg png gif webp
     * 支持多种数据源  网络、本地、资源、Assets 等
     * 高效缓存策略    支持Memory和Disk图片缓存 默认Bitmap格式采用RGB_565内存使用至少减少一半
     * 生命周期集成   根据Activity/Fragment生命周期自动管理请求
     * 高效处理Bitmap  使用Bitmap Pool使Bitmap复用，主动调用recycle回收需要回收的Bitmap，减小系统回收压力
     * 这里默认支持Context，Glide支持Context,Activity,Fragment，FragmentActivity
     */

    //默认加载
    public static void loadImageView(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).into(mImageView);
    }

    public static void loadImageView(Context mContext, Object path, ImageView mImageView) {
        Glide.with(mContext).load(path).into(mImageView);
    }

    public static void loadImageView(Context mContext, @DrawableRes int imgRes, ImageView mImageView) {
        Glide.with(mContext).load(imgRes).into(mImageView);
    }

    public static void loadImageView(Context mContext, File imgRes, ImageView mImageView, int lodingImage) {
        RequestOptions options = new RequestOptions().placeholder(lodingImage).error(lodingImage);
        Glide.with(mContext).load(imgRes).apply(options).into(mImageView);
    }

    //加载大小
    public static void loadImageView(Context mContext, String path, int width, int height, ImageView mImageView) {
        RequestOptions options = new RequestOptions().override(width, height);
        Glide.with(mContext).load(path).apply(options).into(mImageView);
    }

    //设置加载中以及加载失败图片
    public static void loadImageView(Context mContext, String path, ImageView mImageView, int lodingImage) {
        RequestOptions options = new RequestOptions().placeholder(lodingImage).error(lodingImage);
        Glide.with(mContext).load(path).apply(options).into(mImageView);
    }


    //设置加载中以及加载失败图片并且指定大小
    public static void loadImageView(Context mContext, String path, ImageView mImageView, int lodingImage, int errorImageView, boolean isloading) {
        RequestOptions options = new RequestOptions().placeholder(lodingImage).error(errorImageView);
        Glide.with(mContext).load(path).apply(options).into(mImageView);
    }

    //设置加载中以及加载失败图片并且指定大小
    public static void loadImageView(Context mContext, String path, int width, int height, ImageView mImageView, int lodingImage, int errorImageView) {
        RequestOptions options = new RequestOptions().placeholder(lodingImage).error(errorImageView).override(width, height);
        Glide.with(mContext).load(path).apply(options).into(mImageView);
    }

    //设置跳过内存缓存  isCache == true 跳过缓存
    public static void loadImageView(Context mContext, String path, ImageView mImageView, boolean isCache) {
        RequestOptions options = new RequestOptions().skipMemoryCache(isCache);
        Glide.with(mContext).load(path).apply(options).into(mImageView);
    }

    //设置下载优先级  Priority.NORMAL
    public static void loadImageView(Context mContext, String path, ImageView mImageView, Priority priority) {
        RequestOptions options = new RequestOptions().priority(priority);
        Glide.with(mContext).load(path).apply(options).into(mImageView);
    }

    public static void loadImageViewCrop(Context mContext, String path, ImageView mImageView, @DrawableRes int lodingImage) {
        RequestOptions options = new RequestOptions().placeholder(lodingImage).error(lodingImage);
        Glide.with(mContext).load(path).apply(options).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(mImageView);
    }

    public static void loadImageViewCrop(Context mContext, @DrawableRes int locationImage, ImageView mImageView, @DrawableRes int lodingImage) {
        RequestOptions options = new RequestOptions().placeholder(lodingImage).error(lodingImage);
        Glide.with(mContext).load(locationImage).apply(options).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(mImageView);
    }

    /**
     * 策略解说：
     * <p>
     * all:缓存源资源和转换后的资源
     * <p>
     * none:不作任何磁盘缓存
     * <p>
     * source:缓存源资源
     * <p>
     * result：缓存转换后的资源
     */

    //设置缓存策略
    public static void loadImageViewDiskCache(Context mContext, String path, ImageView mImageView) {
        RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext).load(path).apply(options).into(mImageView);
    }

    /**
     * 会先加载缩略图
     */

    //设置缩略图支持
    public static void loadImageViewThumbnail(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).thumbnail(0.1f).into(mImageView);
    }

    /**
     * api提供了比如：centerCrop()、fitCenter()等
     */

    //设置动态GIF加载方式
    public static void loadImageViewDynamicGif(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).into(mImageView);
    }

    //清理磁盘缓存
    public static void GuideClearDiskCache(Context mContext) {
        //理磁盘缓存 需要在子线程中执行
        Glide.get(mContext).clearDiskCache();
    }

    //清理内存缓存
    public static void GuideClearMemory(Context mContext) {
        //清理内存缓存  可以在UI主线程中进行
        Glide.get(mContext).clearMemory();
    }


}
