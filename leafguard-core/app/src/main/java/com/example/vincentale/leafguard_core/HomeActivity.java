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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vincentale.leafguard_core.model.User;
import com.example.vincentale.leafguard_core.model.UserManager;
import com.example.vincentale.leafguard_core.util.DatabaseCallback;
import com.google.firebase.database.DatabaseError;

public class HomeActivity extends AppCompatActivity {

    public static final String TAG = "HomeActivity";
    private UserManager mUserManager;
    private User mUser;
    private LinearLayout profileLoadingLayout;
    private LinearLayout homeContentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mUserManager = UserManager.getInstance();
        profileLoadingLayout = (LinearLayout) findViewById(R.id.profile_loading_layout);
        homeContentLayout = (LinearLayout) findViewById(R.id.home_content_layout);
        mUserManager.getUser(new DatabaseCallback<User>() {
            @Override
            public void onSuccess(User identifiable) {
                mUser = identifiable;
                profileLoadingLayout.setVisibility(View.GONE);
                homeContentLayout.setVisibility(View.VISIBLE);
                Resources res = getResources();
                Toast.makeText(HomeActivity.this, "user is loaded !", Toast.LENGTH_SHORT).show();
                TextView helloText = (TextView) findViewById(R.id.helloText);
                if (mUser != null) {
                    helloText.setText(res.getString(R.string.hello_name, mUser.getUid()));
                } else {
                    Log.i(TAG, "You shouldn't be anonymous on this activity !");
                }
                final Context mainContext = HomeActivity.this;
                Button ourOakButton = (Button) findViewById(R.id.ourOakButton);
                ourOakButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent oakFormIntent = new Intent(mainContext, OakListActivity.class);
                        startActivity(oakFormIntent);
                    }
                });

                Button observationButton = (Button) findViewById(R.id.observationButton);
                observationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent observationIntent = new Intent(mainContext, CatterpillarListActivity.class);
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
            public void onFailure(DatabaseError error) {
                Log.d(TAG, "should not be here");
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
            case R.id.action_profile:
                Intent profilFormIntent = new Intent(this, ProfileActivity.class);
                startActivity(profilFormIntent);
            default: break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
    }
}
