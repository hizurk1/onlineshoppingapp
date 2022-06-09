package com.android.onlineshoppingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import com.android.onlineshoppingapp.adapters.WriteReviewAdapter;
import com.android.onlineshoppingapp.models.Review;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class WriteReviewProductActivity extends AppCompatActivity {

    private RecyclerView rvWriteReview;
    private ImageView ivBack;

    private FirebaseAuth fAuth;
    private FirebaseFirestore db;

    private WriteReviewAdapter reviewAdapter;
    private List<Review> reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review_product);

        // init
        rvWriteReview = findViewById(R.id.rvWriteReview);
        ivBack = findViewById(R.id.ivBackWriteReview);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        reviewList = new ArrayList<>();

        reviewAdapter = new WriteReviewAdapter(reviewList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvWriteReview.setLayoutManager(linearLayoutManager);
        rvWriteReview.setAdapter(reviewAdapter);

    }
}