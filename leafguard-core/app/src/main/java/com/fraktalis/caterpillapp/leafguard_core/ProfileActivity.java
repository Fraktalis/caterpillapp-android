package com.fraktalis.caterpillapp.leafguard_core;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.fraktalis.caterpillapp.leafguard_core.fragment.ProfileFormFragment;
import com.fraktalis.caterpillapp.leafguard_core.fragment.ProfileFragment;

public class ProfileActivity extends AppCompatActivity implements ProfileFragment.OnFragmentInteractionListener, ProfileFormFragment.OnFragmentInteractionListener {

    public static final String TAG = "ProfileActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.profile_fragment_container) != null) {
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
            ProfileFragment profileFragment = new ProfileFragment();
            profileFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.profile_fragment_container, profileFragment).commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
