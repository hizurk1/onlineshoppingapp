package com.android.onlineshoppingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.onlineshoppingapp.adapters.RecyclerViewAdapterProduct;
import com.android.onlineshoppingapp.models.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyStoreActivity extends AppCompatActivity {

    private RecyclerView rvPopularProducts, rvRecentlyProducts;
    private RecyclerViewAdapterProduct recyclerViewAdapterProduct;
    private List<Product> popularProductList, recentlyProductList;

    private TextView tvShopName;
    private CardView cardAddProduct, cardManageProduct;
    private ImageView ivVerifyBtnStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_store);

        // init
        tvShopName = findViewById(R.id.tvShopName);
        cardAddProduct = findViewById(R.id.cardAddProduct);
        cardManageProduct = findViewById(R.id.cardManageProduct);
        ivVerifyBtnStore = findViewById(R.id.ivVerifyBtnStore);

        // change shop name
        String fullname = MainActivity.userInformation.getLastName() + " " + MainActivity.userInformation.getFirstName();
        tvShopName.setText(fullname);

        // click on verify button store
        verifyStoreBtn();

        // show product
        showPopularProduct();
        showRecentlyProduct();

        // click on card add product
        cardAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProduct();
            }
        });

    }

    // --------------- Function -----------------

    private void addProduct() {

    }

    private void verifyStoreBtn() {
        ivVerifyBtnStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MyStoreActivity.this);
                alertDialog.setTitle("Xác minh cửa hàng")
                        .setMessage("Bạn cần tối thiểu 100.000 người theo dõi để được chuyển đổi sang trạng thái \nĐÃ XÁC MINH")
                        .setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    private void showRecentlyProduct() {

        recentlyProductList = new ArrayList<>();
        rvRecentlyProducts = findViewById(R.id.rvRecentlyProducts);

        for (int i = 1; i <= 5; i++) {
            Random rand = new Random();
            float rate = 1 + rand.nextFloat() * (5 - 1);
            int price = rand.nextInt(900000 - 1) + 1;
            price = price / 100 * 100;
            int soldNum = rand.nextInt(2000 - 1) + 1;

            Product product = new Product(i, "Recently product " + i, rate, price, soldNum);
            recentlyProductList.add(product);
        }

        // setup recyclerview: recently products
        recyclerViewAdapterProduct = new RecyclerViewAdapterProduct(recentlyProductList);
        rvRecentlyProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvRecentlyProducts.setAdapter(recyclerViewAdapterProduct);
    }

    private void showPopularProduct() {

        popularProductList = new ArrayList<>();
        rvPopularProducts = findViewById(R.id.rvPopularProducts);

        // add product data: demo
        for (int i = 1; i <= 10; i++) {
            Random rand = new Random();
            float rate = 1 + rand.nextFloat() * (5 - 1);
            int price = rand.nextInt(900000 - 1) + 1;
            price = price / 100 * 100;
            int soldNum = rand.nextInt(2000 - 1) + 1;

            Product product = new Product(i, "Popular product " + i, rate, price, soldNum);
            popularProductList.add(product);
        }

        // setup recyclerview: popular products
        recyclerViewAdapterProduct = new RecyclerViewAdapterProduct(popularProductList);
        rvPopularProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvPopularProducts.setAdapter(recyclerViewAdapterProduct);
    }

}