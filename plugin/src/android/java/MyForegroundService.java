package com.prueba.conex;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import java.text.SimpleDateFormat;
import com.prueba.conex.Message;
import com.prueba.conex.AppDatabase;
import com.traslada.cordovaPluginJavaConnection.R;
import com.traslada.cordovaPluginJavaConnection.MainActivity;
import java.util.Date;

public class MyForegroundService extends Service {

    private static final long DELAY = 10000;

    private Notification notification;
    private HandlerThread mHandlerThread;
    private Handler handler;

    @Override public void onCreate() {
        super.onCreate();

        Log.v("CordovaJava", "Service onCreate");

        mHandlerThread = new HandlerThread("MyHandlerThread", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();

        handler = new Handler(mHandlerThread.getLooper());

        handler.post(new HandlerTask(this));
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("CordovaJava", "Service onStartCommand");

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

        Log.v("CordovaJava", "Service onDestroy");

        mHandlerThread.quitSafely();
    }

    @Override public IBinder onBind(Intent intent) {
        return null;
    }

    class HandlerTask implements Runnable {

        private Context context;
        private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        private AppDatabase appDatabase;

        public HandlerTask(Context context){
            this.context = context;

            appDatabase = new AppDatabase(context);
        }

        public void run() {

            String text = dateFormat.format(new Date())+" Tic";

            Message message = new Message(text);
            long newID = appDatabase.insertMessage(message);

            handler.postDelayed(this, DELAY);
        }
    }
}