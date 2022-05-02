package com.example.wappnea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;

//public class LogIn extends AppCompatActivity {
public class LogIn extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        Button but1 = findViewById(R.id.BtLogIn1);
        Button but2 = findViewById(R.id.BtLogIn2);

        but1.setOnClickListener(this);
        but2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent_LogIn = new Intent(LogIn.this, MainActivity.class);
        switch (v.getId()) {
            case R.id.BtLogIn1:
                startActivity(intent_LogIn);
                break;
            case R.id.BtLogIn2:
                startActivity(intent_LogIn);
                break;
        }
    }
}
