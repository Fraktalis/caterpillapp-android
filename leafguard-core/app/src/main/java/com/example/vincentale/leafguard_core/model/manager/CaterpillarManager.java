package com.example.vincentale.leafguard_core.model.manager;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.vincentale.leafguard_core.model.Caterpillar;
import com.example.vincentale.leafguard_core.model.Oak;
import com.example.vincentale.leafguard_core.util.DatabaseCallback;
import com.example.vincentale.leafguard_core.util.DatabaseListCallback;
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

/**
 * Created by mathilde on 22/01/18.
 */

public class CaterpillarManager implements Manager<Caterpillar> {

    public static final String NODE_NAME = "caterpillars";
    public final static String[] fieldsMapping = {"index", "oakUid", "woundByMammal", "woundByInsect", "woundByBird", "woundByLizard", "woundByOther", "edited", "catterpillarMissing"};
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
    public void update(@NonNull Caterpillar object) {
        DatabaseReference caterRef = firebaseDatabase.getReference().child(NODE_NAME).child(object.getOakUid()).child(object.getUid());
        try {
            for (String field :
                    fieldsMapping) {
                String getterName;
                if (Caterpillar.class.getDeclaredField(field).getType().equals(boolean.class)) {
                    getterName = "is" + capitalize(field);
                } else {
                    getterName = "get" + capitalize(field);
                }
                Log.d(TAG, getterName);
                Method getter = Caterpillar.class.getMethod(getterName);
                Log.d(TAG, ""+getter.invoke(object));
                caterRef.child(field).setValue(getter.invoke(object));
            }
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "update: ", e);
        } catch (InvocationTargetException e) {
            Log.e(TAG, "update: ", e);
        } catch (IllegalAccessException e) {
            Log.e(TAG, "update: ", e);
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "update: ", e);
        }

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
    public ArrayList<Caterpillar> findAll() {
        return null;
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

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
}
