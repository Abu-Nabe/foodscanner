package com.example.unifood.Main.Alarm;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.POWER_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.PowerManager;
import android.provider.Settings;

import com.example.unifood.Main.PushNotification.NotificationBroadcastReceiver;

import java.util.Calendar;

public class SendAlarm
{
    public static void configNotification(Context context){
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
//        Intent intent = new Intent(context, NotificationBroadcastReceiver.class);
//        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES); // required for API 23 and above
//        intent.putExtra("packageName", context.getPackageName()); // required for API 23 and above
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
//
//// Request to ignore battery optimizations
//        Intent batteryIntent = new Intent();
//        String packageName = context.getPackageName();
//        PowerManager pm = (PowerManager) context.getSystemService(POWER_SERVICE);
//        if (pm.isIgnoringBatteryOptimizations(packageName)) {
//            batteryIntent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
//        } else {
//            batteryIntent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
//            batteryIntent.setData(Uri.parse("package:" + packageName));
//        }
//        context.startActivity(batteryIntent);
//
//// Set the alarm to start at 12 PM
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 12);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//
//// Schedule a repeating alarm that triggers every day at 12 PM
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                AlarmManager.INTERVAL_DAY, pendingIntent);

    }
}
