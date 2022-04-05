package com.android.onlineshoppingapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.onlineshoppingapp.LoginActivity;
import com.android.onlineshoppingapp.R;
import com.android.onlineshoppingapp.SettingsActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfilePageFragment extends Fragment {

    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;

    private TextView textViewFullname;
    private ImageView ivSettings;
    private FirebaseAuth fAuth;
    private FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_page, container, false);

        fAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        textViewFullname = view.findViewById(R.id.tvFullName);
        textViewFullname.setText(user.getDisplayName());


        ivSettings = view.findViewById(R.id.btnSettings);
        ivSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        // return view
        return view;
    }

    //--------------- Function ----------------

    private void checkSignIn() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        gsc = GoogleSignIn.getClient(getActivity(), gso);

        // get data
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if (account != null) {
            String userName = account.getDisplayName();
            String userEmail = account.getEmail();
            Toast.makeText(getActivity(), "Name: " + userName + "\nEmail: " + userEmail, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void signOut() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                fAuth.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
    }

}