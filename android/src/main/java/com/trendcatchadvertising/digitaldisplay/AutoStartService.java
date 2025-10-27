package com.trendcatchadvertising.digitaldisplay;

import android.app.Service;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.app.ActivityManager;

import androidx.core.app.NotificationCompat;

public class AutoStartService extends Service {

    private Handler handler;
    private Runnable watchdogRunnable;

    private static final String CHANNEL_ID = "TrendcatchAutoStartChannel";
    private static final int NOTIFICATION_ID = 101;

    // Surveillance intelligente
    private long currentInterval = 30000; // 30 s si instable
    private final long STABLE_INTERVAL = 120000;   // 120 s si stable
    private final long UNSTABLE_INTERVAL = 30000;  // 30 s si instable

    private int stabilityCounter = 0;
    private static final int MAX_STABILITY = 5;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        createNotificationChannel();
        startForegroundServiceWithLaunchIntent();
    }

    private void startForegroundServiceWithLaunchIntent() {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        if (launchIntent == null) {
            // Fallback minimal vers l’appli elle-même si le launch intent est indisponible
            launchIntent = new Intent();
            launchIntent.setPackage(getPackageName());
        }
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                launchIntent,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        ? PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
                        : PendingIntent.FLAG_UPDATE_CURRENT
        );

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Digital Display en exécution")
                .setContentText("Protection active contre fermetures système")
                .setSmallIcon(android.R.drawable.ic_popup_sync)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(NOTIFICATION_ID, notification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Digital Display Autostart",
                    NotificationManager.IMPORTANCE_LOW
            );
            serviceChannel.setShowBadge(false);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        watchdogRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (isAppVisibleOrForeground(getApplicationContext())) {
                        stabilityCounter++;
                        if (stabilityCounter >= MAX_STABILITY) {
                            currentInterval = STABLE_INTERVAL;
                        }
                    } else {
                        stabilityCounter = 0;
                        currentInterval = UNSTABLE_INTERVAL;
                        restartAppViaLaunchIntent();
                    }
                } finally {
                    handler.postDelayed(this, currentInterval);
                }
            }
        };

        handler.post(watchdogRunnable);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null && watchdogRunnable != null) {
            handler.removeCallbacks(watchdogRunnable);
        }
        restartService();
    }

    private void restartAppViaLaunchIntent() {
        PackageManager pm = getPackageManager();
        Intent launch = pm.getLaunchIntentForPackage(getPackageName());
        if (launch != null) {
            launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(launch);
        }
    }

    private void restartService() {
        Intent intent = new Intent(getApplicationContext(), AutoStartService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getApplicationContext().startForegroundService(intent);
        } else {
            getApplicationContext().startService(intent);
        }
    }

    private boolean isAppVisibleOrForeground(Context context) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String pkg = context.getPackageName();

        if (activityManager != null) {
            for (ActivityManager.RunningAppProcessInfo appProcess :
                    activityManager.getRunningAppProcesses()) {

                if (appProcess.processName.equals(pkg)) {
                    int imp = appProcess.importance;
                    return imp == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                            || imp == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;
                }
            }
        }
        return false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
