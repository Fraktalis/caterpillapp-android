package com.fraktalis.caterpillapp.leafguard_core;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fraktalis.caterpillapp.leafguard_core.model.LeavesObservation;
import com.fraktalis.caterpillapp.leafguard_core.model.User;
import com.fraktalis.caterpillapp.leafguard_core.model.manager.LeavesObservationManager;
import com.fraktalis.caterpillapp.leafguard_core.model.manager.UserManager;
import com.fraktalis.caterpillapp.leafguard_core.util.DatabaseCallback;
import com.fraktalis.caterpillapp.leafguard_core.util.OnUpdateCallback;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public class LeavesViewActivity extends AppCompatActivity {


    public static final String TAG = "LeavesViewActivity";
    private UserManager userManager;
    private LeavesObservation leavesObservation = new LeavesObservation();
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
    private ArrayList<TextView> inputList = new ArrayList<>();

    private Button edit;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaves_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userManager = UserManager.getInstance();
        userManager.getUser(new DatabaseCallback<User>() {
            @Override
            public void onSuccess(User identifiable) {
                user = identifiable;
            }

            @Override
            public void onFailure(DatabaseError error) {

            }
        });
        oakUid=user.getOakId();

        nbLeaves = findViewById(R.id.numberObservedLeavesText);
        inputList.add(nbLeaves);
        nbGalls = findViewById(R.id.numbergallsText);
        inputList.add(nbGalls);
        nbMines = findViewById(R.id.numberMinesText);
        inputList.add(nbMines);
        nbClassA = findViewById(R.id.classAText);
        inputList.add(nbClassA);
        nbClassB = findViewById(R.id.classBText);
        inputList.add(nbClassB);
        nbClassC = findViewById(R.id.classCText);
        inputList.add(nbClassC);
        nbClassD = findViewById(R.id.classDText);
        inputList.add(nbClassD);
        nbClassE = findViewById(R.id.classEText);
        inputList.add(nbClassE);
        nbClassF = findViewById(R.id.classFText);
        inputList.add(nbClassF);
        nbClassG = findViewById(R.id.classGText);
        inputList.add(nbClassG);
        nbClassH = findViewById(R.id.classHText);
        inputList.add(nbClassH);
        leavesObservationManager= LeavesObservationManager.getInstance();
        leavesObservationManager.find(user.getOakId(), new DatabaseCallback<LeavesObservation>() {
            @Override
            public void onSuccess(LeavesObservation identifiable) {
                if (identifiable == null) {
                    Intent newObservation = new Intent(LeavesViewActivity.this, LeavesObservationActivity.class);
                    startActivity(newObservation);
                    finish();
                } else {
                    leavesObservation=identifiable;
                    Log.d(TAG,String.valueOf(identifiable.getGallsTotal()));

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

            }
        });

        final Context context= this;

        edit = findViewById(R.id.editLeavesButton);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent leavesObservationIntent = new Intent(context, LeavesObservationActivity.class);
                startActivity(leavesObservationIntent);
            }

        });

        submitButton = findViewById(R.id.submitLeavesObservationButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValid = validateInput(inputList);
                if (isValid) {
                    AlertDialog.Builder warningBuilder = new AlertDialog.Builder(LeavesViewActivity.this);
                    warningBuilder.setMessage(R.string.observation_send_irreversible)
                            .setPositiveButton(R.string.send_action, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    leavesObservationManager.update(leavesObservation, new OnUpdateCallback() {
                                        @Override
                                        public void onSuccess() {
                                            user.setLeavesObservationSent(true);
                                            userManager.update(user,null);
                                            Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_leaves_view_layout), R.string.observation_successfully_sent, Snackbar.LENGTH_SHORT);
                                            snackbar.addCallback(new Snackbar.Callback() {
                                                public void onDismissed(Snackbar snackbar, int event) {
                                                    finish();
                                                }
                                            });
                                            snackbar.show();
                                        }

                                        @Override
                                        public void onError(Throwable err) {
                                            Log.e(TAG, "Update error : " , err);
                                            Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_caterpillar_list_layout), R.string.error_occured, Snackbar.LENGTH_SHORT);
                                            snackbar.show();
                                        }
                                    });
                                }
                            }).setNegativeButton(R.string.cancel_action, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    warningBuilder.create().show();
                }
            }
        });


    }

    public boolean validateInput(ArrayList<TextView> inputs) {
        for (TextView input : inputs) {
            if (input.getText().toString().equals("") || input.getText().toString().isEmpty()) {
                return false;
            }
        }

        return true;
    }
}
