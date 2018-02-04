package com.example.vincentale.leafguard_core;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vincentale.leafguard_core.model.ImageCaterpillar;
import com.example.vincentale.leafguard_core.view.RecyclerViewAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    // TODO : remettre les string dans le fichier
    private static final int ACTIVITY_START_CAMERA_APP = 1;
    private static final int SELECT_PHOTO = 100;
    Uri selectedImage;
    ProgressDialog progressDialog;
    FirebaseStorage storage;
    StorageReference storageRef,imageRef;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<ImageCaterpillar> list = new ArrayList<>();
    private String mImageFileLocation = "";
    private String GALLERY_LOCATION = "image gallery";
    private File mGalleryFolder;

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

        //Permission
        final Button button = findViewById(R.id.photoButton);
        final Button button2 = findViewById(R.id.selectImageButton);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            button.setEnabled(false);
        } else {
            button.setEnabled(true);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            button2.setEnabled(false);
        } else {
            button2.setEnabled(true);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(CameraIntentActivity.this));
        progressDialog = new ProgressDialog(CameraIntentActivity.this);
        progressDialog.setMessage(getString(R.string.loadingFirebase));
        progressDialog.show();
        //todo: Remplacer par le numero de l'arbre et de la chenille
        databaseReference = FirebaseDatabase.getInstance().getReference("images").child("arbrenum").child("chenilleNum2");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ImageCaterpillar imageUploadInfo = postSnapshot.getValue(ImageCaterpillar.class);
                    list.add(imageUploadInfo);
                }
                adapter = new RecyclerViewAdapter(getApplicationContext(), list);
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
                if (adapter.getItemCount() >= 3) {
                    button.setEnabled(false);
                    button2.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                // Hiding the progress dialog.
                progressDialog.dismiss();

            }
        });
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

    public void uploadPhoto(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        selectedImage = photoPickerIntent.getData();
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if(requestCode == ACTIVITY_START_CAMERA_APP && resultCode == RESULT_OK) {
            Toast.makeText(this, getText(R.string.photoToast), Toast.LENGTH_SHORT).show();
            uploadImage();
        }
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
            Toast.makeText(CameraIntentActivity.this, getText(R.string.pictureToast), Toast.LENGTH_SHORT).show();
            selectedImage = data.getData();
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
        // Checking whether FilePathUri Is empty or not.
        if (selectedImage != null) {
            progressDialog.setTitle(getString(R.string.imageUploading));
            progressDialog.show();
            //create reference to images folder and assing a name to the file that will be uploaded
            imageRef = storageRef.child("images/" + selectedImage.getLastPathSegment());
            //imageRef = storageRef.child("images" + System.currentTimeMillis() + "." + GetFileExtension(selectedImage));

            imageRef.putFile(selectedImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), getString(R.string.imageUploadFirebase), Toast.LENGTH_LONG).show();
                            @SuppressWarnings("VisibleForTests")
                            ImageCaterpillar imageUploadInfo = new ImageCaterpillar(taskSnapshot.getDownloadUrl().toString());
                            String ImageUploadId = databaseReference.push().getKey();
                            databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Hiding the progressDialog.
                            progressDialog.dismiss();
                            // Showing exception erro message.
                            Toast.makeText(CameraIntentActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            // Setting progressDialog Title.
                            progressDialog.setTitle(getString(R.string.imageUploading));
                        }
                    });
        }
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

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }
}
