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

import com.example.vincentale.leafguard_core.model.CaterpillarManager;
import com.example.vincentale.leafguard_core.model.Catterpillar;
import com.example.vincentale.leafguard_core.model.User;
import com.example.vincentale.leafguard_core.model.UserManager;
import com.example.vincentale.leafguard_core.util.DatabaseCallback;
import com.google.firebase.database.DatabaseError;

public class CatterpillarViewActivity extends AppCompatActivity {

    public static final String TAG = "CatterpillarViewActi";

    private CaterpillarManager caterpillarManager = CaterpillarManager.getInstance();
    private Catterpillar item;

    private UserManager userManager = UserManager.getInstance();
    private User user;

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
        catterpillarName = (TextView) findViewById(R.id.catterpillarName);
        isMissing = (CheckBox) findViewById(R.id.isMissing);
        attackedByBird = (CheckBox) findViewById(R.id.attackedByBirds);
        attackedByMammals = (CheckBox) findViewById(R.id.attackedByMammals);
        attackedByInsect = (CheckBox) findViewById(R.id.attackedByInsect);
        attackedByOther = (CheckBox) findViewById(R.id.attackedByOther);
        validate = (Button) findViewById(R.id.validateCatterpillarButton);
        woundLayout = (LinearLayout) findViewById(R.id.woundsLayout);

        Intent intent = getIntent();
        final String catterUID = intent.getStringExtra("catterUID");
        final int caterIndex = intent.getIntExtra("caterIndex", 0);
        userManager.getUser(new DatabaseCallback<User>() {
            @Override
            public void onSuccess(User identifiable) {
                user = identifiable;
                caterpillarManager.find(catterUID, new DatabaseCallback<Catterpillar>() {
                    @Override
                    public void onSuccess(Catterpillar identifiable) {
                        if (identifiable == null) {
                            item = new Catterpillar(user.getOak(), caterIndex);
                        } else {
                            item = identifiable;
                        }
                        catterpillarName.setText(getResources().getText(R.string.catterpillar) + " " + item.getIndex());

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
                                caterpillarManager.update(item);
                                finish();

                            }
                        });

                    }

                    @Override
                    public void onFailure(DatabaseError error) {
                        Log.d(TAG, error.toString());
                    }
                });
            }

            @Override
            public void onFailure(DatabaseError error) {

            }
        });
    }

}
