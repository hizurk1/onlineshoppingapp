package com.android.onlineshoppingapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.onlineshoppingapp.adapters.PagerAdapterProductImage;
import com.android.onlineshoppingapp.models.Product;
import com.android.onlineshoppingapp.models.ProductImage;
import com.android.onlineshoppingapp.models.cartProduct;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import me.relex.circleindicator.CircleIndicator;

public class ProductDetailActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private PagerAdapterProductImage pagerAdapterProductImage;
    private TextView tvProductName, tvProductDescription, tvProductSold, tvProductPrice;
    private RatingBar productRate;
    private ImageView ivBackToPrevious, ivHeartPD;
    private CardView cardLikePD, cardViewShoppingCartPD;
    private RelativeLayout layoutAddToCartPD;
    private Button btnAddToCartPD;
    private MaterialCardView cardSeeReview, cardSeeShopPD;

    public Product product = new Product();

    private FirebaseFirestore db;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            product = (Product) bundle.get("product");
            setData(product);
            setEvent();
        }

        viewPager = findViewById(R.id.viewPagerProductDetail);
        circleIndicator = findViewById(R.id.circleIndicatorProductDetail);

        //set image
        db.collection("productImages").document(product.getProductId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                Map<String, Object> map = documentSnapshot.getData();

                if (map != null) {
                    List<String> string = (List<String>) map.get("url");
                    pagerAdapterProductImage = new PagerAdapterProductImage(ProductDetailActivity.this, string);
                    viewPager.setAdapter(pagerAdapterProductImage);
                    circleIndicator.setViewPager(viewPager);
                    pagerAdapterProductImage.registerDataSetObserver(circleIndicator.getDataSetObserver());
                }
            }
        });

        //wish list
        db.collection("Users")
                .document(Objects.requireNonNull(fAuth.getCurrentUser()).getUid())
                .collection("Wishlists")
                .document(product.getProductId())
                .addSnapshotListener((value, error) -> {
                    if (error != null) Log.e(TAG, error.getMessage());

                    if (value != null && value.exists())  {
                        ivHeartPD.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.red_error)));
                    } else {
                        ivHeartPD.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.normal_grey)));
                    }
                });
    }
    // -------------- Function ------------------

    private void setEvent() {

        // init
        ivBackToPrevious = findViewById(R.id.ivBackToPrevious);
        cardLikePD = findViewById(R.id.cardLikePD);
        ivHeartPD = findViewById(R.id.ivHeartPD);
        cardViewShoppingCartPD = findViewById(R.id.cardViewShoppingCartPD);
        btnAddToCartPD = findViewById(R.id.btnAddToCartPD);
        cardSeeReview = findViewById(R.id.cardSeeReviewPD);
        layoutAddToCartPD = findViewById(R.id.layoutAddToCartPD);
        cardSeeShopPD = findViewById(R.id.cardSeeShopPD);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        db.collection("Users").document(fAuth.getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.getString("accountType").equals("Bán hàng")) {
                            layoutAddToCartPD.setVisibility(View.GONE);
                        } else {
                            layoutAddToCartPD.setVisibility(View.VISIBLE);
                        }
                    }
                });

        // click on back
        ivBackToPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // click on like

        cardLikePD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ivHeartPD.getImageTintList() == ColorStateList.valueOf(getResources().getColor(R.color.normal_grey))) {
                    addToWishlist();
                } else {
                    removeFromWishlist();
                }
            }
        });

        // click on cart
        cardViewShoppingCartPD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductDetailActivity.this,
                        ShoppingCartActivity.class));
            }
        });

        // click on add to cart
        btnAddToCartPD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Carts").document(fAuth.getCurrentUser().getUid())
                        .collection("Products")
                        .document(product.getProductId())
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Toast.makeText(ProductDetailActivity.this,
                                                "Sản phẩm đã được thêm vào giỏ hàng từ trước", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Map<String, Object> cartProduct = new HashMap<>();
                                        cartProduct.put("orderQuantity", 1);
                                        cartProduct.put("seller", product.getSeller());
                                        cartProduct.put("productRef", db.document("Products/" + product.getProductId() + "/"));
                                        db.collection("Carts")
                                                .document(Objects.requireNonNull(fAuth.getCurrentUser()).getUid())
                                                .collection("Products")
                                                .document(product.getProductId())
                                                .set(cartProduct)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(ProductDetailActivity.this,
                                                                "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });
            }
        });

        cardSeeShopPD.setOnClickListener(view -> {
            Intent intent = new Intent(ProductDetailActivity.this, MyStoreActivity.class);
            intent.putExtra("sellerOfProduct", product.getSeller());
            startActivity(intent);
        });

        cardSeeReview.setOnClickListener(view -> {
            Intent intent = new Intent(ProductDetailActivity.this, ReviewListActivity.class);
            intent.putExtra("product", product);
            startActivity(intent);
        });

    }

    private void removeFromWishlist() {
        db.collection("Users")
                .document(Objects.requireNonNull(fAuth.getCurrentUser()).getUid())
                .collection("Wishlists")
                .document(product.getProductId())
                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            db.collection("Products").document(product.getProductId())
                                    .update("likeNumber", FieldValue.increment(-1));
                            Toast.makeText(ProductDetailActivity.this, "Đã xoá khỏi yêu thích", Toast.LENGTH_SHORT).show();
                            ivHeartPD.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.normal_grey)));
                        }
                    }
                });
    }

    private void addToWishlist() {

        Map<String, Object> wishList = new HashMap<>();
        wishList.put("productRef", FirebaseFirestore.getInstance().document("Products/" + product.getProductId() + "/"));

        db.collection("Users").document(fAuth.getCurrentUser().getUid())
                .collection("Wishlists")
                .document(product.getProductId())
                .set(wishList)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        db.collection("Products").document(product.getProductId())
                                .update("likeNumber", FieldValue.increment(1));
                        ivHeartPD.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.red_error)));
                        Toast.makeText(ProductDetailActivity.this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("wishList", Objects.requireNonNull(task.getException()).getMessage());
                    }
                });

    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void setData(Product product) {

        tvProductName = findViewById(R.id.tvProductNamePD);
        tvProductDescription = findViewById(R.id.tvProductDescriptionPD);
        tvProductPrice = findViewById(R.id.tvProductPricePD);
        tvProductSold = findViewById(R.id.tvProductSoldPD);
        productRate = findViewById(R.id.ratePD);

        tvProductName.setText(product.getProductName());
        tvProductDescription.setText(product.getDescription());
        tvProductSold.setText("Đã bán " + product.getQuantitySold());
        tvProductPrice.setText(String.format("%,d", product.getProductPrice()) + "đ");
        productRate.setRating(product.getRate());
    }

    private List<ProductImage> getListPhoto(Product product) {

        List<ProductImage> imageList = new ArrayList<>();

        imageList.add(new ProductImage(String.valueOf(R.drawable.logoapp)));

        return imageList;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}