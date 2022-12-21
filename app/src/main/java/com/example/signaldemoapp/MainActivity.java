package com.example.signaldemoapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.signaldemoapp.utils.RetrofitHelper;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    Button btnGetModelName, btnReboot, btnSwitchSource, btnInputSignal, btnSetVolume, btnApkUpdate, btnApkUpdate1, btnTrackApp, btnShareData;
    StartupReceiver receiver;
    BootService bootService;
    AudioManager audioManager;
    TextView tvData;
    ImageView imgUpdate;
    public static boolean flagReboot = true;
    public static boolean flagRestart = false;
    //public  final int progress_bar_type = 0;
    private static ProgressDialog pDialog;
    private String serverPathMonitor = "https://intelisaapk.s3.ap-south-1.amazonaws.com/MonitorApp/Monitor_1.1.apk";
    //private static String file_url = "https://intelisaapk.s3.ap-south-1.amazonaws.com/Autoupdater.apk";
    //private static String file_url = "https://intelisaapk.s3.ap-south-1.amazonaws.com/IntelisaDigitalSignage.apk";
    private static String file_url = " https://intelisaapk.s3.ap-south-1.amazonaws.com/Intelisa.apk";

    public static String _id;
    public static String resetFlag;

    int mRect[] = {0, 0, 500, 500};

    private static final int REQUEST_WRITE_PERMISSION = 786;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


            Log.d("TAG:APK UPDATE APP", "IN permission");


            File path1 = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS);


            File sPath = Environment.getExternalStorageDirectory();
            String filePath = sPath + "/" + "IntelisaDigitalSignage.apk";

            Log.d("TAG:APK UPDATE INTELISA ", "IN BUTTON CLICK");
            // Log.d("TAG::File path -->", filePath);

            Log.d("TAG:", "Before Download..");
            new DownloadFileFromURL().execute(file_url);


            Log.d("TAG:", "Before Intent");


        }
    }


    public void requestFilePermission(Context view, String location) {
        if (ContextCompat.checkSelfPermission(view,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(view,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(view,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(AppState.sActivity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_PERMISSION);

        }
    }


    private boolean canReadWriteExternal() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        AppState.sActivity = MainActivity.this;


        bootService = new BootService();
        tvData = (TextView) findViewById(R.id.tv_data);
        imgUpdate = (ImageView) findViewById(R.id.img_update);


        btnTrackApp = (Button) findViewById(R.id.btnTrackApp);
        btnGetModelName = (Button) findViewById(R.id.btnGetModelName);
        btnInputSignal = (Button) findViewById(R.id.btnInputSignal);
        btnSetVolume = (Button) findViewById(R.id.btnSetVolume);
        btnReboot = (Button) findViewById(R.id.btnReboot);
        btnApkUpdate = (Button) findViewById(R.id.btnApkUpdate);
        btnApkUpdate1 = (Button) findViewById(R.id.btnApkUpdate1);
        btnSwitchSource = (Button) findViewById(R.id.btnSwitchSource);
        btnShareData = (Button) findViewById(R.id.btnShareDta);


        PackageManager manager = MainActivity.this.getPackageManager();
        PackageInfo info = null;

        try {
            info = manager.getPackageInfo(
                    MainActivity.this.getPackageName(), 0);
            String versionName = info.versionName;
            tvData.setText("Version: " + versionName);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        Intent intent = getIntent();
        Log.d("TAG:", "Before passing that intent");
        if (intent != null && !intent.equals("null")) {

            Log.d("TAG:", "In if of Intent");
//            if (intent != null) {

            if (intent.getAction().toString() != null || (intent.getAction().toString().length() > 0)) {

                Log.d("TAG:", "In if of Intent string is not null");

                if (intent.getAction().equals("android.intent.action.MAIN")) {

                    Log.d("TAG:", "In if of data intent");

                    //flagReboot = false;

                    List<ApplicationInfo> applicationInfoList = getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);


                    String[] stringArray = new String[applicationInfoList.size()];

                    for (int i = 0; i < applicationInfoList.size(); i++) {


                        ApplicationInfo applicationInfo = applicationInfoList.get(i);
                        Log.d("TAG:" + i, applicationInfo.packageName);

                        //com.intelisatvapp
                        //com.example.intelisadigitalsignage
                        if (applicationInfo.packageName.equals("com.intelisatvapp")) {

                            Log.d("TAG::", "IN IF ");

                            Timer timer = new Timer(new Runnable() {
                                @Override
                                public void run() {

                                    isAppRunning(MainActivity.this, "com.intelisatvapp");


                                }
                            }, 60000, true);


                        }
                    }

                }
            }
        } else {
            Log.d("TAG:", "In else of Intent");

            List<ApplicationInfo> applicationInfoList = getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);


            String[] stringArray = new String[applicationInfoList.size()];

            for (int i = 0; i < applicationInfoList.size(); i++) {


                ApplicationInfo applicationInfo = applicationInfoList.get(i);
                Log.d("TAG:" + i, applicationInfo.packageName);

                //com.intelisatvapp
                //com.example.intelisadigitalsignage
                if (applicationInfo.packageName.equals("com.intelisatvapp")) {

                    Log.d("TAG::", "IN IF ");

                    Timer timer = new Timer(new Runnable() {
                        @Override
                        public void run() {

                            isAppRunning(MainActivity.this, "com.intelisatvapp");


                        }
                    }, 60000, true);


                }


            }
        }


        btnSetVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("TAG:VOLUME", "IN BUTTON CLICK");

                audioManager = getApplicationContext().getSystemService(AudioManager.class);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);


            }
        });
        btnReboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("TAG:REBOOT", "IN BUTTON CLICK");
                Intent intent = new Intent();

                intent.setAction("cms.intent.action.REBOOT");
                getApplicationContext().sendBroadcast(intent);

                Log.d("TAG:REBOOT", "AFTER BROADCAST");

            }
        });

        btnApkUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //start latest
