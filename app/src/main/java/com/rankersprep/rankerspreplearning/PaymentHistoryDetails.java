package com.rankersprep.rankerspreplearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rankersprep.rankerspreplearning.databinding.ActivityPaymentDetailBinding;
import com.rankersprep.rankerspreplearning.databinding.ActivityPaymentHistoryDetailsBinding;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PaymentHistoryDetails extends AppCompatActivity {

    ActivityPaymentHistoryDetailsBinding binding;
    DatabaseReference mDatabase;
    String key;

    ArrayList<String> menteeNames, plan;
    ArrayList<Integer> amount;

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentHistoryDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        key = intent.getStringExtra("key");

        mDatabase= FirebaseDatabase.getInstance().getReference("payments");
        mDatabase.keepSynced(true);

        menteeNames = new ArrayList<>();
        plan = new ArrayList<>();
        amount = new ArrayList<>();

        lv = binding.mentorPaymentLV;
        PaymentDetailAdapter adapter = new PaymentDetailAdapter(this, menteeNames,amount, plan);
        lv.setAdapter(adapter);

        binding.loading1.setVisibility(View.VISIBLE);
        binding.roleTV.setText("Mentor");

        mDatabase.child(key).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot snapshot = task.getResult();
                    binding.mentorNamePaymentTV.setText(snapshot.child("name").getValue().toString());
                    binding.totalTV.setText("₹ "+snapshot.child("amount").getValue().toString());
                    String date = snapshot.child("date").getValue().toString();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        Date date1  = simpleDateFormat.parse(date);
                        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MMMM dd, yyyy");
                        String date2 = simpleDateFormat1.format(date1);
                        binding.dateTV.setText(date2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    for(DataSnapshot dataSnapshot : snapshot.child("details").getChildren()){
                        menteeNames.add(dataSnapshot.child("mentee").getValue().toString());
                        plan.add(dataSnapshot.child("plan").getValue().toString());
                        amount.add(Integer.parseInt(dataSnapshot.child("amount").getValue().toString()));
                        adapter.notifyDataSetChanged();
                        binding.loading1.setVisibility(View.INVISIBLE);
                    }
                }else{
                    Toast.makeText(PaymentHistoryDetails.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.backToPayments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}