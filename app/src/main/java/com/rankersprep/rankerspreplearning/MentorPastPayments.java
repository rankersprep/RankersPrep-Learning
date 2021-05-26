package com.rankersprep.rankerspreplearning;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rankersprep.rankerspreplearning.databinding.FragmentMentorPastPaymentsBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class MentorPastPayments extends Fragment {

    FragmentMentorPastPaymentsBinding binding;
    DatabaseReference mDatabase;
    ArrayList<String> menteeNames,  date;
    ArrayList<Integer> amount;
    ListView lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.background));
        binding = FragmentMentorPastPaymentsBinding.inflate(inflater,container,false);
        View root = binding.getRoot();

        root.setSystemUiVisibility(root.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        String UID = getArguments().getString("UID");
        mDatabase = FirebaseDatabase.getInstance().getReference("users/"+UID+"/payments");
        mDatabase.keepSynced(true);

        menteeNames = new ArrayList<>();
        date = new ArrayList<>();
        amount = new ArrayList<>();

        lv=binding.lv;
        PaymentDetailAdapter adapter = new PaymentDetailAdapter(getActivity(), menteeNames,amount, date);
        lv.setAdapter(adapter);

        binding.loading3.setVisibility(View.VISIBLE);

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                menteeNames.add(snapshot.child("mentee").getValue().toString());
                date.add(snapshot.child("date").getValue().toString());
                amount.add(Integer.parseInt(snapshot.child("amount").getValue().toString()));
                adapter.notifyDataSetChanged();
                binding.loading3.setVisibility(View.INVISIBLE);
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


        return root;
    }
}