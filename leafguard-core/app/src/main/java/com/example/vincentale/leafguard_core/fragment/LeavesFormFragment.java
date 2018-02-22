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

 * to handle interaction events.
 * Use the {@link LeavesFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeavesFormFragment extends Fragment {

    private UserManager userManager;
    private LeavesObservation leavesObservation;
    private LeavesObservationManager leavesObservationManager;
    private String oakUid;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leaves_form, container, false);
    }




}
