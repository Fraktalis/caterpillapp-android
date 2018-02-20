package com.example.vincentale.leafguard_core.model.manager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.vincentale.leafguard_core.model.CaterpillarObservation;
import com.example.vincentale.leafguard_core.model.LeavesObservation;
import com.example.vincentale.leafguard_core.model.User;
import com.example.vincentale.leafguard_core.util.DatabaseCallback;
import com.example.vincentale.leafguard_core.util.DatabaseListCallback;
import com.example.vincentale.leafguard_core.util.OnUpdateCallback;
import com.example.vincentale.leafguard_core.util.ReflectionHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Firebase manager for Leaves observation
 */

public class LeavesObservationManager implements Manager<LeavesObservation> {

    public static final String NODE_NAME = "observations";
    public static final String NODE_LEAF = "leaves";
    public final static String[] fieldsMapping = {"leavesTotal", "gallsTotal", "minesTotal", "leavesAClassNumber", "leavesBClassNumber", "leavesCClassNumber", "leavesDClassNumber", "leavesEClassNumber", "leavesFClassNumber", "leavesGClassNumber", "leavesHClassNumber"};
    private static final String TAG = "LeavesObsManager";
    private static LeavesObservationManager instance;
    private UserManager userManager = UserManager.getInstance();
    private FirebaseDatabase firebaseDatabase;

    public LeavesObservationManager() {
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public static LeavesObservationManager getInstance() {
        if (instance == null) {
            instance = new LeavesObservationManager();
        }

        return instance;
    }

    @Override
    public void update(@NonNull final LeavesObservation object, @Nullable final OnUpdateCallback onUpdateCallback) {
        UserManager.getInstance().getUser(new DatabaseCallback<User>() {
            @Override
            public void onSuccess(User currentUser) {
                DatabaseReference observationRef = firebaseDatabase.getReference().child(NODE_NAME).child(object.getUid()).child(NODE_LEAF);
                try {
                    for (String field :
                            fieldsMapping) {
                        Object res = ReflectionHelper.invokeGetter(field, object, CaterpillarObservation.class);
                        observationRef.child(field).setValue(res);
                    }
                    //Observation have a timestamp, so we add it to the database
                    observationRef.child("timestamp").setValue(object.getTimestamp());




                } catch (Exception e) {
                    Log.e(TAG, "update: ", e);
                    if (onUpdateCallback != null)
                        onUpdateCallback.onError(e);
                }
                if (onUpdateCallback != null)
                    onUpdateCallback.onSuccess();
            }

            @Override
            public void onFailure(DatabaseError error) {
                Log.e(TAG, "Error during update", error.toException());
            }
        });
    }

    @Override
    public void delete(@NonNull LeavesObservation object) {

    }

    @Override
    public void find(String uid, DatabaseCallback<LeavesObservation> callback) {

    }

    @Override
    public void findAll(final DatabaseListCallback<LeavesObservation> listCallback) {
        final ArrayList<LeavesObservation> observations = new ArrayList<>();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        DatabaseReference observationRef = databaseReference.child(NODE_NAME);
        observationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot oakObservation : dataSnapshot.getChildren()) {
                    String key = oakObservation.getKey();
                    for (DataSnapshot ds : oakObservation.getChildren()) {
                        LeavesObservation newValue = ds.getValue(LeavesObservation.class);
                        newValue.setUid(key);
                        observations.add(newValue);
                    }
                }
                listCallback.onSuccess(observations);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listCallback.onFailure(databaseError);
            }
        });
    }
}
