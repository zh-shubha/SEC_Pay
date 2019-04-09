package com.example.shubha.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.os.Handler;
import android.view.MenuInflater;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class PendingList2 extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_list2);

        setTitle("Pending List");
        listview=(ListView)findViewById(R.id.listview);
        final ArrayList<CustomClass> arrayList = new ArrayList<>();
        CustomAdaptor adaptor = new CustomAdaptor(this,arrayList);
        listview.setAdapter(adaptor);

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
                    if(Status.equals("3"))
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.refreshmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.RefreshID)
        {
            Toast.makeText(PendingList2.this,"Refreshed",Toast.LENGTH_SHORT).show();
            recreate();
        }
        return super.onOptionsItemSelected(item);
    }
}
