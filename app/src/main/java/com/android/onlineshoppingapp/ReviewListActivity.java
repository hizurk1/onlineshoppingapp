package com.android.onlineshoppingapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.onlineshoppingapp.adapters.ReviewListAdapter;
import com.android.onlineshoppingapp.models.Product;
import com.android.onlineshoppingapp.models.Review;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ReviewListActivity extends AppCompatActivity {

    private List<Review> reviewList;
    private ReviewListAdapter reviewAdapter;
    private TextView tvNumberOfReview;

    private FirebaseFirestore db;
    private Product product;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        // init
        db = FirebaseFirestore.getInstance();

        ImageView ivBack = findViewById(R.id.ivBackReviewList);
        TextView tvRate = findViewById(R.id.tvRateReviewList);
        RatingBar ratingbarMain = findViewById(R.id.ratingbarReviewList);
        tvNumberOfReview = findViewById(R.id.tvNumberOfReview);
        ChipGroup chipGroup = findViewById(R.id.chipGroupReviewList);
        RecyclerView recyclerView = findViewById(R.id.rvReviewList);
        product = (Product) getIntent().getSerializableExtra("product");

        // click on back
        ivBack.setOnClickListener(view -> onBackPressed());

        // set up recyclerview
        reviewList = new ArrayList<>();

        reviewAdapter = new ReviewListAdapter(reviewList, ReviewListActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(reviewAdapter);

        // set values
        float rateAvg = Math.round(product.getRate() * 10) / (float) 10;
        tvRate.setText(String.valueOf(rateAvg));
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

    @SuppressLint("NotifyDataSetChanged")
    private void showOneStarReview() {
        reviewList.clear();
        //get reviews
        AsyncTask.execute(() -> db.collection("Products")
                .document(product.getProductId())
                .collection("productRates")
                .whereGreaterThanOrEqualTo("rate", 1)
                .whereLessThan("rate", 2)
                .orderBy("rate", Query.Direction.DESCENDING)
                .orderBy("createdDate", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    reviewList.clear();
                    if (error != null)
                        Log.e("get reviews", error.getMessage());
                    if (value != null) {

                        for (DocumentSnapshot documentSnapshot : value) {
                            reviewList.add(documentSnapshot.toObject(Review.class));
                        }
                        reviewAdapter.notifyDataSetChanged();
                    }
                }));
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showTwoStarReview() {
        reviewList.clear();
        //get reviews
        AsyncTask.execute(() -> db.collection("Products")
                .document(product.getProductId())
                .collection("productRates")
                .whereGreaterThanOrEqualTo("rate", 2)
                .whereLessThan("rate", 3)
                .orderBy("rate", Query.Direction.DESCENDING)
                .orderBy("createdDate", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    reviewList.clear();
                    if (error != null)
                        Log.e("get reviews", error.getMessage());
                    if (value != null) {

                        for (DocumentSnapshot documentSnapshot : value) {
                            reviewList.add(documentSnapshot.toObject(Review.class));
                        }
                        reviewAdapter.notifyDataSetChanged();
                    }
                }));
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showThreeStarReview() {
        reviewList.clear();
        //get reviews
        AsyncTask.execute(() -> db.collection("Products")
                .document(product.getProductId())
                .collection("productRates")
                .whereGreaterThanOrEqualTo("rate", 3)
                .whereLessThan("rate", 4)
                .orderBy("rate", Query.Direction.DESCENDING)
                .orderBy("createdDate", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    reviewList.clear();
                    if (error != null)
                        Log.e("get reviews", error.getMessage());
                    if (value != null) {

                        for (DocumentSnapshot documentSnapshot : value) {
                            reviewList.add(documentSnapshot.toObject(Review.class));
                        }
                        reviewAdapter.notifyDataSetChanged();
                    }
                }));
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showFourStarReview() {
        reviewList.clear();
        //get reviews
        AsyncTask.execute(() -> db.collection("Products")
                .document(product.getProductId())
                .collection("productRates")
                .whereGreaterThanOrEqualTo("rate", 4)
                .whereLessThan("rate", 5)
                .orderBy("rate", Query.Direction.DESCENDING)
                .orderBy("createdDate", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    reviewList.clear();
                    if (error != null)
                        Log.e("get reviews", error.getMessage());
                    if (value != null) {

                        for (DocumentSnapshot documentSnapshot : value) {
                            reviewList.add(documentSnapshot.toObject(Review.class));
                        }
                        reviewAdapter.notifyDataSetChanged();
                    }
                }));
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showFiveStarReview() {
        reviewList.clear();
        //get reviews
        AsyncTask.execute(() -> db.collection("Products")
                .document(product.getProductId())
                .collection("productRates")
                .whereGreaterThanOrEqualTo("rate", 5)
                .orderBy("rate", Query.Direction.DESCENDING)
                .orderBy("createdDate", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    reviewList.clear();
                    if (error != null)
                        Log.e("get reviews", error.getMessage());
                    if (value != null) {

                        for (DocumentSnapshot documentSnapshot : value) {
                            reviewList.add(documentSnapshot.toObject(Review.class));
                        }
                        reviewAdapter.notifyDataSetChanged();
                    }
                }));
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showOldestReview() {
        reviewList.clear();
        //get reviews
        AsyncTask.execute(() -> db.collection("Products")
                .document(product.getProductId())
                .collection("productRates")
                .orderBy("createdDate", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    reviewList.clear();
                    if (error != null)
                        Log.e("get reviews", error.getMessage());
                    if (value != null) {

                        for (DocumentSnapshot documentSnapshot : value) {
                            reviewList.add(documentSnapshot.toObject(Review.class));
                        }
                        reviewAdapter.notifyDataSetChanged();
                    }
                }));
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showNewestReview() {
        reviewList.clear();
        //get reviews
        AsyncTask.execute(() -> db.collection("Products")
                .document(product.getProductId())
                .collection("productRates")
                .orderBy("createdDate", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    reviewList.clear();
                    if (error != null)
                        Log.e("get reviews", error.getMessage());
                    if (value != null) {

                        for (DocumentSnapshot documentSnapshot : value) {
                            reviewList.add(documentSnapshot.toObject(Review.class));
                        }
                        tvNumberOfReview.setText(String.valueOf(reviewList.size()));
                        reviewAdapter.notifyDataSetChanged();
                    }
                }));
    }
}