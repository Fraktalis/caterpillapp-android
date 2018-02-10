package com.example.vincentale.leafguard_core.fragment.admin;


import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.vincentale.leafguard_core.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminObservationFragment extends Fragment {

    public static final String LABEL = "Observation";
    public static final String CHANNEL_ID = "observation_download_channel";

    private Button downloadButton;


    public AdminObservationFragment() {
        // Required empty public constructor
    }

    public static AdminObservationFragment newInstance() {
        return new AdminObservationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_observation, container, false);
        downloadButton = view.findViewById(R.id.downloadObservationButton);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }
}
