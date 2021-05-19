package com.rankersprep.rankerspreplearning.ui.notifications;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rankersprep.rankerspreplearning.R;
import com.rankersprep.rankerspreplearning.databinding.FragmentNotificationsBinding;
import com.rankersprep.rankerspreplearning.databinding.FragmentUpcomingPaymentBinding;


public class UpcomingPaymentFragment extends Fragment {

    FragmentUpcomingPaymentBinding upcomingPaymentBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        upcomingPaymentBinding = FragmentUpcomingPaymentBinding.inflate(inflater,container,false);

        View root = upcomingPaymentBinding.getRoot();

        return root;
    }
}