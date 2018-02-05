package com.example.vincentale.leafguard_core.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vincentale.leafguard_core.CaterpillarListActivity;
import com.example.vincentale.leafguard_core.CaterpillarViewActivity;
import com.example.vincentale.leafguard_core.R;
import com.example.vincentale.leafguard_core.model.Caterpillar;

/**
 * Created by mathilde on 08/01/18.
 */

public class CatterpillarViewHolder extends RecyclerView.ViewHolder{

    private static final String TAG = "CaterpillarViewHolder";
    public Caterpillar currentCaterpillar;
    private Context context;
    private TextView catterName;
    private ImageView feedBackIcon;

    //private
    //itemView est la vue correspondante à 1 cellule
    public CatterpillarViewHolder(View itemView, final Context context) {
        super(itemView);
        this.context = context;
        catterName = (TextView) itemView.findViewById(R.id.catterpillarName);
        feedBackIcon= (ImageView) itemView.findViewById(R.id.checkImage);

        itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                CaterpillarListActivity.setLastKnownIndex(currentCaterpillar.getIndex());
                Intent catterIntent= new Intent (context, CaterpillarViewActivity.class);
                catterIntent.putExtra(CaterpillarViewActivity.CATERPILLAR_UID, currentCaterpillar.getUid());
                catterIntent.putExtra(CaterpillarViewActivity.CATERPILLAR_INDEX, currentCaterpillar.getIndex());
                catterIntent.putExtra(CaterpillarViewActivity.OBSERVATION_INDEX, currentCaterpillar.getObservationIndex());
                context.startActivity(catterIntent);
            }
        });
    }


    public void bind(Caterpillar myObject){

        Log.d(TAG, "bind: "+ myObject.toString());
        catterName.setText(context.getResources().getString(R.string.catterpillar_with_number, myObject.getIndex()));

        if(myObject.isEdited()){
            feedBackIcon.setVisibility(View.VISIBLE);
        }else{
            feedBackIcon.setVisibility(View.INVISIBLE);
        }
    }

    public void setCurrentItem(Caterpillar curentCaterpillar) {
        this.currentCaterpillar = curentCaterpillar;
    }



    public void updateFeedbackIcon(){
        if(currentCaterpillar.isEdited()){
            feedBackIcon.setVisibility(View.VISIBLE);
        }else{
            feedBackIcon.setVisibility(View.INVISIBLE);
        }

    }
}
