package com.han.mvpdome.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import java.util.Stack;

/**
 * app管理类
 */
public class AppManager {
    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        if (activityStack != null && !activityStack.isEmpty()) {
            Activity activity = activityStack.lastElement();
            if (activity != null) {
                finishActivity(activity);
            }
        }

    }

    /**
     * 模拟进行弹栈操作，关闭对应的activity上的activity。 （跳向对应的activity）
     */
    public void turn2Activity(Class cla) {
        int num = 0;
        for (int i = 0; i < activityStack.size(); i++) {
            if (activityStack.get(i).getClass().equals(cla)) {
                num += 1;
            }
        }
        if (num == 0) {
            return;
        }
        try {
            for (int i = activityStack.size() - 1; i >= 0; i--) {
                if (!activityStack.get(i).getClass().equals(cla)) {
                    activityStack.get(i).finish();

                } else return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        try {
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    finishActivity(activity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取是否存在类名的Activity
     */
    public boolean haveActivity(Class<?> cls) {
        try {
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (null != activityStack) {
            for (int i = 0, size = activityStack.size(); i < size; i++) {
                if (null != activityStack.get(i)) {
                    activityStack.get(i).finish();
                }
            }
            activityStack.clear();
        }
    }

    /**
     * 结束Activity之外其他的activity
     */
    public void finishotherActivity(Activity act) {
        if (null != activityStack) {
            for (int i = 0, size = activityStack.size(); i < size; i++) {
                if (null != activityStack.get(i) && activityStack.get(i) != act) {
                    activityStack.get(i).finish();
                }
            }
            activityStack.clear();
            activityStack.add(act);
        }
    }


    /**
     * 检测activity是否finish
     */
    public static boolean isValidContext(Context c) {
        if (c == null) {
            return false;
        } else if (c instanceof Activity) {
            Activity a = (Activity) c;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                return !(a.isDestroyed() || a.isFinishing());
            }
        }
        return true;
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context
                    .ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            Logger.e("AppExit ", " e " + e.getMessage());
        }
    }

}
