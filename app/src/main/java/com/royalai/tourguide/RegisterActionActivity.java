package com.royalai.tourguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegisterActionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_action);
    }

    public void registerTourist(View view) {
        startActivity(new Intent(this, TouristResigterActivity.class));
    }

    public void registerGuide(View view) {
        startActivity(new Intent(this,TourGuideRegisterActivity.class));
    }

}