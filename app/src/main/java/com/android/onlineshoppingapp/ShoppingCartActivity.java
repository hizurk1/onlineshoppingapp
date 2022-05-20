package com.android.onlineshoppingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.onlineshoppingapp.adapters.ShoppingCartAdapter;
import com.android.onlineshoppingapp.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Product> productList;
    private ShoppingCartAdapter adapter;
    private ImageView ivBack;
    private EditText etCoupon;
    private Button btnBuyNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        // init
        recyclerView = findViewById(R.id.rvProductCart);
        ivBack = findViewById(R.id.ivBackCart);
        etCoupon = findViewById(R.id.etCouponCart);
        btnBuyNow = findViewById(R.id.btnBuyCart);
        productList = new ArrayList<>();

        // add data
        Product product1 = new Product("Sữa không đường", "Vinamilk", "Sữa tươi vinamilk", 7000);
        Product product2 = new Product("Sữa có đường", "Vinamilk", "Sữa tươi vinamilk", 8000);
        Product product3 = new Product("Sữa không có đường", "Vinamilk", "Sữa tươi vinamilk", 7000);
        Product product4 = new Product("Sữa có đường cũng như không", "Vinamilk", "Sữa tươi vinamilk", 9000);
        productList.add(product1);
        productList.add(product2);
        productList.add(product3);
        productList.add(product4);

        // set layout and adapter
        adapter = new ShoppingCartAdapter(productList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // click on back
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // click on buy now
        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkCoupon(etCoupon.getText().toString())) {
                    Toast.makeText(ShoppingCartActivity.this, "Applied", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(ShoppingCartActivity.this, "Thanks", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean checkCoupon(String coupon) {
        if (coupon.equals("FREE")) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}