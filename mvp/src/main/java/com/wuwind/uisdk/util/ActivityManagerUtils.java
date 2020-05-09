package com.wuwind.uisdk.util;

import android.app.Activity;

import java.util.Iterator;
import java.util.Stack;

public class ActivityManagerUtils {
    //activity栈
    private Stack<Activity> activityStack = new Stack<>();

    private ActivityManagerUtils() {
    }

    private static class SingletonHolder {
        private static final ActivityManagerUtils INSTANCE = new ActivityManagerUtils();
    }

    public static ActivityManagerUtils getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 把一个activity压入栈中
     *
     * @param activity activity
     */
    public void pushOneActivity(Activity activity) {
        activityStack.add(activity);
    }

    /**
     * 移除一个activity
     *
     * @param activity activity
     */
    public void popOneActivity(Activity activity) {
        if (activityStack != null && activityStack.size() > 0) {
            if (activity != null) {
                activityStack.remove(activity);
                activity.finish();
            }
        }
    }

    /**
     * 移除一个activity
     *
     * @param activity activity
     */
    public synchronized void removeOneActivity(Activity activity) {
        if (activityStack != null && activityStack.size() > 0) {
            if (activity != null) {
                activityStack.remove(activity);
            }
        }
    }

    /**
     * 获取栈顶的activity，先进后出原则
     *
     * @return 栈顶的activity
     */
    public Activity getLastActivity() {
        return activityStack.lastElement();
    }

    /**
     * 结束指定的Activity
     *
     * @param activity activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     *
     * @param cls 指定的Activity
     */
    public void finishActivity(Class<?> cls) {
        Iterator<Activity> iterator = activityStack.iterator();
        Activity tempActivity;
        while (iterator.hasNext()) {
            tempActivity = iterator.next();
            if (tempActivity.getClass().equals(cls)) {
                iterator.remove();
                tempActivity.finish();
            }
        }
    }

    /**
     * 结束指定类名的Activity
     *
     * @param classes 指定的Activitys
     */
    public synchronized void finishActivity(Class<?>... classes) {
        Iterator<Activity> iterator;
        Activity tempActivity;
        for (Class cls : classes) {
            iterator = activityStack.iterator();
            while (iterator.hasNext()) {
                tempActivity = iterator.next();
                if (tempActivity.getClass().equals(cls)) {
                    iterator.remove();
                    tempActivity.finish();
                }
            }
        }
    }

    /**
     * 结束所有activity
     */
    public void finishAllActivity() {
        try {
            for (int i = 0; i < activityStack.size(); i++) {
                if (null != activityStack.get(i)) {
                    activityStack.get(i).finish();
                }
            }
            activityStack.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束所有activity除了指定的activity
     */
    public void finishAllActivityExceptMyself(Class<?> cls) {
        Iterator<Activity> iterator = activityStack.iterator();
        Activity tempActivity;
        while (iterator.hasNext()) {
            tempActivity = iterator.next();
            if (!tempActivity.getClass().equals(cls)) {
                iterator.remove();
                tempActivity.finish();
            }
        }
    }

    /**
     * 退出应用程序
     */
    public void appExit() {
        try {
            finishAllActivity();
            //退出JVM(java虚拟机),释放所占内存资源,0表示正常退出(非0的都为异常退出)
            System.exit(0);
            //从操作系统中结束掉当前程序的进程
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
