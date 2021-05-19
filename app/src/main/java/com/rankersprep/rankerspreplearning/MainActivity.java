package com.rankersprep.rankerspreplearning;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rankersprep.rankerspreplearning.databinding.ActivityRegisterUserBinding;
import com.rankersprep.rankerspreplearning.databinding.ActivitySigninBinding;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CountDownLatch;

public class MainActivity extends AppCompatActivity {
    boolean b = true;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    ActivitySigninBinding binding;
    boolean showpass=false;

    public void register(View view){
        Intent intent = new Intent(this,RegisterUser.class);
        startActivity(intent);
    }

    public void signIn(View view){
        String email = binding.emailID.getText().toString();
        String password = binding.password.getText().toString();
        try {
            binding.CL1.setVisibility(View.INVISIBLE);
            binding.loading.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("loggingIn", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                mDatabase.child("users").child(user.getUid()).child("approval").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                                        try {
                                            String approval = task.getResult().getValue().toString();
                                            if (!approval.matches("approved")) {
                                                Toast.makeText(MainActivity.this, "Account not Approved", Toast.LENGTH_SHORT).show();
                                                FirebaseAuth.getInstance().signOut();
                                                binding.CL1.setVisibility(View.VISIBLE);
                                                binding.loading.setVisibility(View.INVISIBLE);
                                            } else {
                                                mDatabase.child("users").child(user.getUid()).child("role").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                                                        String role = task.getResult().getValue().toString();
                                                        if (role.matches("admin")) {
                                                            Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                                                            startActivity(intent);
                                                        } else if (role.matches("mentor")) {
                                                            //Mentor Activity
                                                        }
                                                    }
                                                });

                                            }
                                        } catch (Exception e) {
                                            Log.w("loggingIn", "signInWithEmail:failure", e);
                                            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                            binding.CL1.setVisibility(View.VISIBLE);
                                            binding.loading.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                });
//                            mDatabase.child("users").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
//                                    DataSnapshot snapshot= task.getResult();
//                                    if(snapshot!=null){
//                                        DataSnapshot snapshot1 = snapshot.child("approval");
//                                        String approval = (String)snapshot1.getValue();
//                                        String role = (String)snapshot.child("role").getValue();
//                                        if(approval.matches("approved")){
//                                            if(role.matches("admin")){
//                                                Intent intent = new Intent(getApplicationContext(),AdminActivity.class);
//                                                startActivity(intent);
//                                            }else if(role.matches("mentor")){
//                                                //Mentor Activity
//                                            }
//                                        }else{
//                                            Toast.makeText(MainActivity.this, "Account not Approved", Toast.LENGTH_SHORT).show();
//                                            FirebaseAuth.getInstance().signOut();
//                                        }
//                                    }
//                                }
//                            });


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("loggingIn", "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                binding.CL1.setVisibility(View.VISIBLE);
                                binding.loading.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show();
            binding.CL1.setVisibility(View.VISIBLE);
            binding.loading.setVisibility(View.INVISIBLE);
        }


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

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

            CountDownTimer countDownTimer = new CountDownTimer(2000,2500) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    Intent intent = new Intent(MainActivity.this,AdminActivity.class);
                    startActivity(intent);
                }
            }.start();
        }

        binding.showPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!showpass){
                    binding.password.setTransformationMethod(null);
                    showpass=true;
                    v.setBackgroundResource(R.drawable.showingpass);
                }else{
                    binding.password.setTransformationMethod(new PasswordTransformationMethod());
                    showpass=false;
                    v.setBackgroundResource(R.drawable.show_pass_icon);
                }
            }
        });

        binding.forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.emailID.getText().toString();

                try {

                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Link to reset password sent on email", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Email not yet registered", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "Enter a valid email", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}