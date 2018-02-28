package com.example.vincentale.leafguard_core;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.example.vincentale.leafguard_core.util.OnUpdateCallback;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

public class LeavesObservationActivity extends AppCompatActivity /*implements LeavesViewFragment.OnFragmentInteractionListener, LeavesFormFragment.OnFragmentInteractionListener*/{

    public static final String TAG = "LeavesObservationAct";
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

    private Button validateButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaves_observation);
        context=getBaseContext();
        firebaseDatabase = FirebaseDatabase.getInstance();
        leavesObservationManager= LeavesObservationManager.getInstance();
        userManager = com.example.vincentale.leafguard_core.model.manager.UserManager.getInstance();
        userManager.getUser(new DatabaseCallback<User>() {
            @Override
            public void onSuccess(User identifiable) {
                user = identifiable;
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

                validateButton = findViewById(R.id.validateLeavesButton);
                validateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        validateLeaves();
                    }
                });

                leavesObservationManager= LeavesObservationManager.getInstance();
                leavesObservationManager.find(user.getOakId(), new DatabaseCallback<LeavesObservation>() {
                    @Override
                    public void onSuccess(LeavesObservation identifiable) {
                        if (identifiable==null) {

                        } else {
                            leavesObservation = identifiable;

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
                        }

                    }

                    @Override
                    public void onFailure(DatabaseError error) {
                        Log.e(TAG, "onFailure: ", error.toException());
                    }
                });
            }

            @Override
            public void onFailure(DatabaseError error) {

            }


        });
    }

    public void validateLeaves(){
        try {
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

            leavesObservation= new LeavesObservation(user.getOak(),intleaves,intgalls,intmines,intclassA,intclassB,intclassC,intclassD,intclassE,intclassF,intclassG,intclassH);
            leavesObservationManager.update(leavesObservation, new OnUpdateCallback() {
                @Override
                public void onSuccess() {
                    Log.d(TAG,"succed save");
                }

                @Override
                public void onError(Throwable err) {

                    Log.e(TAG,"LeavesObservation Validation", err);
                }
            });
            Intent leavesViewIntent = new Intent(context, LeavesViewActivity.class);
            startActivity(leavesViewIntent);
            finish();
        } catch (NumberFormatException ex) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LeavesObservationActivity.this);
            builder.setMessage(R.string.all_inputs_not_filled)
                    .setPositiveButton(R.string.ok_action, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
            builder.create().show();
        }

    }
}
