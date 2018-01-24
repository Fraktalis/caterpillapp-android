package com.example.vincentale.leafguard_core.model;

import android.support.annotation.NonNull;

import com.example.vincentale.leafguard_core.util.DatabaseCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by mathilde on 22/01/18.
 */

public class CaterpillarManager implements Manager<Catterpillar> {

    public static final String NODE_NAME = "caterpillars";
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

    @Override
    public void update(@NonNull Catterpillar object) {

    }

    @Override
    public void delete(@NonNull Catterpillar object) {

    }

    @Override
    public void find(String uid, final DatabaseCallback<Catterpillar> callback) {
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        DatabaseReference oakRef = databaseReference.child(NODE_NAME).child(uid);
        oakRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Catterpillar dbRes = dataSnapshot.getValue(Catterpillar.class);
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
    public ArrayList<Catterpillar> findAll() {
        return null;
    }
}
