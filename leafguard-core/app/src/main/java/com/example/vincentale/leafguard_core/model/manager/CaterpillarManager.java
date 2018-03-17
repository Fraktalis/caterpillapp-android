package com.example.vincentale.leafguard_core.model.manager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.vincentale.leafguard_core.model.Caterpillar;
import com.example.vincentale.leafguard_core.model.Oak;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.vincentale.leafguard_core.util.StringHelper.capitalize;

/**
 * Created by mathilde on 22/01/18.
 */

public class CaterpillarManager implements Manager<Caterpillar> {

    public static final String NODE_NAME = "caterpillars";
    public final static String[] fieldsMapping = {"index", "observationIndex", "oakUid", "woundByMammal", "woundByInsect", "woundByBird", "woundByLizard", "woundByOther", "edited", "catterpillarMissing"};
    private static final String TAG = "CaterpillarManager";
    private static CaterpillarManager managerInstance;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    /**
     * Method to get a manager instance as a Singleton
     * @return
     */
    public static CaterpillarManager getInstance() {
        if (managerInstance == null) {
            managerInstance = new CaterpillarManager();
        }

        return managerInstance;
    }

    /**
     * Helper method to convert an ArrayList of caterpillar into a HashMap
     * @param caterpillars : the key is the UID and the value is the caterpillar itself
     * @return catterpillarMap
     */
    public static Map<String, Caterpillar> toHashMap(List<Caterpillar> caterpillars) {
        HashMap<String, Caterpillar> catterpillarMap = new HashMap<>();
        for (Caterpillar caterpillar : caterpillars) {
            catterpillarMap.put(caterpillar.getUid(), caterpillar);
            Log.d(TAG, "toHashMap: map("+ caterpillar.getUid()+") = " + caterpillar.toString());
        }

        return catterpillarMap;
    }

    @Override
    public void update(@NonNull Caterpillar object, @Nullable final OnUpdateCallback updateCallback) {
        DatabaseReference caterRef = firebaseDatabase.getReference().child(NODE_NAME).child(object.getOakUid()).child(object.getUid());
        try {
            for (String field :
                    fieldsMapping) {
                Object res = ReflectionHelper.invokeGetter(field, object, Caterpillar.class);
                caterRef.child(field).setValue(res);
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
    public void delete(@NonNull Caterpillar object) {

    }

    @Override
    public void find(String uid, final DatabaseCallback<Caterpillar> callback) {

        String[] splitResult = uid.split("_");
        String oakUid = splitResult[0] + "_" + splitResult[1] + "_" + splitResult[2]; // We have to extract the oak UID from the caterpillar one
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        DatabaseReference caterRef = databaseReference.child(NODE_NAME).child(oakUid).child(uid);
        caterRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Caterpillar dbRes = dataSnapshot.getValue(Caterpillar.class);
                if (dbRes != null) {
                    dbRes.setUid(dataSnapshot.getKey());
                    callback.onSuccess(dbRes);
                    Log.d(TAG, "onDataChange: " + dbRes);
                } else {
                    callback.onSuccess(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure(databaseError);
            }
        });
    }

    @Override
    public void findAll(DatabaseListCallback listCallback) {
        //TODO: Implements if necessary
    }

    public void findAllbyOak(Oak oak, final DatabaseListCallback<Caterpillar> callback) {
        final ArrayList<Caterpillar> caterpillars = new ArrayList<>();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        DatabaseReference caterRef = databaseReference.child(NODE_NAME).child(oak.getUid());
        caterRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Caterpillar newValue = ds.getValue(Caterpillar.class);
                    newValue.setUid(ds.getKey());
                    caterpillars.add(newValue);
                }
                callback.onSuccess(caterpillars);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure(databaseError);
            }
        });
    }

}
