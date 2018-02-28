package com.example.vincentale.leafguard_core.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.vincentale.leafguard_core.R;
import com.example.vincentale.leafguard_core.model.LeavesObservation;
import com.example.vincentale.leafguard_core.model.User;
import com.example.vincentale.leafguard_core.model.manager.LeavesObservationManager;
import com.example.vincentale.leafguard_core.util.DatabaseCallback;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *  interface
 * to handle interaction events.
 * Use the {@link LeavesViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeavesViewFragment extends Fragment {

    private com.example.vincentale.leafguard_core.model.manager.UserManager userManager;
    private User user;
    private LeavesObservation leavesObservation;
    private LeavesObservationManager leavesObservationManager;
    private String oakUid;

    private OakFragment.OnFragmentInteractionListener mListener;

    private Button edit;
    private TextView nbLeaves;
    private TextView nbGalls;
    private TextView nbMines;
    private TextView nbClassA;
    private TextView nbClassB;
    private TextView nbClassC;
    private TextView nbClassD;
    private TextView nbClassE;
    private TextView nbClassF;
    private TextView nbClassG;
    private TextView nbClassH;


    public LeavesViewFragment() {
        // Required empty public constructor
    }



    public static LeavesViewFragment newInstance() {
        LeavesViewFragment fragment = new LeavesViewFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //firebaseDatabase = FirebaseDatabase.getInstance();
        userManager = com.example.vincentale.leafguard_core.model.manager.UserManager.getInstance();
        userManager.getUser(new DatabaseCallback<User>() {
            @Override
            public void onSuccess(User identifiable) {
                user = identifiable;
            }

            @Override
            public void onFailure(DatabaseError error) {

            }
        });

        oakUid= user.getOakId();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fragmentView =  inflater.inflate(R.layout.fragment_leaves_view, container, false);

        nbLeaves= fragmentView.findViewById(R.id.numberObservedLeavesText);
        nbGalls=fragmentView.findViewById(R.id.numbergallsText);
        nbMines=fragmentView.findViewById(R.id.numberMinesText);
        nbClassA=fragmentView.findViewById(R.id.classAText);
        nbClassB=fragmentView.findViewById(R.id.classBText);
        nbClassC=fragmentView.findViewById(R.id.classCText);
        nbClassD=fragmentView.findViewById(R.id.classDText);
        nbClassE=fragmentView.findViewById(R.id.classEText);
        nbClassF=fragmentView.findViewById(R.id.classFText);
        nbClassG=fragmentView.findViewById(R.id.classGText);
        nbClassH=fragmentView.findViewById(R.id.classHText);
        //edit= fragmentView.findViewById(R.id.editLeavesButton);
        /*validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                LeavesFormFragment leavesFormFragment= LeavesFormFragment.newInstance();
                fm.beginTransaction()
                        .replace(R.id.oak_fragment_container, leavesFormFragment)
                        .commit();
            }
        });*/




        return fragmentView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OakFragment.OnFragmentInteractionListener) {
            mListener = (OakFragment.OnFragmentInteractionListener) context;
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
    /*public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
*/

}
