package com.example.vincentale.leafguard_core;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.vincentale.leafguard_core.model.Caterpillar;
import com.example.vincentale.leafguard_core.model.ImageCaterpillar;
import com.example.vincentale.leafguard_core.model.User;
import com.example.vincentale.leafguard_core.model.manager.CaterpillarManager;
import com.example.vincentale.leafguard_core.model.manager.UserManager;
import com.example.vincentale.leafguard_core.util.DatabaseCallback;
import com.example.vincentale.leafguard_core.util.RecyclerTouchListener;
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

import static android.view.View.VISIBLE;
import static com.ipaulpro.afilechooser.utils.FileUtils.getPath;

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
    private Boolean dialogOpened = false;

    private Uri selectedImage;
    private String imageFileName;
    private ImageCaterpillar clickedImage;
    private ProgressBar progressBar;
    private StorageReference storageRef, imageRef;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ImageCaterpillar> list = new ArrayList<>();
    private String mImageFileLocation = "";

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
        progressBar = (ProgressBar) findViewById(R.id.progressBarRecycler);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(CaterpillarViewActivity.this, LinearLayoutManager.HORIZONTAL, false));
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
                                    woundLayout.setVisibility(VISIBLE);
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
                        databaseReference = FirebaseDatabase.getInstance().getReference("images").child(user.getOakId()).child(item.getObservationIndex() + "").child(item.getIndex() + "");
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                list.clear();
                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                    ImageCaterpillar imageUploadInfo = new ImageCaterpillar(postSnapshot.getValue(ImageCaterpillar.class), postSnapshot.getKey());
                                    list.add(imageUploadInfo);
                                }
                                adapter = new RecyclerViewAdapter(getApplicationContext(), list);
                                recyclerView.setAdapter(adapter);
                                recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
                                    @Override
                                    public void onClick(View view, int position) {
                                        if (!dialogOpened) {
                                            dialogOpened = true;
                                            clickedImage = list.get(position);
                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CaterpillarViewActivity.this);
                                            LayoutInflater inflater = getLayoutInflater();
                                            View dialogLayout = inflater.inflate(R.layout.image_dialog, null);
                                            ImageView imageOpened = (ImageView) dialogLayout.findViewById(R.id.imageView);
                                            Glide.with(CaterpillarViewActivity.this).load(clickedImage.getImageURL()).into(imageOpened);
                                            alertDialogBuilder.setView(dialogLayout);
                                            alertDialogBuilder
                                                    .setCancelable(false)
                                                    .setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            StorageReference clickedImageRef = FirebaseStorage.getInstance().getReferenceFromUrl(clickedImage.getImageURL());
                                                            clickedImageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    // File deleted successfully
                                                                    Log.d(TAG, "onSuccess: deleted file");
                                                                    clickedImage.getUploadId();
                                                                    databaseReference.child(clickedImage.getUploadId()).removeValue();
                                                                    //CaterpillarViewActivity.this.recreate();
                                                                    Toast.makeText(getApplicationContext(), " Image delected", Toast.LENGTH_SHORT).show();
                                                                    if (adapter.getItemCount() < 3) {
                                                                        photoButton.setEnabled(true);
                                                                        selectImageButton.setEnabled(true);
                                                                    }
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception exception) {
                                                                    // Uh-oh, an error occurred!
                                                                    Log.d(TAG, "onFailure: did not delete file");
                                                                }
                                                            });
                                                            dialogOpened = false;
                                                        }
                                                    })
                                                    .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            // if this button is clicked, just close
                                                            // the dialog box and do nothing
                                                            dialog.cancel();
                                                            dialogOpened = false;
                                                        }
                                                    });
                                            AlertDialog alertDialog = alertDialogBuilder.create();
                                            alertDialog.show();
                                        }
                                    }

                                    @Override
                                    public void onLongClick(View view, int position) {
                                    }
                                }));
                                progressBar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                if (adapter.getItemCount() >= 3) {
                                    photoButton.setEnabled(false);
                                    selectImageButton.setEnabled(false);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
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
        Intent callCameraApplicationIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (callCameraApplicationIntent.resolveActivity(getPackageManager()) != null) {
            try {
                createImageFile();
                Log.d("bracadabra", selectedImage + "");
                callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImage);
                startActivityForResult(callCameraApplicationIntent, ACTIVITY_START_CAMERA_APP);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "IMAGE_" + timeStamp + "_";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, imageFileName);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, imageFileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        selectedImage = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
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
            mImageFileLocation = getPath(getBaseContext(), selectedImage);
            uploadImage(requestCode);
        }
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
            Toast.makeText(getBaseContext(), getText(R.string.pictureToast), Toast.LENGTH_SHORT).show();
            selectedImage = data.getData();
            uploadImage(requestCode);
        }
    }

    public void uploadImage(final int requestCode) {
        // Checking whether FilePathUri Is empty or not.
        if (selectedImage != null) {
            progressBar.setVisibility(VISIBLE);
            recyclerView.setVisibility(View.GONE);
            // create reference to images folder and assing a name to the file that will be uploaded
            imageRef = storageRef.child("images/" + selectedImage.getLastPathSegment());
            imageRef.putFile(selectedImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), getString(R.string.imageUploadFirebase), Toast.LENGTH_LONG).show();
                            ImageCaterpillar imageUploadInfo = new ImageCaterpillar(taskSnapshot.getDownloadUrl().toString());
                            String ImageUploadId = databaseReference.push().getKey();
                            databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                            if (requestCode == ACTIVITY_START_CAMERA_APP) {
                                File temp = new File(getPath(getBaseContext(), selectedImage));
                                if (temp.exists())
                                    getContentResolver().delete(selectedImage, null, null);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getBaseContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    });
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }
}
