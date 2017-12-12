package com.example.vincentale.leafguard_core;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.vincentale.leafguard_core.fragment.OakFormFragment;
import com.example.vincentale.leafguard_core.fragment.OakFragment;
import com.example.vincentale.leafguard_core.fragment.ProfileFragment;

public class OakActivity extends AppCompatActivity implements OakFragment.OnFragmentInteractionListener, OakFormFragment.OnFragmentInteractionListener {

    public static final String TAG = "OakActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oak);

        if (findViewById(R.id.oak_fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            String action = getIntent().getAction();
            if (action.equals(OakFragment.EDIT_OAK_ACTION)) {
                OakFragment oakFragment = new OakFragment();
                oakFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().add(R.id.oak_fragment_container, oakFragment).commit();
            } else if (action.equals(OakFragment.NEW_OAK_ACTION)) {
                OakFormFragment oakFormFragment = new OakFormFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.oak_fragment_container, oakFormFragment).commit();
            } else {
                Log.d(TAG, "The action of the intent is unsupported.");
            }
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
