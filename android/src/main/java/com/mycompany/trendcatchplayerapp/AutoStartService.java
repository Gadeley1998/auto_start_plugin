package com.mycompany.trendcatchplayerapp;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.app.ActivityManager;
import android.content.Context;

public class AutoStartService extends Service {

    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        runnable = new Runnable() {
            @Override
            public void run() {
                if (!isAppInForeground(getApplicationContext())) {
                    Intent i = new Intent();
                    i.setClassName("com.mycompany.trendcatchplayerapp", "com.mycompany.trendcatchplayerapp.MainActivity");
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
                handler.postDelayed(this, 60000); // repeat every 60s
            }
        };

        handler.post(runnable);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private boolean isAppInForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getPackageName();

        if (activityManager != null) {
            for (ActivityManager.RunningAppProcessInfo processInfo : activityManager.getRunningAppProcesses()) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
                        processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
