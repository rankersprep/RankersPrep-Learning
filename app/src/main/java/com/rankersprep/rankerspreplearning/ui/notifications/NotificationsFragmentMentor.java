package com.rankersprep.rankerspreplearning.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rankersprep.rankerspreplearning.PaymentDetailAdapter;
import com.rankersprep.rankerspreplearning.R;
import com.rankersprep.rankerspreplearning.databinding.FragmentNotificationsBinding;
import com.rankersprep.rankerspreplearning.databinding.FragmentNotificationsMentorBinding;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class NotificationsFragmentMentor extends Fragment {

    private FragmentNotificationsMentorBinding binding;

    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    ListView lv;
    ArrayList<String> menteeNames, dateList;
    ArrayList<Integer> amount;

    String slot,slot2;
    int currentMonth, nextPaymentMonth;
    String slot1date, slot2date;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.background));

        binding = FragmentNotificationsMentorBinding.inflate(inflater,container,false);

        View root = binding.getRoot();

        root.setSystemUiVisibility(root.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users/"+mAuth.getCurrentUser().getUid()+"/mentees");
        mDatabase.keepSynced(true);


        menteeNames = new ArrayList<>();
        amount = new ArrayList<>();
        dateList = new ArrayList<>();

        lv = binding.mentorUpcomingPayments;
        PaymentDetailAdapter adapter = new PaymentDetailAdapter(getActivity(),menteeNames,amount,dateList);
        lv.setAdapter(adapter);

        Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.DAY_OF_MONTH);

        if(date >1 && date <= 16){
            slot="2";
            slot2="1";
            calendar.set(Calendar.DAY_OF_MONTH,16);
            String myFormat = "MMMM dd"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
            slot1date=(sdf.format(calendar.getTime()));
            currentMonth = calendar.get(Calendar.MONTH);

            calendar.set(Calendar.DAY_OF_MONTH,1);
            calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)+1);
            nextPaymentMonth = calendar.get(Calendar.MONTH);
            slot2date=(sdf.format(calendar.getTime()).toUpperCase());

        }else{
            slot="1";
            slot2="2";
            calendar.set(Calendar.DAY_OF_MONTH,1);
            calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)+1);
            currentMonth = calendar.get(Calendar.MONTH);
            String myFormat = "MMMM dd"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
            slot1date =  (sdf.format(calendar.getTime()));

            calendar.set(Calendar.DAY_OF_MONTH,16);
            nextPaymentMonth = calendar.get(Calendar.MONTH);
            slot2date=(sdf.format(calendar.getTime()));
        }

        mDatabase.orderByChild("slot").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                if(snapshot.child("slot").getValue().toString().matches(slot) && Integer.parseInt(snapshot.child("nextPaymentMonth").getValue().toString())==currentMonth+1){
                    menteeNames.add(snapshot.child("name").getValue().toString());
                    amount.add(Integer.parseInt(snapshot.child("salary").getValue().toString()));
                    dateList.add(slot1date+"\t     "+snapshot.child("plan").getValue().toString());
                }else if (snapshot.child("slot").getValue().toString().matches(slot2) && Integer.parseInt(snapshot.child("nextPaymentMonth").getValue().toString())==nextPaymentMonth+1){
                    menteeNames.add(snapshot.child("name").getValue().toString());
                    amount.add(Integer.parseInt(snapshot.child("salary").getValue().toString()));
                    dateList.add(slot2date+"\t     "+snapshot.child("plan").getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        binding.backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });





        return root;
    }


}