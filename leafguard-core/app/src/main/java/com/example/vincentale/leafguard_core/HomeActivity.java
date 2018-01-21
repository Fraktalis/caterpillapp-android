package com.example.vincentale.leafguard_core;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.example.vincentale.leafguard_core.model.UserManager;
import com.example.vincentale.leafguard_core.util.DatabaseCallback;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    public static final String TAG = "HomeActivity";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
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

                Button camera2Button = (Button) findViewById(R.id.camera2Button);
                camera2Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        givePermission();
                        if (ContextCompat.checkSelfPermission(HomeActivity.this,
                                android.Manifest.permission.CAMERA)
                                == PackageManager.PERMISSION_GRANTED) {
                            Intent cameraIntent2 = new Intent(HomeActivity.this, CameraIntentActivity.class);
                            startActivity(cameraIntent2);
                        }
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

    public void givePermission() {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, android.Manifest.permission.CAMERA))
            permissionsNeeded.add("Camera");
        if (!addPermission(permissionsList, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Storage");
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
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
