package com.example.vincentale.leafguard_core.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vincentale.leafguard_core.CatterpillarViewActivity;
import com.example.vincentale.leafguard_core.R;
import com.example.vincentale.leafguard_core.model.Catterpillar;

/**
 * Created by mathilde on 08/01/18.
 */

public class CatterpillarViewHolder extends RecyclerView.ViewHolder{

    public Catterpillar curentCatterpillar;
    private TextView catterName;
    private ImageView feedBackIcon;

    //private
    //itemView est la vue correspondante à 1 cellule
    public CatterpillarViewHolder(View itemView, final Context context) {
        super(itemView);
        catterName = (TextView) itemView.findViewById(R.id.catterpillarName);
        feedBackIcon= (ImageView) itemView.findViewById(R.id.checkImage);

        itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent catterIntent= new Intent (context, CatterpillarViewActivity.class);
                catterIntent.putExtra("catterUID", curentCatterpillar.getUid());
                catterIntent.putExtra("caterIndex", curentCatterpillar.getIndex());
                context.startActivity(catterIntent);
            }
        });
    }


    public void bind(Catterpillar myObject){

        catterName.setText(myObject.getUid());

        if(myObject.isEdited()){
            feedBackIcon.setVisibility(View.VISIBLE);
        }else{
            feedBackIcon.setVisibility(View.INVISIBLE);
        }
    }

    public void setCurrentItem(Catterpillar curentCatterpillar) {
        this.curentCatterpillar = curentCatterpillar;
    }



    public void updateFeedbackIcon(){
        if(curentCatterpillar.isEdited()){
            feedBackIcon.setVisibility(View.VISIBLE);
        }else{
            feedBackIcon.setVisibility(View.INVISIBLE);
        }

    }
}
