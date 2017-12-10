package com.example.vincentale.leafguard_core;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
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

import com.example.vincentale.leafguard_core.model.User;
import com.example.vincentale.leafguard_core.model.UserManager;

public class HomeActivity extends AppCompatActivity {

    public static final String TAG = "HomeActivity";
    private UserManager mUserManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Resources res = getResources();
        mUserManager = UserManager.getInstance();
        User mUser = mUserManager.getUser();

        TextView helloText = (TextView) findViewById(R.id.helloText);
        if (mUser != null) {
            helloText.setText(res.getString(R.string.hello_name, mUser.getUid()));
        } else {
            Log.i(TAG, "You shouldn't be anonymous on this activity !");
        }
        final Context mainContext = this;
        Button ourOakButton = (Button) findViewById(R.id.ourOakButton);
        ourOakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent oakFormIntent = new Intent(mainContext, OakListActivity.class);
                startActivity(oakFormIntent);
            }
        });

        Button profilButton = (Button) findViewById(R.id.profilButton);
        profilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profilFormIntent = new Intent(mainContext, ProfileActivity.class);
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

        Button cameraButton = (Button) findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(mainContext, Camera2Activity.class);
                startActivity(cameraIntent);
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
                mUserManager.signOut();
                Toast.makeText(this, R.string.log_out_success, Toast.LENGTH_SHORT).show();
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
                break;
            default: break;
        }
        return true;
    }
}
