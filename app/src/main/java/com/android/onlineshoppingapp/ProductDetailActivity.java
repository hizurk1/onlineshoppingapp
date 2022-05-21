package com.android.onlineshoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.onlineshoppingapp.adapters.PagerAdapterProductImage;
import com.android.onlineshoppingapp.models.Product;
import com.android.onlineshoppingapp.models.ProductImage;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;

public class ProductDetailActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private PagerAdapterProductImage pagerAdapterProductImage;
    private TextView tvProductName, tvProductDescription, tvProductSold, tvProductPrice;
    private RatingBar productRate;
    private ImageView ivBackToPrevious;
    private CardView cardSharePD, cardLikePD, cardViewShoppingCartPD;
    private Button btnAddToCartPD;
    private Product product = new Product();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            product = (Product) bundle.get("product");
            setData(product);
            setEvent();
        }

        viewPager = findViewById(R.id.viewPagerProductDetail);
        circleIndicator = findViewById(R.id.circleIndicatorProductDetail);

        db = FirebaseFirestore.getInstance();
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



    }
    // -------------- Function ------------------

    private void setEvent() {

        // init
        ivBackToPrevious = findViewById(R.id.ivBackToPrevious);
        cardSharePD = findViewById(R.id.cardSharePD);
        cardLikePD = findViewById(R.id.cardLikePD);
        cardViewShoppingCartPD = findViewById(R.id.cardViewShoppingCartPD);
        btnAddToCartPD = findViewById(R.id.btnAddToCartPD);

        // click on back
        ivBackToPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // click on share
        cardSharePD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProductDetailActivity.this, "Chia sẻ thành công", Toast.LENGTH_SHORT).show();
            }
        });

        // click on like
        cardLikePD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProductDetailActivity.this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
            }
        });

        // click on cart
        cardViewShoppingCartPD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductDetailActivity.this, ShoppingCartActivity.class));
            }
        });

        // click on add to cart
        btnAddToCartPD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProductDetailActivity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setData(Product product) {

        tvProductName = findViewById(R.id.tvProductNamePD);
        tvProductDescription = findViewById(R.id.tvProductDescriptionPD);
        tvProductPrice = findViewById(R.id.tvProductPricePD);
        tvProductSold = findViewById(R.id.tvProductSoldPD);
        productRate = findViewById(R.id.ratePD);

        tvProductName.setText(product.getProductName());
        tvProductDescription.setText(product.getDescription());
        int soldNum = product.getQuantitySold();
        String soldNumStr = String.valueOf(soldNum);
        if (soldNum > 1000) {
            soldNumStr = String.valueOf(soldNum / 100 * 100) + "+";
        }
        tvProductSold.setText("Đã bán " + soldNumStr);
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