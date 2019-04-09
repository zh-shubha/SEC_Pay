package com.example.shubha.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class SignupPage extends AppCompatActivity {

    private EditText name,reg,dept,session,email,password1,password2;
    private String empty;
    private CheckBox Cbox;
    private Button submit;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase Database;
    public String namecheck,regcheck, deptcheck, sessioncheck, emailcheck, pass1check, pass2check, regcheck1,status,dURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        setTitle("Signup");

        firebaseAuth = FirebaseAuth.getInstance();
        Database = FirebaseDatabase.getInstance();
        name=(EditText)findViewById(R.id.name);
        reg=(EditText)findViewById(R.id.reg);
        dept=(EditText)findViewById(R.id.dept);
        session=(EditText)findViewById(R.id.session);
        email=(EditText)findViewById(R.id.email);
        password1=(EditText)findViewById(R.id.password1);
        password2=(EditText)findViewById(R.id.password2);

        Cbox=(CheckBox)findViewById(R.id.checkBox);
        submit=(Button)findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                namecheck=name.getText().toString();
                regcheck=reg.getText().toString();
                regcheck1=reg.getText().toString();
                deptcheck=dept.getText().toString();
                sessioncheck=session.getText().toString();
                emailcheck=email.getText().toString();
                pass1check=password1.getText().toString();
                pass2check=password2.getText().toString();
                empty= "Null";
                status="1";
                dURL="https://firebasestorage.googleapis.com/v0/b/myapplication2-942c9.appspot.com/o/images%2Fa8b228b8-1c7c-4738-a892-888e99c72d3d?alt=media&token=21a70df8-87e8-45ba-b8ae-cad91550d22f";
                    if(!TextUtils.isEmpty(namecheck) && !TextUtils.isEmpty(regcheck) && !TextUtils.isEmpty(deptcheck) &&
                            !TextUtils.isEmpty(sessioncheck) && !TextUtils.isEmpty(emailcheck) && !TextUtils.isEmpty(pass1check) &&
                            !TextUtils.isEmpty(pass2check) && Cbox.isChecked())
                    {
                        if(pass1check.equals(pass2check)){
                            AlertDialog.Builder alert = new AlertDialog.Builder(SignupPage.this);
                            alert.setTitle("Creating New Account");
                            alert.setIcon(R.drawable.question_mark2);
                            alert.setMessage("Are you sure you want to Create New Account?");
                            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    firebaseAuth.createUserWithEmailAndPassword(emailcheck,pass1check).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                DatabaseReference Ref0 = Database.getReference("user");
                                                DatabaseReference Ref1 = Ref0.child(firebaseAuth.getUid());
                                                DatabaseReference Ref2 = Ref1.child("Name");
                                                Ref2.setValue(namecheck);
                                                DatabaseReference Ref3 = Ref1.child("RegNo");
                                                Ref3.setValue(regcheck);
                                                DatabaseReference Ref4 = Ref1.child("Dept");
                                                Ref4.setValue(deptcheck);
                                                DatabaseReference Ref5 = Ref1.child("Session");
                                                Ref5.setValue(sessioncheck);
                                                DatabaseReference Ref6 = Ref1.child("Money");
                                                Ref6.setValue(empty);
                                                DatabaseReference Ref7 = Ref1.child("Date");
                                                Ref7.setValue(empty);
                                                DatabaseReference Ref8 = Ref1.child("TransactionID");
                                                Ref8.setValue(empty);
                                                DatabaseReference Ref9 = Ref1.child("Status");
                                                Ref9.setValue(status);
                                                DatabaseReference Ref10 = Ref1.child("Download URL");
                                                Ref10.setValue(dURL);
                                                DatabaseReference Ref11 = Ref1.child("DateSent");
                                                Ref11.setValue(empty);

                                                Toast.makeText(SignupPage.this, "SIGN UP Successful", Toast.LENGTH_SHORT).show();

                                                Intent intent=new Intent(getApplicationContext(),Login.class);
                                                startActivity(intent);
                                                finish();;
                                            } else {
                                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                                    Toast.makeText(SignupPage.this, "You Already Registered", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(SignupPage.this, "Error : "+task.getException().getMessage(),Toast.LENGTH_SHORT).show(); }
                                            }
                                        }

                                    });
                                }
                            });
                            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(SignupPage.this, "Pressed NO", Toast.LENGTH_SHORT).show();
                                }
                            });
                            alert.create().show();
                        }
                        else {
                            Toast.makeText(SignupPage.this, "Passwords didn't Match", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(!TextUtils.isEmpty(namecheck) && !TextUtils.isEmpty(regcheck) && !TextUtils.isEmpty(deptcheck) &&
                        !TextUtils.isEmpty(sessioncheck) && !TextUtils.isEmpty(emailcheck) && !TextUtils.isEmpty(pass1check) &&
                        !TextUtils.isEmpty(pass2check) && !Cbox.isChecked())
                    {
                        Toast.makeText(SignupPage.this, "You Have to accept terms and conditions", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(SignupPage.this, "Empty Field", Toast.LENGTH_SHORT).show();
                    }
            }
        });

    }
}
