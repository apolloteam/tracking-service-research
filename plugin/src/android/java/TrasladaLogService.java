package com.traslada.prestadores.plugin;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TrasladaLogService {

    @POST("log/demomarco/{code}/{gpsStatus}")
    Call<Void> registerGpsStatus(@Path("code") Integer code, @Path("gpsStatus") String status);

    @POST("log/demomarco/{code}/{usbStatus}")
    Call<Void> registerUsbConnectionStatus(@Path("code") Integer code, @Path("usbStatus") String usbStatus);
}
