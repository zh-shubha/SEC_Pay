package com.example.shubha.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class FailedList2 extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failed_list2);

        setTitle("Failed List");
        listview=(ListView)findViewById(R.id.listview);
        final ArrayList<CustomClass> arrayList = new ArrayList<>();

        CustomAdaptor adaptor = new CustomAdaptor(this,arrayList);
        listview.setAdapter(adaptor);
        //arrayList.add(new CustomClass("Shuvo","34567","by7vu","4000","22/09/19"));

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
                    String Name = data.get("Name");
                    String Money = data.get("Money");
                    String Date = data.get("DateSent");
                    if(Status.equals("4"))
                    {
                        arrayList.add(new CustomClass(Name,Reg,TrxID,Money,Date));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
