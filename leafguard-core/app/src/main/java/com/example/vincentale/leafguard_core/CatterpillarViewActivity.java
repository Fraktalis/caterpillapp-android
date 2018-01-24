package com.example.vincentale.leafguard_core;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vincentale.leafguard_core.dummy.DummyContent;
import com.example.vincentale.leafguard_core.model.Catterpillar;

public class CatterpillarViewActivity extends AppCompatActivity {

    public static final String TAG = "CatterpillarViewActi";
    private Catterpillar item;
    private TextView catterpillarName;
    private CheckBox isMissing;
    private LinearLayout woundLayout;
    private CheckBox attackedByMammals;
    private CheckBox attackedByInsect;
    private CheckBox attackedByBird;
    private CheckBox attackedByOther;
    private Button validate;

    public void setItem(Catterpillar item) {
        this.item = item;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catterpillar_view);
        Intent intent = getIntent();
        String iD = intent.getStringExtra("catteriD");

        int i = Integer.parseInt(iD);
        catterpillarName = (TextView) findViewById(R.id.catterpillarName);

        isMissing = (CheckBox) findViewById(R.id.isMissing);
        attackedByBird = (CheckBox) findViewById(R.id.attackedByBirds);
        attackedByMammals = (CheckBox) findViewById(R.id.attackedByMammals);
        attackedByInsect = (CheckBox) findViewById(R.id.attackedByInsect);
        attackedByOther = (CheckBox) findViewById(R.id.attackedByOther);
        validate = (Button) findViewById(R.id.validateCatterpillarButton);
        woundLayout = (LinearLayout) findViewById(R.id.woundsLayout);


        catterpillarName.setText(iD);

        isMissing.setChecked(item.isCatterpillarMissing());
        if (isMissing.isChecked()) {
            woundLayout.setVisibility(View.INVISIBLE);
        }
        isMissing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMissing.isChecked()) {
                    woundLayout.setVisibility(View.INVISIBLE);
                } else {
                    woundLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        attackedByBird.setChecked(item.isWoundByBird());
        attackedByInsect.setChecked(item.isWoundByInsect());
        attackedByMammals.setChecked(item.isWoundByMammal());
        Log.d("isWoundByMammal", Boolean.toString(item.isWoundByMammal()));

        attackedByOther.setChecked(item.isWoundByOther());
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setEdited(true);

                item.setCatterpillarMissing(isMissing.isChecked());
                item.setWoundByBird(attackedByBird.isChecked());
                item.setWoundByInsect(attackedByInsect.isChecked());
                item.setWoundByMammal(attackedByMammals.isChecked());
                item.setWoundByOther(attackedByOther.isChecked());
                finish();

            }
        });

    }

}
