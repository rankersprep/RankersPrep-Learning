package com.rankersprep.rankerspreplearning;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
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
import com.rankersprep.rankerspreplearning.databinding.FragmentMenteeListBinding;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MenteeListFragment extends Fragment {

    ListView lv;
    static ArrayList<String> menteeNames, exam, mentorName,approvals,uids;
    static ArrayList<Bitmap> pics;
    private DatabaseReference mDatabase;
    FragmentMenteeListBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.background));

        binding = FragmentMenteeListBinding.inflate(inflater, container, false);
        View root=binding.getRoot();
        root.setSystemUiVisibility(root.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        FirebaseDatabase.getInstance().goOnline();
        mDatabase = FirebaseDatabase.getInstance().getReference("mentees");
        mDatabase.keepSynced(true);

        menteeNames = new ArrayList<String>();
        exam = new ArrayList<String>();
        mentorName = new ArrayList<String>();
        approvals= new ArrayList<String>();
        uids= new ArrayList<String>();
        pics = new ArrayList<Bitmap>();

        lv=(ListView) binding.menteeListView;
        CustomAdapter adapter=new CustomAdapter(getActivity(), menteeNames,pics,mentorName,exam,approvals);
        lv.setAdapter(adapter);

        binding.loading2.setVisibility(View.VISIBLE);

        binding.addMentee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMenteeFragment nextFrag= new AddMenteeFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, nextFrag ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                uids.add(snapshot.getKey());
                menteeNames.add(snapshot.child("name").getValue().toString());
                exam.add(snapshot.child("exam").getValue().toString());
                mentorName.add("Mentor: "+snapshot.child("mentor").getValue().toString());
                String endDate = snapshot.child("endDate").getValue().toString();
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date = format.parse(endDate);
                    Date currentDate = Calendar.getInstance().getTime();
                    if(date.after(currentDate)){
                        Log.i("date",date.toString()+currentDate.toString());
                        approvals.add("pending");
                    }else{
                        approvals.add("approved");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                pics.add(BitmapFactory.decodeResource(getResources(), R.drawable.sagar_pic));
                adapter.notifyDataSetChanged();
                binding.loading2.setVisibility(View.INVISIBLE);
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

                Bundle bundle = new Bundle();
                bundle.putString("UID", uids.get(position));

                MenteeProfileFragment nextFrag= new MenteeProfileFragment();
                nextFrag.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, nextFrag ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });

        CountDownTimer countDownTimer = new CountDownTimer(4000,4000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                binding.loading2.setVisibility(View.INVISIBLE);
            }
        }.start();

        return root;
    }
}