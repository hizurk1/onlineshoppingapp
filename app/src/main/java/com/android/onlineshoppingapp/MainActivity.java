package com.android.onlineshoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.onlineshoppingapp.databinding.ActivityMainBinding;
import com.android.onlineshoppingapp.fragments.CategoryPageFragment;
import com.android.onlineshoppingapp.fragments.HomePageFragment;
import com.android.onlineshoppingapp.fragments.ProfilePageFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private TextView textViewInfoGg, textViewInfoFb;

    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get data from Google sign in account
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null) {
            String personName = signInAccount.getDisplayName();
            String personEmail = signInAccount.getEmail(); // furthermore we can get image, etc.

//            textViewInfoGg = findViewById(R.id.txtInfoGg);
//            textViewInfoGg.setText("Name: " + personName + "\nEmail: " + personEmail);
        }

        // show bottom navigation view
        replaceFragment(new HomePageFragment());
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home_nav:
                    replaceFragment(new HomePageFragment());
                    break;
                case R.id.category_nav:
                    replaceFragment(new CategoryPageFragment());
                    break;
                case R.id.profile_nav:
                    replaceFragment(new ProfilePageFragment());
                    break;
            }
            return true;
        });

    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_main, fragment)
                .commit();
    }

//    private void signOutAccount() {
//        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(Task<Void> task) {
//                // navigate to Login activity
//                finish();
//                startActivity(new Intent(MainActivity.this, LoginActivity.class));
//            }
//        });
//    }

}