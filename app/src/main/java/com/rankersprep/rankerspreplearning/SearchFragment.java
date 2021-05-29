package com.rankersprep.rankerspreplearning;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rankersprep.rankerspreplearning.databinding.FragmentMentorListBinding;
import com.rankersprep.rankerspreplearning.databinding.FragmentSearchBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class SearchFragment extends Fragment {

   FragmentSearchBinding binding;
    DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    ArrayList<String> mentorNames,UIDs,collegeNames;
    ArrayList<Bitmap> images;
    ButtonListAdapter adapter;
    ListView lv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.background));

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        root.setSystemUiVisibility(root.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        mDatabase= FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();

        mDatabase.keepSynced(true);
        lv=binding.mentorViewList;

        mentorNames = new ArrayList<>();
        UIDs = new ArrayList<>();
        collegeNames = new ArrayList<>();
        images = new ArrayList<>();


        adapter = new ButtonListAdapter(getActivity(),mentorNames,UIDs,images,collegeNames,getFragmentManager());
        lv.setAdapter(adapter);


        mDatabase.orderByChild("name").limitToFirst(15).addChildEventListener(new ChildEventListener() {
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


        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mDatabase.orderByChild("name").startAt(s.toString(),"name").endAt(s.toString()+"\uf8ff","name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        mentorNames = new ArrayList<>();
                        UIDs = new ArrayList<>();
                        collegeNames = new ArrayList<>();
                        images = new ArrayList<>();
                        adapter = new ButtonListAdapter(getActivity(),mentorNames,UIDs,images,collegeNames,getFragmentManager());
                        lv.setAdapter(adapter);

                        if(snapshot.exists() && snapshot.getChildrenCount()>0){
                            for(DataSnapshot ds: snapshot.getChildren()){
                                Log.i("textchanged", s.toString());
                                String currentUID = ds.getKey();
                                if(!currentUID.matches(mAuth.getCurrentUser().getUid())){
                                    mentorNames.add(ds.child("name").getValue().toString());
                                    UIDs.add(ds.getKey());
                                    collegeNames.add(ds.child("college").getValue().toString());
                                    images.add(BitmapFactory.decodeResource(getResources(), R.drawable.sagar_pic));
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mentorNames = new ArrayList<>();
        UIDs = new ArrayList<>();
        collegeNames = new ArrayList<>();
        images = new ArrayList<>();
        adapter = new ButtonListAdapter(getActivity(),mentorNames,UIDs,images,collegeNames,getFragmentManager());
        lv.setAdapter(adapter);


    }
}