package com.android.onlineshoppingapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.onlineshoppingapp.adapters.OrderAdapter;
import com.android.onlineshoppingapp.models.Order;
import com.android.onlineshoppingapp.models.UserAddress;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShipperActivity extends AppCompatActivity {

    private ImageView ivLogout;
    private RecyclerView recyclerView;
    private LinearLayout layoutBlank;

    private FirebaseAuth fAuth;
    private FirebaseFirestore db;

    private OrderAdapter adapter;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipper);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        ivLogout = findViewById(R.id.ivLogoutShipper);
        recyclerView = findViewById(R.id.rvShipper);
        layoutBlank = findViewById(R.id.layoutEmptyShipper);

        ivLogout.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Đăng xuất")
                    .setMessage("Xác nhận đăng xuất tài khoản?").setCancelable(false)
                    .setPositiveButton("OK", (dialogInterface, i) -> {
                        fAuth.signOut();
                        startActivity(new Intent(ShipperActivity.this, LoginActivity.class));
                        finish();
                    }).setNegativeButton("Huỷ bỏ", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    }).show();
        });

        orderList = new ArrayList<>();
        db.collection("Orders")
                .whereEqualTo("orderStatus", 2)
                .addSnapshotListener((value, error) -> {
                    orderList.clear();
                    if (error != null) {
                        Log.e("error", error.getMessage());
                        return;
                    }
                    for (DocumentSnapshot documentSnapshot : value) {
                        //get list product
                        Order order = new Order();
                        order.setOrderId(documentSnapshot.getId());
                        order.setOrderer(documentSnapshot.getString("orderer"));
                        order.setOrderStatus(Integer.valueOf(String.valueOf(documentSnapshot.get("orderStatus"))));
                        order.setTotalPrice(Integer.valueOf(String.valueOf(documentSnapshot.get("totalPrice"))));
                        order.setAddress(documentSnapshot.get("address", UserAddress.class));
                        orderList.add(order);
                    }
                    // set up
                    if (orderList.isEmpty()) {
                        layoutBlank.setVisibility(View.VISIBLE);
                    } else {
                        layoutBlank.setVisibility(View.GONE);
                        adapter = new OrderAdapter(orderList, this);
                        adapter.isShipper = true;
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(adapter);
                    }
                });

    }
}