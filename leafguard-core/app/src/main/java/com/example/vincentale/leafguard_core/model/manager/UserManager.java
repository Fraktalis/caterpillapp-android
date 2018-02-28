package com.example.vincentale.leafguard_core.model.manager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.vincentale.leafguard_core.model.Oak;
import com.example.vincentale.leafguard_core.model.User;
import com.example.vincentale.leafguard_core.util.DatabaseCallback;
import com.example.vincentale.leafguard_core.util.DatabaseListCallback;
import com.example.vincentale.leafguard_core.util.DatabaseListCallback;
import com.example.vincentale.leafguard_core.util.OnUpdateCallback;
import com.example.vincentale.leafguard_core.util.ReflectionHelper;
import com.example.vincentale.leafguard_core.util.StringHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class UserManager implements Manager<User> {
    public final static String TAG = "UserManager";
    public final static String NODE_NAME = "users";
    public final static String[] fieldsMapping = {"name", "surname", "email", "oakId", "role", "partnerId", "schoolName", "schoolLevel", "studentAge", "leavesObservationSent", "observationUids"};
    private static FirebaseUser firebaseUser;
    private static User user;
    private static UserManager userManager;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private OakManager oakManager;



    private UserManager() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        oakManager = OakManager.getInstance();
    }

    /**
     *
     * @return a singleton instance of {@link UserManager}
     */
    public static UserManager getInstance() {
        if (userManager == null) {
            userManager = new UserManager();
        }

        return userManager;
    }

    /**
     * Method to retrieve the main user of the application as a singleton.
     * @param callback
     */
    public void getUser(final DatabaseCallback<User> callback) {
        if (user != null) {
            callback.onSuccess(user);
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference dbRef = database.getReference();
        if (user == null) {
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();
            Query users = dbRef.child(NODE_NAME).child(firebaseUser.getUid());
            users.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "user retrieved from firebase");
                    Log.d(TAG, "onDataChange: " + dataSnapshot.toString());
                    User databaseEntries = dataSnapshot.getValue(User.class);
                    Log.d(TAG, "user from db : " + databaseEntries);
                    if (databaseEntries == null) { //No entry for this particuliar entry. It shouldn't happen
                        user = new User(firebaseAuth.getCurrentUser().getUid());
                    } else {
                        databaseEntries.setUid(dataSnapshot.getKey());
                        user = new User(databaseEntries);
                    }
                    user.setEmail(firebaseAuth.getCurrentUser().getEmail());
                    if (user.getOakId() != null && user.getOak() == null) {
                        oakManager.find(user.getOakId(), new DatabaseCallback<Oak>() {
                            @Override
                            public void onSuccess(Oak identifiable) {

                                user.setOak(identifiable);
                                Log.d(TAG, "no Oak user : " + user);
                                callback.onSuccess(user);
                            }

                            @Override
                            public void onFailure(DatabaseError error) {
                            }
                        });
                    } else {
                        Log.d(TAG, "no Oak user : " + user);
                        callback.onSuccess(user);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    callback.onFailure(databaseError);
                }
            });
        }
        if (user == null) {
            user = new User(firebaseAuth.getCurrentUser().getUid());
            user.setEmail(firebaseAuth.getCurrentUser().getEmail());
        }
    }

    /**
     * Wrapper method to sign out from firebase Auth and clear user cache
     */
    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        user = null;
    }

    @Override
    public void update(@NonNull User object, @Nullable final OnUpdateCallback updateCallback) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userRef = firebaseDatabase.getReference().child(NODE_NAME).child(object.getUid());
        try {
            for (String field :
                    fieldsMapping) {
                Object res = ReflectionHelper.invokeGetter(field, object, User.class);
                if (res != null && res instanceof Oak) { //if res is an Oak, we only want it's UID
                    Log.d(TAG, "Res is an Oak");
                    userRef.child(field).setValue(((Oak) res).getUid());
                } else {
                    userRef.child(field).setValue(res);
                }
            }
            userRef.child("observationUids").setValue(Arrays.toString(user.getObservationUidSet().toArray()));
        } catch (Exception e) {
            Log.e(TAG, "update:", e);
            if (updateCallback != null)
                updateCallback.onError(e);
        }
        if (updateCallback != null)
            updateCallback.onSuccess();
    }

    @Override
    public void delete(@NonNull User object) {

    }

    @Override
    public void find(String uid, DatabaseCallback<User> callback) {


    }

    @Override
    public void findAll(DatabaseListCallback<User> listCallback) {
    }


}
