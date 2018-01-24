package com.example.vincentale.leafguard_core.fragment.admin;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.vincentale.leafguard_core.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ipaulpro.afilechooser.utils.FileUtils;

import java.io.File;


@SuppressWarnings("VisibleForTests")
public class AdminUserFragment extends Fragment {
    private static final int FILE_SELECT_CODE = 0;
    private static final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 2;
    private static final String TAG = "AdminUserFragment";

    private Button browseFileButton;
    private EditText browseFileEditText;
    private LinearLayout adminUserProgressLayout;
    private LinearLayout adminUserUploadLayout;
    private Button cancelFileUploadButton;
    private ProgressBar provisionUploadProgressBar;

    private Uri provisionFile;
    private StorageReference provisionRef;
    private UploadTask uploadTask;

    private OnFragmentInteractionListener mListener;

    public AdminUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AdminUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminUserFragment newInstance() {
        AdminUserFragment fragment = new AdminUserFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        provisionRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_admin_user, container, false);
        adminUserUploadLayout = fragmentView.findViewById(R.id.admin_user_upload_layout);
        adminUserProgressLayout = fragmentView.findViewById(R.id.admin_user_progress_layout);
        cancelFileUploadButton = fragmentView.findViewById(R.id.cancelFileUploadButton);
        provisionUploadProgressBar = fragmentView.findViewById(R.id.provisionUploadProgressBar);
        provisionUploadProgressBar.setMax(100);

        browseFileButton = fragmentView.findViewById(R.id.browseFileButton);
        browseFileButton.setEnabled(false);
        browseFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminUserProgressLayout.setVisibility(View.VISIBLE);
                adminUserUploadLayout.setVisibility(View.GONE);
                StorageReference provisionFileRef = provisionRef.child("provisions").child(provisionFile.getLastPathSegment());
                uploadTask = provisionFileRef.putFile(provisionFile);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        adminUserProgressLayout.setVisibility(View.GONE);
                        adminUserUploadLayout.setVisibility(View.VISIBLE);

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        int progress = (int) ((100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                        Log.d(TAG, "progress : " + progress);
                        provisionUploadProgressBar.setProgress(progress);
                    }
                });
            }
        });

        browseFileEditText = fragmentView.findViewById(R.id.browseFileEditText);
        browseFileEditText.setFocusable(false);
        browseFileEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) getContext(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);

                } else {
                    showFileChooser();
                }
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

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getContext(), "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == FragmentActivity.RESULT_OK) {
                    Uri uri = data.getData();
                    provisionFile = uri;
                    Toast.makeText(getContext(), uri.toString(), Toast.LENGTH_SHORT).show();
                    browseFileEditText.setText(getFileName(uri));
                    browseFileButton.setEnabled(true);
                }
        }
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

    /**
     * Utility function to have the the name of a file by its URI
     * @param uri
     * @return
     */
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = this.getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
