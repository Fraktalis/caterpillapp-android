package com.example.vincentale.leafguard_core;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    public static final String TAG = "HomeActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Resources res = getResources();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        TextView helloText = (TextView) findViewById(R.id.helloText);
        if (mUser != null) {
            helloText.setText(res.getString(R.string.hello_name, mUser.getDisplayName()));
        } else {
            Log.i(TAG, "You shouldn't be anonymous on this activity !");
        }
    }
}
