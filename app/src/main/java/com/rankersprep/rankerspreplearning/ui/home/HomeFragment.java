package com.rankersprep.rankerspreplearning.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.rankersprep.rankerspreplearning.AdminActivity;
import com.rankersprep.rankerspreplearning.AnnouncementActivity;
import com.rankersprep.rankerspreplearning.MenteeListFragment;
import com.rankersprep.rankerspreplearning.MentorListFragment;
import com.rankersprep.rankerspreplearning.R;
import com.rankersprep.rankerspreplearning.databinding.FragmentHomeBinding;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.themeColor));

        binding.createAnAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getContext(), AnnouncementActivity.class);
                startActivity(intent);
            }
        });

        Log.i("Container", container.toString());

        binding.mentors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MentorListFragment nextFrag= new MentorListFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, nextFrag ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });

        binding.mentees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenteeListFragment nextFrag= new MenteeListFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, nextFrag ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });
        Log.i("snackbar",String.valueOf(AdminActivity.b));
        if(AdminActivity.b){
            CoordinatorLayout view1 = binding.placeSnackBar;
            Snackbar snackbar = Snackbar
                    .make(binding.placeSnackBar, "Your announcement was sent successfully", Snackbar.LENGTH_LONG);
            snackbar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            });
            snackbar.setBackgroundTint(getResources().getColor(R.color.grey));
            snackbar.setActionTextColor(getResources().getColor(R.color.themeColor));
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(14);
            textView.setMinimumHeight(175);
            textView.setGravity(Gravity.CENTER);
            snackbar.show();
            AdminActivity.b=false;
        }



        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}