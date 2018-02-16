package com.example.vincentale.leafguard_core;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vincentale.leafguard_core.model.Caterpillar;
import com.example.vincentale.leafguard_core.model.manager.CaterpillarManager;
import com.example.vincentale.leafguard_core.model.User;
import com.example.vincentale.leafguard_core.model.manager.UserManager;
import com.example.vincentale.leafguard_core.util.DatabaseCallback;
import com.google.firebase.database.DatabaseError;

public class CaterpillarViewActivity extends AppCompatActivity {

    public static final String TAG = "CatterpillarViewActi";
    public static final String CATERPILLAR_UID = "caterUID";
    public static final String CATERPILLAR_INDEX = "caterIndex";
    public static final String OBSERVATION_INDEX = "observationIndex";

    private CaterpillarManager caterpillarManager = CaterpillarManager.getInstance();
    private Caterpillar item;

    private UserManager userManager = UserManager.getInstance();
    private User user;

    private TextView catterpillarName;
    private CheckBox isMissing;
    private LinearLayout woundLayout;
    private CheckBox attackedByMammals;
    private CheckBox attackedByInsect;
    private CheckBox attackedByBird;
    private CheckBox attackedByLizard;
    private CheckBox attackedByOther;
    private Button validate;

    public void setItem(Caterpillar item) {
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
        attackedByLizard = (CheckBox) findViewById(R.id.attackedByLizards);
        attackedByOther = (CheckBox) findViewById(R.id.attackedByOther);
        validate = (Button) findViewById(R.id.validateCatterpillarButton);
        woundLayout = (LinearLayout) findViewById(R.id.woundsLayout);

        Intent intent = getIntent();
        final String catterUID = intent.getStringExtra(CATERPILLAR_UID);
        final int caterIndex = intent.getIntExtra(CATERPILLAR_INDEX, 0);
        final int observationIndex = intent.getIntExtra(OBSERVATION_INDEX, -1);
        userManager.getUser(new DatabaseCallback<User>() {
            @Override
            public void onSuccess(User identifiable) {
                user = identifiable;
                caterpillarManager.find(catterUID, new DatabaseCallback<Caterpillar>() {
                    @Override
                    public void onSuccess(Caterpillar identifiable) {
                        if (identifiable == null) {
                            item = new Caterpillar(user.getOak(), observationIndex, caterIndex);
                        } else {
                            item = identifiable;
                        }
                        catterpillarName.setText(String.format(getResources().getString(R.string.catterpillar_with_number), item.getIndex()));

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
                        attackedByLizard.setChecked(item.isWoundByLizard());

                        attackedByOther.setChecked(item.isWoundByOther());
                        validate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                item.setEdited(true);

                                item.setCatterpillarMissing(isMissing.isChecked());
                                item.setWoundByBird(attackedByBird.isChecked());
                                item.setWoundByInsect(attackedByInsect.isChecked());
                                item.setWoundByMammal(attackedByMammals.isChecked());
                                item.setWoundByLizard(attackedByLizard.isChecked());
                                item.setWoundByOther(attackedByOther.isChecked());
                                caterpillarManager.update(item, null);
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