//                File path = Environment.getExternalStoragePublicDirectory(
//                        Environment.DIRECTORY_DOWNLOADS);
//
//                Log.d("TAG:APK UPDATE INTELISA ", "IN BUTTON CLICK");
//                 Log.d("TAG::File path -->", path.toString());
//                Intent intent = new Intent();
//                intent.setAction("cms.intent.action.UPDATE_APK");
//                intent.putExtra("filePath", file_url + "/" + "IntelisaDigitalSignage.apk");//you have to put app update apk in device download first
//                intent.putExtra("keep", false);
//                intent.putExtra("packageName", "com.example.signaldemoapp");
//                intent.putExtra("activityName", "com.example.signaldemoapp.MainActivity");
//                getApplicationContext().sendBroadcast(intent);

                //End latest
                //In above code appUpdate apk is set in device download for testing only

                //String serverPath = "https://intelisaapk.s3.ap-south-1.amazonaws.com/IntelisaDigitalSignage/IntelisaDigitalSignage.apk";


//                Intent intent = new Intent();
//                intent.setAction("cms.intent.action.UPDATE_APK");
//                intent.putExtra("filePath", file_url);//new apk is updated from picking path server
//                intent.putExtra("keep", false);
//                intent.putExtra("packageName", "com.example.intelisadigitalsignage");
//                intent.putExtra("activityName", "com.example.intelisadigitalsignage");
//                getApplicationContext().sendBroadcast(intent);

                // act.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.")));

                //getintelisaAppVersion();

                //new DownloadFileFromURL().execute(file_url);
                // requestPermission();
                requestFilePermission(getApplicationContext(), file_url);


            }
        });

        btnApkUpdate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File path = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS);

                Log.d("TAG:APK UPDATE MONITOR APP", "IN BUTTON CLICK");
                // Log.d("TAG::File path -->", path.toString());


                Log.d("TAG:APK UPDATE", "IN BUTTON CLICK");
//                if (serverPathMonitor != null) {
//                    Log.d("TAGServer::File path -->", serverPathMonitor);
//                }
                String sPath = Environment.getExternalStorageDirectory() + "/Download/";
                String filePath = path + "/" + "Monitor_1.1.apk";
