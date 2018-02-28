package com.example.vincentale.leafguard_core;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.vincentale.leafguard_core.model.LeavesObservation;
import com.example.vincentale.leafguard_core.model.User;
import com.example.vincentale.leafguard_core.model.manager.LeavesObservationManager;
import com.google.firebase.database.FirebaseDatabase;

public class LeavesViewActivity extends AppCompatActivity {


    public static final String TAG = "LeavesViewActivity";
    private FirebaseDatabase firebaseDatabase;
    private com.example.vincentale.leafguard_core.model.manager.UserManager userManager;
    private LeavesObservation leavesObservation= new LeavesObservation();
    private LeavesObservationManager leavesObservationManager;
    private String oakUid;
    private User user;


    private TextView nbLeaves;
    private TextView nbGalls;
    private TextView nbMines;
    private TextView nbClassA;
    private TextView nbClassB;
    private TextView nbClassC;
    private TextView nbClassD;
    private TextView nbClassE;
    private TextView nbClassF;
    private TextView nbClassG;
    private TextView nbClassH;

    private Button edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaves_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        leavesObservation= new LeavesObservation();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        nbLeaves = findViewById(R.id.numberObservedLeavesText);
        nbGalls = findViewById(R.id.numbergallsText);
        nbMines = findViewById(R.id.numberMinesText);
        nbClassA = findViewById(R.id.classAText);
        nbClassB = findViewById(R.id.classBText);
        nbClassC = findViewById(R.id.classCText);
        nbClassD = findViewById(R.id.classDText);
        nbClassE = findViewById(R.id.classEText);
        nbClassF = findViewById(R.id.classFText);
        nbClassG = findViewById(R.id.classGText);
        nbClassH = findViewById(R.id.classHText);

        final Context context= this;

        nbLeaves.setText(String.valueOf(leavesObservation.getLeavesTotal()));
        nbGalls.setText(String.valueOf(leavesObservation.getGallsTotal()));
        nbMines.setText(String.valueOf(leavesObservation.getMinesTotal()));
        nbClassA.setText(String.valueOf(leavesObservation.getLeavesAClassNumber()));
        nbClassB.setText(String.valueOf(leavesObservation.getLeavesBClassNumber()));
        nbClassC.setText(String.valueOf(leavesObservation.getLeavesCClassNumber()));
        nbClassD.setText(String.valueOf(leavesObservation.getLeavesDClassNumber()));
        nbClassE.setText(String.valueOf(leavesObservation.getLeavesEClassNumber()));
        nbClassF.setText(String.valueOf(leavesObservation.getLeavesFClassNumber()));
        nbClassG.setText(String.valueOf(leavesObservation.getLeavesGClassNumber()));
        nbClassH.setText(String.valueOf(leavesObservation.getLeavesHClassNumber()));

        edit = findViewById(R.id.editLeavesButton);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent leavesObservationIntent = new Intent(context, LeavesObservationActivity.class);
                startActivity(leavesObservationIntent);

            }

        });


    }

}
