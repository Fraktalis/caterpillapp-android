package com.example.vincentale.leafguard_core.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserManager;
import android.support.v4.app.Fragment;
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

 * to handle interaction events.
 * Use the {@link LeavesFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeavesFormFragment extends Fragment {

    private OakFragment.OnFragmentInteractionListener mListener;


    private FirebaseDatabase firebaseDatabase;
    private com.example.vincentale.leafguard_core.model.manager.UserManager userManager;
    private LeavesObservation leavesObservation;
    private LeavesObservationManager leavesObservationManager;
    private String oakUid;
    private User user;


    private EditText nbLeaves;
    private EditText nbGalls;
    private EditText nbMines;
    private EditText nbClassA;
    private EditText nbClassB;
    private EditText nbClassC;
    private EditText nbClassD;
    private EditText nbClassE;
    private EditText nbClassF;
    private EditText nbClassG;
    private EditText nbClassH;

    private Button validate;



    public LeavesFormFragment() {
        // Required empty public constructor
    }


    public static LeavesFormFragment newInstance() {
        LeavesFormFragment fragment = new LeavesFormFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseDatabase = FirebaseDatabase.getInstance();
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




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fragmentView = inflater.inflate(R.layout.fragment_leaves_form, container, false);
        nbLeaves= fragmentView.findViewById(R.id.nbLeaves);
        nbGalls= fragmentView.findViewById(R.id.nbGalls);
        nbMines= fragmentView.findViewById(R.id.nbMines);
        nbClassA = fragmentView.findViewById(R.id.nbClassA);
        nbClassB = fragmentView.findViewById(R.id.nbClassB);
        nbClassC = fragmentView.findViewById(R.id.nbClassC);
        nbClassD = fragmentView.findViewById(R.id.nbClassD);
        nbClassE = fragmentView.findViewById(R.id.nbClassE);
        nbClassF= fragmentView.findViewById(R.id.nbClassF);
        nbClassG = fragmentView.findViewById(R.id.nbClassG);
        nbClassH = fragmentView.findViewById(R.id.nbClassH);
        validate = fragmentView.findViewById(R.id.validateButton);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


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
  /*  public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
*/

}
