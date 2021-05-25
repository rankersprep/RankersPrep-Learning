package com.rankersprep.rankerspreplearning.ui.notifications;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rankersprep.rankerspreplearning.PaymentDetailAdapter;
import com.rankersprep.rankerspreplearning.PaymentHistoryDetails;
import com.rankersprep.rankerspreplearning.R;
import com.rankersprep.rankerspreplearning.databinding.FragmentPaymentHistoryBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class PaymentHistoryFragment extends Fragment {

    FragmentPaymentHistoryBinding historyBinding;
    ListView lv;
    private DatabaseReference mDatabase;

    ArrayList<String> keys,date,mentorName;
    ArrayList<Integer> amount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        historyBinding = FragmentPaymentHistoryBinding.inflate(inflater,container,false);
        View root = historyBinding.getRoot();

        mDatabase = FirebaseDatabase.getInstance().getReference("payments");
        mDatabase.keepSynced(true);

        keys = new ArrayList<>();
        amount = new ArrayList<>();
        date = new ArrayList<>();
        mentorName = new ArrayList<>();

        lv = historyBinding.paymentHistoryLV;
        PaymentDetailAdapter adapter = new PaymentDetailAdapter(getActivity(), mentorName,amount, date);
        lv.setAdapter(adapter);

        historyBinding.loading1.setVisibility(View.VISIBLE);

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                keys.add(snapshot.getKey());
                amount.add(Integer.parseInt(snapshot.child("amount").getValue().toString()));
                date.add(snapshot.child("date").getValue().toString());
                mentorName.add(snapshot.child("name").getValue().toString());
                adapter.notifyDataSetChanged();
                historyBinding.loading1.setVisibility(View.INVISIBLE);
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
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PaymentHistoryDetails.class);
                intent.putExtra("key",keys.get(position));
                startActivity(intent);
            }
        });


        return root;
    }
}