package cn.flandre.review.tools;

import android.app.*;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.Vibrator;
import androidx.core.app.NotificationCompat;
import cn.flandre.review.R;

public class NotificationHelper {
    public static final int NOTIFICATION_ID = 0x49502;  // 通知栏ID
//    public static final int ID = 495;

    public static Notification getNotification(Context context) {
        Notification notification;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // 当版本大于26时要设置channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Novel Music";
            String description = "Looking Book And Listen This Music ：)";
            int importance = NotificationManager.IMPORTANCE_MIN;
            NotificationChannel mChannel = new NotificationChannel(String.valueOf(NOTIFICATION_ID), name, importance);
            mChannel.setDescription(description);
            mChannel.setLightColor(Color.RED);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(mChannel);
        }
        final Intent nowPlayingIntent = new Intent();
        nowPlayingIntent.setComponent(new ComponentName("cn.flandre.review", "cn.flandre.review.activity.ReviewActivity"));
        nowPlayingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent click = PendingIntent.getActivity(context, 0, nowPlayingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, String.valueOf(NOTIFICATION_ID))
                .setOngoing(true)
                .setContentTitle("今天不学习，明天变傻逼")
                .setContentText("一个小时过去了，只要持之以恒，知识丰富了，终能发现其奥秘")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setContentIntent(click)
                .setWhen(System.currentTimeMillis());
        notification = builder.build();
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        return notification;
    }

    public static void cancel(Service service){
        service.stopForeground(true);
    }

    public static void notify(Service service){
        Vibrator mVibrator = (Vibrator) service.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0,1000,1000};
        AudioAttributes audioAttributes = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM) //key
                    .build();
            mVibrator.vibrate(pattern, -1, audioAttributes);
        }else {
            mVibrator.vibrate(pattern, -1);
        }

        service.startForeground(NotificationHelper.NOTIFICATION_ID,
                NotificationHelper.getNotification(service));
    }
}
