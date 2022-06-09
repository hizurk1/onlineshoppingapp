package com.android.onlineshoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.onlineshoppingapp.adapters.ReviewListAdapter;
import com.android.onlineshoppingapp.models.Review;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ReviewListActivity extends AppCompatActivity {

    private ImageView ivBack;
    private TextView tvRate, tvNumberOfReview;
    private RatingBar ratingbarMain;
    private ChipGroup chipGroup;
    private RecyclerView recyclerView;

    private List<Review> reviewList;
    private ReviewListAdapter reviewAdapter;

    private FirebaseAuth fAuth;
    private FirebaseFirestore db;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        // init
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        ivBack = findViewById(R.id.ivBackReviewList);
        tvRate = findViewById(R.id.tvRateReviewList);
        ratingbarMain = findViewById(R.id.ratingbarReviewList);
        tvNumberOfReview = findViewById(R.id.tvNumberOfReview);
        chipGroup = findViewById(R.id.chipGroupReviewList);
        recyclerView = findViewById(R.id.rvReviewList);

        // click on back
        ivBack.setOnClickListener(view -> {
            onBackPressed();
        });

        // set up recyclerview
        reviewList = new ArrayList<>();
        reviewList.add(new Review("1", "Hieu bui", "Day la noi dung danh gia cuc ky hay", 4, "08/06/2022 11:00:00"));
        reviewList.add(new Review("2", "Hieu uwu", "Day la noi dung danh gia khong hay lam", 5, "05/01/2022 11:00:00"));
        reviewList.add(new Review("3", "Hieu", "Day la noi dung danh gia khong hay lam", 2, "05/02/2022 11:00:00"));
        reviewList.add(new Review("4", "Hieu ko uwu", "Day la noi dung danh gia khong hay lam", 3, "05/01/2021 11:00:00"));
        reviewList.add(new Review("5", "Hieu ko co uwu", "Day la noi dung danh gia khong hay lam", 1, "05/01/2020 11:00:00"));

        reviewAdapter = new ReviewListAdapter(reviewList, ReviewListActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(reviewAdapter);

        // set values
        float rateAvg = getIntent().getFloatExtra("productRate", 0);
        tvRate.setText(String.valueOf(rateAvg));
        tvNumberOfReview.setText(String.valueOf(reviewList.size()));
        ratingbarMain.setRating(rateAvg);

        // chips
        if (chipGroup.getCheckedChipId() == R.id.chipNewestReviewList) {
            showNewestReview();
        }

        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            switch (group.getCheckedChipId()) {
                case R.id.chipNewestReviewList:
                    showNewestReview();
                    break;
                case R.id.chipOldestReviewList:
                    showOldestReview();
                    break;
                case R.id.chip5ReviewList:
                    showFiveStarReview();
                    break;
                case R.id.chip4ReviewList:
                    showFourStarReview();
                    break;
                case R.id.chip3ReviewList:
                    showThreeStarReview();
                    break;
                case R.id.chip2ReviewList:
                    showTwoStarReview();
                    break;
                case R.id.chip1ReviewList:
                    showOneStarReview();
                    break;
            }
        });

    }

    private void showOneStarReview() {
        Toast.makeText(this, "1 star", Toast.LENGTH_SHORT).show();
    }

    private void showTwoStarReview() {
        Toast.makeText(this, "2 stars", Toast.LENGTH_SHORT).show();
    }

    private void showThreeStarReview() {
        Toast.makeText(this, "3 stars", Toast.LENGTH_SHORT).show();
    }

    private void showFourStarReview() {
        Toast.makeText(this, "4 stars", Toast.LENGTH_SHORT).show();
    }

    private void showFiveStarReview() {
        Toast.makeText(this, "5 stars", Toast.LENGTH_SHORT).show();
    }

    private void showOldestReview() {
        Toast.makeText(this, "Oldest checked", Toast.LENGTH_SHORT).show();
    }

    private void showNewestReview() {
        Toast.makeText(this, "Newest checked", Toast.LENGTH_SHORT).show();
    }
}