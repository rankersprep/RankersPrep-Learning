package com.rankersprep.rankerspreplearning.ui.notifications;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rankersprep.rankerspreplearning.CustomAdapter;
import com.rankersprep.rankerspreplearning.MenteeListFragment;
import com.rankersprep.rankerspreplearning.PaymentsListAdapter;
import com.rankersprep.rankerspreplearning.R;
import com.rankersprep.rankerspreplearning.databinding.FragmentNotificationsBinding;
import com.rankersprep.rankerspreplearning.databinding.FragmentUpcomingPaymentBinding;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class UpcomingPaymentFragment extends Fragment {

    FragmentUpcomingPaymentBinding upcomingPaymentBinding;
    ListView lv,lv2;
    static ArrayList<String> mentorNames, amountList,UIDs, mentorNames2, amountList2, UIDs2,menteeNames,menteeUIDs;
    static  ArrayList<Integer> amountBreakdown;
    private DatabaseReference mDatabase;
    String slot,slot2;
    int currentMonth, nextPaymentMonth;
    public static ArrayList<ArrayList<String>> menteesNamesList,menteeUIDsList;
    static ArrayList<ArrayList<Integer>> amountBreakdownList;


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        upcomingPaymentBinding = FragmentUpcomingPaymentBinding.inflate(inflater,container,false);

        View root = upcomingPaymentBinding.getRoot();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference users= FirebaseDatabase.getInstance().getReference("users");
        users.keepSynced(true);

        mentorNames = new ArrayList<>();
        amountList = new ArrayList<>();
        UIDs = new ArrayList<>();
        mentorNames2 = new ArrayList<>();
        amountList2 = new ArrayList<>();
        UIDs2 = new ArrayList<>();

        menteeUIDsList = new ArrayList<>();
        amountBreakdownList = new ArrayList<>();
        menteesNamesList = new ArrayList<>();



//        amountBreakdown.add(0);
//        menteeNames.add("");
//        menteeUIDs.add("");

        lv=upcomingPaymentBinding.slotList;
        PaymentsListAdapter adapter=new PaymentsListAdapter(getActivity(), mentorNames,amountList);
        lv.setAdapter(adapter);

        lv2 = upcomingPaymentBinding.nextSlotList;
        PaymentsListAdapter adapter2 = new PaymentsListAdapter(getActivity(),mentorNames2,amountList2);
        lv2.setAdapter(adapter2);

        Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.DAY_OF_MONTH);

        if(date >1 && date <= 16){
            slot="2";
            slot2="1";
            calendar.set(Calendar.DAY_OF_MONTH,16);
            String myFormat = "MMMM dd"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
            upcomingPaymentBinding.slot.setText(sdf.format(calendar.getTime()));
            currentMonth = calendar.get(Calendar.MONTH);

            calendar.set(Calendar.DAY_OF_MONTH,1);
            calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)+1);
            nextPaymentMonth = calendar.get(Calendar.MONTH);
            upcomingPaymentBinding.nextSlot.setText(sdf.format(calendar.getTime()).toUpperCase());

        }else{
            slot="1";
            slot2="2";
            calendar.set(Calendar.DAY_OF_MONTH,1);
            if(date==1){
                calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH));
            }else {
                calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
            }
            currentMonth = calendar.get(Calendar.MONTH);
            String myFormat = "MMMM dd"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
            upcomingPaymentBinding.slot.setText(sdf.format(calendar.getTime()).toUpperCase());

            calendar.set(Calendar.DAY_OF_MONTH,16);
            nextPaymentMonth = calendar.get(Calendar.MONTH);
            upcomingPaymentBinding.nextSlot.setText(sdf.format(calendar.getTime()).toUpperCase());
        }

        upcomingPaymentBinding.loading1.setVisibility(View.VISIBLE);


        users.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                int amount=0;
                int amount2=0;
                if(snapshot.child("mentees").getChildrenCount()>0){
                    menteeNames= new ArrayList<>();
                    menteeUIDs= new ArrayList<>();
                    amountBreakdown = new ArrayList<>();

                    for(DataSnapshot mentee: snapshot.child("mentees").getChildren()){
                        Log.i("mentee", mentee.toString());

                        if(mentee.child("slot").getValue().toString().matches(slot) && Integer.parseInt(mentee.child("nextPaymentMonth").getValue().toString())==currentMonth+1){
                            amount+= Integer.parseInt(mentee.child("salary").getValue().toString());
                            if(Integer.parseInt(mentee.child("salary").getValue().toString())!=0) {
                                amountBreakdown.add(Integer.parseInt(mentee.child("salary").getValue().toString()));
                                menteeNames.add(mentee.child("name").getValue().toString());
                                menteeUIDs.add(mentee.getKey());
                            }
                        }else if(mentee.child("slot").getValue().toString().matches(slot2) && Integer.parseInt(mentee.child("nextPaymentMonth").getValue().toString())==nextPaymentMonth+1){
                            amount2+= Integer.parseInt(mentee.child("salary").getValue().toString());
                        }
                    }
                    if(amount>0){
                        amountList.add(String.valueOf(amount));
                        mentorNames.add(snapshot.child("name").getValue().toString());
                        UIDs.add(snapshot.getKey());
                        menteesNamesList.add(menteeNames);
                        menteeUIDsList.add(menteeUIDs);
                        amountBreakdownList.add(amountBreakdown);
                        adapter.notifyDataSetChanged();
                        upcomingPaymentBinding.loading1.setVisibility(View.INVISIBLE);
                        setListViewHeightBasedOnChildren(lv);

                    }
                    if(amount2>0){
                        amountList2.add(String.valueOf(amount2));
                        UIDs2.add(snapshot.getKey());
                        mentorNames2.add(snapshot.child("name").getValue().toString());
                        adapter2.notifyDataSetChanged();
                        upcomingPaymentBinding.loading1.setVisibility(View.INVISIBLE);
                        setListViewHeightBasedOnChildren(lv2);
                    }

                };
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

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PaymentDetailActivity.class);
                intent.putExtra("mentorName",mentorNames.get(position));
                intent.putExtra("amount", amountList.get(position));
                intent.putExtra("UID",UIDs.get(position));
                intent.putExtra("menteeNames",menteesNamesList.get(position));
                intent.putExtra("menteeUIDs", menteeUIDsList.get(position));
                intent.putExtra("amountBreakdown",amountBreakdownList.get(position));
                startActivity(intent);
            }
        });


        return root;
    }


}