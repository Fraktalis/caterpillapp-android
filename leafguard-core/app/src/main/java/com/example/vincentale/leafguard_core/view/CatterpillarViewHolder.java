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
    private TextView iD;
    private ImageView feedBackIcon;

    //private
    //itemView est la vue correspondante à 1 cellule
    public CatterpillarViewHolder(View itemView, final Context context) {
        super(itemView);

        iD = (TextView) itemView.findViewById(R.id.catterpillarName);

       // Name=(TextView) itemView.findViewById(R.id.nameofcattarpillar);

        feedBackIcon= (ImageView) itemView.findViewById(R.id.checkImage);


        itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent catterIntent= new Intent (context, CatterpillarViewActivity.class);

                catterIntent.putExtra("catteriD", iD.getText());

                context.startActivity(catterIntent);

            }
        });
    }


    public void bind(Catterpillar myObject){

        CharSequence text= iD.getText();
        iD.setText(myObject.getUid());
        //Name.setText("Catterpillar");

        if(myObject.isEdited()){
            feedBackIcon.setVisibility(View.VISIBLE);
        }else{
            feedBackIcon.setVisibility(View.INVISIBLE);
        }

        //int wound = myObject.getWounds();

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
