package com.yzhg.tool.utils.common;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/****
 *  Android 随时随地的退出
 */
public class ActivitysManager {

    private static ActivitysManager activitysManager;

    /**
     * 存放Activity的map
     */
    private List<Activity> mActivities = new ArrayList<>();

    //将构造方法私有化，所以不能通构造方法来初始化ActivityManager
    private ActivitysManager() {
    }

    //采用单例模式初始化ActivityManager，使只初始化一次
    public static ActivitysManager getInstance() {
        if (activitysManager == null) {
            activitysManager = new ActivitysManager();
        }
        return activitysManager;
    }

    //添加activity
    public void addActivity(Activity activity) {
        if (!mActivities.contains(activity)) {
            mActivities.add(activity);
        }
    }

    //移除activity
    public void removeActivity(Activity activity) {
        if (activity != null) {
            if (mActivities.contains(activity)) {
                mActivities.remove(activity);
            }
            activity.finish();
            activity = null;
        }
    }

    public boolean judgeActivity(Activity activity) {
        return mActivities.contains(activity);
    }

    //将activity全部关闭掉
    public void clearAll() {
        for (Activity activity : mActivities) {
            activity.finish();
        }
    }

    //关闭掉MainAcitiy以外的activity
    public void clearOther() {

        for (Activity activity : mActivities) {
            if (activity.getClass().getSimpleName().equals("MainActivity")) {

                continue;
            }
            activity.finish();
        }
    }

}
