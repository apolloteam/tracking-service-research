package com.prueba.conex;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PlugInControlReceiver extends BroadcastReceiver {

    private Call<Void> retrofitCall;

    @Override public void onReceive(final Context context, Intent intent) {

        String action = intent.getAction();

        String usbConnectionStatus = null;

        if(action.equals(Intent.ACTION_POWER_CONNECTED)) {

            Toast.makeText(context, "USB Connected", Toast.LENGTH_SHORT).show();

            usbConnectionStatus = "usb_connected";
        }
        else if(action.equals(Intent.ACTION_POWER_DISCONNECTED)) {

            Toast.makeText(context, "USB Disconnected", Toast.LENGTH_SHORT).show();

            usbConnectionStatus = "usb_disconnected";
        }

        if(usbConnectionStatus == null)
            return;

        AppPreferences preferences = new AppPreferences(context);

        String urlLog = preferences.getLogApiBaseUrl();

        if(urlLog.isEmpty()){
            Log.e("PlugInControlReceiver","URL LOG is empty");
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlLog)
                .build();

        TrasladaLogService service = retrofit.create(TrasladaLogService.class);

        retrofitCall = service.registerUsbConnectionStatus(1, usbConnectionStatus);

        retrofitCall.enqueue(new Callback<Void>() {

            @Override public void onResponse(Call<Void> call, Response<Void> response) {

                if(response.code() == 204){
                    Toast.makeText(context, "El cambio de estado del USB connection fue registrado con exito.", Toast.LENGTH_SHORT).show();
                }
                Log.v("PlugInControlReceiver","code: "+response.code());
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