package com.fraktalis.caterpillapp.leafguard_core.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fraktalis.caterpillapp.leafguard_core.R;
import com.fraktalis.caterpillapp.leafguard_core.model.Oak;
import com.fraktalis.caterpillapp.leafguard_core.model.manager.OakManager;
import com.fraktalis.caterpillapp.leafguard_core.model.User;
import com.fraktalis.caterpillapp.leafguard_core.model.manager.UserManager;
import com.fraktalis.caterpillapp.leafguard_core.util.DatabaseCallback;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OakFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OakFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OakFragment extends Fragment {
    public static final String TAG = "OakFragment";

    public static final String NEW_OAK_ACTION = "new_oak";
    public static final String EDIT_OAK_ACTION = "edit_oak";

    private Calendar myCalendar = Calendar.getInstance();
    private TextView longitudeText;
    private TextView latitudeText;
    private TextView circumferenceText;
    private TextView heightText;
    private TextView installationDateText;

    private Button editOakButton;

    private FirebaseDatabase firebaseDatabase;
    private OakManager oakManager;
    private UserManager userManager;
    private User user;
    private Oak oak;
    private String oakUid;

    private OnFragmentInteractionListener mListener;

    public OakFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileFragment.
     */
    public static OakFragment newInstance() {
        OakFragment fragment = new OakFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called !");
        firebaseDatabase = FirebaseDatabase.getInstance();
        userManager = UserManager.getInstance();
        oakManager = OakManager.getInstance();
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

        final View fragmentView = inflater.inflate(R.layout.fragment_oak, container, false);
        oakManager.find(oakUid, new DatabaseCallback<Oak>() {
            @Override
            public void onSuccess(Oak identifiable) {
                oak = identifiable;
                longitudeText = fragmentView.findViewById(R.id.longitudeText);
                latitudeText = fragmentView.findViewById(R.id.latitudeText);
                circumferenceText = fragmentView.findViewById(R.id.circumferenceText);
                heightText = fragmentView.findViewById(R.id.heightText);
                installationDateText = fragmentView.findViewById(R.id.installationDateText);
                editOakButton = fragmentView.findViewById(R.id.editOakButton);

                longitudeText.setText(String.valueOf(oak.getLongitude()));
                latitudeText.setText(String.valueOf(oak.getLatitude()));
                circumferenceText.setText(String.valueOf(oak.getOakCircumference()));
                heightText.setText(String.valueOf(oak.getOakHeight()));
                myCalendar.setTimeInMillis(oak.getInstallationDate());
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                installationDateText.setText(sdf.format(myCalendar.getTime()));

                editOakButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        OakFormFragment formFragment = OakFormFragment.newInstance();
                        formFragment.setArguments(OakFragment.this.getArguments());
                        fm.beginTransaction()
                                .replace(R.id.oak_fragment_container, formFragment)
                                .commit();
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
        void onFragmentInteraction(Uri uri);
    }
}
