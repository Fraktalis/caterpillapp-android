package com.example.vincentale.leafguard_core.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vincentale.leafguard_core.R;
import com.example.vincentale.leafguard_core.model.User;
import com.example.vincentale.leafguard_core.model.UserManager;
import com.google.firebase.database.DatabaseReference;
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
        user = userManager.getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_profil_form, container, false);
        userManager = UserManager.getInstance();
        User mUser = userManager.getUser();
        final String userUid = mUser.getUid();
        surnameEditText = fragmentView.findViewById(R.id.surnameEditText);
        if (mUser.getSurname() != null && !mUser.getSurname().isEmpty()) {
            surnameEditText.setText(mUser.getSurname());
        }
        nameEditText = fragmentView.findViewById(R.id.nameEditText);
        if (mUser.getName() != null && !mUser.getName().isEmpty()) {
            nameEditText.setText(mUser.getName());
        }
        submitButton = fragmentView.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setSurname(surnameEditText.getText().toString());
                user.setName(nameEditText.getText().toString());
                userManager.update(user);
                Toast.makeText(getActivity(), getText(R.string.information_update_success), Toast.LENGTH_SHORT).show();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                ProfileFragment formFragment = ProfileFragment.newInstance();
                fm.beginTransaction()
                        .replace(R.id.profile_fragment_container, formFragment)
                        .commit();
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
