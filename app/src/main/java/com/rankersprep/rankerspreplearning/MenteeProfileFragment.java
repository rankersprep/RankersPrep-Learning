package com.rankersprep.rankerspreplearning;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rankersprep.rankerspreplearning.databinding.FragmentMenteeProfileBinding;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MenteeProfileFragment extends Fragment {

   FragmentMenteeProfileBinding binding;
   DatabaseReference mDatabase,databaseReference;
   private FirebaseAuth mAuth;

   String mentorUID;
   String role;

   public String changeDateForm(String date){
       SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
       try {
           Date date1  = simpleDateFormat.parse(date);
           SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MMM-yyyy");
           String date2 = simpleDateFormat1.format(date1);
           return date2;
       } catch (ParseException e) {
           e.printStackTrace();
       }
       return null;
   }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.background));

        mAuth = FirebaseAuth.getInstance();

        // Inflate the layout for this fragment
        binding = FragmentMenteeProfileBinding.inflate(inflater,container,false);
        View root = binding.getRoot();
        root.setSystemUiVisibility(root.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        String UID = getArguments().getString("UID");
        mDatabase = FirebaseDatabase.getInstance().getReference("mentees/"+UID);
        databaseReference = FirebaseDatabase.getInstance().getReference("users/"+mAuth.getCurrentUser().getUid()+"/role");
        mDatabase.keepSynced(true);
        databaseReference.keepSynced(true);
        binding.loading4.setVisibility(View.VISIBLE);

        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot snapshot = task.getResult();
                    binding.menteeName.setText(snapshot.child("name").getValue().toString());
                    binding.planNameMentee.setText(snapshot.child("plan").getValue().toString());
                    binding.targetExam.setText(snapshot.child("exam").getValue().toString());
                    binding.emailMenteeTextView.setText(snapshot.child("email").getValue().toString());
                    binding.contactMentee.setText(snapshot.child("contact").getValue().toString());
                    binding.mentorNameTV.setText(snapshot.child("mentor").getValue().toString());
                    binding.planDateStart.setText(changeDateForm(snapshot.child("startDate").getValue().toString()));
                    binding.planDateEnd.setText(changeDateForm(snapshot.child("endDate").getValue().toString()));
                    binding.note.setText(snapshot.child("note").getValue().toString());
                    mentorUID = snapshot.child("mentorUID").getValue().toString();
                    binding.loading4.setVisibility(View.INVISIBLE);
                }else{
                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        databaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                role = dataSnapshot.getValue().toString();
            }
        });



        binding.emailMenteeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+binding.emailMenteeTextView.getText().toString())));
            }
        });

        binding.contactMentee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+91"+binding.contactMentee.getText().toString()));
                startActivity(intent);
            }
        });

        binding.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("UID", mentorUID);

                MentorProfileFragment nextFrag= new MentorProfileFragment();
                nextFrag.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                if(role.matches("admin")) {
                    transaction.replace(R.id.nav_host_fragment, nextFrag); // give your fragment container id in first parameter
                } else if (role.matches("mentor")) {
                    transaction.replace(R.id.nav_host_fragment_mentor, nextFrag);
                }
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });



        return root;
    }
}