package com.example.signaldemoapp.utils;

import static android.content.Context.ALARM_SERVICE;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

import com.example.signaldemoapp.MainActivity;
import com.example.signaldemoapp.managers.SharePreferenceManager;

public class Tool {



    public static void restartApplication(Context context) {
        Log.d("TAG", "restartApplication: ");


            Intent mainIntent = new Intent(context, MainActivity.class);

            Log.d("TAG", "restartApplication: ");
            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntent(mainIntent);
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT |PendingIntent.FLAG_IMMUTABLE);
            long alarmTime = System.currentTimeMillis() + (60 * 1000);
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
            Log.d("TAG", "restartApplication: 111");








//        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(ALARM_SERVICE);
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//        stackBuilder.addNextIntent(mainIntent);
//        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
//        long alarmTime = System.currentTimeMillis() + (1 * 1000);
//
//
//
//
//
//
//        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);

    }
}
