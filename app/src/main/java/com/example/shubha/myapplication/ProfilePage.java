package com.example.shubha.myapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class ProfilePage extends AppCompatActivity {

    private Button btnChoose, btnUpload;
    private ImageView imageView;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    private TextView ProfileName, ProfileReg, ProfileDept, ProfileSession, ProfileDate, ProfileDue, RemainingDays;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private LinearLayout Layout1, Layout2, Layout3, Layout4;
    private String Status, DateString;
    static long diffDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        setTitle("Personal Account");

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("loading Profile...");
        progressDialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                progressDialog.dismiss();
            }
        }, 5000); // 3000 milliseconds delay

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        btnChoose=(Button)findViewById(R.id.changepic);
        btnUpload=(Button)findViewById(R.id.uploadpic);
        ProfileName=(TextView)findViewById(R.id.User);
        ProfileReg=(TextView)findViewById(R.id.UserReg);
        ProfileDept=(TextView)findViewById(R.id.UserDept);
        ProfileSession=(TextView)findViewById(R.id.UserSession);
        ProfileDue=(TextView)findViewById(R.id.due);
        ProfileDate=(TextView)findViewById(R.id.date);
        RemainingDays=(TextView)findViewById(R.id.remDay);
        imageView=(ImageView)findViewById(R.id.ProfilePic);

        Layout1=(LinearLayout)findViewById(R.id.layout1);
        Layout2=(LinearLayout)findViewById(R.id.layout2);
        Layout3=(LinearLayout)findViewById(R.id.layout3);
        Layout4=(LinearLayout)findViewById(R.id.layout4);

        Layout1.setVisibility(View.GONE);
        Layout2.setVisibility(View.GONE);
        Layout3.setVisibility(View.GONE);
        Layout4.setVisibility(View.GONE);

        DatabaseReference pRef0 = firebaseDatabase.getReference("user");
        DatabaseReference pRef1 = pRef0.child(firebaseAuth.getUid());

        pRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,String>data=(Map)dataSnapshot.getValue();
                ProfileName.setText(data.get("Name"));
                ProfileReg.setText(data.get("RegNo"));
                ProfileDept.setText(data.get("Dept"));
                ProfileSession.setText(data.get("Session"));
                ProfileDue.setText(data.get("Money"));
                ProfileDate.setText(data.get("Date"));
                DateString=data.get("Date");

                String imageUrl = data.get("Download URL");
                Picasso.get().load(imageUrl).into(imageView);

                Status=data.get("Status");

                if(Status.equals("1"))
                {
                    Layout1.setVisibility(View.VISIBLE);
                }
                if(Status.equals("2"))
                {
                    Layout2.setVisibility(View.VISIBLE);
                }
                if(Status.equals("3"))
                {
                    Layout3.setVisibility(View.VISIBLE);
                }
                if(Status.equals("4"))
                {
                    Layout4.setVisibility(View.VISIBLE);
                }

                try {
                    Date dateObj3 = Calendar.getInstance().getTime();
                    DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
                    String strDate = formatter.format(dateObj3);

                    String date1 = strDate;
                    String date2 = DateString;
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                    Date dateObj1 = sdf.parse(date1);
                    Date dateObj2 = sdf.parse(date2);
                    long diff = dateObj2.getTime() - dateObj1.getTime();
                    diffDay =(diff/86400000);
                    RemainingDays.setText(Integer.toString((int)diffDay));

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfilePage.this, "Could Not Load Profile", Toast.LENGTH_SHORT).show();
            }
        });

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    public void onBackPressed(){

        AlertDialog.Builder alert = new AlertDialog.Builder(ProfilePage.this);
        alert.setTitle("Exiting App");
        alert.setMessage("Are you sure you want to Close The App?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Toast.makeText(ProfilePage.this, "Pressed NO", Toast.LENGTH_SHORT).show();
            }
        });
        alert.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.signoutmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void Pay(View view) {
        Intent intent=new Intent(this,HowToPay.class);
        startActivity(intent);
    }

    public void SignOut(MenuItem item) {

        firebaseAuth.getInstance().signOut();
        finish();
        Intent intent=new Intent(this,Login.class);
        startActivity(intent);
        Toast.makeText(ProfilePage.this, "Sign Out Successfull", Toast.LENGTH_SHORT).show();
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            Toast.makeText(ProfilePage.this, "Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(ProfilePage.this, "Uploaded", Toast.LENGTH_SHORT).show();

                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();
                            String downUrl = String.valueOf(downloadUrl);

                            DatabaseReference Ref0 = firebaseDatabase.getReference("user");
                            DatabaseReference Ref1 = Ref0.child(firebaseAuth.getUid());
                            DatabaseReference Ref2 = Ref1.child("Download URL");
                            Ref2.setValue(downUrl);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ProfilePage.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }
}
