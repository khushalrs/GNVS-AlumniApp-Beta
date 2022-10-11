package com.GNVS.AlumniApp;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class ProfileFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference users = database.getReference().child("users");
    Context context;
    Button messageButton;
    View appbar;
    boolean redirected = false;
    RecyclerView profilePosts;
    ArrayList<PostList> postList = new ArrayList<>();
    ArrayList<String>likeList = new ArrayList<>();
    ProfileAdapter profileAdapter;
    String name, userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
    ImageButton signout;
    ImageView profilePic;
    TextView nameText, emailText, batchText, jobText, companyText;
    Uri mImageCaptureUri, downloadedImage;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString("userId");
            redirected = true;
        }
        queryData();
        postsData();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileAdapter = new ProfileAdapter(context, postList, redirected);
        profilePosts.setLayoutManager(new LinearLayoutManager(context));
        profilePosts.setAdapter(profileAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.fragment_profile, container, false);
        context = Objects.requireNonNull(container).getContext();
        appbar = V.findViewById(R.id.appbar);
        signout = appbar.findViewById(R.id.messageBtn);
        signout.setImageResource(R.drawable.ic_baseline_logout_24);
        nameText = V.findViewById(R.id.nameText);
        emailText = V.findViewById(R.id.emailText);
        batchText = V.findViewById(R.id.batchText);
        jobText = V.findViewById(R.id.job_Text);
        companyText = V.findViewById(R.id.company_Text);
        messageButton = V.findViewById(R.id.messageButton);
        profilePosts = V.findViewById(R.id.profile_recycler);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked();
            }
        });
        if(!Objects.equals(userId, FirebaseAuth.getInstance().getCurrentUser().getUid())){
            messageButton.setText("Message");
            messageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    messageCall();
                }
            });
        }
        else{
            messageButton.setText("Edit Profile");
            messageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Edit_Profile1 ep = new Edit_Profile1();
                    getParentFragmentManager().beginTransaction().replace(R.id.frame_layout, ep).commit();
                }
            });
        }
        profilePic = V.findViewById(R.id.profilePic);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImage();
            }
        });
        return V;
    }

    public void messageCall(){
        Intent i = new Intent(context, ChatActivity.class);
        i.putExtra("user", userId);
        i.putExtra("name", name);
        startActivity(i);
    }


    public void buttonClicked(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(context,SignInActivity.class));
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
                        Log.i("Profile activity", "launchsomeactivity");
                        doCrop();
                    }
                }
            });


    ActivityResultLauncher<Intent> uCropActivityResult =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->{
                if (result.getData() != null && result.getResultCode() == RESULT_OK) {
                    Log.i("Profile pic upload", "True");
                    mImageCaptureUri = UCrop.getOutput(result.getData());
                    profilePic.setImageURI(mImageCaptureUri);
                    upload();
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
        option.withMaxResultSize(200, 200);
        option.withAspectRatio(1, 1);
        return option;
    }

    public void upload(){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        // Defining the child of storageReference
        StorageReference ref = FirebaseStorage.getInstance().getReference().child("images/" + UUID.randomUUID().toString());

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
                                    users.child(userId).child("propic").setValue(downloadedImage);
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
            Toast.makeText(context, "Please try again", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    public void queryData() {
        users.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Log.i("Profile Path", snapshot.getKey());
                User user = snapshot.getValue(User.class);
                nameText.setText(user.getName());
                emailText.setText(user.getEmail());
                batchText.setText(user.getBatch());
                jobText.setText(user.getJob());
                companyText.setText(user.getCompany());
                name = user.getName();
                if(!Objects.equals(user.getPropic(), "")){
                    Picasso.get().load(user.getPropic()).fit().centerCrop().into(profilePic);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void postsData(){
        Query q = users.child(userId).child("posts").orderByChild("dateTime");
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PostList m = dataSnapshot.getValue(PostList.class);
                    likeList.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("likeId").getChildren()) {
                        String val = dataSnapshot1.getKey();
                        likeList.add(val);
                    }
                    m.addLikeId(likeList);
                    postList.add(m);
                }
                profileAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}