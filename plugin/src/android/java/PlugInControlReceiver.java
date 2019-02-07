package com.prueba.conex;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import java.util.Date;
import retrofit2.Response;
import retrofit2.Retrofit;
import android.os.Build;

public class PlugInControlReceiver extends BroadcastReceiver {

    private Call<Void> retrofitCall;
    private int counter = 1;
    private AppPreferences appPreferences;

    @Override public void onReceive(final Context context, Intent intent) {
        Log.v("PlugIn:::","onReceive()");
        String action = intent.getAction();
        Log.v("PlugInControlReceiver","action: "+action);
        String usbConnectionStatus = null;

        appPreferences = new AppPreferences(context);
        boolean isConnected = false;

        if(action.equals(Intent.ACTION_POWER_CONNECTED)) {

            Toast.makeText(context, "USB Connected", Toast.LENGTH_SHORT).show();
            usbConnectionStatus = "usb_connected";
            isConnected = true;

        } else if(action.equals(Intent.ACTION_POWER_DISCONNECTED)) {

            Toast.makeText(context, "USB Disconnected", Toast.LENGTH_SHORT).show();
            usbConnectionStatus = "usb_disconnected";
            isConnected = false;
        }

        if(usbConnectionStatus == null)
            return;

        long currentUsbMillis = new Date().getTime();

        long lastUsbMillis = appPreferences.getUsbMillisecond();
        boolean lastUsbStatus = appPreferences.getUsbLastValue();

        if(lastUsbMillis != 0 && currentUsbMillis < lastUsbMillis+5000 && lastUsbStatus == isConnected) {
            Log.v("PlugIn:::","onReceive() fue antes");

            Toast.makeText(context, "fue antes", Toast.LENGTH_SHORT).show();
            return;
        }
        appPreferences.setUsbMillisecond(new Date().getTime());
        appPreferences.setUsbLastValue(isConnected);

        Toast.makeText(context, " si paso", Toast.LENGTH_SHORT).show();
        Log.v("PlugIn:::","onReceive() si paso");

        String logUrl = appPreferences.getLogApiBaseUrl();

        if( logUrl.isEmpty() ){
            return;
        }

        TrasladaLogService service = RetrofitCreator.getRetrofit(logUrl).create(TrasladaLogService.class);

        retrofitCall = service.registerUsbConnectionStatus(1, usbConnectionStatus);

        retrofitCall.enqueue(new Callback<Void>() {

            @Override public void onResponse(Call<Void> call, Response<Void> response) {

                if(response.code() == 204){
                    counter = counter + 1;
                    Toast.makeText(context, "El cambio de estado del USB connection fue registrado con exito. "+counter, Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(context, "code: "+response.code(), Toast.LENGTH_SHORT).show();
            }

            @Override public void onFailure(Call<Void> call, Throwable t) {

                t.printStackTrace();
                Toast.makeText(context, "Ocurrio un error al registrar el cambio de estado del USB.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override protected void finalize() throws Throwable {
        super.finalize();

        if(retrofitCall!=null)
            retrofitCall.cancel();
    }
}
