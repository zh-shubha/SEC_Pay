package com.example.shubha.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private Button LoginButton,SignupButton;
    private EditText editText1,editText2;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        setTitle("Login");
        mAuth=FirebaseAuth.getInstance();
        editText1=(EditText)findViewById(R.id.Email);
        editText2=(EditText) findViewById(R.id.Password);
    }

    public void onBackPressed(){

        AlertDialog.Builder alert = new AlertDialog.Builder(Login.this);
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
                Toast.makeText(Login.this, "Pressed NO", Toast.LENGTH_SHORT).show();
            }
        });
        alert.create().show();
    }

    public void Login(View view) {
        String EMAIL = editText1.getText().toString().trim();
        String PASSWORD = editText2.getText().toString().trim();

        if(!EMAIL.isEmpty() && !PASSWORD.isEmpty())
        {
            mAuth.signInWithEmailAndPassword(EMAIL,PASSWORD).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Login.this, "LOGIN Successful", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),ProfilePage.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Login.this, "LOGIN Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            Toast.makeText(Login.this, "Empty Field", Toast.LENGTH_SHORT).show();
        }
    }

    public void signup(View view) {
        Intent intent=new Intent(this,SignupPage.class);
        startActivity(intent);
    }

    public void Admin(View view) {
        Intent intent=new Intent(this,AdminLogin.class);
        startActivity(intent);
    }

    public void Forgot(View view) {
        Intent intent=new Intent(this,Forgot.class);
        startActivity(intent);
    }
}
