package com.rankersprep.rankerspreplearning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class MainActivity extends AppCompatActivity {
    boolean b = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        View view = findViewById(R.id.logoMain);


        CountDownTimer countDownTimer = new CountDownTimer(2000,200) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(millisUntilFinished<=1500 && b){
                    //view.animate().alpha(0).rotationBy(1080).setDuration(1500);
                    view.animate().y(116.7f).scaleX((float) (250.0/300)).scaleY((float) (54.7/63.0)).setDuration(1400);
                    b=false;

                }
            }

            @Override
            public void onFinish() {

                ConstraintLayout view1= (ConstraintLayout) findViewById(R.id.CL1);
                view1.animate().alpha(1).setDuration(1000);


            }
        }.start();

    }
}