package com.example.vincentale.leafguard_core.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.vincentale.leafguard_core.R;
import com.example.vincentale.leafguard_core.model.User;
import com.example.vincentale.leafguard_core.model.manager.UserManager;
import com.example.vincentale.leafguard_core.util.DatabaseCallback;
import com.example.vincentale.leafguard_core.util.OnUpdateCallback;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFormFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFormFragment extends Fragment {
    public static final String TAG = "ProfileFormFragment";

    private EditText schoolNameEditText;
    private EditText surnameEditText;
    private EditText nameEditText;
    private Button submitButton;

    private UserManager userManager;
    private FirebaseDatabase firebaseDatabase;
    private User user;


    private OnFragmentInteractionListener mListener;

    public ProfileFormFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFormFragment newInstance() {
        ProfileFormFragment fragment = new ProfileFormFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseDatabase = FirebaseDatabase.getInstance();
        userManager = UserManager.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View fragmentView = inflater.inflate(R.layout.fragment_profil_form, container, false);
        userManager = UserManager.getInstance();
        userManager.getUser(new DatabaseCallback<User>() {
            @Override
            public void onSuccess(User identifiable) {
                user = identifiable;
                schoolNameEditText = fragmentView.findViewById(R.id.schoolNameEditText);
                if (user.getSchoolName() != null && !user.getSchoolName().isEmpty()) {
                    schoolNameEditText.setText(user.getSchoolName());
                }
                surnameEditText = fragmentView.findViewById(R.id.surnameEditText);
                if (user.getSurname() != null && !user.getSurname().isEmpty()) {
                    surnameEditText.setText(user.getSurname());
                }
                nameEditText = fragmentView.findViewById(R.id.nameEditText);
                if (user.getName() != null && !user.getName().isEmpty()) {
                    nameEditText.setText(user.getName());
                }
                submitButton = fragmentView.findViewById(R.id.submitButton);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.setSchoolName(schoolNameEditText.getText().toString());
                        user.setSurname(surnameEditText.getText().toString());
                        user.setName(nameEditText.getText().toString());
                        userManager.update(user, new OnUpdateCallback() {
                            @Override
                            public void onSuccess() {
                                Snackbar validationSnackbar = Snackbar.make(fragmentView.findViewById(R.id.fragment_profil_form_layout),
                                        R.string.profile_saved, Snackbar.LENGTH_SHORT);
                                validationSnackbar.show();
                                final FragmentManager fm = getActivity().getSupportFragmentManager();
                                final ProfileFragment formFragment = ProfileFragment.newInstance();
                                validationSnackbar.addCallback(new Snackbar.Callback() {
                                    @Override
                                    public void onDismissed(Snackbar transientBottomBar, int event) {
                                        super.onDismissed(transientBottomBar, event);
                                        fm.beginTransaction()
                                                .replace(R.id.profile_fragment_container, formFragment)
                                                .commit();
                                    }
                                });
                            }

                            @Override
                            public void onError(Throwable err) {
                                Snackbar validationSnackbar = Snackbar.make(fragmentView.findViewById(R.id.fragment_profil_form_layout),
                                        R.string.error_occured, Snackbar.LENGTH_SHORT);
                                validationSnackbar.show();
                            }
                        });
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
