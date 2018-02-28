package com.example.vincentale.leafguard_core;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vincentale.leafguard_core.model.User;
import com.example.vincentale.leafguard_core.model.manager.UserManager;
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
                    helloText.setText(res.getString(R.string.hello_name, mUser.getSurname()));
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

                Drawable img = HomeActivity.this.getResources().getDrawable(R.drawable.ic_check_black_24dp);
                if (mUser.getOakId() != null) {
                    ourOakButton.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                }

                String obervationUid = mUser.getOakId() + "_" + 1;
                Button observationButton = (Button) findViewById(R.id.observationButton);
                if (mUser.hasObservation(obervationUid)) {
                    observationButton.setEnabled(false);
                    observationButton.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                } else {
                    Log.d(TAG, "onSuccess: " + mUser.getObservationUids());
                    observationButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent observationIntent = new Intent(mainContext, CaterpillarListActivity.class);
                            observationIntent.setAction(HomeActivity.FIRST_OBSERVATION_ACTION);
                            observationIntent.putExtra("observationIndex", 1);
                            startActivity(observationIntent);
                        }
                    });
                }

                obervationUid = mUser.getOakId() + "_" + 2;
                Button observationBisButton = (Button) findViewById(R.id.observationBisButton);
                if (mUser.hasObservation(obervationUid)) {

                    observationBisButton.setEnabled(false);
                    observationBisButton.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                } else {
                    observationBisButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent observationIntent = new Intent(mainContext, CaterpillarListActivity.class);
                            observationIntent.setAction(HomeActivity.SECOND_OBSERVATION_ACTION);
                            observationIntent.putExtra("observationIndex", 2);
                            startActivity(observationIntent);
                        }
                    });
                }

                Button leavesObservationButton = findViewById(R.id.leavesObservationButton);
                if (mUser.isLeavesObservationSent()) {
                    leavesObservationButton.setEnabled(false);
                    leavesObservationButton.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                } else {
                    leavesObservationButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent leavesObservationIntent = new Intent(mainContext, LeavesViewActivity.class);
                            startActivity(leavesObservationIntent);
                        }
                    });
                }
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
