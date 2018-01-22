package com.example.vincentale.leafguard_core.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vincentale.leafguard_core.R;
import com.example.vincentale.leafguard_core.model.Catterpillar;

import java.util.List;

/**
 * Created by mathilde on 10/01/18.
 */

public class CatterpillarListAdapter extends RecyclerView.Adapter<CatterpillarViewHolder> {

    private List<Catterpillar> items;

    public CatterpillarListAdapter(List<Catterpillar> items) {
        this.items = items;
    }


    @Override
    public CatterpillarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context =parent.getContext();

        int layoutIdForListItem=R.layout.catterpillar_card_view;
        LayoutInflater inflater = LayoutInflater.from(context);

        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        CatterpillarViewHolder viewHolder= new CatterpillarViewHolder(view,context);
        //CatterpillarViewHolder vh = new CatterpillarViewHolder(v);


        //return vh;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CatterpillarViewHolder holder, int position) {
        holder.setCurrentItem(items.get(position));
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return 20;
    }
}
