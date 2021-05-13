package com.rankersprep.rankerspreplearning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.rankersprep.rankerspreplearning.databinding.ActivityRegisterUserBinding;
import com.rankersprep.rankerspreplearning.databinding.ActivitySigninBinding;

public class MainActivity extends AppCompatActivity {
    boolean b = true;

    ActivitySigninBinding binding;

    public void register(View view){
        Intent intent = new Intent(this,RegisterUser.class);
        startActivity(intent);
    }

    public void signIn(View view){
        Intent intent = new Intent(this,AdminActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        View view2 = binding.getRoot();
        setContentView(view2);

        View view = findViewById(R.id.logoMain);

        //Animation at start
        if(b) {
            CountDownTimer countDownTimer = new CountDownTimer(2000, 200) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (millisUntilFinished <= 1500 && b) {
                        //view.animate().alpha(0).rotationBy(1080).setDuration(1500);
                        view.animate().y(116.7f).scaleX((float) (250.0 / 300)).scaleY((float) (54.7 / 63.0)).setDuration(1400);
                        b = false;

                    }
                }

                @Override
                public void onFinish() {

                    ConstraintLayout view1 = (ConstraintLayout) findViewById(R.id.CL1);
                    view1.animate().alpha(1).setDuration(1000);


                }
            }.start();
        }

    }

}