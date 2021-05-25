package com.rankersprep.rankerspreplearning;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rankersprep.rankerspreplearning.databinding.FragmentAddMenteeBinding;
import com.rankersprep.rankerspreplearning.databinding.FragmentProfileBinding;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class AddMenteeFragment extends Fragment {

    FragmentAddMenteeBinding binding;
    private DatabaseReference mDatabase;
    ArrayList<String> mentorNames,mentorUIDs;
    String slot;
    String months;
    int startMonth,endMonth;
    String nextPaymentMonth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.background));

        binding = FragmentAddMenteeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        root.setSystemUiVisibility(root.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        DatabaseReference mentors = FirebaseDatabase.getInstance().getReference("users");
        mentors.keepSynced(true);

        mentorNames = new ArrayList<String>();
        mentorUIDs = new ArrayList<String>();

        mentors.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                mentorUIDs.add(snapshot.getKey());
                mentorNames.add(snapshot.child("name").getValue().toString());
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



        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mentorNames);
        AutoCompleteTextView autoCompleteTextView = binding.mentorAssignedEditText;
        autoCompleteTextView.setAdapter(arrayAdapter);


        binding.calendarStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, month);
                        Log.i("Month", String.valueOf(month));
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "dd/MM/yy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                        binding.startDate.setText(sdf.format(myCalendar.getTime()));
                        if(dayOfMonth<16){
                            slot = "2";
                            nextPaymentMonth = String.valueOf(month+2);
                        }else{
                            slot="1";
                            nextPaymentMonth = String.valueOf(month+3);
                        }
                        startMonth=myCalendar.get(Calendar.MONTH)+1;
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        binding.calendarEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, month);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "dd/MM/yy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                        binding.endDate.setText(sdf.format(myCalendar.getTime()));
                        endMonth=month+1;
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        binding.addMentee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,email,contact,plan, exam, startDate, endDate, mentor, salary, note;
                name = binding.nameEditTextMentee.getText().toString();
                email = binding.emailEditTextMentee.getText().toString();
                contact = binding.numberEditTextMentee.getText().toString();
                plan = binding.planNameEditText.getText().toString();
                exam = binding.targetExamEditText.getText().toString();
                startDate = binding.startDate.getText().toString();
                endDate = binding.endDate.getText().toString();
                mentor = binding.mentorAssignedEditText.getText().toString();
                salary = binding.salaryEditText.getText().toString();
                note = binding.noteEditText.getText().toString();
                if(!name.matches("") && !email.matches("") && contact.length()==10 && !plan.matches("") && !exam.matches("") && !startDate.matches("") && !endDate.matches("") && !mentor.matches("") && !salary.matches("")  && !note.matches("")){
                    if(endMonth-startMonth!=0){
                        months = String.valueOf(endMonth-startMonth);
                    }else{
                        months = "1";
                    }
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("name",name);
                    map.put("email",email);
                    map.put("contact",contact);
                    map.put("plan",plan);
                    map.put("exam",exam);
                    map.put("startDate",startDate);
                    map.put("endDate",endDate);
                    map.put("mentor",mentor);
                    map.put("salary",salary);
                    map.put("note",note);
                    map.put("monthsRemaining",months);
                    String mentoruid = mentorUIDs.get(mentorNames.indexOf(mentor));


                    map.put("mentorUID",mentoruid);
                    String menteeUID = mentors.child(mentoruid).child("mentees").push().getKey();
                    HashMap<String,Object> map1 = new HashMap<>();
                    map1.put("name",name);
                    map1.put("salary",salary);
                    map1.put("slot",slot);
                    map1.put("monthsRemaining",months);
                    map1.put("startDate", startDate);
                    map1.put("nextPaymentMonth",nextPaymentMonth);
                    map1.put("startMonth",String.valueOf(startMonth));
                    mentors.child(mentoruid).child("mentees").child(menteeUID).updateChildren(map1);

                    mDatabase.child("mentees").child(menteeUID).updateChildren(map, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
                            Toast.makeText(getActivity(), "Mentee Added", Toast.LENGTH_SHORT).show();
                            MenteeListFragment nextFrag= new MenteeListFragment();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.nav_host_fragment, nextFrag ); // give your fragment container id in first parameter
                            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                            transaction.commit();
                        }
                    });

                }else{
                    Toast.makeText(getActivity(), "Fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }
}