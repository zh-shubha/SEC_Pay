package com.example.shubha.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HowToPay extends AppCompatActivity {

    private EditText TransID;
    private Button Send;
    String TrxID,Status;
    private FirebaseAuth mAuth;
    private FirebaseDatabase Database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_pay);
        setTitle("Payment Method");

        mAuth = FirebaseAuth.getInstance();
        Database = FirebaseDatabase.getInstance();
        TransID=(EditText)findViewById(R.id.Transaction);
        Send=(Button)findViewById(R.id.Send);

    }

    public void Send (View view) {

        Date dateObj3 = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        final String strDate = formatter.format(dateObj3);

        TrxID=TransID.getText().toString();
        Status="3";

        if(!TrxID.isEmpty())
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(HowToPay.this);
            alert.setTitle("Sending Trx. ID");
            alert.setIcon(R.drawable.question_mark2);
            alert.setMessage("Are you sure this the Transaction ID?");
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseUser CurrentUser = mAuth.getCurrentUser();
                    DatabaseReference Ref0 = Database.getReference("user");
                    DatabaseReference Ref1 = Ref0.child(CurrentUser.getUid());
                    DatabaseReference Ref2 = Ref1.child("TransactionID");
                    Ref2.setValue(TrxID);
                    DatabaseReference Ref3 = Ref1.child("Status");
                    Ref3.setValue(Status);
                    DatabaseReference Ref4 = Ref1.child("DateSent");
                    Ref4.setValue(strDate);
                    Toast.makeText(HowToPay.this, "TransactionID Sent", Toast.LENGTH_SHORT).show();
                }
            });
            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(HowToPay.this, "Pressed No", Toast.LENGTH_SHORT).show();
                }
            });
            alert.create().show();
        }
        else
        {
            Toast.makeText(HowToPay.this,"Empty Field",Toast.LENGTH_SHORT).show();
        }

    }

    public void onBackPressed(){

        Intent intent=new Intent(this,ProfilePage.class);
        startActivity(intent);
        finish();
    }
}
