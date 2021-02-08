package com.krys.exoplayer.utils;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.krys.exoplayer.R;
import com.krys.exoplayer.activities.DashboardActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationUtils {

    @SuppressLint("StaticFieldLeak")
    private static NotificationCompat.Builder mBuilder;
    private static final int REQUEST_CODE = 12;

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static NotificationChannel setNotiChannel() {
        NotificationChannel channel = new NotificationChannel(ConstantStrings.NOTIFICATION_CHANNEL_ID,
                ConstantStrings.NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription(ConstantStrings.NOTIFICATION_CONTENT_DESCRIPTION);
        channel.enableLights(true);
        return channel;
    }

    private static void getNotiManager(Context context){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (isOreo()) notificationManager.createNotificationChannel(setNotiChannel());
        notificationManager.notify(ConstantStrings.NOTIFICATION_ID, mBuilder.build());
    }

    public static void clearNotifications(Context context, int notificationId){
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);
    }

    private static boolean isOreo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    private static PendingIntent createPendingIntent(Context context, int requestCode, int flags, Intent resultIntent){
        return PendingIntent.getActivity(context, requestCode, resultIntent, flags);
    }

    private static Intent getIntent(Context context, Class<?> mClass){
        return new Intent(context, mClass);
    }

    public static void showSimpleNotification(Context context, String title, String message) {
        mBuilder = isOreo() ? new NotificationCompat.Builder(context, ConstantStrings.NOTIFICATION_CHANNEL_ID) : new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.ic_baseline_ondemand_video_24);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(message);
        mBuilder.setAutoCancel(true);
        mBuilder.setColor(ContextCompat.getColor(context, R.color.blue_A400));
        mBuilder.setContentIntent(createPendingIntent(context, REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT, getIntent(context, DashboardActivity.class)));
        getNotiManager(context);
    }

    public static void showBigTextNotification(Context context, String title, String message) {
        mBuilder = isOreo() ? new NotificationCompat.Builder(context, ConstantStrings.NOTIFICATION_CHANNEL_ID) : new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.ic_baseline_ondemand_video_24);
        mBuilder.setContentText(message);
        mBuilder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(message)
                .setSummaryText(title));
        mBuilder.setAutoCancel(true);
        mBuilder.setColor(ContextCompat.getColor(context, R.color.blue_A400));
        mBuilder.setContentIntent(createPendingIntent(context, REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT, getIntent(context, DashboardActivity.class)));
        getNotiManager(context);
    }

    public static void showProgressNotification(Context context, String title, String message) {
        mBuilder = isOreo() ? new NotificationCompat.Builder(context, ConstantStrings.NOTIFICATION_CHANNEL_ID) : new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.ic_baseline_ondemand_video_24);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(message);
        mBuilder.setAutoCancel(true);
        mBuilder.setOngoing(true);
        mBuilder.setOnlyAlertOnce(true);
        mBuilder.setPriority(NotificationCompat.PRIORITY_LOW);
        mBuilder.setColor(ContextCompat.getColor(context, R.color.blue_A400));
        mBuilder.setContentIntent(createPendingIntent(context, REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT, getIntent(context, DashboardActivity.class)));
        new Thread(new Runnable() {
            @Override
            public void run() {
                int incr;
                for (incr = 0; incr <= 100; incr+=5) {
                    mBuilder.setProgress(100, incr, false);
                    getNotiManager(context);
                    try {
                        Thread.sleep(1*1000);
                    } catch (InterruptedException e) {
                        Log.e("TAG", "sleep failure");
                    }
                }
                mBuilder.setContentText("Download completed")
                        .setProgress(0,0,false);
                getNotiManager(context);
            }
        }
        ).start();

    }

}
