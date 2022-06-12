package com.android.onlineshoppingapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.onlineshoppingapp.R;
import com.android.onlineshoppingapp.adapters.CategoryAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoryPageFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<String> categoryList;
    private CategoryAdapter categoryAdapter;
    private FirebaseAuth fAuth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_page, container, false);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerView = view.findViewById(R.id.rvCategory);
        categoryList = new ArrayList<>();
        db.collection("Categories").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    categoryList.add(doc.getString("name"));
                }
                categoryAdapter = new CategoryAdapter(categoryList, getActivity());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(categoryAdapter);
            } else {
                Log.d("error", "Error getting documents: ", task.getException());
            }
        });


        return view;
    }
}