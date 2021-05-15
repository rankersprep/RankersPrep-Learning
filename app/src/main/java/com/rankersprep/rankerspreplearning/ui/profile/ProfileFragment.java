package com.rankersprep.rankerspreplearning.ui.profile;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rankersprep.rankerspreplearning.MainActivity;
import com.rankersprep.rankerspreplearning.R;
import com.rankersprep.rankerspreplearning.databinding.FragmentHomeBinding;
import com.rankersprep.rankerspreplearning.databinding.FragmentProfileBinding;

import org.jetbrains.annotations.NotNull;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    static String quote;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.background));

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        root.setSystemUiVisibility(root.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        FirebaseDatabase.getInstance().goOnline();

        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("quote").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    quote = task.getResult().getValue().toString();
                    binding.loremIpsum.setText(quote);
                }else{
                    Log.i("error",task.getException().getMessage());
                }
            }
        });

        binding.gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:sagar@rankersprep.com")));
            }
        });

        binding.instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.instagram.com/rankersprep/");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.instagram.com/rankersprep/")));
                }
            }
        });

        binding.linkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.linkedin.com/company/rankersprep-education");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.linkedin.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.linkedin.com/company/rankersprep-education")));
                }
            }
        });


        binding.edit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.quote1.setVisibility(View.INVISIBLE);
                binding.quote2.setVisibility(View.INVISIBLE);
                binding.edit1.setVisibility(View.INVISIBLE);
                binding.loremIpsum.setVisibility(View.INVISIBLE);
                binding.editQuote.setVisibility(View.VISIBLE);
                binding.editQuote.setText(binding.loremIpsum.getText().toString());
                binding.doneTV.setVisibility(View.VISIBLE);
                binding.previousAnnouncements.setVisibility(View.INVISIBLE);
                binding.logOut.setVisibility(View.INVISIBLE);
            }
        });

        binding.doneTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.quote1.setVisibility(View.VISIBLE);
                binding.quote2.setVisibility(View.VISIBLE);
                binding.edit1.setVisibility(View.VISIBLE);
                binding.loremIpsum.setText(binding.editQuote.getText().toString());
                binding.loremIpsum.setVisibility(View.VISIBLE);
                binding.editQuote.setVisibility(View.INVISIBLE);
                binding.doneTV.setVisibility(View.INVISIBLE);
                mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("quote").setValue(binding.editQuote.getText().toString());
                binding.previousAnnouncements.setVisibility(View.VISIBLE);
                binding.logOut.setVisibility(View.VISIBLE);
            }
        });

        binding.logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }
}