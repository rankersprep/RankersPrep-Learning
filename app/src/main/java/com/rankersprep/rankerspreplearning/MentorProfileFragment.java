package com.rankersprep.rankerspreplearning;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rankersprep.rankerspreplearning.databinding.FragmentMentorProfileBinding;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;


public class MentorProfileFragment extends Fragment {

    FragmentMentorProfileBinding binding;
    DatabaseReference mDatabase;

    public int monthsBetweenDates(Date startDate, Date endDate){

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);

        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        int monthsBetween = 0;
        int dateDiff = end.get(Calendar.DAY_OF_MONTH)-start.get(Calendar.DAY_OF_MONTH);

        if(dateDiff<0) {
            int borrow = end.getActualMaximum(Calendar.DAY_OF_MONTH);
            dateDiff = (end.get(Calendar.DAY_OF_MONTH)+borrow)-start.get(Calendar.DAY_OF_MONTH);
            monthsBetween--;

            if(dateDiff>0) {
                monthsBetween++;
            }
        }
        else {
            monthsBetween++;
        }
        monthsBetween += end.get(Calendar.MONTH)-start.get(Calendar.MONTH);
        monthsBetween  += (end.get(Calendar.YEAR)-start.get(Calendar.YEAR))*12;
        return monthsBetween;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.background));

        // Inflate the layout for this fragment
        binding = FragmentMentorProfileBinding.inflate(inflater,container,false);
        View root = binding.getRoot();
        root.setSystemUiVisibility(root.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        String UID = getArguments().getString("UID");
        binding.loading1.setVisibility(View.VISIBLE);

        mDatabase.child("users").child(UID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot snapshot = task.getResult();
                    binding.mentorName.setText(snapshot.child("name").getValue().toString());
                    binding.collegeMentorTV.setText(snapshot.child("college").getValue().toString());
                    String expertIn = snapshot.child("expertIn").getValue().toString();
                    if(expertIn.equals("JEE (Mains + Advance)")){
                        expertIn="JEE ADV";
                    }
                    binding.expert.setText(expertIn.toUpperCase());
                    binding.AIR.setText(snapshot.child("AIR").getValue().toString());
                    String joinedOn = snapshot.child("joinedOn").getValue().toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                    try {
                        Date date = sdf.parse(joinedOn);
                        Date currentDate = Calendar.getInstance().getTime();

                        String months = String.valueOf(monthsBetweenDates(date,currentDate));
                        binding.months.setText(months+" months");

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    binding.menteeNumber.setText(String.valueOf(snapshot.child("mentees").getChildrenCount()));
                    binding.emailMentorTextView.setText(snapshot.child("email").getValue().toString());
                    binding.contactMentorTextView.setText(snapshot.child("contact").getValue().toString());

                    binding.loading1.setVisibility(View.INVISIBLE);
                }else{
                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.paymentInfoClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("UID", UID);

                MentorPastPayments nextFrag= new MentorPastPayments();
                nextFrag.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, nextFrag ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });



        return root;
    }
}