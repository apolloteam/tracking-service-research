package com.prueba.conex;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import com.traslada.cordovaPluginJavaConnection.R;
import com.traslada.cordovaPluginJavaConnection.MainActivity;

public class MyForegroundService extends Service {

    private Notification notification;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private AppDatabase appDatabase;

    private LocationManager locationManager;

    private LocationListener locationListener = new LocationListener() {

        @Override public void onLocationChanged(Location location) {

            if (location == null) {
                Log.v("MyForegroundService","location is null");
                return;
            }

            Log.v("MyForegroundService", "lat: "+location.getLatitude()+", long: "+location.getLongitude()+", acc:"+location.getAccuracy()+", speed: "+location.getSpeed());

            String text = dateFormat.format(new Date())+" lat: "+location.getLatitude()+" long: "+location.getLongitude();

            Message message = new Message(text);

            long newID = appDatabase.insertMessage(message);
        }

        @Override public void onStatusChanged(String s, int i, Bundle bundle) {

            Log.v("MyForegroundService","onStatusChanged: "+i+", s: "+s);
        }

        @Override public void onProviderEnabled(String s) {

            Log.v("MyForegroundService","onProviderEnabled: "+s);
            Toast.makeText(MyForegroundService.this, "GPS Enabled", Toast.LENGTH_SHORT).show();
        }

        @Override public void onProviderDisabled(String s) {

            Log.v("MyForegroundService","onProviderDisabled: "+s);
            Toast.makeText(MyForegroundService.this, "GPS Disabled", Toast.LENGTH_SHORT).show();
        }
    };

    @SuppressLint("MissingPermission")
    @Override public void onCreate() {
        super.onCreate();

        Log.v("MyForegroundService", "onCreate");

        appDatabase = new AppDatabase(this);

        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locationListener);
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("Service", "onStartCommand");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        notification = new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                .setContentTitle("Marco Estrella")
                .setContentText("Demo Permanent Service")
                .setSmallIcon(R.drawable.ic_running)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        return START_STICKY;
    }

    @Override public void onDestroy() {
        super.onDestroy();

        Log.v("Service", "onDestroy");

        locationManager.removeUpdates(locationListener);
    }

    @Override public IBinder onBind(Intent intent) {
        return null;
    }
}
