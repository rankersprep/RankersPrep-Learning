package com.rankersprep.rankerspreplearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rankersprep.rankerspreplearning.databinding.ActivityRegisterUserBinding;

public class RegisterUser extends AppCompatActivity {

    private FirebaseAuth mAuth;
    boolean[] expertCheck= {false,false,false};
    String expertIn=null;
    private DatabaseReference mDatabase;


    ActivityRegisterUserBinding binding;

    public void setExpertCheck(View view){
        int i = Integer.parseInt(view.getTag().toString());
        TextView[] views = {binding.jeeMains,binding.jeeMainsplusAdvance,binding.NEET};
        for(int j=0;j<3;j++){
            if(j==i){
                views[j].setBackgroundResource(R.drawable.submit_register_user);
                views[j].setTextColor(Color.WHITE);
                expertCheck[j]=true;
                expertIn = views[j].getText().toString();
            }else{
                views[j].setBackgroundResource(R.drawable.rounded_edittext);
                views[j].setTextColor(Color.BLACK);
                expertCheck[j]=false;
            }
        }
    }

    public void registerUser(View view){
        String name = binding.nameEditText.getText().toString();
        String email = binding.emailEditText.getText().toString();
        String contact = binding.numberEditText.getText().toString();
        String AIR = binding.AIREditText.getText().toString();
        String college = binding.collegeNameEditText.getText().toString();
        String language = binding.languagesEditText.getText().toString();
        String password  = binding.passwordEditText.getText().toString();

        if(!name.matches("") && !email.matches("") && !(expertIn ==null) && !contact.matches("") && !AIR.matches("") && !college.matches("") && !language.matches("")  && contact.length()==10){
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information

                                Log.d("Register", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                assert user != null;
                                String userId= user.getUid();
                                mDatabase.child("users").child(userId).setValue(userId);
                                mDatabase.child("users").child(userId).child("name").setValue(name);
                                mDatabase.child("users").child(userId).child("email").setValue(email);
                                mDatabase.child("users").child(userId).child("password").setValue(password);
                                mDatabase.child("users").child(userId).child("contact").setValue(contact);
                                mDatabase.child("users").child(userId).child("AIR").setValue(AIR);
                                mDatabase.child("users").child(userId).child("college").setValue(college);
                                mDatabase.child("users").child(userId).child("language").setValue(language);
                                mDatabase.child("users").child(userId).child("approval").setValue("pending");
                                mDatabase.child("users").child(userId).child("role").setValue("mentor");
                                mDatabase.child("users").child(userId).child("expertIn").setValue(expertIn);

                                FirebaseAuth.getInstance().signOut();

                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("Register", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterUser.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //
                            }
                        }
                    });
        }else{
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

    public void back(View view){
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterUserBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}