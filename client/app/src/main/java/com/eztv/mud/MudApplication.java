package com.eztv.mud;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import com.eztv.mud.controller.SocketService;

import java.util.ArrayList;
import java.util.List;

public class MudApplication extends Application {

    private static MudApplication instance = new MudApplication();
    private static List<Activity> activityStack = new ArrayList<Activity>();

    public static MudApplication getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, SocketService.class));
    }


    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new ArrayList<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 移除指定的Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity = null;
        }
    }
    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
        System.exit(0);
    }
}
