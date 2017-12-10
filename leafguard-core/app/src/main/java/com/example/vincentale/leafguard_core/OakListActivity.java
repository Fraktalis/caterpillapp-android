package com.example.vincentale.leafguard_core;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.vincentale.leafguard_core.fragment.OakListFragment;

public class OakListActivity extends AppCompatActivity implements OakListFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oak);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.oak_fragment_container) != null) {
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
            OakListFragment oakListFragment = new OakListFragment();
            oakListFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.oak_fragment_container, oakListFragment).commit();
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
