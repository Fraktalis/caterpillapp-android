package com.example.vincentale.leafguard_core.fragment.admin;


import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.vincentale.leafguard_core.R;
import com.example.vincentale.leafguard_core.model.manager.CaterpillarObservationManager;
import com.example.vincentale.leafguard_core.service.ObservationService;
import com.example.vincentale.leafguard_core.util.ObservationServiceCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminObservationFragment extends Fragment {

    public static final String TAG = "AdminObsFragment";
    public static final String LABEL = "Observation";
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 42;

    private Button downloadButton;
    private ProgressBar observationprogressBar;
    private TextView observationHaveChangedText;
    private ObservationService observationService;
    private boolean isServiceBound = false;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ObservationService.ObservationBinder binder = (ObservationService.ObservationBinder) iBinder;
            observationService = binder.getService();
            isServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            observationService = null;
            isServiceBound = false;
        }
    };


    public AdminObservationFragment() {
        // Required empty public constructor
    }

    public static AdminObservationFragment newInstance() {
        return new AdminObservationFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        final Context context = getContext();
        Intent downloadIntent = new Intent(context, ObservationService.class);
        context.bindService(downloadIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        getContext().unbindService(mConnection);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_observation, container, false);
        observationprogressBar = view.findViewById(R.id.observationProgressBar);
        observationprogressBar.setIndeterminate(true);
        observationHaveChangedText = view.findViewById(R.id.observationHaveChanged);
        downloadButton = view.findViewById(R.id.downloadObservationButton);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permissionCheck = ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {
                    downloadButton.setVisibility(View.GONE);
                    observationprogressBar.setVisibility(View.VISIBLE);
                    if (isServiceBound) {
                        observationService.saveObservations(new ObservationServiceCallback() {
                            @Override
                            public void onSuccess(String observationFullPath) {
                                observationHaveChangedText.setText(getString(R.string.observation_saved_at_location, observationFullPath));
                                downloadButton.setVisibility(View.VISIBLE);
                                observationprogressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(Exception ex) {
                                Log.e(TAG, "Observations save failed", ex);
                                observationHaveChangedText.setTextColor(getResources().getColor(R.color.accent));
                                observationHaveChangedText.setText(R.string.error_occured);
                                downloadButton.setVisibility(View.VISIBLE);
                                observationprogressBar.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        Log.e(TAG, "Service unbound");
                        observationHaveChangedText.setTextColor(getResources().getColor(R.color.accent));
                        observationHaveChangedText.setText(R.string.error_occured);
                        downloadButton.setVisibility(View.VISIBLE);
                        observationprogressBar.setVisibility(View.GONE);
                    }
                }
            }
        });

        return view;
    }
}
