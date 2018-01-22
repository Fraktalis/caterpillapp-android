package com.example.vincentale.leafguard_core;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vincentale.leafguard_core.model.ImageUploadInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CameraIntentActivity extends Activity {
    // TODO : Action depuis la recycleview
    // TODO : Récupérer de firebase
    // TODO : remettre les string dans le fichier
    private static final int ACTIVITY_START_CAMERA_APP = 1;
    Uri selectedImage;
    ProgressDialog progressDialog;
    UploadTask uploadTask;
    FirebaseStorage storage;
    StorageReference storageRef,imageRef;
    // Creating DatabaseReference.
    DatabaseReference databaseReference;
    // Creating RecyclerView.
    RecyclerView recyclerView;
    // Creating RecyclerView.Adapter.
    RecyclerView.Adapter adapter;
    // Creating List of ImageUploadInfo class.
    List<ImageUploadInfo> list = new ArrayList<>();
    private ImageView mPhotoCapturedImageView;
    private String mImageFileLocation = "";
    private String GALLERY_LOCATION = "image gallery";
    private File mGalleryFolder;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_intent);
        createImageGallery();
        //accessing the firebase storage
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        imageRef = storageRef.child("images");
        final ImageView imageView = findViewById(R.id.imageView);
//        Glide.with(this)
//                .load("https://firebasestorage.googleapis.com/v0/b/leafguard-firebase.appspot.com/o/IMAGE_20180122_161651_-2071772904.jpg?alt=media&token=8e383c52-cbea-4c71-a0f6-ce501ce67046")
//                .into(imageView);


//        imageRef.child("IMAGE_20180121_173345_144153276.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Glide.with(getApplicationContext()).load(uri.toString()).into(imageView);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//            }
//        });

//        // Assign id to RecyclerView.
//        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//
//        // Setting RecyclerView size true.
//        recyclerView.setHasFixedSize(true);
//
//        // Setting RecyclerView layout as LinearLayout.
//        recyclerView.setLayoutManager(new LinearLayoutManager(CameraIntentActivity.this));
//
//        // Assign activity this to progress dialog.
//        progressDialog = new ProgressDialog(CameraIntentActivity.this);
//
//        // Setting up message in Progress dialog.
//        progressDialog.setMessage("Loading Images From Firebase.");
//
//        // Showing progress dialog.
//        progressDialog.show();
//        // The path is already defined in MainActivity.
//        databaseReference = FirebaseDatabase.getInstance().getReference("images");
//        // Adding Add Value Event Listener to databaseReference.
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//                    ImageUploadInfo imageUploadInfo = postSnapshot.getValue(ImageUploadInfo.class);
//                    list.add(imageUploadInfo);
//                }
//                adapter = new RecyclerViewAdapter(getApplicationContext(), list);
//                recyclerView.setAdapter(adapter);
//                // Hiding the progress dialog.
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//                // Hiding the progress dialog.
//                progressDialog.dismiss();
//
//            }
//        });

        //Permission
        Button button = findViewById(R.id.photoButton);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            button.setEnabled(false);
        } else {
            button.setEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera_intent, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void takePhoto(View view) {
        Intent callCameraApplicationIntent = new Intent();
        callCameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
            callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, defineUriFromFile(photoFile));
            startActivityForResult(callCameraApplicationIntent, ACTIVITY_START_CAMERA_APP);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if(requestCode == ACTIVITY_START_CAMERA_APP && resultCode == RESULT_OK) {
            Toast.makeText(this, "Picture taken successfully", Toast.LENGTH_SHORT).show();
            uploadImage();
        }
    }

    private void createImageGallery() {
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        mGalleryFolder = new File(storageDirectory, GALLERY_LOCATION);
        if(!mGalleryFolder.exists()) {
            mGalleryFolder.mkdirs();
        }
    }

    File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        File image = File.createTempFile(imageFileName,".jpg", mGalleryFolder);
        mImageFileLocation = image.getAbsolutePath();
        selectedImage = defineUriFromFile(image);
        return image;
    }

    public void uploadImage() {
        //create reference to images folder and assing a name to the file that will be uploaded
        imageRef = storageRef.child(selectedImage.getLastPathSegment());
        //creating and showing progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMax(100);
        progressDialog.setMessage("Uploading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        progressDialog.setCancelable(false);
        //starting upload
        uploadTask = imageRef.putFile(selectedImage);
        // Observe state change events such as progress, pause, and resume
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                //sets and increments value of progressbar
                progressDialog.incrementProgressBy((int) progress);
            }
        });
        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(CameraIntentActivity.this,"Error in uploading!",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                //adding an upload to firebase database
//                String uploadId = databaseReference.push().getKey();
//                databaseReference.child(uploadId).setValue(downloadUrl.toString());
                Toast.makeText(CameraIntentActivity.this,"Upload successful",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private Uri defineUriFromFile(File file) {
        if (Build.VERSION.SDK_INT >= 24) {
            return FileProvider.getUriForFile(CameraIntentActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file);
        } else {
            return Uri.fromFile(file);
        }
    }
}
