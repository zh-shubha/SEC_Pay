package com.example.shubha.myapplication;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class AdminProfile extends AppCompatActivity {

    private EditText RegNo,Money;
    private TextView Date;
    private Button SetDate;
    private String UpdateReg, GetReg, UpdateDept, GetDept, UpdateSession, GetSession, UpdateMoney, UpdateDate, GetStatus;
    private String string, strDate, SelectedDept, SelectedSession;
    private Spinner spinner_dept,Spinner_Session;
    String[] DeptList,SessionList;

    private int day, month, year, check;
    private Date dateObj;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference().child("user");
    private DatabaseReference databaseReference1 = firebaseDatabase.getReference().child("user");
    private DatabaseReference databaseReference2 = firebaseDatabase.getReference().child("user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        setTitle("Admin Control");
        check=0;
        RegNo=(EditText)findViewById(R.id.UpdateReg);
        Money=(EditText)findViewById(R.id.UpdateMoney);
        Date=(TextView) findViewById(R.id.UpdateDate);
        SetDate=(Button) findViewById(R.id.datePicker);
        spinner_dept=(Spinner)findViewById(R.id.spinnerDept);
        Spinner_Session=(Spinner)findViewById(R.id.spinnerSession);

        DeptList = getResources().getStringArray(R.array.Depts);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,R.layout.spinner_sample,R.id.spinnerSample,DeptList);
        spinner_dept.setAdapter(adapter1);
        spinner_dept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                SelectedDept=spinner_dept.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        SessionList = getResources().getStringArray(R.array.Sessions);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,R.layout.spinner_sample,R.id.spinnerSample,SessionList);
        Spinner_Session.setAdapter(adapter2);
        Spinner_Session.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                SelectedSession=Spinner_Session.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        SetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SetDatePickerDialog();
            }
        });
    }

    public void SendPayInfo(View view) {

        UpdateDept=SelectedDept;
        UpdateSession=SelectedSession;
        UpdateMoney=Money.getText().toString().trim();
        UpdateDate=strDate;

        if(!UpdateDept.isEmpty() && !UpdateSession.isEmpty() && !UpdateMoney.isEmpty() && check==1)
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(AdminProfile.this);
            alert.setTitle("Payment Info Update");
            alert.setIcon(R.drawable.question_mark);
            alert.setMessage("Are you sure you want to send new payment information?");
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot2) {
                            //Get map of users in datasnapshot
                            Map<String,Object> mymap2 =(Map<String,Object>) dataSnapshot2.getValue();
                            for (Map.Entry<String, Object> entry : mymap2.entrySet()){
                                //Get user map
                                Map singleUser2 = (Map) entry.getValue();
                                //Get Specified field and append to list
                                GetDept =(String)singleUser2.get("Dept");
                                GetSession =(String)singleUser2.get("Session");
                                if(UpdateDept.contains(GetDept) && UpdateSession.contains(GetSession))
                                {
                                    singleUser2.put("Money",UpdateMoney);
                                    singleUser2.put("Date",UpdateDate);
                                    singleUser2.put("Status","2");
                                    DatabaseReference rf2 = databaseReference1.child(entry.getKey());
                                    rf2.updateChildren(singleUser2);
                                }
                            }
                            Toast.makeText(AdminProfile.this, "Payment Information Sent", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //handle databaseError
                            Toast.makeText(AdminProfile.this, "Failed to Sent", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(AdminProfile.this, "Pressed NO", Toast.LENGTH_SHORT).show();
                }
            });
            alert.create().show();
        }
        else
        {
            Toast.makeText(AdminProfile.this, "Empty Fields", Toast.LENGTH_SHORT).show();
        }
    }

    public void PaymentDone(View view) {

        UpdateReg=RegNo.getText().toString().trim();
        if(!UpdateReg.isEmpty()){
            AlertDialog.Builder alert = new AlertDialog.Builder(AdminProfile.this);
            alert.setTitle("Payment Verification");
            alert.setIcon(R.drawable.question_mark);
            alert.setMessage("Are you sure you want to verify this student's Payment?");
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Get map of users in datasnapshot
                            Map<String,Object> mymap =(Map<String,Object>) dataSnapshot.getValue();
                            for (Map.Entry<String, Object> entry : mymap.entrySet()){
                                //Get user map
                                Map singleUser = (Map) entry.getValue();
                                //Get Specified field and append to list
                                GetReg =(String)singleUser.get("RegNo");
                                GetStatus = (String)singleUser.get("Status");
                                if(UpdateReg.contains(GetReg))
                                {
                                    if(GetStatus.equals("1"))
                                    {
                                        Toast.makeText(AdminProfile.this, "EROR : This Student Has Nothing Due !!", Toast.LENGTH_SHORT).show();
                                    }
                                    if(GetStatus.equals("2"))
                                    {
                                        Toast.makeText(AdminProfile.this, "EROR : This Student Has Not Payed Yet !!", Toast.LENGTH_SHORT).show();
                                    }
                                    if(GetStatus.equals("3") || GetStatus.equals("4"))
                                    {
                                        singleUser.put("Status","1");
                                        singleUser.put("TransactionID","Null");
                                        singleUser.put("Money","Null");
                                        singleUser.put("Date","Null");
                                        singleUser.put("DateSent","Null");
                                        DatabaseReference rf = databaseReference.child(entry.getKey());
                                        rf.updateChildren(singleUser);
                                        Toast.makeText(AdminProfile.this, "Verification Completed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(AdminProfile.this, "Verification NOT Completed", Toast.LENGTH_SHORT).show();
                            }
                    });
                }
            });
            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(AdminProfile.this, "Pressed NO", Toast.LENGTH_SHORT).show();
                }
            });
            alert.create().show();

        }
        else
        {
            Toast.makeText(AdminProfile.this, "Put Registration No.", Toast.LENGTH_SHORT).show();
        }
    }

    public void VisitOffice(View view) {

        UpdateReg=RegNo.getText().toString().trim();
        if(!UpdateReg.isEmpty())
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(AdminProfile.this);
            alert.setTitle("Payment Verification");
            alert.setIcon(R.drawable.question_mark);
            alert.setMessage("Want to move to Failed List?");
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Map<String,Object> mymap3 =(Map<String,Object>) dataSnapshot.getValue();
                            for (Map.Entry<String, Object> entry : mymap3.entrySet()){
                                Map singleUser3 = (Map) entry.getValue();
                                GetReg =(String)singleUser3.get("RegNo");
                                GetStatus=(String)singleUser3.get("Status");
                                if(UpdateReg.contains(GetReg))
                                {
                                    if(GetStatus.equals("1"))
                                    {
                                        Toast.makeText(AdminProfile.this, "Error: This student has nothing due Yet", Toast.LENGTH_SHORT).show();
                                    }
                                    if(GetStatus.equals("2"))
                                    {
                                        Toast.makeText(AdminProfile.this, "Error: This student has not paid yet", Toast.LENGTH_SHORT).show();
                                    }
                                    if(GetStatus.equals("3"))
                                    {
                                        singleUser3.put("Status","4");
                                        DatabaseReference rf = databaseReference2.child(entry.getKey());
                                        rf.updateChildren(singleUser3);
                                        Toast.makeText(AdminProfile.this, "Information Updated", Toast.LENGTH_SHORT).show();
                                    }
                                    if(GetStatus.equals("4"))
                                    {
                                        Toast.makeText(AdminProfile.this, "Error: This student is already on failed list", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(AdminProfile.this, "Information NOT Updated", Toast.LENGTH_SHORT).show();
                            }
                    });
                }
            });
            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(AdminProfile.this, "Pressed NO", Toast.LENGTH_SHORT).show();
                }
            });
            alert.create().show();
        }
        else
        {
            Toast.makeText(AdminProfile.this, "Put Registration No.", Toast.LENGTH_SHORT).show();
        }
    }

    public void gotopendinglist(View view) {
        Intent intent=new Intent(this, PendingList2.class);
        startActivity(intent);
    }

    public void GotoFailedList(View view) {
        Intent intent=new Intent(this, FailedList2.class);
        startActivity(intent);
    }

    public void DatePicker(View view) {

        SetDatePickerDialog();
    }

    public  void SetDatePickerDialog(){

        calendar = Calendar.getInstance();
        day= calendar.get(Calendar.DAY_OF_MONTH);
        month= calendar.get(Calendar.MONTH);
        year= calendar.get(Calendar.YEAR);

        datePickerDialog = new DatePickerDialog(AdminProfile.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Date.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                check=1;
                string=Date.getText().toString();

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                    dateObj = sdf.parse(string);
                    DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
                    strDate = formatter.format(dateObj);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        },year,month,day);
        datePickerDialog.show();
    }
}