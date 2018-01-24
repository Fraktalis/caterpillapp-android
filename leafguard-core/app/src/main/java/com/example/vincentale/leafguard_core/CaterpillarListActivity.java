package com.example.vincentale.leafguard_core;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.vincentale.leafguard_core.dummy.DummyContent;
import com.example.vincentale.leafguard_core.model.CaterpillarManager;
import com.example.vincentale.leafguard_core.model.Catterpillar;
import com.example.vincentale.leafguard_core.model.Oak;
import com.example.vincentale.leafguard_core.model.User;
import com.example.vincentale.leafguard_core.model.UserManager;
import com.example.vincentale.leafguard_core.view.CaterpillarListAdapter;

import java.util.ArrayList;
import java.util.List;

public class CaterpillarListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton sendObservation;
    private String action;

    private UserManager userManager = UserManager.getInstance();
    private CaterpillarManager caterpillarManager = CaterpillarManager.getInstance();
    private ArrayList<Catterpillar> catterpillars = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cattarpillar_list);

        action=getIntent().getAction();


        recyclerView=(RecyclerView) findViewById(R.id.catterpillarRecycleView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
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

}
