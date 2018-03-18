package com.fraktalis.caterpillapp.leafguard_core.model.manager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.fraktalis.caterpillapp.leafguard_core.model.Oak;
import com.fraktalis.caterpillapp.leafguard_core.util.DatabaseCallback;
import com.fraktalis.caterpillapp.leafguard_core.util.DatabaseListCallback;
import com.fraktalis.caterpillapp.leafguard_core.util.OnUpdateCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    public void update(@NonNull Oak object, @Nullable final OnUpdateCallback updateCallback) {
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
        } catch (Exception e) {
            Log.e(TAG, "update:", e);
            if (updateCallback != null)
                updateCallback.onError(e);
        }
        if (updateCallback != null)
            updateCallback.onSuccess();
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
    public void findAll(final DatabaseListCallback<Oak> listCallback) {
        final ArrayList<Oak> oaks=new ArrayList<>();

        DatabaseReference databaseReference = firebaseDatabase.getReference();
        DatabaseReference allOakReference= databaseReference.child(NODE_NAME);
        allOakReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot oakInfo : dataSnapshot.getChildren()){
                    String key= oakInfo.getKey();
                    Oak newValue= oakInfo.getValue(Oak.class);
                    newValue.setUid(key);
                    Log.d(TAG,key);
                    oaks.add(newValue);
                }
                listCallback.onSuccess(oaks);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listCallback.onFailure(databaseError);
            }
        });

    }


    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
}
