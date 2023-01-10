package com.example.signaldemoapp;



import static com.example.signaldemoapp.MainActivity.checkPermission;
import static com.example.signaldemoapp.MainActivity.getDeviceIP;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import com.example.signaldemoapp.managers.SharePreferenceManager;
import com.example.signaldemoapp.utils.Tool;

public class StartupReceiver extends BroadcastReceiver {

    public static String reset = "0";
    public static String deviceIMEI = "XXXXXX";


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TAG:ON RECEIVE", "startupreceiver");
        Log.d("ANDROID", "In StartupReceiver received!!!");
        Log.d("ANDROID", intent.getAction());



        Log.d("TAG:BEFORE RESET DATA",reset);

        if ("com.intelisa.reset".equals(intent.getAction())) {
            Log.d("TAG:Reset=>", intent.getAction());
           // String resetFlag = intent.getStringExtra("reset");
             deviceIMEI = intent.getStringExtra("deviceIMEI");
           // Log.d("TAG:IN RECEIVER:ResetFlag", resetFlag);
            Log.d("TAG:IN RECEIVER:deviceIMEI", deviceIMEI);



//                reset = "10";
//
//            SharedPreferences.Editor editor = SharePreferenceManager.getInstance().getEditor();
//            editor.putString("reset", reset);
//            editor.commit();


//            Intent i = new Intent(context, MainActivity.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            i.putExtra("reset", reset);
//            context.startActivity(i);
//
//
//            Log.d("TAG:AFTER RESET DATA",reset);
//            Log.d("TAG:AFTER RESET DEVICEIMEI",deviceIMEI);



//                updateResetFlag(deviceIMEI, "0");
//
//                Intent intent1 = new Intent();
//                intent1.setAction("cms.intent.action.REBOOT");
//                context.sendBroadcast(intent1);


//                Timer timerip = new Timer(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        Log.d("TAG:IN 1 MIN..TIME STARTUP"," :)");
//
//
//
//
//                        getDeviceIP(deviceIMEI);
//
//
//                    }
//                }, 60000, true);


               checkPermission(deviceIMEI);

               Log.d("TAG:","After file operation");

        }

//        if("com.intelisa.Newversion".equals(intent.getAction()))
//        {
//            String APPversion = intent.getStringExtra("VERSION");
//            Log.d("TAG:INTELISA:VERSION",APPversion);
//        }

        if("com.intelisa.appversion".equals(intent.getAction()))
        {
            String APPversion = intent.getStringExtra("INTELISAAPPVERSION");
            Log.d("TAG:INTELISA:VERSION",APPversion);
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

            Log.d("TAGReceiver:filepath",filePath);



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
