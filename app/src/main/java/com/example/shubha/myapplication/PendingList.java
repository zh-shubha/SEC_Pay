package com.example.shubha.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class PendingList extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    ListView listview1,listView2;

    private final int PICK_IMAGE_REQUEST = 71;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_list);

        setTitle("Pending List");
        listview1=(ListView)findViewById(R.id.Listview1);
        listView2=(ListView)findViewById(R.id.Listview2);
        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,R.layout.sample,R.id.sample);
        listview1.setAdapter(adapter1);
        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,R.layout.sample2,R.id.sample2);
        listView2.setAdapter(adapter2);

        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ds1 = firebaseDatabase.getReference("user");
        ds1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    Map<String,String> data=(Map)ds.getValue();
                    String Reg = data.get("RegNo");
                    String TrxID = data.get("TransactionID");
                    String Status = data.get("Status");
                    if(Status.equals("3"))
                    {
                        adapter1.add(Reg);
                        adapter2.add(TrxID);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

