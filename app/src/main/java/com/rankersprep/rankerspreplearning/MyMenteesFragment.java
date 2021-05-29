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
import com.rankersprep.rankerspreplearning.databinding.FragmentMyMenteesBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class MyMenteesFragment extends Fragment {

    FragmentMyMenteesBinding binding;
    DatabaseReference mDatabase,databaseReference;
    private FirebaseAuth mAuth;
    ArrayList<String> menteeNames,UIDs,exam;
    ArrayList<Bitmap> images;
    ButtonListAdapter adapter;
    ListView lv;
    boolean b = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.background));

        binding = FragmentMyMenteesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        root.setSystemUiVisibility(root.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        mAuth = FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference("users/"+mAuth.getCurrentUser().getUid()+"/mentees");
        databaseReference = FirebaseDatabase.getInstance().getReference("mentees");

        mDatabase.keepSynced(true);
        databaseReference.keepSynced(true);
        lv=binding.menteesLV;

        menteeNames = new ArrayList<>();
        UIDs = new ArrayList<>();
        exam = new ArrayList<>();
        images = new ArrayList<>();



        adapter = new ButtonListAdapter(getActivity(),menteeNames,UIDs,images,exam,getFragmentManager(),true);
        lv.setAdapter(adapter);

//        databaseReference.child(snapshot.getKey()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//            @Override
//            public void onSuccess(DataSnapshot dataSnapshot) {
//                exam.add(dataSnapshot.child("exam").getValue().toString());
//                menteeNames.add(snapshot.child("name").getValue().toString());
//                UIDs.add(snapshot.getKey());
//                images.add(BitmapFactory.decodeResource(getResources(), R.drawable.sagar_pic));
//                adapter.notifyDataSetChanged();
//            }
//        });

        mDatabase.orderByChild("name").limitToFirst(15).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if(snapshot.exists() && snapshot.getChildrenCount()>0){

                    menteeNames = new ArrayList<>();
                    UIDs = new ArrayList<>();
                    exam = new ArrayList<>();
                    images = new ArrayList<>();
                    adapter = new ButtonListAdapter(getActivity(), menteeNames, UIDs, images, exam, getFragmentManager(), true);
                    lv.setAdapter(adapter);
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String currentUID = ds.getKey();

                        databaseReference.child(ds.getKey()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                if(b) {

                                    exam.add(dataSnapshot.child("exam").getValue().toString());
                                    menteeNames.add(ds.child("name").getValue().toString());
                                    UIDs.add(ds.getKey());
                                    images.add(BitmapFactory.decodeResource(getResources(), R.drawable.sagar_pic));
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        binding.searchEditText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mDatabase.orderByChild("name").startAt(s.toString(),"name").endAt(s.toString()+"\uf8ff","name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        menteeNames = new ArrayList<>();
                        UIDs = new ArrayList<>();
                        exam = new ArrayList<>();
                        images = new ArrayList<>();
                        adapter = new ButtonListAdapter(getActivity(),menteeNames,UIDs,images,exam,getFragmentManager(),true);
                        lv.setAdapter(adapter);

                        if(snapshot.exists() && snapshot.getChildrenCount()>0){
                            for(DataSnapshot ds: snapshot.getChildren()){
                                Log.i("textchanged", s.toString());
                                String currentUID = ds.getKey();

                                    databaseReference.child(ds.getKey()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                        @Override
                                        public void onSuccess(DataSnapshot dataSnapshot) {
                                            exam.add(dataSnapshot.child("exam").getValue().toString());
                                            menteeNames.add(ds.child("name").getValue().toString());
                                            UIDs.add(ds.getKey());
                                            images.add(BitmapFactory.decodeResource(getResources(), R.drawable.sagar_pic));
                                            adapter.notifyDataSetChanged();
                                        }
                                    });


                            }
                            b = false;
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

        menteeNames = new ArrayList<>();
        UIDs = new ArrayList<>();
        exam = new ArrayList<>();
        images = new ArrayList<>();
        adapter = new ButtonListAdapter(getActivity(),menteeNames,UIDs,images,exam,getFragmentManager(),true);
        lv.setAdapter(adapter);
        binding = null;

    }
}