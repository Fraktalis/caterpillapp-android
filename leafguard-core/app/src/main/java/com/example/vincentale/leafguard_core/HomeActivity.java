package com.example.vincentale.leafguard_core;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    public static final String TAG = "HomeActivity";
    public static final String FIRST_OBSERVATION_ACTION = "first_observation";
    public static final String SECOND_OBSERVATION_ACTION = "second_observation";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private UserManager mUserManager;
    private User mUser;
    private Menu homeMenu;
    private LinearLayout profileLoadingLayout;
    private LinearLayout homeContentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        givePermission();
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
                if (mUser.getOakId() != null) {
                    Drawable img = HomeActivity.this.getResources().getDrawable(R.drawable.ic_check_black_24dp);
                    ourOakButton.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                }

                String obervationUid = mUser.getOakId() + "_" + 1;
                Button observationButton = (Button) findViewById(R.id.observationButton);
                if (mUser.hasObservation(obervationUid)) {
                    Drawable img = HomeActivity.this.getResources().getDrawable(R.drawable.ic_check_black_24dp);
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
                    Drawable img = HomeActivity.this.getResources().getDrawable(R.drawable.ic_check_black_24dp);
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

//                Button camera2Button = (Button) findViewById(R.id.camera2Button);
//                camera2Button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        givePermission();
//                        if (ContextCompat.checkSelfPermission(HomeActivity.this,
//                                android.Manifest.permission.CAMERA)
//                                == PackageManager.PERMISSION_GRANTED) {
//                            Intent cameraIntent2 = new Intent(HomeActivity.this, CameraIntentActivity.class);
//                            startActivity(cameraIntent2);
//                        }
//                    }
//                });
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

    public void givePermission() {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, android.Manifest.permission.CAMERA))
            permissionsNeeded.add(getString(R.string.cameraPermission));
        if (!addPermission(permissionsList, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add(getString(R.string.storagePermission));
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = getString(R.string.messagePermission) + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (Build.VERSION.SDK_INT >= 23) {
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_PERMISSIONS);
                                } else {
                                    ActivityCompat.requestPermissions(HomeActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_PERMISSIONS);
                                }
                            }
                        });
                return;
            }
            if (Build.VERSION.SDK_INT >= 23) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_PERMISSIONS);
            } else {
                ActivityCompat.requestPermissions(HomeActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(HomeActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        } else {

        }
        return true;
    }
}
