package com.example.vincentale.leafguard_core.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.vincentale.leafguard_core.R;
import com.example.vincentale.leafguard_core.fragment.admin.AdminObservationFragment;
import com.example.vincentale.leafguard_core.model.CaterpillarObservation;
import com.example.vincentale.leafguard_core.model.manager.CaterpillarObservationManager;
import com.example.vincentale.leafguard_core.util.DatabaseListCallback;
import com.example.vincentale.leafguard_core.util.ObservationCSVBuilder;
import com.google.firebase.database.DatabaseError;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ObservationDownloadService extends Service {

    public static final String TAG = "ObsnDownloadService";
    private Handler handler;
    private NotificationManager notificationManager;
    private int NOTIFICATION = R.string.download_service_started;
    private CaterpillarObservationManager observationManager = CaterpillarObservationManager.getInstance();

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
        final ObservationCSVBuilder csvBuilder = new ObservationCSVBuilder();
        observationManager.findAll(new DatabaseListCallback<CaterpillarObservation>() {
            @Override
            public void onSuccess(List<CaterpillarObservation> identifiables) {
                CaterpillarObservation firstObservation = null;
                CaterpillarObservation secondObservation = null;
                for (CaterpillarObservation observation : identifiables) {
                    Log.d(TAG, observation.toString());
                    if (firstObservation == null) { //first initialization;
                        Log.d(TAG, "Initialization");
                        firstObservation = observation;
                    } else {
                        if (firstObservation.getUid().equals(observation.getUid())) {
                            secondObservation = observation;
                            csvBuilder.add(firstObservation, secondObservation);
                            firstObservation = null;
                        } else {
                            csvBuilder.add(firstObservation);
                            firstObservation = observation;
                        }
                    }
                }
                if (firstObservation != null)

                Log.d(TAG, "onSuccess: " + csvBuilder.toString());


                String filename = "observations.csv";
                String string = csvBuilder.toString();
                FileOutputStream outputStream;

                try {
                    outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(string.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                notificationManager.cancel(NOTIFICATION);
                ObservationDownloadService.this.stopSelf();
            }

            @Override
            public void onFailure(DatabaseError error) {
                //observationHaveChangedText.setText(error.getMessage());
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    public class DownloadBinder extends Binder {
        ObservationDownloadService getService() {
            return ObservationDownloadService.this;
        }
    }


}
