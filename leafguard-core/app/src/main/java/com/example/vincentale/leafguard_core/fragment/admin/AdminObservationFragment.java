package com.example.vincentale.leafguard_core.fragment.admin;


import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.vincentale.leafguard_core.R;
import com.example.vincentale.leafguard_core.model.CaterpillarObservation;
import com.example.vincentale.leafguard_core.model.manager.CaterpillarObservationManager;
import com.example.vincentale.leafguard_core.service.ObservationDownloadService;
import com.example.vincentale.leafguard_core.util.DatabaseListCallback;
import com.google.firebase.database.DatabaseError;

import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminObservationFragment extends Fragment {

    public static final String LABEL = "Observation";
    public static final String CHANNEL_ID = "observation_download_channel";

    private Button downloadButton;
    private TextView observationHaveChangedText;
    private CaterpillarObservationManager observationManager = CaterpillarObservationManager.getInstance();


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
        observationHaveChangedText = view.findViewById(R.id.observationHaveChanged);
        downloadButton = view.findViewById(R.id.downloadObservationButton);
        final Context context = getContext();
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent downloadIntent = new Intent(context, ObservationDownloadService.class);
                context.startService(downloadIntent);

            }
        });
        return view;
    }
}
