package com.example.vincentale.leafguard_core;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class OakFormActivity extends AppCompatActivity {

    private Calendar myCalendar = Calendar.getInstance();
    private EditText datePickerInput;
    DatePickerDialog.OnDateSetListener date;
    private EditText longitude;
    private EditText latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oak_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        datePickerInput = (EditText) findViewById(R.id.datePicker);
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

        longitude= (EditText)  findViewById(R.id.longitude);
        latitude= (EditText)  findViewById(R.id.latitude);
        Button loc= (Button) findViewById(R.id.localisation);
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateLocalisation();
            }});


    }

    private void updateLabel() {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        datePickerInput.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLocalisation(){

        String locationProvider = LocationManager.GPS_PROVIDER;

        // Or use LocationManager.GPS_PROVIDER
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try {
            //locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);

            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

            latitude.setText(Double.toString(lastKnownLocation.getLatitude()));
            longitude.setText(Double.toString(lastKnownLocation.getLongitude()));
        }catch (SecurityException e){

            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Le GPS n'est pas accessible.");
            //dlgAlert.setTitle("App Title");

            dlgAlert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss the dialog
                        }
                    });

            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }

    }

}
