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
       /* nbClassA = findViewById(R.id.classAText);
        nbClassB = findViewById(R.id.classBText);
        nbClassC = findViewById(R.id.classCText);
        nbClassD = findViewById(R.id.classDText);
        nbClassE = findViewById(R.id.classEText);
        nbClassF = findViewById(R.id.classFText);
        nbClassG = findViewById(R.id.classGText);
        nbClassH = findViewById(R.id.classHText);*/

        final Context context= this;

        nbLeaves.setText(leavesObservation.getLeavesTotal());
        nbGalls.setText(leavesObservation.getGallsTotal());
        nbMines.setText(leavesObservation.getMinesTotal());
        /*nbClassA.setText(leavesObservation.getLeavesAClassNumber());
        nbClassB.setText(leavesObservation.getLeavesBClassNumber());
        nbClassC.setText(leavesObservation.getLeavesCClassNumber());
        nbClassD.setText(leavesObservation.getLeavesDClassNumber());
        nbClassE.setText(leavesObservation.getLeavesEClassNumber());
        nbClassF.setText(leavesObservation.getLeavesFClassNumber());
        nbClassG.setText(leavesObservation.getLeavesGClassNumber());
        nbClassH.setText(leavesObservation.getLeavesHClassNumber());*/

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
