package com.android.onlineshoppingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.android.onlineshoppingapp.adapters.WriteReviewAdapter;
import com.android.onlineshoppingapp.models.Product;
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

        ivBack.setOnClickListener(view -> onBackPressed());

        reviewList = new ArrayList<>();
        List<String> productIdList = (List<String>) getIntent().getSerializableExtra("productList");
        String orderId = getIntent().getStringExtra("orderId");
        List<Product> productList = new ArrayList<>();
        reviewAdapter = new WriteReviewAdapter(productList, orderId);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvWriteReview.setLayoutManager(linearLayoutManager);
        rvWriteReview.setAdapter(reviewAdapter);
        for (String item : productIdList) {
            db.collection("Products")
                    .document(item)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                       Product product = documentSnapshot.toObject(Product.class);
                       product.setProductId(documentSnapshot.getId());
                       productList.add(product);
                       reviewAdapter.notifyDataSetChanged();
                    });
        }
    }
}