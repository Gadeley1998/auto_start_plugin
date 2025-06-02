package com.mycompany.trendcatchplayerapp;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AppRestartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!isAppInForeground(context)) {
            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage("com.mycompany.trendcatchplayerapp");
            if (launchIntent != null) {
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(launchIntent);
            }
        }
    }

    private boolean isAppInForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null) {
            String packageName = context.getPackageName();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                for (ActivityManager.AppTask task : am.getAppTasks()) {
                    if (task.getTaskInfo().baseIntent.getComponent().getPackageName().equals(packageName)) {
                        return true;
                    }
                }
            } else {
                for (ActivityManager.RunningAppProcessInfo appProcess : am.getRunningAppProcesses()) {
                    if (appProcess.processName.equals(packageName) &&
                        appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
