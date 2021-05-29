package com.rankersprep.rankerspreplearning;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rankersprep.rankerspreplearning.databinding.FragmentAnnouncementListBinding;
import com.rankersprep.rankerspreplearning.databinding.FragmentMyMenteesBinding;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AnnouncementListFragment extends Fragment {

    FragmentAnnouncementListBinding binding;
    DatabaseReference mDatabase;
    ArrayList<String> title,date,description;
    ArrayList<Boolean> isUrgent;
    ListView lv;


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

    public Date millisToDate(String x){


        long milliSeconds= Long.parseLong(x);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return ((calendar.getTime()));
    }

    public String dateToString(Date date){
        DateFormat formatter = new SimpleDateFormat("EEEE");
        DateFormat format = new SimpleDateFormat("hh:mm a");
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date);
        boolean sameDay = cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
        if(sameDay){
            return format.format(date);
        }else {
            return formatter.format(date);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.background));

        binding = FragmentAnnouncementListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        root.setSystemUiVisibility(root.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        mDatabase= FirebaseDatabase.getInstance().getReference("announcements");
        mDatabase.keepSynced(true);

        boolean[] isFor = getArguments().getBooleanArray("isFor");

        title = new ArrayList<>();
        date = new ArrayList<>();
        description = new ArrayList<>();
        isUrgent = new ArrayList<>();

        lv = binding.annoucementsListView;
        AnnouncementAdapter adapter = new AnnouncementAdapter(getActivity(),title,date,description,isUrgent);
        lv.setAdapter(adapter);

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                if(toDelete(snapshot.child("time").getValue().toString())){
                    mDatabase.child(snapshot.getKey()).removeValue();
                }else {
                    boolean[] b = {Boolean.parseBoolean(snapshot.child("sendToMentors").getValue().toString()), Boolean.parseBoolean(snapshot.child("sendToMentees").getValue().toString())};
                    if (isFor[0] && b[0]) {
                        title.add(0,snapshot.child("title").getValue().toString());
                        isUrgent.add(0,Boolean.parseBoolean(snapshot.child("urgent").getValue().toString()));
                        description.add(0,snapshot.child("description").getValue().toString());
                        date.add(0,dateToString(millisToDate(snapshot.child("time").getValue().toString())));
                        adapter.notifyDataSetChanged();
                    }else if(isFor[1] && b[1]){
                        title.add(0,snapshot.child("title").getValue().toString());
                        isUrgent.add(0,Boolean.parseBoolean(snapshot.child("urgent").getValue().toString()));
                        description.add(0,snapshot.child("description").getValue().toString());
                        date.add(0,dateToString(millisToDate(snapshot.child("time").getValue().toString())));
                        adapter.notifyDataSetChanged();
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


        return root;
    }
}