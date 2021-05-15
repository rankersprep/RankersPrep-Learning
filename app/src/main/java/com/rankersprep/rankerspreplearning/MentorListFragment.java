package com.rankersprep.rankerspreplearning;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
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
import com.rankersprep.rankerspreplearning.databinding.FragmentMentorListBinding;
import com.rankersprep.rankerspreplearning.databinding.FragmentProfileBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class MentorListFragment extends Fragment {

    ListView lv;
    static ArrayList<String> mentorNames, expertIn, noMentees,approvals,uids;
    static ArrayList<Bitmap> pics;
    private DatabaseReference mDatabase;
    FragmentMentorListBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.background));

        binding = FragmentMentorListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        root.setSystemUiVisibility(root.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        FirebaseDatabase.getInstance().goOnline();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mentorNames = new ArrayList<String>();
        expertIn = new ArrayList<String>();
        noMentees = new ArrayList<String>();
        approvals= new ArrayList<String>();
        uids= new ArrayList<String>();
        pics = new ArrayList<Bitmap>();

        lv=(ListView) root.findViewById(R.id.listView);
        CustomAdapter adapter=new CustomAdapter(getActivity(), mentorNames,pics,noMentees,expertIn,approvals);
        lv.setAdapter(adapter);

        binding.loading1.setVisibility(View.VISIBLE);



        mDatabase.child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                uids.add(snapshot.getKey());
                mentorNames.add(snapshot.child("name").getValue().toString());
                expertIn.add(snapshot.child("expertIn").getValue().toString());
                if(snapshot.hasChild("noOfMentees")){
                    noMentees.add("Mentees: "+snapshot.child("noOfMentees").getValue().toString());
                }else{
                    noMentees.add("Mentees: "+"0");
                }
                approvals.add(snapshot.child("approval").getValue().toString());
                pics.add(BitmapFactory.decodeResource(getResources(), R.drawable.sagar_pic));
                adapter.notifyDataSetChanged();
                binding.loading1.setVisibility(View.INVISIBLE);
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

                Toast.makeText(getActivity(), mentorNames.get(position), Toast.LENGTH_SHORT).show();

            }
        });

        return root;
    }
}