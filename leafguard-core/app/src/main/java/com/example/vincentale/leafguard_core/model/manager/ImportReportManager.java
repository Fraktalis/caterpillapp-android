package com.example.vincentale.leafguard_core.model.manager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.vincentale.leafguard_core.model.ImportReport;
import com.example.vincentale.leafguard_core.util.DatabaseCallback;
import com.example.vincentale.leafguard_core.util.DatabaseListCallback;
import com.example.vincentale.leafguard_core.util.OnUpdateCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

/**
 * Created by vincentale on 03/03/18.
 */

public class ImportReportManager implements Manager<ImportReport> {

    public static final String NODE_NAME = "reports";
    public static final String TAG = "ImportReportManager";
    private static ImportReportManager instance;
    private FirebaseDatabase firebaseDatabase =FirebaseDatabase.getInstance();

    public static ImportReportManager getInstance() {
        if (instance == null) {
            instance = new ImportReportManager();
        }

        return instance;
    }

    @Override
    public void update(@NonNull ImportReport object, @Nullable OnUpdateCallback onUpdateCallback) {
    }

    @Override
    public void delete(@NonNull ImportReport object) {

    }

    @Override
    public void find(String uid, DatabaseCallback<ImportReport> callback) {

    }

    @Override
    public void findAll(DatabaseListCallback<ImportReport> listCallback) {

    }

    public void findLast(final DatabaseCallback<ImportReport> callback) {
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        final DatabaseReference reportRef = databaseReference.child(NODE_NAME);
        ValueEventListener reportEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> reportIterator = dataSnapshot.getChildren().iterator();
                DataSnapshot lastReport = reportIterator.next();
                while (reportIterator.hasNext()) {
                    lastReport = reportIterator.next();
                }

                ImportReport report = lastReport.getValue(ImportReport.class);
                callback.onSuccess(report);
                reportRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: ", databaseError.toException());
            }
        };

        reportRef.addValueEventListener(reportEventListener);
    }
}
