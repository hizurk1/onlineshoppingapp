package com.android.onlineshoppingapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.onlineshoppingapp.R;
import com.android.onlineshoppingapp.adapters.OrderAdapter;
import com.android.onlineshoppingapp.models.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

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
        orderList.add(new Order("Sữa tươi không đường vị ngon 100% làm từ sữa tốt cho sức khoẻ", "ABC", "Ngon", 5000, "AAA", 2, 35000, 7));

        // set up
        if (orderList.isEmpty()) {
            layoutBlank.setVisibility(View.VISIBLE);
        } else {
            layoutBlank.setVisibility(View.GONE);
            adapter = new OrderAdapter(orderList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
        }

        return view;
    }
}