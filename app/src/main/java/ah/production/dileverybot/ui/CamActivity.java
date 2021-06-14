package ah.production.dileverybot.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import ah.production.dileverybot.R;

public class CamActivity extends AppCompatActivity implements View.OnClickListener {

    private View v_camera;
    private View v_gallery;
    private ImageView iv_camera;
    private ImageView iv_galler;
    String encodedImage;
    Bitmap bitmap;
    File tempFile_upload;
    FirebaseAuth firebaseAuth;
    String userId= "";
    FirebaseStorage storage;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);

        firebaseAuth  = FirebaseAuth.getInstance();
        userId = firebaseAuth.getUid();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        v_camera = findViewById(R.id.btn_camera);
        v_gallery = findViewById(R.id.btn_gallery);
        iv_camera = findViewById(R.id.iv_camera);
        iv_galler = findViewById(R.id.iv_gallery);

        v_camera.setOnClickListener(this);
        v_gallery.setOnClickListener(this);
        iv_camera.setOnClickListener(this);
        iv_galler.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.
                PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest
                    .permission.WRITE_EXTERNAL_STORAGE}, 0);
        }


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.btn_camera:
                takePhoto(v);
                break;

            case R.id.btn_gallery:
                uploadPhoto(v);
                break;

            case R.id.iv_camera:
                takePhoto(v);
                break;

            case R.id.iv_gallery:
                uploadPhoto(v);
                break;

        }
    }

    private void uploadPhoto(View v) {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 1);
    }

    private void takePhoto(View v) {
        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri path = data.getData();
                        bitmap = (Bitmap) data.getExtras().get("data");
                        File tempFile = new File(Environment.getExternalStorageDirectory() + File.separator + "ABCDDDD.jpg");
                        try {
                            tempFile.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //convert
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] b = baos.toByteArray();
                        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                       // img_profile.setImageBitmap(bitmap);
                       // img_profile.setVisibility(View.VISIBLE);
                        tempFile_upload = tempFile;

                        String path1 = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), bitmap, "Title", null);
                        Log.d("TAG", "onActivityResult: " + Uri.parse(path1));
                        showUploadImage(Uri.parse(path1), this);

                        byte[] bitmapdata = baos.toByteArray();
                        /*try {
                          //  FileUtils.writeByteArrayToFile(tempFile, bitmapdata);
                            //showProgressDialog("Image Uploading...");
                            Log.d("TAG", "onActivityResult: " + tempFile);
                        } catch (IOException e) {
                            throw new RuntimeException("Could not write to file", e);
                        }*/
                    }
                    break;
                case 1:
                    ActivityCompat.requestPermissions(CamActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    if (resultCode == RESULT_OK && data != null) {
                        Uri path = data.getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), path);
                            File tempFile = new File(Environment.getExternalStorageDirectory() + File.separator + "ABCDDDD.jpg");
                            try {
                                tempFile.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //convert
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] b = baos.toByteArray();
                            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

                           /* img_profile.setImageBitmap(bitmap);
                            img_profile.setVisibility(View.VISIBLE);*/
                            showUploadImage(path, this);
                            tempFile_upload = tempFile;

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }

    private void showUploadImage(Uri path, Context context) {

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog_imageupload);

        Button btn_upload = dialog.findViewById(R.id.btn_upload);
        ImageView iv_preview = dialog.findViewById(R.id.iv_imagepreview);
        ImageView iv_close = dialog.findViewById(R.id.iv_close);

        iv_preview.setImageBitmap(bitmap);

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.getWindow().setLayout((4 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgressDialog progressDialog = new ProgressDialog(CamActivity.this);
                progressDialog.setMessage("Uploading...");
                progressDialog.show();

                Date currentTime = Calendar.getInstance().getTime();

                StorageReference storageReferenceRef = storageRef.child("images/"+userId+"/" + userId + "-"+currentTime);
                storageReferenceRef.putFile(path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                });

                storageReferenceRef.putFile(path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        progressDialog.dismiss();
                        Log.d("TAG", "onSuccess: Image URL " + taskSnapshot);
                        //String uploadedURI = taskSnapshot.getDownloadUrl().toString();
                        dialog.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double pp = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        progressDialog.setMessage("Progress  " + (int)pp + "%");
                    }
                });

            }
        });

    }

}