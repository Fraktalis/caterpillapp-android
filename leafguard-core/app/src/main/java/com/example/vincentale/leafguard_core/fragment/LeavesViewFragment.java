package com.example.vincentale.leafguard_core.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.vincentale.leafguard_core.R;
import com.example.vincentale.leafguard_core.model.LeavesObservation;
import com.example.vincentale.leafguard_core.model.manager.LeavesObservationManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LeavesViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LeavesViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeavesViewFragment extends Fragment {

    private UserManager userManager;
    private LeavesObservation leavesObservation;
    private LeavesObservationManager leavesObservationManager;
    private String oakUid;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leaves_view, container, false);
    }





}
