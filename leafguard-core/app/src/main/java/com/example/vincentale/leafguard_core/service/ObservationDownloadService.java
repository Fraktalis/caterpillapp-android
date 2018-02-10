package com.example.vincentale.leafguard_core.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.example.vincentale.leafguard_core.R;
import com.example.vincentale.leafguard_core.fragment.admin.AdminObservationFragment;

public class ObservationDownloadService extends Service {

    private Handler handler;
    private NotificationManager notificationManager;
    private int NOTIFICATION = R.string.download_service_started;

    public ObservationDownloadService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        handler = new Handler();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Notification showNotification() {
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_nature)
                .setContentTitle("My notification")
                .setContentText("content of notification");
        Notification notification = notifBuilder.build();
        notificationManager.notify(NOTIFICATION, notification);

        return notification;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notif = showNotification();
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                notificationManager.cancel(NOTIFICATION);
                ObservationDownloadService.this.stopSelf();
            }
        };

        handler.postDelayed(r, 5000);
        return super.onStartCommand(intent, flags, startId);
    }

    public class DownloadBinder extends Binder {
        ObservationDownloadService getService() {
            return ObservationDownloadService.this;
        }
    }


}
