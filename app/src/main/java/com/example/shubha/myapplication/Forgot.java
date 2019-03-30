package com.example.shubha.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private TextView send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        setTitle("Reset Password");
        send=(TextView)findViewById(R.id.forgot);

    }

    public void ForgotPasword(View view) {

        String str =send.getText().toString();
        FirebaseAuth.getInstance().sendPasswordResetEmail(str).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Forgot.this, "Mail Sent", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
