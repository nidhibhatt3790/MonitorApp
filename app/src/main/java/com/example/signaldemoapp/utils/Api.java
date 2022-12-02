package com.example.signaldemoapp.utils;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    String URLPrefix = "https://login.intelisa.in/api/";
    String DevicePrefix = "https://login.intelisa.in/api/device/";

    @GET(URLPrefix + "getSerialNumber/")
    Call<ResponseBody> get_unique_key();


    @GET(URLPrefix + "getMonitorAPKVersion/")
    Call<ResponseBody> get_app_version();

    @GET(URLPrefix + "getAppAPKVersion/")
    Call<ResponseBody> get_intelisa_app_version();

    //    @FormUrlEncoded
//    @POST("telnet/6003/index.php")
//    Call<ResponseBody> upload_location(@Field("data") String data);

    @FormUrlEncoded
    @POST(DevicePrefix + "updateDeviceDebugResetFlags/")
    Call<ResponseBody> update_resetFlag(
            @Field("deviceIMEI") String deviceIMEI,
            @Field("resetFlag") String resetFlag
            );

    @FormUrlEncoded
    @POST(DevicePrefix + "updateDeviceIP/")
    Call<ResponseBody> get_deviceIP(
            @Field("deviceIMEI") String deviceIMEI


    );


//    }


}
