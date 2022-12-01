package com.example.signaldemoapp;


import static com.example.signaldemoapp.MainActivity.getDeviceIP;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class StartupReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TAG:ON RECEIVE", "startupreceiver");
        Log.d("ANDROID", "In StartupReceiver received!!!");
        Log.d("ANDROID", intent.getAction());


        if ("com.intelisa.reset".equals(intent.getAction())) {
            Log.d("TAG::ACTION IS .", intent.getAction());
           // String resetFlag = intent.getStringExtra("reset");
            String deviceIMEI = intent.getStringExtra("deviceIMEI");
           // Log.d("TAG:IN RECEIVER:ResetFlag", resetFlag);
            Log.d("TAG:IN RECEIVER:deviceIMEI", deviceIMEI);




//                updateResetFlag(deviceIMEI, "0");
//
//                Intent intent1 = new Intent();
//                intent1.setAction("cms.intent.action.REBOOT");
//                context.sendBroadcast(intent1);


                Timer timerip = new Timer(new Runnable() {
                    @Override
                    public void run() {

                        Log.d("TAG:IN 1 MIN..TIME STARTUP"," :)");


                        getDeviceIP(deviceIMEI);


                    }
                }, 60000, true);




        }

        if ("cms.intent.action.GetInputSignal".equals(intent.getAction())) {
            Log.d("TAG::ACTION IS", intent.getAction());

        }
        if ("cms.intent.action.SwitchSource".equals(intent.getAction())) {
            Log.d("TAG::", intent.getAction());

        }
        if ("cms.intent.action.UPDATE_APK".equals(intent.getAction())) {

            Log.d("ANDROID app update", intent.getAction());

            String filePath = intent.getStringExtra("filePath");

            Log.d("TAG:filepath receiver",filePath);

//            new MainActivity.DownloadFileFromURL().execute(filePath);
//
//            Log.d("TAG:UPDATE Monitor app","Downloaded successfully");


        }

        if ("cms.intent.action.REBOOT".equals(intent.getAction())) {
            Log.d("TAG", intent.getAction());
        }
        if ("com.tpv.fq.reply.getModelName".equals(intent.getAction())) {
            String modelName = intent.getStringExtra("modelName");

            Log.d("TAG:: ", "In StartupReceiver modelName!!!" + modelName);
            Toast.makeText(context.getApplicationContext(), "MODEL NAME IS:: " + modelName, Toast.LENGTH_LONG).show();
        }

        if ("com.tpv.action.SetPowerOffTime".equals(intent.getAction())) {

            Log.d("ACTION IS::", intent.getAction());

            // String modelName = intent.getStringExtra("modelName");
            //this.restartApp(context);
            //MainApplication.getApp().showToast("Autostart App!!", Toast.LENGTH_LONG);
            // Log.d("TAG:: ", "In StartupReceiver modelName!!!" + modelName);
            //Toast.makeText(context.getApplicationContext(),"MODEL NAME IS:: "+modelName,Toast.LENGTH_LONG).show();
        }

    }

}
