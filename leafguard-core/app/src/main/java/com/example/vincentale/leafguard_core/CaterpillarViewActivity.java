package com.example.vincentale.leafguard_core;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vincentale.leafguard_core.model.Caterpillar;
import com.example.vincentale.leafguard_core.model.ImageCaterpillar;
import com.example.vincentale.leafguard_core.model.User;
import com.example.vincentale.leafguard_core.model.manager.CaterpillarManager;
import com.example.vincentale.leafguard_core.model.manager.UserManager;
import com.example.vincentale.leafguard_core.util.DatabaseCallback;
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

public class CaterpillarViewActivity extends AppCompatActivity {

    public static final String TAG = "CatterpillarViewActi";
    public static final String CATERPILLAR_UID = "caterUID";
    public static final String CATERPILLAR_INDEX = "caterIndex";
    public static final String OBSERVATION_INDEX = "observationIndex";
    private static final int ACTIVITY_START_CAMERA_APP = 1;
    private static final int SELECT_PHOTO = 100;

    private CaterpillarManager caterpillarManager = CaterpillarManager.getInstance();
    private Caterpillar item;

    private UserManager userManager = UserManager.getInstance();
    private User user;

    private TextView catterpillarName;
    private CheckBox isMissing;
    private LinearLayout woundLayout;
    private CheckBox attackedByMammals;
    private CheckBox attackedByInsect;
    private CheckBox attackedByBird;
    private CheckBox attackedByLizard;
    private CheckBox attackedByOther;
    private Button photoButton;
    private Button selectImageButton;
    private Button validate;

    private Uri selectedImage;
    private ProgressDialog progressDialog;
    private StorageReference storageRef, imageRef;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ImageCaterpillar> list = new ArrayList<>();
    private String mImageFileLocation = "";
    private String GALLERY_LOCATION = "caterpillapp";
    private File mGalleryFolder;
    private File photoFile = null;