//                Intent intent = new Intent();
//                intent.setAction("cms.intent.action.UPDATE_APK");
//                intent.putExtra("filePath", path + "/" + "Monitor_1.1.apk");//new apk is updated from picking server path
//                intent.putExtra("keep", false);
//                intent.putExtra("packageName", "com.example.signaldemoapp");
//                intent.putExtra("activityName", "com.example.signaldemoapp.MainActivity");
//                getApplicationContext().sendBroadcast(intent);
//
//                getAppVersion();

                //new DownloadFileFromURL().execute(serverPathMonitor);
                // installApk();
                //  requestFilePermission(getApplicationContext(),sPath);
                // installNewApk();
                // InstallAPKCMD(filePath);
                requestFilePermission(getApplicationContext(), serverPathMonitor);

            }
        });

        btnSwitchSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("TAG:SWITCH SOURCE", "in button click");
                Intent intent = new Intent();
                intent.setAction("cms.intent.action.SwitchSource");
                intent.putExtra("source", "USB");
                getApplicationContext().sendBroadcast(intent);
                Log.d("TAG::", "after broadcast");
            }
        });

        btnShareData.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {


                Log.d("TAG::", "after broadcast");

//                Context myContext = null;
//                try {
////                    myContext = createPackageContext("com.example.intelisadigitalsignage", getApplicationContext().MODE_PRIVATE);
////                    SharedPreferences testPrefs = myContext.getSharedPreferences("com.example.intelisadigitalsignage" +
////                            "", getApplicationContext().MODE_PRIVATE);
////
////                    String number = testPrefs.getString("SERIALNUMBER","");
////                    Log.d("TAG:NUMBER",number);
//                    Context con = null;
//                    try {
//                        con = createPackageContext("com.example.intelisadigitalsignage", Context.CONTEXT_IGNORE_SECURITY);
//                    } catch (PackageManager.NameNotFoundException e) {
//                        e.printStackTrace();
//                    }
//
//                    try {
//                        if (con != null) {
//                            SharedPreferences pref = con.getSharedPreferences(
//                                    "token_id", Context.MODE_MULTI_PROCESS);
//
//                            String data = pref.getString("shared_token", "");
//                            Log.d("msg", "Other App Data: " + data);
//                        } else {
//                            Log.d("msg", "Other App Data: Context null");
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

            }
        });

        receiver = new StartupReceiver();

    }

    public static void getDeviceIP(String resnum) {

        RetrofitHelper retrofitHelper = new RetrofitHelper();

        Call<ResponseBody> call = retrofitHelper.api().get_deviceIP(resnum);
        retrofitHelper.callApi(call, new RetrofitHelper.ConnectionCallBack() {


            @Override
            public void onSuccess(Response<ResponseBody> body) {
                try {
                    String response = body.body().string();

                    Log.d("TAG:IN API", "");
                    if (response.equals("")) {
                        Log.d("TAG", "EMPTY RESP");
                    }
                    Log.i("TAG:deviceip", "onSuccess: " + response);

                    JSONObject json = new JSONObject(response);

                    _id = json.getString("_id");

                    if (json.has("resetFlag")) {
                        resetFlag = json.getString("resetFlag");


                        Log.d("TAG:", "before updat");
                        if (resetFlag != null) {

                            if (resetFlag.equalsIgnoreCase("1")) {

                                updateResetFlag(resnum, "0");

//                        Log.d("TAG:", "After flag update");
//
//                        Intent intent = new Intent();
//
//                        intent.setAction("cms.intent.action.REBOOT");
//                        AppState.sContext.sendBroadcast(intent);
                            }

                        } else {

                        }
                    }

                } catch (IOException | NullPointerException | JsonSyntaxException | JSONException e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int code, String error) {
                //Utils.showAlert(activity, error);
                // Utils.dismissPro1gress();
                Log.d("Error in api call DEVICE IP", error.toString() + code);
            }
        });
    }

    public static void updateResetFlag(String resnum, String resetFlag) {

        RetrofitHelper retrofitHelper = new RetrofitHelper();

        Call<ResponseBody> call = retrofitHelper.api().update_resetFlag(resnum, resetFlag);
        retrofitHelper.callApi(call, new RetrofitHelper.ConnectionCallBack() {

            String _id, resetFlag;

            @Override
            public void onSuccess(Response<ResponseBody> body) {
                try {
                    String response = body.body().string();

                    if (response.equals("")) {
                        Log.d("TAG", "EMPTY RESP");
                    }
                    Log.d("TAG:updateResetFlag", "onSuccess: " + response);

                    Log.d("TAG:", "After flag update");

                    Intent intent = new Intent();

                    intent.setAction("cms.intent.action.REBOOT");
                    AppState.sActivity.sendBroadcast(intent);

                    Log.d("TAG:REBOOT", "AFTER BROADCAST");
                } catch (Exception e) {

                }

            }

            @Override
            public void onError(int code, String error) {
                //Utils.showAlert(activit
                //
                // y, error);
                // Utils.dismissPro1gress();
                Log.d("Error in api call updateResetFlag ", error.toString() + code);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();


        Log.d("TAG:ONRESUME", "");
//        Intent intent = getIntent();
//        String data = intent.getStringExtra("data");
//
//        if (intent.getAction().equals("Your message to post")) {
//            tvData.setText(intent.getStringExtra("data"));
//
//            Timer timerip = new Timer(new Runnable() {
//                @Override
//                public void run() {
//
//
//                    getDeviceIP(tvData.getText().toString());
//
//
//                }
//            }, 60000, true);
//
//        }


    }

    public boolean isAppRunning(final Context context, final String packageName) {

        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null) {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {

                    Log.d("TAG:::::::::", processInfo.processName);
                    Log.d("TAG:", "App is running");

                    return true;
                } else {
                    Log.d("TAG::::::", "app is not running");

                    Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(
                            "com.intelisatvapp");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Log.d("TAG::PACKAGE NAME:", intent.getPackage().toString());
                    Log.d("TAG:::::::", "App is running now");


                }

            }
        }
        return false;
    }

    private void getAppVersion() {

        RetrofitHelper retrofitHelper = new RetrofitHelper();

        Call<ResponseBody> call = retrofitHelper.api().get_app_version();
        retrofitHelper.callApi(call, new RetrofitHelper.ConnectionCallBack() {
            @Override
            public void onSuccess(Response<ResponseBody> body) {
                //Utils.dismissProgress();
                try {
                    String response = body.body().string();
                    Log.i("TAG:monitorappversion", "onSuccess: " + response);

                    String newVersion = response;

                    PackageManager manager = MainActivity.this.getPackageManager();
                    PackageInfo info = manager.getPackageInfo(
                            MainActivity.this.getPackageName(), 0);
                    String Currentversion = info.versionName;

                    Double BuildVersion = 0.0;

                    try {
                        BuildVersion = Double.parseDouble(Currentversion);

                        Log.d("TAG" + " BuildVersion is==", "" + BuildVersion);
                        Log.d("TAG" + "currentversion is==", "" + Currentversion);

                    } catch (NumberFormatException nfe) {
                        // Handle parse error.
                        Log.i("TAG", "BuildVersion error: " + nfe.getMessage());

                    }


                } catch (IOException | NullPointerException | JsonSyntaxException e) {
                    e.printStackTrace();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int code, String error) {
                //Utils.showAlert(activity, error);
                // Utils.dismissPro1gress();
                Log.d("Error in api call get app version", error.toString() + code);

                if (code == 404) {

                } else {
                    Log.d("TAG:", "Ooops something went wrong");
                }
            }
        });

    }

    public void getintelisaAppVersion() {

        RetrofitHelper retrofitHelper = new RetrofitHelper();

        Call<ResponseBody> call = retrofitHelper.api().get_intelisa_app_version();
        retrofitHelper.callApi(call, new RetrofitHelper.ConnectionCallBack() {
            @Override
            public void onSuccess(Response<ResponseBody> body) {
                //Utils.dismissProgress();
                try {
                    String response = body.body().string();
                    Log.i("TAG:Intelisaappversion", "onSuccess: " + response);

                    String newVersion = response;

                    PackageManager manager = MainActivity.this.getPackageManager();
                    PackageInfo info = manager.getPackageInfo(
                            MainActivity.this.getPackageName(), 0);
                    String Currentversion = info.versionName;

                    Double BuildVersion = 0.0;

                    try {
                        BuildVersion = Double.parseDouble(Currentversion);

                        Log.d("TAG" + " BuildVersion is==", "" + BuildVersion);
                        Log.d("TAG" + "currentversion is==", "" + Currentversion);

                    } catch (NumberFormatException nfe) {
                        // Handle parse error.
                        Log.i("TAG", "BuildVersion error: " + nfe.getMessage());

                    }


                } catch (IOException | NullPointerException | JsonSyntaxException e) {
                    e.printStackTrace();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int code, String error) {
                //Utils.showAlert(activity, error);
                // Utils.dismissPro1gress();
                Log.d("Error in api call get app version", error.toString() + code);

                if (code == 404) {

                } else {
                    Log.d("TAG:", "Ooops something went wrong");
                }
            }
        });

    }

    //    @Override
//    protected Dialog onCreateDialog(int id) {
//        switch (id) {
//            case progress_bar_type: // we set this to 0
//                pDialog = new ProgressDialog(this);
//                pDialog.setMessage("Downloading file. Please wait...");
//                pDialog.setIndeterminate(false);
//                pDialog.setMax(100);
//                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                pDialog.setCancelable(true);
//                pDialog.show();
//                return pDialog;
//            default:
//                return null;
//        }
//    }
    private void installApk() {
        try {
            //String PATH = Objects.requireNonNull(getApplicationContext().getExternalFilesDir(null)).getAbsolutePath();
//            File  PATH = Environment.getExternalStoragePublicDirectory(
//                    Environment.DIRECTORY_DOWNLOADS);
//            File file = new File(PATH + "/Monitor_1.1.apk");
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            if (Build.VERSION.SDK_INT >= 24) {
//                Uri downloaded_apk = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
//                intent.setDataAndType(downloaded_apk, "application/vnd.android.package-archive");
//                List<ResolveInfo> resInfoList = getApplicationContext().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
//                for (ResolveInfo resolveInfo : resInfoList) {
//                    getApplicationContext().grantUriPermission(getApplicationContext().getApplicationContext().getPackageName() + ".provider", downloaded_apk, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                }
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                startActivity(intent);
//            } else {
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
//                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            }
//            startActivity(intent);


            //new code

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/Download/" + "Monitor_1.1.apk")), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            AppState.sActivity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class DownloadFileFromURL extends AsyncTask<String, String, String> {


        /**
         * Before starting background thread Show Progress Bar Dialog
         */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(AppState.sActivity);
            pDialog.setMessage("Downloading file. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
                File path = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS);

                Log.d("TAG:NEW FILE PATH IS::", path.toString());


                File file = new File(path, "/" + "Intelisa.apk");

                OutputStream output = new FileOutputStream(file);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

                Log.d("TAG:", "File Downloaded");
                File sPath = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS);

                String filePath = sPath + "/" + "Intelisa.apk";


                Intent intent = new Intent();
                intent.setAction("cms.intent.action.UPDATE_APK");
                intent.putExtra("filePath", filePath);//you have to put app update apk in device download first
                intent.putExtra("keep", false);
                intent.putExtra("packageName", "com.intelisatvapp");
                intent.putExtra("activityName", "com.intelisatvapp.MainActivity");
                getApplicationContext().sendBroadcast(intent);

                Log.d("TAG:", "After Broadcast");

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(MainActivity.this, "Download error: " + file_url, Toast.LENGTH_LONG).show();
                //   = "NotComplete";
            } else {
                Toast.makeText(MainActivity.this, "File downloaded", Toast.LENGTH_SHORT).show();
                // = "complete";


            }
        }

    }

//    public class DownloadFileFromURL extends AsyncTask<String, String, String> {
//
//
//
//
//        /**
//         * Before starting background thread Show Progress Bar Dialog
//         */
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(AppState.sActivity);
//            pDialog.setMessage("Downloading file. Please wait...");
//            pDialog.setIndeterminate(false);
//            pDialog.setMax(100);
//            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            pDialog.setCancelable(true);
//            pDialog.show();
//        }
//
//        /**
//         * Downloading file in background thread
//         */
//        @Override
//        protected String doInBackground(String... f_url) {
//            int count;
//
//
//            Log.d("TAG:Download","...");
//            try {
//                URL url = new URL(f_url[0]);
//                URLConnection connection = url.openConnection();
//                connection.connect();
//
//                // this will be useful so that you can show a tipical 0-100%
//                // progress bar
//                int lenghtOfFile = connection.getContentLength();
//
//                // download the file
//                InputStream input = new BufferedInputStream(url.openStream(),
//                        8192);
//
//                // Output stream
//                String sPath = Environment.getExternalStorageDirectory() + "/Download/";
//                File path = Environment.getExternalStoragePublicDirectory(
//                        Environment.DIRECTORY_DOWNLOADS);
//               // File file = new File(path, "/" + "Monitor_1.1.apk");
//
//
//               // File file = new File(f_url[0]);
//                File file = new File(sPath,f_url[0]);
//
//                file.getParentFile().mkdirs();
//                file.createNewFile();
//
//
//
//                OutputStream output = new FileOutputStream(file);
//
//                byte data[] = new byte[1024];
//
//                long total = 0;
//
//                while ((count = input.read(data)) != -1) {
//                    total += count;
//                    // publishing the progress....
//                    // After this onProgressUpdate will be called
//                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
//
//                    // writing data to file
//                    output.write(data, 0, count);
//                }
//
//                // flushing output
//                output.flush();
//
//                // closing streams
//                output.close();
//                input.close();
//                Log.d("TAG:Update monitor app", "Done");
//
//                file.setReadable(true, false);
//
//
//                // installApk();
//                //OpenNewVersion(sPath);
//
//                Log.d("TAG:Installed new  monitor app", "Done");
//
//
//            } catch (Exception e) {
//                Log.e("Error: ", e.getMessage());
//            }
//
//            return null;
//        }
//
//        /**
//         * Updating progress bar
//         */
//        protected void onProgressUpdate(String... progress) {
//            // setting progress percentage
//            pDialog.setProgress(Integer.parseInt(progress[0]));
//            pDialog.setMessage("Downloading file. Please wait...");
//
//        }
//
//        /**
//         * After completing background task Dismiss the progress dialog
//         **/
//        @Override
//        protected void onPostExecute(String file_url) {
//            // dismiss the dialog after the file was downloaded
//            pDialog.dismiss();
//
//
//        }
//
//    }

    void OpenNewVersion() {

        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
        File file = new File(path, "Monitor_1.1.apk");

        if (!file.exists()) {
            //storageDir.mkdirs();
            Log.d("TAG:", "File not exist");
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                Uri apkUri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);
//                intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
//                intent.setData(apkUri);
//                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            //NEW APPROACH
            Uri apkUri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);
            intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            intent.setData(apkUri);
            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        } else {
            Uri apkUri = Uri.fromFile(file);
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        MainActivity.this.startActivity(intent);


    }

    public static void installNewApk() {
        try {
            Runtime.getRuntime().exec(new String[]{"su", "-c", "adb install  -r /mnt/internal/Download/Monitor_1.1.apk"});
        } catch (IOException e) {
            System.out.println(e.toString());
            System.out.println("no root");
        }
    }

    public static void InstallAPKCMD(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            try {
                String command;
                command = "adb install -r " + filename;
                Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", command});
                proc.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {

            String serverPath = "https://intelisaapk.s3.ap-south-1.amazonaws.com/IntelisaDigitalSignage/IntelisaDigitalSignage.apk";


            //  OpenNewVersion();
            new DownloadFileFromURL().execute(file_url);
        }
    }
}