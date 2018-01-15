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
    public static final String FIRST_OBSERVATION_ACTION = "first_observation";
    public static final String SECOND_OBSERVATION_ACTION = "second_observation";

    private UserManager mUserManager;
    private User mUser;
    private Menu homeMenu;
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
                if (homeMenu != null) {
                    MenuItem adminItem = homeMenu.findItem(R.id.action_admin);
                    if (adminItem != null) {
                        adminItem.setVisible(mUser.isAdmin());
                    }
                }
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

                        observationIntent.setAction(HomeActivity.FIRST_OBSERVATION_ACTION);
                        startActivity(observationIntent);
                    }
                });

                Button observationBisButton = (Button) findViewById(R.id.observationBisButton);
                observationBisButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent observationIntent = new Intent(mainContext, CatterpillarListActivity.class);

                        observationIntent.setAction(HomeActivity.SECOND_OBSERVATION_ACTION);
                        startActivity(observationIntent);
                    }
                });
                Button UploadButton = (Button) findViewById(R.id.uploadButton);
                UploadButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent UploadIntent = new Intent(mainContext, UploadActivity.class);
                        startActivity(UploadIntent);
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
                Button camera2Button = (Button) findViewById(R.id.camera2Button);
                camera2Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent cameraIntent2 = new Intent(mainContext, CameraIntentActivity.class);
                        startActivity(cameraIntent2);
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
        homeMenu = menu;
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
                break;
            case R.id.action_admin:
                if (mUser != null && mUser.isAdmin()) {
                    Intent adminIntent = new Intent(this, AdminActivity.class);
                    startActivity(adminIntent);
                }
            default: break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
    }
}
