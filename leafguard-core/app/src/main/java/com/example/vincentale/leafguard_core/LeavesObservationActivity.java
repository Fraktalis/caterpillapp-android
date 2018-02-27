package com.example.vincentale.leafguard_core;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.vincentale.leafguard_core.fragment.LeavesFormFragment;
import com.example.vincentale.leafguard_core.fragment.LeavesViewFragment;
import com.example.vincentale.leafguard_core.model.LeavesObservation;
import com.example.vincentale.leafguard_core.model.User;
import com.example.vincentale.leafguard_core.model.manager.LeavesObservationManager;
import com.example.vincentale.leafguard_core.model.manager.UserManager;
import com.example.vincentale.leafguard_core.util.DatabaseCallback;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

public class LeavesObservationActivity extends AppCompatActivity /*implements LeavesViewFragment.OnFragmentInteractionListener, LeavesFormFragment.OnFragmentInteractionListener*/{

    public static final String TAG = "LeavesObsevationActivity";
   /* private UserManager userManager;
    private User user;*/

    Context context;
    private FirebaseDatabase firebaseDatabase;
    private com.example.vincentale.leafguard_core.model.manager.UserManager userManager;
    private LeavesObservation leavesObservation;
    private LeavesObservationManager leavesObservationManager;
    private String oakUid;
    private User user;


    private EditText nbLeaves;
    private EditText nbGalls;
    private EditText nbMines;
    private EditText nbClassA;
    private EditText nbClassB;
    private EditText nbClassC;
    private EditText nbClassD;
    private EditText nbClassE;
    private EditText nbClassF;
    private EditText nbClassG;
    private EditText nbClassH;

    private Button validate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaves_observation);
        context=this;
        firebaseDatabase = FirebaseDatabase.getInstance();
        userManager = com.example.vincentale.leafguard_core.model.manager.UserManager.getInstance();
        userManager.getUser(new DatabaseCallback<User>() {
            @Override
            public void onSuccess(User identifiable) {
                user = identifiable;
            }

            @Override
            public void onFailure(DatabaseError error) {

            }
        });
        nbLeaves= findViewById(R.id.nbLeaves);
        nbGalls= findViewById(R.id.nbGalls);
        nbMines= findViewById(R.id.nbMines);
        nbClassA = findViewById(R.id.nbClassA);
        nbClassB = findViewById(R.id.nbClassB);
        nbClassC = findViewById(R.id.nbClassC);
        nbClassD = findViewById(R.id.nbClassD);
        nbClassE = findViewById(R.id.nbClassE);
        nbClassF = findViewById(R.id.nbClassF);
        nbClassG = findViewById(R.id.nbClassG);
        nbClassH = findViewById(R.id.nbClassH);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //userManager= com.example.vincentale.leafguard_core.model.manager.UserManager.getInstance();


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.sendObservation);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        //LeavesViewFragment leavesViewFragment = new LeavesViewFragment();

        //getSupportFragmentManager().beginTransaction().add(R.id.leaves_fragment_container, leavesViewFragment).commit();

       /* edit = findViewById(R.id.editLeavesButton);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent leavesObservationIntent = new Intent(context, LeavesObservationActivity.class);
                startActivity(leavesObservationIntent);

            }

        });*/

        validate = findViewById(R.id.validateLeavesButton);

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent leavesViewIntent = new Intent(context, LeavesViewActivity.class);
                startActivity(leavesViewIntent);

            }
        });
    }

}
