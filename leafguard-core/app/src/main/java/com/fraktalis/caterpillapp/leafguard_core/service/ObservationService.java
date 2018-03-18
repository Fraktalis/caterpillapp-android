package com.fraktalis.caterpillapp.leafguard_core.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.fraktalis.caterpillapp.leafguard_core.R;
import com.fraktalis.caterpillapp.leafguard_core.model.AbstractObservation;
import com.fraktalis.caterpillapp.leafguard_core.model.CaterpillarObservation;
import com.fraktalis.caterpillapp.leafguard_core.model.LeavesObservation;
import com.fraktalis.caterpillapp.leafguard_core.model.manager.CaterpillarObservationManager;
import com.fraktalis.caterpillapp.leafguard_core.util.DatabaseListCallback;
import com.fraktalis.caterpillapp.leafguard_core.util.ObservationCSVBuilder;
import com.fraktalis.caterpillapp.leafguard_core.util.ObservationServiceCallback;
import com.google.firebase.database.DatabaseError;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class ObservationService extends Service {

    public static final String TAG = "ObsnDownloadService";
    private final ObservationBinder mBinder = new ObservationBinder();
    private CaterpillarObservationManager observationManager = CaterpillarObservationManager.getInstance();


    public ObservationService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** method for clients */
    public void saveObservations(final ObservationServiceCallback callback) {
        final ObservationCSVBuilder csvBuilder = new ObservationCSVBuilder();
        observationManager.findAllObservations(new DatabaseListCallback<AbstractObservation>() {
            @Override
            public void onSuccess(List<AbstractObservation> identifiables) {
                CaterpillarObservation firstObservation = null;
                CaterpillarObservation secondObservation = null;
                LeavesObservation leavesObservation = null;
                String currentUid = identifiables.get(0).getUid();
                for (AbstractObservation observation : identifiables) {
                    if (!observation.getUid().equals(currentUid)) {
                        currentUid = observation.getUid();
                        if (firstObservation != null) {
                            csvBuilder.add(firstObservation, secondObservation, leavesObservation);
                        }
                        firstObservation = null;
                        secondObservation = null;
                        leavesObservation = null;
                    }

                    if (observation instanceof CaterpillarObservation) {
                        if (((CaterpillarObservation) observation).getObservationIndex() == 1) {
                            firstObservation = (CaterpillarObservation) observation;
                        } else {
                            secondObservation = (CaterpillarObservation) observation;
                        }
                    } else if (observation instanceof LeavesObservation) {
                        leavesObservation = (LeavesObservation) observation;
                    }
                }

                if (firstObservation != null) {
                    csvBuilder.add(firstObservation, secondObservation, leavesObservation);
                }

                Log.d(TAG, "onSuccess: " + csvBuilder.toString());


                String filename = "observations.csv";
                String directoryName = getResources().getString(R.string.app_name);
                String string = csvBuilder.toString();
                FileOutputStream outputStream;

                try {
                    File caterpilappDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                            File.separator +
                            directoryName +
                            File.separator);
                    if (!caterpilappDir.exists()) {
                        caterpilappDir.mkdir();
                    }

                    File csvFile = new File(caterpilappDir, filename);
                    if (!csvFile.exists()) {
                        csvFile.createNewFile();
                    }
                    outputStream = new FileOutputStream(csvFile);
                    outputStream.write(string.getBytes());
                    outputStream.close();
                    callback.onSuccess(directoryName + File.separator + csvFile.getName());
                } catch (Exception e) {
                    callback.onFailure(e);
                    Log.e(TAG, "ERROR", e);
                }
            }

            @Override
            public void onFailure(DatabaseError error) {
                callback.onFailure(error.toException());
            }
        });
    }


    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class ObservationBinder extends Binder {
        public ObservationService getService() {
            // Return this instance of LocalService so clients can call public methods
            return ObservationService.this;
        }
    }



}
