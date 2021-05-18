package com.rankersprep.rankerspreplearning.ui.notifications;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rankersprep.rankerspreplearning.R;
import com.rankersprep.rankerspreplearning.databinding.FragmentPaymentHistoryBinding;


public class PaymentHistoryFragment extends Fragment {

    FragmentPaymentHistoryBinding historyBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        historyBinding = FragmentPaymentHistoryBinding.inflate(inflater,container,false);
        View root = historyBinding.getRoot();



        return root;
    }
}