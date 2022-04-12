package com.android.onlineshoppingapp.fragments;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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
import com.android.onlineshoppingapp.models.UserInformation;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfilePageFragment extends Fragment {

    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private FirebaseAuth fAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;

    private TextView textViewFullname, tvUserRanking;
    private ImageView ivSettings, ivShoppingCart, ivAvatar, ivWallet, ivChecking, ivDelivery, ivFeedback;
    private CardView cardCoin, cardCoupon, cardSeen, cardLove, cardFollow, cardGift, cardPurchasedProduct, cardSupport;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_page, container, false);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        // init
        textViewFullname = view.findViewById(R.id.tvFullName);
        ivSettings = view.findViewById(R.id.ivSettings);
        ivShoppingCart = view.findViewById(R.id.ivShoppingCart);
        ivAvatar = view.findViewById(R.id.ivAvatar);
        tvUserRanking = view.findViewById(R.id.tvUserRanking);
        cardCoin = view.findViewById(R.id.cardCoin);
        cardCoupon = view.findViewById(R.id.cardCoupon);
        ivWallet = view.findViewById(R.id.ivWallet);
        ivChecking = view.findViewById(R.id.ivChecking);
        ivDelivery = view.findViewById(R.id.ivDelivery);
        ivFeedback = view.findViewById(R.id.ivFeedback);
        cardSeen = view.findViewById(R.id.cardSeen);
        cardLove = view.findViewById(R.id.cardLove);
        cardFollow = view.findViewById(R.id.cardFollow);
        cardGift = view.findViewById(R.id.cardGift);
        cardPurchasedProduct = view.findViewById(R.id.cardPurchasedProduct);
        cardSupport = view.findViewById(R.id.cardSupport);

        // click on settings button
        ivSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        // click on shopping cart
        ivShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        // change name
        textViewFullname.setText("");
        db.collection("Users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + documentSnapshot.getData());
                        UserInformation userInformation = documentSnapshot.toObject(UserInformation.class);
                        textViewFullname.setText(userInformation.getLastName() + " " + userInformation.getFirstName());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        // click on card coin
        cardCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // click on card coupon
        cardCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // click on wallet
        ivWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // click on checking
        ivChecking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // click on delivery
        ivDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // click on feedback
        ivFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // click on card seen
        cardSeen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // click on card love
        cardLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // click on card follow
        cardFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // click on card gift
        cardGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // click on card purchased product
        cardPurchasedProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // click on card support
        cardSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


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