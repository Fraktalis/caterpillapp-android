package com.example.vincentale.leafguard_core.view;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vincentale.leafguard_core.OakActivity;
import com.example.vincentale.leafguard_core.R;
import com.example.vincentale.leafguard_core.fragment.OakFragment;
import com.example.vincentale.leafguard_core.model.Identifiable;
import com.example.vincentale.leafguard_core.model.Oak;
import com.example.vincentale.leafguard_core.model.User;
import com.example.vincentale.leafguard_core.model.UserManager;
import com.example.vincentale.leafguard_core.util.DatabaseCallback;
import com.google.firebase.database.DatabaseError;

public class OakViewHolder extends RecyclerView.ViewHolder {

    public View currentView;
    public Oak currentItem;
    private TextView oakNameText;
    private ImageView checkImage;
    private UserManager userManager;
    private User user;
    public static Context context;

    public OakViewHolder(View itemView, final Context context) {
        super(itemView);
        OakViewHolder.context = context;
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
        currentView = itemView;
        oakNameText = itemView.findViewById(R.id.oakNameText);
        checkImage = itemView.findViewById(R.id.checkImage);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent oakIntent = new Intent(context, OakActivity.class);
                oakIntent.putExtra("oakUid", currentItem.getUid());
                oakIntent.setAction(OakFragment.EDIT_OAK_ACTION);
                context.startActivity(oakIntent);
            }
        });
    }
    
    public void setCurrentItem(Oak item) {
        currentItem = item;
    }

    public void bind(Oak oak) {
        if (oak != null) {
            oakNameText.setText(oak.getDisplayName());
            if (oak.getUid().equals(user.getOakId())) {
                checkImage.setVisibility(View.VISIBLE);
            }

        }
    }

}
