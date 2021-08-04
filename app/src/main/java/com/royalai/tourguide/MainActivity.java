package com.royalai.tourguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void register(View view){
        startActivity(new Intent(this, RegisterActionActivity.class));
    }

    public void login(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}