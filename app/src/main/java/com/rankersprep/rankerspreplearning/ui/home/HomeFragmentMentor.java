package com.rankersprep.rankerspreplearning.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rankersprep.rankerspreplearning.AnnouncementListFragment;
import com.rankersprep.rankerspreplearning.ButtonListAdapter;
import com.rankersprep.rankerspreplearning.GuidelinesFragment;
import com.rankersprep.rankerspreplearning.MyMenteesFragment;
import com.rankersprep.rankerspreplearning.R;
import com.rankersprep.rankerspreplearning.SearchFragment;
import com.rankersprep.rankerspreplearning.databinding.FragmentHomeMentorBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragmentMentor extends Fragment {

    private FragmentHomeMentorBinding binding;
    DatabaseReference mDatabase,announcements;
    private FirebaseAuth mAuth;
    ArrayList<String> mentorNames,UIDs,collegeNames;
    ArrayList<Bitmap> images;
    ListView lv;
    int c=0;

    public boolean toDelete(String x){
        long milliSeconds= Long.parseLong(x);
        long currentMillis = Calendar.getInstance().getTimeInMillis();
        int days = (int) ((currentMillis-milliSeconds)/86400000);
        if(days>7){
            return true;
        }else{
            return false;
        }
    }



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeMentorBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.themeColor));
        mAuth = FirebaseAuth.getInstance();

        mDatabase= FirebaseDatabase.getInstance().getReference("users");
        announcements = FirebaseDatabase.getInstance().getReference("announcements");

        mDatabase.keepSynced(true);

        mentorNames = new ArrayList<>();
        UIDs = new ArrayList<>();
        collegeNames = new ArrayList<>();
        images = new ArrayList<>();
        c=0;

        lv=binding.listOFMentors;
        ButtonListAdapter adapter = new ButtonListAdapter(getActivity(),mentorNames,UIDs,images,collegeNames,getFragmentManager());
        lv.setAdapter(adapter);




        mDatabase.child(mAuth.getCurrentUser().getUid()).child("name").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                binding.userNameTV.setText("Hello, "+dataSnapshot.getValue().toString().split(" ",3)[0]);
            }
        });

        mDatabase.orderByChild("name").limitToFirst(5).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                String currentUID = snapshot.getKey();
                if(!currentUID.matches(mAuth.getCurrentUser().getUid())){
                    mentorNames.add(snapshot.child("name").getValue().toString());
                    UIDs.add(snapshot.getKey());
                    collegeNames.add(snapshot.child("college").getValue().toString());
                    images.add(BitmapFactory.decodeResource(getResources(), R.drawable.sagar_pic));
                    adapter.notifyDataSetChanged();
                }
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

        binding.searchMentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFragment nextFrag= new SearchFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_mentor, nextFrag ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });

        binding.guidelines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuidelinesFragment nextFrag= new GuidelinesFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_mentor, nextFrag ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });

        binding.menteesListForMentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMenteesFragment nextFrag= new MyMenteesFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_mentor, nextFrag ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });

        binding.announcements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBooleanArray("isFor", new boolean[]{true, false});

                AnnouncementListFragment nextFrag= new AnnouncementListFragment();
                nextFrag.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_mentor, nextFrag ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });

        announcements.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                if(toDelete(snapshot.child("time").getValue().toString())){
                    mDatabase.child(snapshot.getKey()).removeValue();
                }else if(snapshot.child("sendToMentors").getValue().toString().matches("true")){
                    c++;
                    if(c!=0) {
                        binding.noOfAnnouncements.setText(String.valueOf(c));
                    }else{
                        binding.noOfAnnouncements.setVisibility(View.INVISIBLE);
                    }
                }
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

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

