package com.rankersprep.rankerspreplearning;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MentorActivity extends AppCompatActivity {

    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.RankersTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor);
        BottomNavigationView navView = findViewById(R.id.nav_view_mentor);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home_mentor, R.id.navigation_profile_mentor, R.id.navigation_notifications_mentor)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_mentor);

        NavigationUI.setupWithNavController(navView, navController);




    }

}