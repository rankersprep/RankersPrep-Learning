package com.rankersprep.rankerspreplearning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegisterUser extends AppCompatActivity {

    public void back(View view){
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}