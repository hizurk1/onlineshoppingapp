package com.android.onlineshoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.android.onlineshoppingapp.adapters.PurchaseOrderAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class PurchaseOrderActivity extends AppCompatActivity {

    private ImageView ivBack;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private PurchaseOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_order);

        //init
        ivBack = findViewById(R.id.ivBackPurchaseOrder);
        viewPager2 = findViewById(R.id.vpPurchaseOrder);
        tabLayout = findViewById(R.id.tabLayoutPurchaseOrder);
        adapter = new PurchaseOrderAdapter(this);
        viewPager2.setAdapter(adapter);
        viewPager2.setCurrentItem(getCurrentItem());

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Chờ xác nhận");
                    break;
                case 1:
                    tab.setText("Đang lấy hàng");
                    break;
                case 2:
                    tab.setText("Đang giao hàng");
                    break;
                case 3:
                    tab.setText("Đã giao hàng");
                    break;
                case 4:
                    tab.setText("Đã huỷ");
                    break;
            }
        }).attach();

        // click on back
        ivBack.setOnClickListener(view -> {
            onBackPressed();
        });


    }

    private int getCurrentItem() {

        switch (getIntent().getStringExtra("orderState")) {
            case "receiveOrder":
                return 0;
            case "waitForProduct":
                return 1;
            case "shipping":
                return 2;
            case "waitForReview":
                return 3;
        }
        return 0;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}