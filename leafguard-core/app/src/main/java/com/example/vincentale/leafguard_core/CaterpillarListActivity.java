package com.example.vincentale.leafguard_core;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.vincentale.leafguard_core.model.Caterpillar;
import com.example.vincentale.leafguard_core.model.CaterpillarObservation;
import com.example.vincentale.leafguard_core.model.manager.CaterpillarManager;
import com.example.vincentale.leafguard_core.model.User;
import com.example.vincentale.leafguard_core.model.manager.CaterpillarObservationManager;
import com.example.vincentale.leafguard_core.model.manager.UserManager;
import com.example.vincentale.leafguard_core.util.DatabaseCallback;
import com.example.vincentale.leafguard_core.util.DatabaseListCallback;
import com.example.vincentale.leafguard_core.util.OnUpdateCallback;
import com.example.vincentale.leafguard_core.view.CaterpillarListAdapter;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CaterpillarListActivity extends AppCompatActivity {

    private static final String TAG = "CatterpillarListAct";
    private static int lastKnownIndex = -1;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayout loadingLayout;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton sendObservation;

    private UserManager userManager = UserManager.getInstance();
    private User currentUser;
    private CaterpillarManager caterpillarManager = CaterpillarManager.getInstance();
    private ArrayList<Caterpillar> caterpillars = new ArrayList<>();
    private CaterpillarObservationManager caterpillarObservationManager = CaterpillarObservationManager.getInstance();
    private int editedCount = 0;
    private int observationIndex = -1;

    public static void setLastKnownIndex(int newIndex) {
        lastKnownIndex = newIndex;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cattarpillar_list);
        observationIndex = getIntent().getIntExtra("observationIndex", -1);
        recyclerView=(RecyclerView) findViewById(R.id.catterpillarRecycleView);
        loadingLayout = findViewById(R.id.list_loading_layout);

        userManager.getUser(new DatabaseCallback<User>() {
            @Override
            public void onSuccess(final User identifiable) {
                currentUser = identifiable;
                if (identifiable.getOak() == null) {
                    Toast.makeText(CaterpillarListActivity.this, "You didn't set up the tree you want to manage", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    caterpillarManager.findAllbyOak(currentUser.getOak(), new DatabaseListCallback<Caterpillar>() {
                        @Override
                        public void onSuccess(List<Caterpillar> identifiables) {
                            HashMap<String, Caterpillar> catterpillarHashMap = (HashMap<String, Caterpillar>) CaterpillarManager.toHashMap(identifiables);
                            for (int i = 1; i <= 20; i++) {
                                String currentKey = currentUser.getOak().getUid() + "_" + observationIndex + "_" + i;
                                if (!catterpillarHashMap.containsKey(currentKey)) { // The caterpillar doesn't exist in Firebase. We need to create it
                                    Caterpillar newCaterpillar = new Caterpillar(currentUser.getOak(), observationIndex, i);
                                    catterpillarHashMap.put(currentKey, newCaterpillar);
                                    caterpillars.add(newCaterpillar);
                                } else {
                                    caterpillars.add(catterpillarHashMap.get(currentKey));
                                    editedCount++;
                                }
                            }

                            mAdapter =new CaterpillarListAdapter(caterpillars);
                            recyclerView.setAdapter(mAdapter);
                            loadingLayout.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            layoutManager = new LinearLayoutManager(CaterpillarListActivity.this);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setHasFixedSize(true);
                            Log.d(TAG, "onSuccess: lastKnownIndex = " + lastKnownIndex);
                            if (lastKnownIndex > 0) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerView.smoothScrollToPosition(lastKnownIndex);
                                    }
                                }, 200);
                            }

                            sendObservation= (FloatingActionButton) findViewById(R.id.sendObservation);
                            sendObservation.setVisibility(View.VISIBLE);
                            sendObservation.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Context context=getApplicationContext();
                                    if (editedCount != Caterpillar.INDEX_LIMIT) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(CaterpillarListActivity.this);
                                        builder.setMessage(R.string.not_all_caterpillars_edited)
                                                .setPositiveButton(R.string.ok_action, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }
                                                });
                                        builder.create().show();
                                    } else {
                                        AlertDialog.Builder warningBuilder = new AlertDialog.Builder(CaterpillarListActivity.this);
                                        warningBuilder.setMessage(R.string.observation_send_irreversible)
                                                .setPositiveButton(R.string.send_action, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        final CaterpillarObservation observation = new CaterpillarObservation(currentUser.getOak(), observationIndex);
                                                        observation.setCaterpillars(caterpillars);
                                                        caterpillarObservationManager.update(observation, new OnUpdateCallback() {
                                                            @Override
                                                            public void onSuccess() {
                                                                currentUser.addObservation(observation.getUid());
                                                                userManager.update(currentUser,null);
                                                                Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_caterpillar_list_layout), R.string.observation_successfully_sent, Snackbar.LENGTH_SHORT);
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

                        @Override
                        public void onFailure(DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(DatabaseError error) {
                Log.e(TAG, error.toString());
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
       finish();
       startActivity(getIntent()); //Brute method to invalidate the recycler view and refresh the view...
    }
}