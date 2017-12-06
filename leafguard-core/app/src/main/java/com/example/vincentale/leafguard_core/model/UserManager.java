package com.example.vincentale.leafguard_core.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserManager {
    public final static String TAG = "UserManager";
    private FirebaseAuth firebaseAuth;
    private static FirebaseUser firebaseUser;

    private static User user;
    private static UserManager userManager;



    private UserManager() {
    }

    public User getUser() {
        if (user == null) {
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference dbRef = database.getReference();
            Query users = dbRef.child("users").child(firebaseUser.getUid());
            users.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User databaseEntries = dataSnapshot.getValue(User.class);
                    if (databaseEntries == null) { //No entry for this particuliar entry. It shouldn't happen
                        user = new User(firebaseAuth.getCurrentUser().getUid());
                    } else {
                        databaseEntries.setUid(dataSnapshot.getKey());
                        user = new User(databaseEntries);
                    }
                    user.setEmail(firebaseAuth.getCurrentUser().getEmail());
                    Log.d(TAG, "user = " + user.toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        if (user == null) {
            user = new User(firebaseAuth.getCurrentUser().getUid());
            user.setEmail(firebaseAuth.getCurrentUser().getEmail());
        }
        return user;
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
     * Wrapper method to sign out from firebase Auth and clear user cache
     */
    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        user = null;
    }

    public void updateUser(@NonNull User user) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = dbRef.child("users").child(user.getUid());
        userRef.child("name").setValue(user.getName());
        userRef.child("surname").setValue(user.getSurname());
        userRef.child("role").setValue(user.getRole());
    }
}
