package com.prueba.conex;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GpsStatusReceiver extends BroadcastReceiver {

    private static int gpsCount = 0;

    private Call<Void> retrofitCall;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().matches(LocationManager.PROVIDERS_CHANGED_ACTION)) {

            final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            gpsCount++;

            sendGpsStatus(context, manager.isProviderEnabled(LocationManager.GPS_PROVIDER));
        }
    }

    private void sendGpsStatus(final Context context, Boolean isLocationAvailable) {

        String gpsStatus = isLocationAvailable ? "gps_on" : "gps_off";

        AppPreferences preferences = new AppPreferences(context);

        String logUrl = preferences.getLogApiBaseUrl();

        if (logUrl.isEmpty()) {
            return;
        }

        Retrofit retrofit = new Retrofit.Builder().baseUrl(logUrl).build();

        TrasladaLogService service = retrofit.create(TrasladaLogService.class);

        retrofitCall = service.registerGpsStatus(1, gpsStatus);

        retrofitCall.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.code() == 204) {
                    Toast.makeText(context, "El cambio de estado del GPS fue registrado con exito." + gpsCount,
                            Toast.LENGTH_SHORT).show();
                }

                Log.v("MyForegroundService", "code: " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(context, "Ocurrio un error al registrar el cambio de estado del GPS.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
