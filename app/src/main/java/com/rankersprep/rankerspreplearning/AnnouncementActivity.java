package com.rankersprep.rankerspreplearning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.rankersprep.rankerspreplearning.databinding.ActivityAnnouncementBinding;

import java.util.HashMap;

public class AnnouncementActivity extends AppCompatActivity {

    ActivityAnnouncementBinding binding;
    private DatabaseReference mDatabase;

    Boolean[] b = {false,false};

    public void sendAnnouncement(View view){
        String title = binding.titleAnnouncement.getText().toString();
        String description = binding.discriptionAnnouncement.getText().toString();
        Boolean urgent = binding.switch1.isChecked();
        long millis = System.currentTimeMillis();
        if (!title.matches("") && !description.matches("") && !(!b[0] && !b[1])) {
            String key = mDatabase.child("announcements").push().getKey();
            mDatabase.child("announcements").child(key).child("title").setValue(title);
            mDatabase.child("announcements").child(key).child("description").setValue(description);
            mDatabase.child("announcements").child(key).child("urgent").setValue(String.valueOf(urgent));
            mDatabase.child("announcements").child(key).child("sendToMentors").setValue(String.valueOf(b[0]));
            mDatabase.child("announcements").child(key).child("sendToMentees").setValue(String.valueOf(b[1]));
            mDatabase.child("announcements").child(key).child("time").setValue(String.valueOf(millis));
            Intent intent = new Intent(this, AdminActivity.class);
            intent.putExtra("showSnackBar",true);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Fill all Fields", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAnnouncementBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        binding.mentorsAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView view = (TextView)v;
                if(!b[0]) {
                    view.setBackgroundResource(R.drawable.submit_register_user);
                    view.setTextColor(getResources().getColor(R.color.white));
                    b[0]=true;
                }else{
                    view.setBackgroundResource(R.drawable.rounded_edittext);
                    view.setTextColor(getResources().getColor(R.color.black));
                    b[0]=false;
                }

            }
        });

        binding.menteesAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView view = (TextView)v;
                if(!b[1]) {
                    view.setBackgroundResource(R.drawable.submit_register_user);
                    view.setTextColor(getResources().getColor(R.color.white));
                    b[1]=true;
                }else{
                    view.setBackgroundResource(R.drawable.rounded_edittext);
                    view.setTextColor(getResources().getColor(R.color.black));
                    b[1]=false;
                }

            }
        });

    }
}