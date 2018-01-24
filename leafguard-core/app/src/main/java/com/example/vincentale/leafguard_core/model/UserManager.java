package com.example.vincentale.leafguard_core.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.vincentale.leafguard_core.util.DatabaseCallback;
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

public class UserManager implements Manager<User> {
    public final static String TAG = "UserManager";
    public final static String NODE_NAME = "users";
    public final static String[] fieldsMapping = {"name", "surname", "email", "oakId", "role"};
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
                            }

                            @Override
                            public void onFailure(DatabaseError error) {
                            }
                        });
                    }
                    callback.onSuccess(user);
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
    public void update(@NonNull User object) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference oakRef = firebaseDatabase.getReference().child(NODE_NAME).child(object.getUid());
        try {
            for (String field :
                    fieldsMapping) {
                String getterName = "get" + capitalize(field);
                Method getter = User.class.getMethod(getterName);
                Object res = getter.invoke(object);
                if (res != null && res instanceof Oak) { //if res is an Oak, we only want it's UID
                    Log.d(TAG, "Res is an Oak");
                    oakRef.child(field).setValue(((Oak) res).getUid());
                } else {
                    oakRef.child(field).setValue(res);
                }
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
    public void delete(@NonNull User object) {

    }

    @Override
    public void find(String uid, DatabaseCallback<User> callback) {


    }

    @Override
    public ArrayList<User> findAll() {
        return null;
    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
}
