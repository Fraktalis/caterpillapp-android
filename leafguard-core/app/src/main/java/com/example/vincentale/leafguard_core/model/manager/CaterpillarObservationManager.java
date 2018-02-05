package com.example.vincentale.leafguard_core.model.manager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.vincentale.leafguard_core.model.Caterpillar;
import com.example.vincentale.leafguard_core.model.CaterpillarObservation;
import com.example.vincentale.leafguard_core.util.DatabaseCallback;
import com.example.vincentale.leafguard_core.util.DatabaseListCallback;
import com.example.vincentale.leafguard_core.util.OnUpdateCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.example.vincentale.leafguard_core.util.StringHelper.capitalize;

/**
 * Created by vincentale on 29/01/18.
 */

public class CaterpillarObservationManager implements Manager<CaterpillarObservation> {

    public static final String NODE_NAME = "caterpillar_observations";
    public final static String[] fieldsMapping = {"totalCaterpillars","missingCaterpillars","totalPredationMarks","birdPredationMarks","arthropodPredationMarks","mammalPredationMarks","lizardPredationMarks"};
    private static final String TAG = "CaterpillarObsManager";
    private static CaterpillarObservationManager instance;
    private FirebaseDatabase firebaseDatabase;

    public CaterpillarObservationManager() {
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public static CaterpillarObservationManager getInstance() {
        if (instance == null) {
            instance = new CaterpillarObservationManager();
        }

        return instance;
    }

    @Override
    public void update(@NonNull CaterpillarObservation object, @Nullable final OnUpdateCallback updateCallback) {
        DatabaseReference observationRef = firebaseDatabase.getReference().child(NODE_NAME).child(object.getUid());
        try {
            for (String field :
                    fieldsMapping) {
                String getterName;
                if (CaterpillarObservation.class.getDeclaredField(field).getType().equals(boolean.class)) {
                    getterName = "is" + capitalize(field);
                } else {
                    getterName = "get" + capitalize(field);
                }
                Log.d(TAG, getterName);
                Method getter = CaterpillarObservation.class.getMethod(getterName);
                Log.d(TAG, ""+getter.invoke(object));
                observationRef.child(field).setValue(getter.invoke(object));
            }
            //Observation have a timestamp, so we add it to the database
            observationRef.child("timestamp").setValue(object.getTimestamp());
        } catch (Exception e) {
            Log.e(TAG, "update: ", e);
            if (updateCallback != null)
                updateCallback.onError(e);
        }
        if (updateCallback != null)
            updateCallback.onSuccess();
    }

    @Override
    public void delete(@NonNull CaterpillarObservation object) {

    }

    @Override
    public void find(String uid, DatabaseCallback<CaterpillarObservation> callback) {

    }

    @Override
    public void findAll(DatabaseListCallback<CaterpillarObservation> listCallback) {

    }
}
