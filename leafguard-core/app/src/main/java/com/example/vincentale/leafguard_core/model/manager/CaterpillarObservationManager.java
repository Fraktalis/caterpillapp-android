package com.example.vincentale.leafguard_core.model.manager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.vincentale.leafguard_core.model.Caterpillar;
import com.example.vincentale.leafguard_core.model.CaterpillarObservation;
import com.example.vincentale.leafguard_core.model.Oak;
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.example.vincentale.leafguard_core.util.StringHelper.capitalize;

/**
 * Created by vincentale on 29/01/18.
 */

public class CaterpillarObservationManager implements Manager<CaterpillarObservation> {

    public static final String NODE_NAME = "observations";
    public final static String[] fieldsMapping = {"totalCaterpillars","missingCaterpillars","totalPredationMarks","birdPredationMarks","arthropodPredationMarks","mammalPredationMarks","lizardPredationMarks", "observationIndex"};
    private static final String TAG = "CaterpillarObsManager";
    private static CaterpillarObservationManager instance;
    private UserManager userManager = UserManager.getInstance();
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
    public void update(@NonNull final CaterpillarObservation object, @Nullable final OnUpdateCallback updateCallback) {
        UserManager.getInstance().getUser(new DatabaseCallback<User>() {
            @Override
            public void onSuccess(User currentUser) {
                DatabaseReference observationRef = firebaseDatabase.getReference().child(NODE_NAME).child(object.getUid()).child(String.valueOf(object.getObservationIndex()));
                try {
                    for (String field :
                            fieldsMapping) {
                        Object res = ReflectionHelper.invokeGetter(field, object, CaterpillarObservation.class);
                        observationRef.child(field).setValue(res);
                    }
                    //Observation have a timestamp, so we add it to the database
                    observationRef.child("timestamp").setValue(object.getTimestamp());

                    /**
                     * The first observation holds the informations about user and oak, in order
                     * to avoid loading those data at CSV generation and deal with multiple
                     * async database call.
                     */
                    if (object.getObservationIndex() == 1) {
                        observationRef.child("partnerNumber").setValue("To_IMPLEMENT");
                        observationRef.child("longitude").setValue(currentUser.getOak().getLongitude());
                        observationRef.child("latitude").setValue(currentUser.getOak().getLatitude());
                        observationRef.child("circumference").setValue(currentUser.getOak().getOakCircumference());
                        observationRef.child("height").setValue(currentUser.getOak().getOakHeight());
                        observationRef.child("installationDate").setValue(new SimpleDateFormat("yyyy-MM-dd").format(currentUser.getOak().getInstallationDate()));

                        //About user
                        observationRef.child("schoolName").setValue("To_IMPLEMENT");
                        observationRef.child("fullname").setValue(currentUser.getDisplayName());
                        observationRef.child("email").setValue(currentUser.getEmail());
                        observationRef.child("studentsAge").setValue(-1);
                    }



                } catch (Exception e) {
                    Log.e(TAG, "update: ", e);
                    if (updateCallback != null)
                        updateCallback.onError(e);
                }
                if (updateCallback != null)
                    updateCallback.onSuccess();
            }

            @Override
            public void onFailure(DatabaseError error) {

            }
        });
    }

    @Override
    public void delete(@NonNull CaterpillarObservation object) {

    }

    @Override
    public void find(String uid, DatabaseCallback<CaterpillarObservation> callback) {

    }

    @Override
    public void findAll(final DatabaseListCallback<CaterpillarObservation> listCallback) {
        final ArrayList<CaterpillarObservation> observations = new ArrayList<>();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        DatabaseReference observationRef = databaseReference.child(NODE_NAME);
        observationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot oakObservation : dataSnapshot.getChildren()) {
                    String key = oakObservation.getKey();
                    for (DataSnapshot ds : oakObservation.getChildren()) {
                        CaterpillarObservation newValue = ds.getValue(CaterpillarObservation.class);
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
