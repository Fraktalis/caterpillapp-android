package com.example.vincentale.leafguard_core.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vincentale.leafguard_core.R;
import com.example.vincentale.leafguard_core.model.Oak;
import com.example.vincentale.leafguard_core.model.OakManager;
import com.example.vincentale.leafguard_core.model.User;
import com.example.vincentale.leafguard_core.model.UserManager;
import com.example.vincentale.leafguard_core.util.DatabaseCallback;
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

    private Calendar myCalendar = Calendar.getInstance();
    private EditText longitudeEditText;
    private EditText latitudeEditText;
    private EditText oakCircumferenceEditText;
    private EditText oakHeightEditText;

    private EditText datePickerInput;
    private long installationDate;

    private Button validateButton;

    private FirebaseDatabase firebaseDatabase;
    private OakManager oakManager;
    private UserManager userManager;
    private User user;

    DatePickerDialog.OnDateSetListener date;


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View fragmentView = inflater.inflate(R.layout.fragment_oak_form, container, false);
        userManager.getUser(new DatabaseCallback<User>() {
            @Override
            public void onSuccess(User identifiable) {
                user = identifiable;
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
                        if (user == null || user.getUid() == null) {
                            Toast.makeText(OakFormFragment.this.getActivity(), "User information are not complete. Please wait before sending the form", Toast.LENGTH_SHORT).show();
                        } else {
                            Oak oak = new Oak(user, longitude, latitude);
                            oak.setOakCircumference(oakCircumference);
                            oak.setOakHeight(oakHeight);
                            oak.setInstallationDate(installationDate);
                            oakManager.update(oak);
                            user.setOak(oak);
                            userManager.update(user);
                            Toast.makeText(OakFormFragment.this.getActivity(), "Oak successfully added !", Toast.LENGTH_SHORT).show();
                        }
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
                        updateLabel();
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
            }

            @Override
            public void onFailure(DatabaseError error) {

            }
        });


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

    private void updateLabel() {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        installationDate = myCalendar.getTime().getTime();
        datePickerInput.setText(sdf.format(myCalendar.getTime()));
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
