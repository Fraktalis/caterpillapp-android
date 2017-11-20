package com.example.vincentale.leafguard_core;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final Context mainContext = this;
        Button ourOakButton = (Button) findViewById(R.id.ourOakButton);
        ourOakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent oakFormIntent = new Intent(mainContext, OakFormActivity.class);
                startActivity(oakFormIntent);
            }
        });

        Button profilButton = (Button) findViewById(R.id.profilButton);
        profilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profilFormIntent = new Intent(mainContext, ProfilFormActivity.class);
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
    }
}
