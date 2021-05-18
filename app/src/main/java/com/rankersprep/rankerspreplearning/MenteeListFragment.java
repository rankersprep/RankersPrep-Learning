package com.rankersprep.rankerspreplearning;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rankersprep.rankerspreplearning.databinding.FragmentMenteeListBinding;


public class MenteeListFragment extends Fragment {

    FragmentMenteeListBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.background));

        binding = FragmentMenteeListBinding.inflate(inflater, container, false);
        View root=binding.getRoot();
        root.setSystemUiVisibility(root.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

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

        return root;
    }
}