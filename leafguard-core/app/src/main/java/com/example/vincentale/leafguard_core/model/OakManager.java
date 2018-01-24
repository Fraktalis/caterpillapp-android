package com.example.vincentale.leafguard_core.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.vincentale.leafguard_core.util.DatabaseCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class OakManager implements Manager<Oak> {
    public static final String TAG = "OakManager";

    public static final String NODE_NAME = "oaks";
    private static final String[] fieldsMapping = {"longitude", "latitude", "oakCircumference", "oakHeight", "installationDate"};
    private static OakManager manager;
    private FirebaseDatabase firebaseDatabase;


    public OakManager() {
        this.firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public static OakManager getInstance() {
        if (manager == null) {
            manager = new OakManager();
        }

        return manager;
    }

    @Override
    public void update(@NonNull Oak object) {
        DatabaseReference oakRef = firebaseDatabase.getReference().child(NODE_NAME).child(object.getUid());
        try {
            for (String field :
                    fieldsMapping) {
                String getterName = "get" + capitalize(field);
                Log.d(TAG, getterName);
                Method getter = Oak.class.getMethod(getterName);
                Log.d(TAG, ""+getter.invoke(object));
                oakRef.child(field).setValue(getter.invoke(object));
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(@NonNull Oak object) {
    }

    @Override
    public void find(String uid, final DatabaseCallback<Oak> callback) {
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        DatabaseReference oakRef = databaseReference.child(NODE_NAME).child(uid);
        oakRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Oak dbRes = dataSnapshot.getValue(Oak.class);
                dbRes.setUid(dataSnapshot.getKey());
                callback.onSuccess(dbRes);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure(databaseError);
            }
        });
    }

    @Override
    public ArrayList<Oak> findAll() {

        return null;
    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
}
