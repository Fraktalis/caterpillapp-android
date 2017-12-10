package com.example.vincentale.leafguard_core.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vincentale.leafguard_core.R;
import com.example.vincentale.leafguard_core.model.Oak;

import java.util.List;

/**
 * Created by vincentale on 10/12/17.
 */

public class OakAdapter extends RecyclerView.Adapter<OakViewHolder> {

    List<Oak> oakList;

    public OakAdapter(List<Oak> oakList) {
        this.oakList = oakList;
    }

    @Override
    public OakViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.oak_card_view,parent,false);
        return new OakViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OakViewHolder holder, int position) {
        Oak oak = oakList.get(position);
    }

    @Override
    public int getItemCount() {
        return oakList.size();
    }
}
