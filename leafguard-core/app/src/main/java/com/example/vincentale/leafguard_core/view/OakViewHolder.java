package com.example.vincentale.leafguard_core.view;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vincentale.leafguard_core.R;
import com.example.vincentale.leafguard_core.model.Oak;

public class OakViewHolder extends RecyclerView.ViewHolder {

    public View currentView;
    public Oak currentItem;
    private TextView oakNameText;

    public OakViewHolder(View itemView) {
        super(itemView);
        currentView = itemView;
        oakNameText = itemView.findViewById(R.id.oakNameText);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "View '" + currentItem.getDisplayName() + "' clicked.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setCurrentItem(Oak item) {
        currentItem = item;
    }

    public void bind(Oak oak) {
        oakNameText.setText(oak.getDisplayName());
    }


}
