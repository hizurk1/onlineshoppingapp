package com.android.onlineshoppingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import com.android.onlineshoppingapp.adapters.FollowAdapter;
import com.android.onlineshoppingapp.models.Following;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ListOfFollowActivity extends AppCompatActivity {

    private ImageView ivBack;
    private RecyclerView recyclerView;

    private FollowAdapter adapter;
    private List<Following> followingList;

    private FirebaseFirestore db;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_follow);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        ivBack = findViewById(R.id.ivBackListOfFollow);
        recyclerView = findViewById(R.id.rvListOfFollow);

        followingList = new ArrayList<>();
        followingList.add(new Following("1", "Shop hey"));
        followingList.add(new Following("2", "Shop wow"));
        followingList.add(new Following("3", "Shop uwu"));
        followingList.add(new Following("4", "Shop 123"));
        followingList.add(new Following("5", "Shop qweqwe"));

        adapter = new FollowAdapter(followingList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

    }
}