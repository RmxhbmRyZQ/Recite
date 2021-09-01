package cn.flandre.review.tools;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import cn.flandre.review.logic.recite.ReciteTimeManager;
import cn.flandre.review.service.ReciteService;

import java.util.Calendar;

/**
 * @author RmxhbmRyZQ 2021.8.30
 */
public class AlarmHelper {
    public static void scheduleTimeout(Context context, int time){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, time);
        Intent intent = new Intent(ReciteService.NOTIFICATION_BROADCAST);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.cancel(pendingIntent);
        Log.e("COMPLETE", time + ":" + ReciteTimeManager.turnSystemTimeToDetailTime(calendar.getTimeInMillis()));
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}
