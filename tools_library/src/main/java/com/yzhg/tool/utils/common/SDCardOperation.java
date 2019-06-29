package com.yzhg.tool.utils.common;


import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;

public class SDCardOperation {

    /**
     * 判断当前目录是否存在,如果不存在就创建目录
     *
     * @param path
     * @return
     */
    public static File ExistSDCardMkdirs(String path) {
        /*判断当前SD卡是否存在,以及SD容量是否大于30M*/
        if (!ExistSDCard() || getSDFreeSize() < 30) {
        //    ToastTools.showToast("SD卡空间不足");
            return null;
        }
        File file = new File(Environment.getExternalStorageDirectory(), path);
        /*判断当前目录是否存在,如果不存在,就创建目录*/
        if (!file.exists()) {
            Log.i("设置目录", "ExistSDCardMkdirs: 创建目录");
            file.mkdirs();
        }
        return file;
    }

    /**
     * 判断SD卡是否存在
     */
    public static boolean ExistSDCard() {
        return Environment.getExternalStorageState().equals(Environment
                .MEDIA_MOUNTED);
    }

    /**
     * 判断SD卡剩余空间
     */
    public static long getSDFreeSize() {
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        //返回SD卡空闲大小
        //return freeBlocks * blockSize;  //单位Byte
        //return (freeBlocks * blockSize)/1024;   //单位KB
        return (freeBlocks * blockSize) / 1024 / 1024; //单位MB
    }

    /**
     * 判断SD卡容量
     */
    public long getSDAllSize() {
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //获取所有数据块数
        long allBlocks = sf.getBlockCount();
        //返回SD卡大小
        //return allBlocks * blockSize; //单位Byte
        //return (allBlocks * blockSize)/1024; //单位KB
        return (allBlocks * blockSize) / 1024 / 1024; //单位MB
    }
}
