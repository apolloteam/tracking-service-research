package com.prueba.conex;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import java.text.SimpleDateFormat;
import java.util.Date;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import com.traslada.cordovaPluginJavaConnection.R;
import com.traslada.cordovaPluginJavaConnection.MainActivity;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.content.Context;

public class MyForegroundService extends Service {

    private static final String ACTION = "android.location.PROVIDERS_CHANGED";

    private static final String ACTION_CONNECT_USB = "android.intent.action.ACTION_POWER_CONNECTED";
    private static final String ACTION_DISCONNECT_USB = "android.intent.action.ACTION_POWER_DISCONNECTED";

    private GpsStatusReceiver gpsStatusReceiver;
    private PlugInControlReceiver plugInControlReceiver;

    private Notification notification;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private AppDatabase appDatabase;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;

    private AppPreferences preferences;

    private AudioPlayer audioPlayer;

    private LocationCallback locationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            if (locationResult == null) {
                Log.v("MyForegroundService", "location is null");
                return;
            }

            Location location = locationResult.getLastLocation();

            String dateString = dateFormat.format(new Date());
            String lat = "" + location.getLatitude();
            String lng = "" + location.getLongitude();
            String accuracy = "" + location.getAccuracy();
            String speed = "" + location.getSpeed();
            Integer holderId = preferences.getHolderId();
            Integer activityId = preferences.getActivityId();
            Integer ownerId = preferences.getOwnerId();
            Integer holderStatus = preferences.getHolderStatus();
            Integer activityStatus = preferences.getActivityStatus();

            // store on Shared Preferences

            String lastPositionString = lat + "|" + lng + "|" + accuracy + "|" + speed;

            preferences.setLastPositionDate(dateString);
            preferences.setLastPosition(lastPositionString);

            // store on DB
            if (preferences.getActivityId() != 0) {

                TrackingPositionModel trackingPositionModel = new TrackingPositionModel();
                trackingPositionModel.setDate(dateString);
                trackingPositionModel.setHolderId(holderId);
                trackingPositionModel.setActivityId(activityId);
                trackingPositionModel.setOwnerId(ownerId);
                trackingPositionModel.setHolderStatus(holderStatus);
                trackingPositionModel.setActivityStatus(activityStatus);
                trackingPositionModel.setLat(lat);
                trackingPositionModel.setLng(lng);
                trackingPositionModel.setAccuracy(location.getAccuracy());
                trackingPositionModel.setSpeed(location.getSpeed());

                long newID = appDatabase.insertMessage(trackingPositionModel);
            }

            // store on API

            StringBuffer dataSb = new StringBuffer("\"").append(dateString).append("|").append(holderId).append("|")
                    .append(activityId).append("|").append(ownerId).append("|").append(holderStatus).append("|")
                    .append(activityStatus).append("|").append(lastPositionString).append("\"");

            sendTrackedPosition(dataSb.toString());
        }

        @Override
        public void onLocationAvailability(LocationAvailability locationAvailability) {
            super.onLocationAvailability(locationAvailability);
            // Log.v("MyForegroundService", "locationAvailability:
            // "+locationAvailability.isLocationAvailable());
            // sendGpsStatus(locationAvailability.isLocationAvailable());
        }
    };

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();

        Log.v("MyForegroundService", "onCreate");
        audioPlayer = new AudioPlayer();

        gpsStatusReceiver = new GpsStatusReceiver();
        plugInControlReceiver = new PlugInControlReceiver();

        final IntentFilter theFilter = new IntentFilter();
        theFilter.addAction(ACTION);
        this.registerReceiver(this.gpsStatusReceiver, theFilter);

        final IntentFilter theFilterPluginUsb = new IntentFilter();
        theFilterPluginUsb.addAction(ACTION_CONNECT_USB);
        theFilterPluginUsb.addAction(ACTION_DISCONNECT_USB);
        this.registerReceiver(this.plugInControlReceiver, theFilterPluginUsb);

        preferences = new AppPreferences(this);

        appDatabase = new AppDatabase(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        if (preferences.getGspInterval() > 0) {

            locationRequest.setMaxWaitTime(preferences.getGspInterval() * 1000);
            locationRequest.setInterval(preferences.getGspInterval() * 1000);
            locationRequest.setFastestInterval((preferences.getGspInterval() - 1) * 1000);
        } else {

            locationRequest.setMaxWaitTime(0);
            locationRequest.setInterval(0);
            locationRequest.setFastestInterval(0);
        }
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("Service", "onStartCommand");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        notification = new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID).setContentTitle("Marco Estrella")
                .setContentText("Demo Permanent Service").setSmallIcon(R.drawable.ic_running)
                .setContentIntent(pendingIntent).build();

        startForeground(1, notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.v("Service", "onDestroy");

        if (gpsStatusReceiver != null) {
            this.unregisterReceiver(this.gpsStatusReceiver);
        }

        if (plugInControlReceiver != null) {
            this.unregisterReceiver(this.plugInControlReceiver);
        }

        if (retrofitRegisterGpsStatusCall != null)
            retrofitRegisterGpsStatusCall.cancel();

        if (retrofitTrackPositionCall != null)
            retrofitTrackPositionCall.cancel();

        mFusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Call<Void> retrofitRegisterGpsStatusCall;
    private Call<Void> retrofitTrackPositionCall;

    private void sendTrackedPosition(String data) {

        String trackingUrl = preferences.getTrackingApiBaseUrl();

        if (trackingUrl.isEmpty()) {
            return;
        }

        TrasladaTrackerService service = RetrofitCreator.getRetrofit(trackingUrl).create(TrasladaTrackerService.class);

        retrofitTrackPositionCall = service.trackPosition(data);

        retrofitTrackPositionCall.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.code() != 204) {
                    Toast.makeText(MyForegroundService.this, "Error al enviar la posicion al API.", Toast.LENGTH_SHORT)
                            .show();
                }

                Log.v("MyForegroundService", "code: " + response.code());
                audioPlayer.play(MyForegroundService.this, R.raw.quite_impressed);
                vibrate();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void vibrate(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if(v == null)
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(1000);
        }
    }
}
