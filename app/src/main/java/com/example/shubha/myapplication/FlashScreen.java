package com.example.shubha.myapplication;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class FlashScreen extends AppCompatActivity {

    private ProgressBar progressBar;
    private int progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_screen);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        progressBar=(ProgressBar)findViewById(R.id.ProgressBar);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
                appstart();
            }
        });
        thread.start();
    }

    public void doWork(){

        for (progress=0;progress<=100;progress=progress+20)
        {
            try {
                Thread.sleep(500);
                progressBar.setProgress(progress);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void appstart(){
        Intent intent=new Intent(this,Login.class);
        startActivity(intent);
        finish();
    }
}
