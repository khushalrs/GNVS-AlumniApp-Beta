package com.GNVS.AlumniApp;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NewPostFragment extends Fragment {
    RelativeLayout addImage;
    EditText postDescription;
    FirebaseDatabase database;
    DatabaseReference ref1, ref2;
    FirebaseUser user;
    Context context;
    ImageView imageView;
    FirebaseStorage storage;
    StorageReference storageReference;
    SharedPreferences sharedPreferences;
    Uri mImageCaptureUri, downloadedImage;
    View appbar;
    ImageButton messageButton;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    public NewPostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        ref1 = database.getReference().child("posts");
        user = FirebaseAuth.getInstance().getCurrentUser();
        ref2 = database.getReference().child("users").child(user.getUid()).child("posts");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        sharedPreferences = getActivity().getSharedPreferences("ThisUser", Context.MODE_PRIVATE);
    }

    private void getImage(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (i.resolveActivity(getActivity().getPackageManager()) != null) {
            launchSomeActivity.launch(i);
        }
    }
    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        mImageCaptureUri = selectedImageUri;
                        doCrop();
                    }
                }
            });


    ActivityResultLauncher<Intent> uCropActivityResult =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->{
                if (result.getData() != null && result.getResultCode() == RESULT_OK) {
                    mImageCaptureUri = UCrop.getOutput(result.getData());
                    imageView.setImageURI(mImageCaptureUri);
                }
    });

    public static Intent getUcropIntent(Context context , Uri mediaScannerUri , Uri destination , UCrop.Options options ) {
        Intent intent = new Intent();
        Bundle uCropBundle = options.getOptionBundle();
        uCropBundle.putParcelable(UCrop.EXTRA_INPUT_URI, mediaScannerUri);
        uCropBundle.putParcelable(UCrop.EXTRA_OUTPUT_URI, destination);
        intent.putExtras(options.getOptionBundle());
        intent.setClass(context , UCropActivity.class);
        return intent;
    }

    public void doCrop(){
        String destin = "SampleCropImg";
        destin += ".jpg";
        uCropActivityResult.launch(getUcropIntent(context, mImageCaptureUri,
                Uri.fromFile(new File(getActivity().getCacheDir(), destin)),
                options()));
    }

    private UCrop.Options options(){
        UCrop.Options option = new UCrop.Options();
        option.setHideBottomControls(false);
        option.setFreeStyleCropEnabled(true);
        option.withMaxResultSize(1080, 566);
        option.withAspectRatio(16, 9);
        return option;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImage();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();
        View v = inflater.inflate(R.layout.fragment_new_post, container, false);
        addImage = v.findViewById(R.id.postImage);
        imageView = v.findViewById(R.id.postImageView);
        postDescription = v.findViewById(R.id.postDescription);
        appbar = v.findViewById(R.id.appbar);
        messageButton = appbar.findViewById(R.id.messageBtn);
        messageButton.setImageResource(R.drawable.ic_baseline_send_24);
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }
        });
        return v;
    }

    public void upload(){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        // Defining the child of storageReference
        StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());

        if(mImageCaptureUri!=null) {
            // adding listeners on upload
            // or failure of image
            ref.putFile(mImageCaptureUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadedImage = uri;
                                    Toast.makeText(context, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                    String description = postDescription.getText().toString();
                                    Calendar calendar = Calendar.getInstance();
                                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                                    String strTime = timeFormat.format(calendar.getTime());
                                    String strDate = new SimpleDateFormat("MMM d", Locale.getDefault()).format(new Date());
                                    String dateTime = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                                    dateTime = dateTime + " " + strTime;
                                    DatabaseReference r1 = ref1.push();
                                    DatabaseReference r2 = ref2.push();
                                    PostList p = new PostList(strDate, dateTime, strTime, downloadedImage.toString(), description, user.getUid(),
                                            sharedPreferences.getString("UserName", ""), r1.toString(), r2.toString());
                                    r1.setValue(p);
                                    r2.setValue(p);
                                    postDescription.setText("");
                                    imageView.setImageDrawable(null);
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            String description = postDescription.getText().toString();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String strTime = timeFormat.format(calendar.getTime());
            String strDate = new SimpleDateFormat("MMM d", Locale.getDefault()).format(new Date());
            String dateTime = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            dateTime = dateTime + " " + strTime;
            DatabaseReference r1 = ref1.push();
            DatabaseReference r2 = ref2.push();
            PostList p = new PostList(strDate, dateTime, strTime, "", description, user.getUid(),
                    sharedPreferences.getString("UserName", ""), r1.toString(), r2.toString());
            r1.setValue(p);
            r2.setValue(p);
            r1.child("likeId").setValue("");
            r2.child("likeId").setValue("");
            postDescription.setText("");
            imageView.setImageDrawable(null);
            progressDialog.dismiss();
        }
    }

}