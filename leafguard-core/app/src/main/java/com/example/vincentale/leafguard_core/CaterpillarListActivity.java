package com.example.vincentale.leafguard_core;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.vincentale.leafguard_core.model.CaterpillarManager;
import com.example.vincentale.leafguard_core.model.Catterpillar;
import com.example.vincentale.leafguard_core.model.Oak;
import com.example.vincentale.leafguard_core.model.User;
import com.example.vincentale.leafguard_core.model.UserManager;
import com.example.vincentale.leafguard_core.util.DatabaseCallback;
import com.example.vincentale.leafguard_core.util.DatabaseListCallback;
import com.example.vincentale.leafguard_core.view.CaterpillarListAdapter;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CaterpillarListActivity extends AppCompatActivity {

    private static final String TAG = "CatterpillarListAct";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton sendObservation;

    private UserManager userManager = UserManager.getInstance();
    private User currentUser;
    private CaterpillarManager caterpillarManager = CaterpillarManager.getInstance();
    private ArrayList<Catterpillar> catterpillars = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cattarpillar_list);
        recyclerView=(RecyclerView) findViewById(R.id.catterpillarRecycleView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        userManager.getUser(new DatabaseCallback<User>() {
            @Override
            public void onSuccess(final User identifiable) {
                currentUser = identifiable;
                if (identifiable.getOak() == null) {
                    Toast.makeText(CaterpillarListActivity.this, "You didn't set up the tree you want to manage", Toast.LENGTH_SHORT).show();
                } else {
                    caterpillarManager.findAllbyOak(currentUser.getOak(), new DatabaseListCallback<Catterpillar>() {
                        @Override
                        public void onSuccess(List<Catterpillar> identifiables) {
                            HashMap<String, Catterpillar> catterpillarHashMap = (HashMap<String, Catterpillar>) CaterpillarManager.toHashMap(identifiables);
                            for (int i = 1; i <= 20; i++) {
                                String currentKey = currentUser.getOak().getUid() + "_" + i;
                                if (!catterpillarHashMap.containsKey(currentKey)) { // The caterpillar doesn't exist in Firebase. We need to create it
                                    Catterpillar newCaterpillar = new Catterpillar(currentUser.getOak(), i);
                                    catterpillarHashMap.put(currentKey, newCaterpillar);
                                    catterpillars.add(newCaterpillar);
                                } else {
                                    catterpillars.add(catterpillarHashMap.get(currentKey));
                                }
                            }



                            mAdapter =new CaterpillarListAdapter(catterpillars);
                            recyclerView.setAdapter(mAdapter);

                            sendObservation= (FloatingActionButton) findViewById(R.id.sendObservation);
                            sendObservation.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Context context=getApplicationContext();

                                    String text= "add action to send the catterpillars";
                                    int duration= Toast.LENGTH_SHORT;
                                    Toast.makeText(context, text, duration).show();
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


}
