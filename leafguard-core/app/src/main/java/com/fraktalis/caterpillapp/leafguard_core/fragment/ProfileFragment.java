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
import com.fraktalis.caterpillapp.leafguard_core.model.User;
import com.fraktalis.caterpillapp.leafguard_core.model.manager.UserManager;
import com.fraktalis.caterpillapp.leafguard_core.util.DatabaseCallback;
import com.google.firebase.database.DatabaseError;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private TextView emailText;
    private TextView nameText;
    private TextView partnerIdText;
    private TextView schoolNameText;
    private TextView oakText;
    private Button editButton;

    private UserManager userManager;
    private User user;


    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View fragmentView = inflater.inflate(R.layout.fragment_profile, container, false);
        userManager = UserManager.getInstance();
        userManager.getUser(new DatabaseCallback<User>() {
            @Override
            public void onSuccess(User identifiable) {
                user = identifiable;
                emailText = (TextView) fragmentView.findViewById(R.id.emailEditText);
                emailText.setText(user.getEmail());
                nameText = (TextView) fragmentView.findViewById(R.id.nameText);
                nameText.setText(user.getDisplayName());
                schoolNameText = (TextView) fragmentView.findViewById(R.id.schoolNameText);
                Log.d("ProfileFragment", "user.getSchoolName() = " + user.getSchoolName());
                schoolNameText.setText(user.getSchoolName());
                partnerIdText = fragmentView.findViewById(R.id.partnerIdText);
                partnerIdText.setText(user.getPartnerId());
                oakText = (TextView) fragmentView.findViewById(R.id.oakText);
                if (user.getOak() != null) {
                    oakText.setText(R.string.oak_already_associated);
                }
                editButton = fragmentView.findViewById(R.id.editButton);
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        ProfileFormFragment formFragment = ProfileFormFragment.newInstance();
                        fm.beginTransaction()
                                .replace(R.id.profile_fragment_container, formFragment)
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
