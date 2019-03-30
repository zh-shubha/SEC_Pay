package com.example.shubha.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AdminLogin extends AppCompatActivity {

    private Button Login;
    private EditText editText1,editText2;
    private String Admin1Email, Admin1Password, Admin2Email, Admin2Password;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        firebaseDatabase = FirebaseDatabase.getInstance();
        setTitle("Admin Login");
        editText1 = (EditText) findViewById(R.id.AdminEmail);
        editText2 = (EditText) findViewById(R.id.AdminPassword);
        Login =(Button) findViewById(R.id.button);

        DatabaseReference pRef0 = firebaseDatabase.getReference("admin/admin1");
        pRef0.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,String> data=(Map)dataSnapshot.getValue();
                Admin1Email =data.get("Email");
                Admin1Password =data.get("Password");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference pRef1 = firebaseDatabase.getReference("admin/admin2");
        pRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,String> data=(Map)dataSnapshot.getValue();
                Admin2Email =data.get("Email");
                Admin2Password =data.get("Password");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email =editText1.getText().toString();
                String Password =editText2.getText().toString();
                if(!Email.isEmpty() && !Password.isEmpty())
                {
                    if( (Email.equals(Admin1Email))&& (Password.equals(Admin1Password)) || (Email.equals(Admin2Email)) && (Password.equals(Admin2Password)))
                    {
                        Toast.makeText(AdminLogin.this, "Admin LOGIN Successful", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),AdminProfile.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(AdminLogin.this, "Admin LOGIN Failed", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(AdminLogin.this, "Empty Field", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
