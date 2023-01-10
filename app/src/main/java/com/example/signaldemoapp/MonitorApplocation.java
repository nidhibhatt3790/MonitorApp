package com.example.signaldemoapp;

import static com.example.signaldemoapp.utils.Constants.KEY_IS_APP_BACKGROUND;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.example.signaldemoapp.managers.SharePreferenceManager;
import com.example.signaldemoapp.utils.Tool;

import java.util.Calendar;

public class MonitorApplocation extends Application implements LifecycleObserver {


    public void onCreate() {
        super.onCreate();

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        AppState.sContext = getApplicationContext();

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onAppForegrounded() {

        Log.d("TAG:", "App in foreground");

        SharePreferenceManager.getInstance().getEditor().putBoolean(KEY_IS_APP_BACKGROUND, false).commit();

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void  onAppResume()
    {
        Log.d("TAG","On app resume");
        //Tool.restartApplication(getApplicationContext());


    }
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onAppPause() {

        Log.d("TAG:", "App in Pause");

        Log.d("TAG:Before restart app...", "MonitorApp");


        // Create the IntentFilter
//        Intent startAppIntent = new Intent(getApplicationContext(), MainActivity.class);
//        startAppIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
//         getApplicationContext().startActivity(startAppIntent);
      // Tool.restartApplication(getApplicationContext());
        SharePreferenceManager.getInstance().getEditor().putBoolean(KEY_IS_APP_BACKGROUND, true).commit();


        Log.d("TAG:After restart app...", "MonitorApp");


    }


}
