package com.example.vincentale.leafguard_core;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    public static final String TAG = "LeavesObservationAct";
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


        validate = findViewById(R.id.validateLeavesButton);
/*
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, nbGalls.getText().toString());
                leavesObservation= new LeavesObservation(Integer.getInteger(nbLeaves.getText().toString()),
                        Integer.getInteger(nbGalls.getText().toString()),
                        Integer.getInteger(nbMines.getText().toString()),
                        Integer.getInteger(nbClassA.getText().toString()),
                        Integer.getInteger(nbClassB.getText().toString()),
                        Integer.getInteger(nbClassC.getText().toString()),
                        Integer.getInteger(nbClassD.getText().toString()),
                        Integer.getInteger(nbClassE.getText().toString()),
                        Integer.getInteger(nbClassF.getText().toString()),
                        Integer.getInteger(nbClassG.getText().toString()),
                        Integer.getInteger(nbClassH.getText().toString()));

                Log.d(TAG, String.valueOf(leavesObservation.getLeavesAClassNumber()));

                Intent leavesViewIntent = new Intent(context, LeavesViewActivity.class);
                startActivity(leavesViewIntent);

            }
        });*/
    }

    public void validateLeaves(View view){
        Log.d(TAG, nbGalls.getText().toString());
        if (nbLeaves.getText()==null||nbGalls.getText()==null||nbMines.getText()==null||nbClassA.getText()==null
                ||nbClassA.getText()==null||nbClassB.getText()==null||nbClassC.getText()==null||
                nbClassD.getText()==null||nbClassE.getText()==null||nbClassF.getText()==null||
                nbClassG.getText()==null||nbClassH.getText()==null){


        }else{

            int intleaves = Integer.valueOf(nbLeaves.getText().toString());
            int intgalls = Integer.valueOf(nbGalls.getText().toString());
            int intmines = Integer.valueOf(nbMines.getText().toString());
            int intclassA= Integer.valueOf(nbClassA.getText().toString());
            int intclassB= Integer.valueOf(nbClassB.getText().toString());
            int intclassC= Integer.valueOf(nbClassC.getText().toString());
            int intclassD= Integer.valueOf(nbClassD.getText().toString());
            int intclassE= Integer.valueOf(nbClassE.getText().toString());
            int intclassF= Integer.valueOf(nbClassF.getText().toString());
            int intclassG= Integer.valueOf(nbClassG.getText().toString());
            int intclassH= Integer.valueOf(nbClassH.getText().toString());



            leavesObservation= new LeavesObservation(intleaves,intgalls,intmines,intclassA,intclassB,intclassC,intclassD,intclassE,intclassF,intclassG,intclassH);


            Log.d(TAG, String.valueOf(leavesObservation.getLeavesAClassNumber()));

            Intent leavesViewIntent = new Intent(context, LeavesViewActivity.class);
            startActivity(leavesViewIntent);
        }

    }
}
