package com.example.vincentale.leafguard_core;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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
        final Context mainContext = this;
        Button ourOakButton = (Button) findViewById(R.id.ourOakButton);
        ourOakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent oakFormIntent = new Intent(mainContext, OakFormActivity.class);
                startActivity(oakFormIntent);
            }
        });

        Button profilButton = (Button) findViewById(R.id.profilButton);
        profilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profilFormIntent = new Intent(mainContext, ProfilFormActivity.class);
                startActivity(profilFormIntent);
            }
        });

        Button observationButton = (Button) findViewById(R.id.observationButton);
        observationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent observationIntent = new Intent(mainContext, ObservationActivity.class);
                startActivity(observationIntent);
            }
        });

        Button observationBisButton = (Button) findViewById(R.id.observationBisButton);
        observationBisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent observationBisIntent = new Intent(mainContext, ObservationActivity.class);
                startActivity(observationBisIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
                break;
            default: break;
        }
        return true;
    }
}