    public void setItem(Caterpillar item) {
        this.item = item;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catterpillar_view);
        storageRef = FirebaseStorage.getInstance().getReference();
        imageRef = storageRef.child("images");
        photoButton = (Button) findViewById(R.id.photoButton);
        selectImageButton = (Button) findViewById(R.id.selectImageButton);
        catterpillarName = (TextView) findViewById(R.id.catterpillarName);
        isMissing = (CheckBox) findViewById(R.id.isMissing);
        attackedByBird = (CheckBox) findViewById(R.id.attackedByBirds);
        attackedByMammals = (CheckBox) findViewById(R.id.attackedByMammals);
        attackedByInsect = (CheckBox) findViewById(R.id.attackedByInsect);
        attackedByLizard = (CheckBox) findViewById(R.id.attackedByLizards);
        attackedByOther = (CheckBox) findViewById(R.id.attackedByOther);
        validate = (Button) findViewById(R.id.validateCatterpillarButton);
        woundLayout = (LinearLayout) findViewById(R.id.woundsLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //createImageGallery();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(CaterpillarViewActivity.this));
        Intent intent = getIntent();
        final String catterUID = intent.getStringExtra(CATERPILLAR_UID);
        final int caterIndex = intent.getIntExtra(CATERPILLAR_INDEX, 0);
        final int observationIndex = intent.getIntExtra(OBSERVATION_INDEX, -1);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            photoButton.setEnabled(false);
        } else {
            selectImageButton.setEnabled(true);
        }
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            photoButton.setEnabled(false);
        } else {
            selectImageButton.setEnabled(true);
        }
        userManager.getUser(new DatabaseCallback<User>() {
            @Override
            public void onSuccess(User identifiable) {
                user = identifiable;
                caterpillarManager.find(catterUID, new DatabaseCallback<Caterpillar>() {
                    @Override
                    public void onSuccess(Caterpillar identifiable) {
                        if (identifiable == null) {
                            item = new Caterpillar(user.getOak(), observationIndex, caterIndex);
                        } else {
                            item = identifiable;
                        }
                        catterpillarName.setText(String.format(getResources().getString(R.string.catterpillar_with_number), item.getIndex()));

                        isMissing.setChecked(item.isCatterpillarMissing());
                        if (isMissing.isChecked()) {
                            woundLayout.setVisibility(View.INVISIBLE);
                        }
                        isMissing.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (isMissing.isChecked()) {
                                    woundLayout.setVisibility(View.INVISIBLE);
                                } else {
                                    woundLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                        attackedByBird.setChecked(item.isWoundByBird());
                        attackedByInsect.setChecked(item.isWoundByInsect());
                        attackedByMammals.setChecked(item.isWoundByMammal());
                        attackedByLizard.setChecked(item.isWoundByLizard());
                        attackedByOther.setChecked(item.isWoundByOther());
                        validate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                item.setEdited(true);
                                item.setCatterpillarMissing(isMissing.isChecked());
                                item.setWoundByBird(attackedByBird.isChecked());
                                item.setWoundByInsect(attackedByInsect.isChecked());
                                item.setWoundByMammal(attackedByMammals.isChecked());
                                item.setWoundByLizard(attackedByLizard.isChecked());
                                item.setWoundByOther(attackedByOther.isChecked());
                                caterpillarManager.update(item, null);
                                finish();

                            }
                        });
                        progressDialog = new ProgressDialog(CaterpillarViewActivity.this);
                        progressDialog.setMessage(getString(R.string.loadingFirebase));
                        progressDialog.show();
                        databaseReference = FirebaseDatabase.getInstance().getReference("images").child(user.getOakId()).child(item.getIndex() + "");
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
                                    photoButton.setEnabled(false);
                                    selectImageButton.setEnabled(false);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                progressDialog.dismiss();
                            }
                        });

                    }
                    @Override
                    public void onFailure(DatabaseError error) {
                        Log.d(TAG, error.toString());
                    }
                });
            }

            @Override
            public void onFailure(DatabaseError error) {
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_camera_intent, menu);
//        return true;
//    }

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
        Intent callCameraApplicationIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (callCameraApplicationIntent.resolveActivity(getPackageManager()) != null) {
            try {
                photoFile = createImageFile();
                callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, defineUriFromFile(photoFile));
                startActivityForResult(callCameraApplicationIntent, ACTIVITY_START_CAMERA_APP);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadPhoto(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        selectedImage = photoPickerIntent.getData();
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_START_CAMERA_APP && resultCode == RESULT_OK) {
            Toast.makeText(this, getText(R.string.photoToast), Toast.LENGTH_SHORT).show();
            uploadImage(ACTIVITY_START_CAMERA_APP);
        }
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
            Toast.makeText(CaterpillarViewActivity.this, getText(R.string.pictureToast), Toast.LENGTH_SHORT).show();
            selectedImage = data.getData();
            uploadImage(SELECT_PHOTO);
        }
    }

//    private void createImageGallery() {
//       // File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        File storageDirectory = getApplicationContext().getDir(GALLERY_LOCATION, Context.MODE_PRIVATE);
//        mGalleryFolder = new File(storageDirectory, GALLERY_LOCATION);
//        if (!mGalleryFolder.exists()) {
//            mGalleryFolder.mkdirs();
//        }
//    }

    File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        File image = File.createTempFile(imageFileName, ".jpg", this.getFilesDir());
        mImageFileLocation = image.getAbsolutePath();
        selectedImage = defineUriFromFile(image);
        return image;
    }

    public void uploadImage(final int requestCode) {
        // Checking whether FilePathUri Is empty or not.
        if (selectedImage != null) {
            progressDialog.setTitle(getString(R.string.imageUploading));
            progressDialog.show();
            //create reference to images folder and assing a name to the file that will be uploaded
            imageRef = storageRef.child("images/" + selectedImage.getLastPathSegment());
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
//                            if (requestCode == ACTIVITY_START_CAMERA_APP) {
//                                photoFile.delete();
//                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Hiding the progressDialog.
                            progressDialog.dismiss();
                            // Showing exception erro message.
                            Toast.makeText(CaterpillarViewActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
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
            return FileProvider.getUriForFile(CaterpillarViewActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file);
        } else {
            return Uri.fromFile(file);
        }
    }
}
