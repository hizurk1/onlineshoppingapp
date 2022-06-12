package com.android.onlineshoppingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;

import com.android.onlineshoppingapp.adapters.FollowAdapter;
import com.android.onlineshoppingapp.models.Following;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListOfFollowActivity extends AppCompatActivity {

    private ImageView ivBack;
    private RecyclerView recyclerView;

    private FollowAdapter adapter;
    private List<Following> followingList;

    private FirebaseFirestore db;
    private FirebaseAuth fAuth;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_follow);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        ivBack = findViewById(R.id.ivBackListOfFollow);
        recyclerView = findViewById(R.id.rvListOfFollow);

        followingList = new ArrayList<>();

        ivBack.setOnClickListener(view -> onBackPressed());

        //get following
        db.collection("Users")
                .document(Objects.requireNonNull(fAuth.getCurrentUser()).getUid())
                .collection("Following")
                .addSnapshotListener((value, error) -> {
                    followingList.clear();
                    if (value != null) {
                        List<String> sellerIdList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : value)
                            sellerIdList.add(documentSnapshot.getId());


                        if (!sellerIdList.isEmpty()) {
                            for (String item : sellerIdList) {
                                db.collection("Users")
                                        .document(item)
                                        .get()
                                        .addOnSuccessListener(documentSnapshot -> {
                                           Following following = new Following(documentSnapshot.getId(),
                                                   documentSnapshot.getString("lastName") + " " + documentSnapshot.getString("firstName"),
                                                   documentSnapshot.getString("avatarUrl"));
                                           followingList.add(following);
                                           adapter.notifyDataSetChanged();
                                        });
                            }
                        }
                    }
                });

        adapter = new FollowAdapter(followingList, ListOfFollowActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

    }
}