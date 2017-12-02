package com.example.vincentale.leafguard_core;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.vincentale.leafguard_core.fragment.Camera2Fragment;

public class Camera2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (null == savedInstanceState) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, Camera2Fragment.newInstance())
                        .commit();
            }                
        } else {
            Toast.makeText(this, "Camera2 API is not supported on this display", Toast.LENGTH_SHORT).show();
        }

    }

}
