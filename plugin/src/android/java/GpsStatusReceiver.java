package com.prueba.conex;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;
import java.util.Date;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GpsStatusReceiver extends BroadcastReceiver {

    private AppPreferences appPreferences;
    private Call<Void> retrofitCall;
    private int counter = 0;

    @Override public void onReceive(Context context, Intent intent) {

        appPreferences = new AppPreferences(context);

        if (intent.getAction().matches(LocationManager.PROVIDERS_CHANGED_ACTION)) {

            final LocationManager manager = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );

            Boolean isLocationAvailable = manager.isProviderEnabled( LocationManager.GPS_PROVIDER);
            long currentGpsMillis = new Date().getTime();

            long lastGpsMillis = appPreferences.getGpsMillisecond();
            boolean lastGpsValue = appPreferences.getGpsLastValue();

            if(lastGpsMillis != 0 && currentGpsMillis < lastGpsMillis+5000 && lastGpsValue == isLocationAvailable) {
                Log.v("GPS:::","onReceive() fue antes");
                return;
            }

            appPreferences.setGpsLastValue(isLocationAvailable);
            appPreferences.setGpsMillisecond(new Date().getTime());
            Log.v("GPS:::","onReceive() si paso");

            sendGpsStatus(context, isLocationAvailable);
        }
    }

    private void sendGpsStatus(final Context context, Boolean isLocationAvailable) {

        final String gpsStatus = isLocationAvailable ? "gps_on" : "gps_off";

        AppPreferences preferences = new AppPreferences(context);

        String logUrl = preferences.getLogApiBaseUrl();

        if( logUrl.isEmpty() ){
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(logUrl)
                .build();

        TrasladaLogService service = retrofit.create(TrasladaLogService.class);

        retrofitCall = service.registerGpsStatus(1, gpsStatus);

        retrofitCall.enqueue(new Callback<Void>() {

            @Override public void onResponse(Call<Void> call, Response<Void> response) {

                if(response.code() == 204){

                    counter = counter + 1;
                    Toast.makeText(context, "new GPS status = "+gpsStatus+" counter: "+counter, Toast.LENGTH_SHORT).show();
                }

                Log.v("MyForegroundService","code: "+response.code());
            }

            @Override public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(context, "Ocurrio un error al registrar el cambio de estado del GPS.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
