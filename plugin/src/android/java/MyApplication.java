package com.prueba.conex;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

public class MyApplication extends Application {

    public static final String CHANNEL_ID = "my_channel";

    @Override
    public void onCreate() {
        super.onCreate();

        Log.v("CordovaJava", "MyApplication is running...");

        createNotificationChannel();
    }

    private void createNotificationChannel(){

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {

            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, "My Service Channel", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager notManager = getSystemService(NotificationManager.class);

            notManager.createNotificationChannel(serviceChannel);
        }
    }
}
