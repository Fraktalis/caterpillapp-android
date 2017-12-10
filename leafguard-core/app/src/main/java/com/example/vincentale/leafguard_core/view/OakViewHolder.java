package com.example.vincentale.leafguard_core.view;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.vincentale.leafguard_core.R;
import com.example.vincentale.leafguard_core.model.Oak;

public class OakViewHolder extends RecyclerView.ViewHolder {

    private TextView oakNameText;

    public OakViewHolder(View itemView) {
        super(itemView);

        oakNameText = itemView.findViewById(R.id.oakNameText);
    }

    public void bind(Oak oak) {
        oakNameText.setText(oak.getDisplayName());
    }
}
