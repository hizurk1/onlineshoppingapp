package com.android.onlineshoppingapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.onlineshoppingapp.R;
import com.android.onlineshoppingapp.adapters.OrderAdapter;
import com.android.onlineshoppingapp.models.Order;
import com.android.onlineshoppingapp.models.UserAddress;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShippingFragment extends Fragment {

    private LinearLayout layoutBlank;
    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private List<Order> orderList;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    public ShippingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shipping, container, false);

        // init
        recyclerView = view.findViewById(R.id.rvShipping);
        layoutBlank = view.findViewById(R.id.layoutEmptyOrder2);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        orderList = new ArrayList<>();

        db.collection("Users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.getString("accountType").equals("Bán hàng"))
                        showOrderForCustomer("seller");
                    else
                        showOrderForCustomer("orderer");
                });


        // set up
        if (orderList.isEmpty()) {
            layoutBlank.setVisibility(View.VISIBLE);
        } else {
            layoutBlank.setVisibility(View.GONE);
            adapter = new OrderAdapter(orderList,getContext());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
        }

        return view;
    }

    private void showOrderForCustomer(String seller) {
        db.collection("Orders")
                .whereEqualTo(seller, Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                .whereEqualTo("orderStatus", 2)
                .addSnapshotListener((value, error) -> {
                    orderList.clear();
                    if (error != null) {Log.e("error", error.getMessage()); return;}

                    for (DocumentSnapshot documentSnapshot : value) {
                        //get list product
                        Order order = new Order();
                        order.setOrderId(documentSnapshot.getId());
                        order.setOrderer(documentSnapshot.getString("orderer"));
                        order.setOrderStatus(Integer.parseInt(String.valueOf(documentSnapshot.get("orderStatus"))));
                        order.setTotalPrice(Integer.parseInt(String.valueOf(documentSnapshot.get("totalPrice"))));
                        order.setAddress(documentSnapshot.get("address", UserAddress.class));
                        orderList.add(order);

                    }
                    // set up
                    if (orderList.isEmpty()) {
                        layoutBlank.setVisibility(View.VISIBLE);
                    } else {
                        layoutBlank.setVisibility(View.GONE);
                        adapter = new OrderAdapter(orderList,getContext());
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(adapter);
                    }
                });
    }
}