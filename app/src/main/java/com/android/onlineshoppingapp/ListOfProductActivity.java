package com.android.onlineshoppingapp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.onlineshoppingapp.adapters.RecyclerViewAdapterProduct;
import com.android.onlineshoppingapp.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ListOfProductActivity extends AppCompatActivity {

    private TextView tvTitle;
    private RecyclerView rvPopularProducts, rvRecentlyProducts, rvAllProducts;
    private RecyclerViewAdapterProduct recyclerViewAdapterProduct;
    private List<Product> popularProductList, recentlyProductList, allProductList;

    private FirebaseFirestore db;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_product);

        // init
        ImageView ivBack = findViewById(R.id.ivBackListOfProduct);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        tvTitle = findViewById(R.id.tvTitleListOfProduct);

        // click on back
        ivBack.setOnClickListener(view -> onBackPressed());

        String seeMoreStatus = getIntent().getStringExtra("see_more_product");
        String seller = getIntent().getStringExtra("sellerDetail");
        switch (seeMoreStatus) {
            case "popular":
                showPopularProduct(seller);
                break;
            case "recently":
                showRecentlyProduct(seller);
                break;
            case "all":
                showAllProduct(seller);
                break;
            case "search":
                searchProduct(getIntent().getStringExtra("searchString"));
                break;
            case "favorite":
                showFavoriteProduct();
                break;
            case "purchasedProduct":
                showPurchasedProduct();
                break;
            default:
                showProductCategory(seeMoreStatus);
                break;
        }

    }

    private void showProductCategory(String category) {
        tvTitle.setText(category);
        recentlyProductList = new ArrayList<>();
        rvRecentlyProducts = findViewById(R.id.rvListOfProduct);

        db.collection("Products")
                .whereEqualTo("category", category)
                .get()
                .addOnCompleteListener(task -> {
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
                });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showPurchasedProduct() {
        tvTitle.setText("Sản phẩm đã mua");
        rvPopularProducts = findViewById(R.id.rvListOfProduct);
        db.collection("Users")
                .document(Objects.requireNonNull(fAuth.getCurrentUser()).getUid())
                .collection("boughtProducts")
                .addSnapshotListener((value, error) -> {
                    if (error != null)
                        Log.e("showPurchasedProduct", error.getMessage());

                    if (value != null) {
                        List<Product> productList = new ArrayList<>();
                        List<String> productIdList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : value) {
                            productIdList.add(documentSnapshot.getId());
                        }

                        //get product info
                        for (String item : productIdList) {
                            Log.e("", item);
                            DocumentReference productRef = db.collection("Products").document(item);
                            productRef.addSnapshotListener((value1, error1) -> {
                                if (value1 != null) {
                                    Product product = value1.toObject(Product.class);
                                    if (product != null) {
                                        product.setProductId(value1.getId());
                                    }
                                    productList.add(product);
                                    recyclerViewAdapterProduct.notifyDataSetChanged();
                                }
                            });
                        }
                        // setup recyclerview: recently products
                        recyclerViewAdapterProduct = new RecyclerViewAdapterProduct(productList, ListOfProductActivity.this);
                        rvPopularProducts.setLayoutManager(new GridLayoutManager(ListOfProductActivity.this, 2));
                        rvPopularProducts.setAdapter(recyclerViewAdapterProduct);

                    }
                });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showFavoriteProduct() {
        tvTitle.setText("Sản phẩm yêu thích");
        rvPopularProducts = findViewById(R.id.rvListOfProduct);
        db.collection("Users")
                .document(Objects.requireNonNull(fAuth.getCurrentUser()).getUid())
                .collection("Wishlists")
                .addSnapshotListener((value, error) -> {
                    if (error != null)
                        Log.e("showFavoriteProduct", error.getMessage());

                    if (value != null) {
                        List<Product> productList = new ArrayList<>();
                        List<String> productIdList = new ArrayList<>();

                        for (DocumentSnapshot documentSnapshot : value)
                            productIdList.add(documentSnapshot.getId());
                        for (String item : productIdList) {
                            db.collection("Products")
                                    .document(item)
                                    .addSnapshotListener((value1, error1) -> {
                                        if (error1 != null)
                                            Log.e("showFavoriteProduct", error1.getMessage());

                                        if (value1 != null) {
                                            Product product = value1.toObject(Product.class);
                                            assert product != null;
                                            product.setProductId(value1.getId());
                                            productList.add(product);
                                            recyclerViewAdapterProduct.notifyDataSetChanged();
                                        }
                                    });
                        }
                        // setup recyclerview
                        recyclerViewAdapterProduct = new RecyclerViewAdapterProduct(productList, ListOfProductActivity.this);
                        rvPopularProducts.setLayoutManager(new GridLayoutManager(ListOfProductActivity.this, 2));
                        rvPopularProducts.setAdapter(recyclerViewAdapterProduct);
                    }

                });
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
                            .filter(str -> str.getProductName().toLowerCase().trim().contains(searchString.toLowerCase().trim()))
                            .collect(Collectors.toList());

                    // setup recyclerview: recently products
                    recyclerViewAdapterProduct = new RecyclerViewAdapterProduct(productList, ListOfProductActivity.this);
                    rvPopularProducts.setLayoutManager(new GridLayoutManager(ListOfProductActivity.this, 2));
                    rvPopularProducts.setAdapter(recyclerViewAdapterProduct);
                });
    }

    private void showPopularProduct(String seller) {

        tvTitle.setText("Sản phẩm phổ biến");
        popularProductList = new ArrayList<>();
        rvPopularProducts = findViewById(R.id.rvListOfProduct);

        db.collection("Products")
                .whereEqualTo("seller", (seller.equals("own")) ? Objects.requireNonNull(fAuth.getCurrentUser()).getUid() : seller)
                .orderBy("quantitySold", Query.Direction.DESCENDING)
                .orderBy("productPrice", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
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
                });
    }

    private void showRecentlyProduct(String seller) {

        tvTitle.setText("Sản phẩm gần đây");
        recentlyProductList = new ArrayList<>();
        rvRecentlyProducts = findViewById(R.id.rvListOfProduct);

        db.collection("Products")
                .whereEqualTo("seller", (seller.equals("own")) ? Objects.requireNonNull(fAuth.getCurrentUser()).getUid() : seller)
                .orderBy("quantitySold", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
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
                });
    }

    private void showAllProduct(String seller) {

        tvTitle.setText("Tất cả sản phẩm");
        allProductList = new ArrayList<>();
        rvAllProducts = findViewById(R.id.rvListOfProduct);

        db.collection("Products")
                .whereEqualTo("seller", (seller.equals("own")) ? Objects.requireNonNull(fAuth.getCurrentUser()).getUid() : seller)
                .get()
                .addOnCompleteListener(task -> {
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
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}