package com.example.vincentale.leafguard_core.fragment;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.vincentale.leafguard_core.R;
import com.example.vincentale.leafguard_core.model.Oak;
import com.example.vincentale.leafguard_core.model.manager.OakManager;
import com.example.vincentale.leafguard_core.model.User;
import com.example.vincentale.leafguard_core.model.manager.UserManager;
import com.example.vincentale.leafguard_core.util.DatabaseCallback;
import com.example.vincentale.leafguard_core.util.LocationHelper;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OakFormFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OakFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OakFormFragment extends Fragment {
    public static final String TAG = "OakFormfragment";
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    DatePickerDialog.OnDateSetListener date;
    LocationHelper locationHelper;
    private Calendar myCalendar = Calendar.getInstance();
    private EditText longitudeEditText;
    private EditText latitudeEditText;
    private EditText oakCircumferenceEditText;
    private EditText oakHeightEditText;
    private EditText datePickerInput;
    private long installationDate;
    private Button validateButton;
    private ImageButton locationImageButton;
    private Button cancelLocationButton;
    private LinearLayout geolocationProgressLayout;
    private LinearLayout geolocationContentLayout;
    private ProgressBar geolocationProgressBar;
    private FirebaseDatabase firebaseDatabase;
    private OakManager oakManager;
    private UserManager userManager;
    private User user;
    private String oakUid;
    private Oak oak;
    private Oak oakSave;
    private Activity activityContext;
    private OnFragmentInteractionListener mListener;

    public OakFormFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OakFormFragment newInstance() {
        OakFormFragment fragment = new OakFormFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseDatabase = FirebaseDatabase.getInstance();
        userManager = UserManager.getInstance();
        oakManager = OakManager.getInstance();
        locationHelper = LocationHelper.getInstance();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            oakUid = bundle.getString("oakUid");
        }
        userManager.getUser(new DatabaseCallback<User>() {
            @Override
            public void onSuccess(User identifiable) {
                user = identifiable;
            }

            @Override
            public void onFailure(DatabaseError error) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View fragmentView = inflater.inflate(R.layout.fragment_oak_form, container, false);
        activityContext  = getActivity();
        geolocationContentLayout = fragmentView.findViewById(R.id.geolocation_content_layout);
        geolocationProgressLayout = fragmentView.findViewById(R.id.geolocation_progess_layout);
        geolocationProgressBar = fragmentView.findViewById(R.id.geolocationProgressBar);
        geolocationProgressBar.setMax(LocationHelper.DURATION);
        locationImageButton = fragmentView.findViewById(R.id.locationImageButton);
        locationImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(activityContext,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) activityContext,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                }

                int permissionCheck = ContextCompat.checkSelfPermission(activityContext,
                        Manifest.permission.ACCESS_FINE_LOCATION);

                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    locationHelper = new LocationHelper();
                    ObjectAnimator progressAnimation = ObjectAnimator.ofInt(geolocationProgressBar, "progress", 0, LocationHelper.DURATION);
                    progressAnimation.setDuration(LocationHelper.DURATION);
                    progressAnimation.setInterpolator(new DecelerateInterpolator());
                    geolocationProgressLayout.setVisibility(View.VISIBLE);
                    geolocationContentLayout.setVisibility(View.GONE);
                    progressAnimation.start();
                    boolean locationEnabled = locationHelper.getLocation(activityContext, new LocationHelper.LocationResult() {
                        @Override
                        public void acceptLocation(Location location) {
                            final boolean noLocation = (location == null);
                            final double longitude = (noLocation) ? 0 : location.getLongitude();
                            final double latitude = (noLocation) ? 0 : location.getLatitude();

                            activityContext.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (noLocation) {
                                        Toast.makeText(activityContext, "null location", Toast.LENGTH_SHORT).show();
                                    } else {
                                        longitudeEditText.setText(String.valueOf(longitude));
                                        latitudeEditText.setText(String.valueOf(latitude));
                                    }

                                    geolocationProgressLayout.setVisibility(View.GONE);
                                    geolocationContentLayout.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });
                    if (!locationEnabled) {
                        geolocationProgressLayout.setVisibility(View.GONE);
                        geolocationContentLayout.setVisibility(View.VISIBLE);
                        AlertDialog.Builder locationDialog = new AlertDialog.Builder(getContext());
                        locationDialog.setMessage(getString(R.string.gps_not_enabled));
                        locationDialog.setPositiveButton(getString(R.string.ok_action), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        locationDialog.show();
                    }
                } else {
                    geolocationProgressLayout.setVisibility(View.GONE);
                    geolocationContentLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        cancelLocationButton = fragmentView.findViewById(R.id.locationCancelbutton);
        cancelLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationHelper.cancel();
                geolocationProgressLayout.setVisibility(View.GONE);
                geolocationContentLayout.setVisibility(View.VISIBLE);
            }
        });

        String action = getActivity().getIntent().getAction();
        switch (action) {
            case OakFragment.NEW_OAK_ACTION:
                newOak(fragmentView);
                break;

            case OakFragment.EDIT_OAK_ACTION:
                editOak(fragmentView);
                break;
            default:
                Log.d(TAG, "Action " + action + "not supported on this fragment.");
        }

        return fragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void updateLabel(Calendar calendar) {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        installationDate = calendar.getTime().getTime();
        datePickerInput.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel(Oak oak) {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        installationDate = oak.getInstallationDate();
        datePickerInput.setText(sdf.format(myCalendar.getTime()));
    }

    public void editOak(final View fragmentView) {
        oakManager.find(oakUid, new DatabaseCallback<Oak>() {
            @Override
            public void onSuccess(Oak identifiable) {
                oak = identifiable;
                oakSave = new Oak(identifiable);
                installationDate = oak.getInstallationDate();
                myCalendar.setTimeInMillis(installationDate);
                longitudeEditText = (EditText) fragmentView.findViewById(R.id.longitudeEditText);
                latitudeEditText = (EditText) fragmentView.findViewById(R.id.latitudeEditText);
                oakCircumferenceEditText = (EditText) fragmentView.findViewById(R.id.oakCircumferenceEditText);
                oakHeightEditText = (EditText) fragmentView.findViewById(R.id.oakHeightEditText);
                datePickerInput = (EditText) fragmentView.findViewById(R.id.installationDate);
                datePickerInput.setKeyListener(null); //To make it uneditable ! Java :D
                updateLabel(oak);

                date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel(myCalendar);
                        datePickerInput.clearFocus();
                    }

                };

                final Context activityContext = getActivity();
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

                longitudeEditText.setText(String.valueOf(oak.getLongitude()));
                latitudeEditText.setText(String.valueOf(oak.getLatitude()));
                oakCircumferenceEditText.setText(String.valueOf(oak.getOakCircumference()));
                oakHeightEditText.setText(String.valueOf(oak.getOakHeight()));
                installationDate = oak.getInstallationDate();

                validateButton = (Button) fragmentView.findViewById(R.id.validateButton);
                validateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        float longitude = Float.parseFloat(longitudeEditText.getText().toString());
                        float latitude = Float.parseFloat(latitudeEditText.getText().toString());
                        float oakCircumference = Float.parseFloat(oakCircumferenceEditText.getText().toString());
                        float oakHeight = Float.parseFloat(oakHeightEditText.getText().toString());

                        oak.setLongitude(longitude);
                        oak.setLatitude(latitude);
                        oak.setOakCircumference(oakCircumference);
                        oak.setOakHeight(oakHeight);
                        oak.setInstallationDate(installationDate);

                        oakManager.update(oak);
                        user.setOak(oak);
                        userManager.update(user);
                        Snackbar mySnackbar = Snackbar.make(fragmentView.findViewById(R.id.fragment_oak_form_layout),
                                R.string.oak_saved, Snackbar.LENGTH_SHORT);
                        mySnackbar.setAction(R.string.undo_action, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                oak = new Oak(oakSave);
                                oakManager.update(oak);
                            }
                        });
                        mySnackbar.show();


                    }
                });

                datePickerInput = (EditText) fragmentView.findViewById(R.id.installationDate);
                datePickerInput.setKeyListener(null); //To make it uneditable ! Java :D

                date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel(myCalendar);
                        datePickerInput.clearFocus();
                    }

                };

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

            @Override
            public void onFailure(DatabaseError error) {

            }
        });
    }

    public void newOak(final View fragmentView) {
        longitudeEditText = (EditText) fragmentView.findViewById(R.id.longitudeEditText);
        latitudeEditText = (EditText) fragmentView.findViewById(R.id.latitudeEditText);
        oakCircumferenceEditText = (EditText) fragmentView.findViewById(R.id.oakCircumferenceEditText);
        oakHeightEditText = (EditText) fragmentView.findViewById(R.id.oakHeightEditText);

        validateButton = (Button) fragmentView.findViewById(R.id.validateButton);
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float longitude = Float.parseFloat(longitudeEditText.getText().toString());
                float latitude = Float.parseFloat(latitudeEditText.getText().toString());
                float oakCircumference = Float.parseFloat(oakCircumferenceEditText.getText().toString());
                float oakHeight = Float.parseFloat(oakHeightEditText.getText().toString());
                oak = new Oak(user, longitude, latitude);
                oak.setLongitude(longitude);
                oak.setLatitude(latitude);
                oak.setOakCircumference(oakCircumference);
                oak.setOakHeight(oakHeight);
                oak.setInstallationDate(installationDate);

                oakManager.update(oak);
                user.setOak(oak);
                userManager.update(user);
                Snackbar mySnackbar = Snackbar.make(fragmentView.findViewById(R.id.fragment_oak_form_layout),
                        R.string.oak_saved, Snackbar.LENGTH_SHORT);
                mySnackbar.show();


            }
        });

        datePickerInput = (EditText) fragmentView.findViewById(R.id.installationDate);
        datePickerInput.setKeyListener(null); //To make it uneditable ! Java :D

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar);
                datePickerInput.clearFocus();
            }

        };

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
