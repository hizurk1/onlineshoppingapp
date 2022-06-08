package com.android.onlineshoppingapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.android.onlineshoppingapp.adapters.RecyclerViewAdapterProduct;
import com.android.onlineshoppingapp.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ListOfProductActivity extends AppCompatActivity {

    private ImageView ivBack;
    private RecyclerView rvPopularProducts, rvRecentlyProducts, rvAllProducts;
    private RecyclerViewAdapterProduct recyclerViewAdapterProduct;
    private List<Product> popularProductList, recentlyProductList, allProductList;

    private FirebaseFirestore db;
    private FirebaseAuth fAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_product);

        // init
        ivBack = findViewById(R.id.ivBackListOfProduct);

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        // click on back
        ivBack.setOnClickListener(view -> {
            onBackPressed();
        });

        String seeMoreStatus = getIntent().getStringExtra("see_more_product");
        switch (seeMoreStatus) {
            case "popular":
                showPopularProduct();
                break;
            case "recently":
                showRecentlyProduct();
                break;
            case "all":
                showAllProduct();
                break;
            case "search":
                searchProduct(getIntent().getStringExtra("searchString"));
                break;
        }

    }

    private void searchProduct(String searchString) {
        rvPopularProducts = findViewById(R.id.rvListOfProduct);
        db.collection("Products")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Product> productList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Product product = documentSnapshot.toObject(Product.class);
                        assert product != null;
                        product.setProductId(documentSnapshot.getId());
                        productList.add(product);
                    }
                    productList = productList.stream()
                            .filter(str -> str.getProductName().trim().contains(searchString.trim()) ||
                                    str.getProductName().trim().contains(searchString.toLowerCase().trim()) ||
                                    str.getProductName().trim().contains(searchString.toUpperCase().trim()))
                            .collect(Collectors.toList());

                    // setup recyclerview: recently products
                    recyclerViewAdapterProduct = new RecyclerViewAdapterProduct(productList, ListOfProductActivity.this);
                    rvPopularProducts.setLayoutManager(new GridLayoutManager(ListOfProductActivity.this, 2));
                    rvPopularProducts.setAdapter(recyclerViewAdapterProduct);
                });
    }

    private void showPopularProduct() {

        popularProductList = new ArrayList<>();
        rvPopularProducts = findViewById(R.id.rvListOfProduct);

        db.collection("Products")
                .whereEqualTo("seller", fAuth.getCurrentUser().getUid())
                .orderBy("quantitySold", Query.Direction.DESCENDING)
                .orderBy("productPrice", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Product product = document.toObject(Product.class);
                                product.setProductId(document.getId());
                                popularProductList.add(product);
                            }

                            // setup recyclerview: recently products
                            recyclerViewAdapterProduct = new RecyclerViewAdapterProduct(popularProductList, ListOfProductActivity.this);
                            rvPopularProducts.setLayoutManager(new GridLayoutManager(ListOfProductActivity.this, 2));
                            rvPopularProducts.setAdapter(recyclerViewAdapterProduct);
                        } else {
                            Log.e(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void showRecentlyProduct() {

        recentlyProductList = new ArrayList<>();
        rvRecentlyProducts = findViewById(R.id.rvListOfProduct);

        db.collection("Products")
                .whereEqualTo("seller", fAuth.getCurrentUser().getUid())
                .orderBy("quantitySold", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Product product = document.toObject(Product.class);
                                product.setProductId(document.getId());
                                recentlyProductList.add(product);
                            }

                            // setup recyclerview: recently products
                            recyclerViewAdapterProduct = new RecyclerViewAdapterProduct(recentlyProductList, ListOfProductActivity.this);
                            rvRecentlyProducts.setLayoutManager(new GridLayoutManager(ListOfProductActivity.this, 2));
                            rvRecentlyProducts.setAdapter(recyclerViewAdapterProduct);
                        } else {
                            Log.e(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void showAllProduct() {

        allProductList = new ArrayList<>();
        rvAllProducts = findViewById(R.id.rvListOfProduct);

        db.collection("Products")
                .whereEqualTo("seller", fAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Product product = document.toObject(Product.class);
                                product.setProductId(document.getId());
                                allProductList.add(product);
                            }

                            // setup recyclerview: recently products
                            recyclerViewAdapterProduct = new RecyclerViewAdapterProduct(allProductList, ListOfProductActivity.this);
                            rvAllProducts.setLayoutManager(new GridLayoutManager(ListOfProductActivity.this, 2));
                            rvAllProducts.setAdapter(recyclerViewAdapterProduct);
                        } else {
                            Log.e(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}