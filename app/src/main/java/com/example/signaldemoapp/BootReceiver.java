package com.example.signaldemoapp;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.util.Log;

import com.example.signaldemoapp.utils.Tool;


import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("TAG:ONRECEIVE", intent.getAction());

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.d("TAG", "onReceive: " + intent);


//        Intent startAppIntent = new Intent(context, MainActivity.class);
//        startAppIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(startAppIntent);

            Log.d("TAG:Before restart app from receiver...", "MonitorApp");

//            Intent startAppIntent = new Intent(context, MainActivity.class);
//            startAppIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(startAppIntent);
            Tool.restartApplication(context);

            Log.d("TAG:After restart app from receiver...", "MonitorApp");


        }
    }

}
