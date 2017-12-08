package com.example.vincentale.leafguard_core;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vincentale.leafguard_core.model.Oak;
import com.example.vincentale.leafguard_core.model.OakManager;
import com.example.vincentale.leafguard_core.model.User;
import com.example.vincentale.leafguard_core.model.UserManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class OakFormActivity extends AppCompatActivity {

    private Calendar myCalendar = Calendar.getInstance();
    private EditText longitudeEditText;
    private EditText latitudeEditText;
    private EditText oakCircumferenceEditText;
    private EditText oakHeightEditText;

    private EditText datePickerInput;
    private long installationDate;

    private Button validateButton;

    private OakManager oakManager;
    private UserManager userManager;
    private User user;

    DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oak_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userManager = UserManager.getInstance();
        user = userManager.getUser();
        oakManager = OakManager.getInstance();


        longitudeEditText = (EditText) findViewById(R.id.longitudeEditText);
        latitudeEditText = (EditText) findViewById(R.id.latitudeEditText);
        oakCircumferenceEditText = (EditText) findViewById(R.id.oakCircumferenceEditText);
        oakHeightEditText = (EditText) findViewById(R.id.oakHeightEditText);
        validateButton = (Button) findViewById(R.id.validateButton);
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float longitude = Float.parseFloat(longitudeEditText.getText().toString());
                float latitude = Float.parseFloat(latitudeEditText.getText().toString());
                float oakCircumference = Float.parseFloat(oakCircumferenceEditText.getText().toString());
                float oakHeight = Float.parseFloat(oakHeightEditText.getText().toString());
                if (user == null || user.getUid() == null) {
                    Toast.makeText(OakFormActivity.this, "User information are not complete. Please wait before sending the form", Toast.LENGTH_SHORT).show();
                } else {
                    Oak oak = new Oak(user, longitude, latitude);
                    oak.setOakCircumference(oakCircumference);
                    oak.setOakHeight(oakHeight);
                    oak.setInstallationDate(installationDate);
                    oakManager.update(oak);
                    user.setOak(oak);
                    userManager.update(user);
                    Toast.makeText(OakFormActivity.this, "Oak successfully added !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        datePickerInput = (EditText) findViewById(R.id.installationDate);
        datePickerInput.setKeyListener(null); //To make it uneditable ! Java :D

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
                datePickerInput.clearFocus();
            }

        };

        final Context activityContext = this;
        datePickerInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    new DatePickerDialog(activityContext, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });
    }

    private void updateLabel() {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        installationDate = myCalendar.getTime().getTime();
        datePickerInput.setText(sdf.format(myCalendar.getTime()));
    }

}
