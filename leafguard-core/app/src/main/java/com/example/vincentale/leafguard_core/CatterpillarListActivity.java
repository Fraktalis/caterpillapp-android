package com.example.vincentale.leafguard_core;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.vincentale.leafguard_core.dummy.DummyContent;
import com.example.vincentale.leafguard_core.model.Catterpillar;
import com.example.vincentale.leafguard_core.view.CatterpillarListAdapter;

import java.util.List;

public class CatterpillarListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cattarpillar_list);
        recyclerView=(RecyclerView) findViewById(R.id.catterpillarRecycleView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        List<Catterpillar> input= DummyContent.ITEMS;
        mAdapter =new CatterpillarListAdapter(input);


        recyclerView.setAdapter(mAdapter);



    }


}
