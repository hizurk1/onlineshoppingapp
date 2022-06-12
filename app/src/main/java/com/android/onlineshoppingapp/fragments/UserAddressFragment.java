package com.android.onlineshoppingapp.fragments;

import android.content.Intent;
import android.os.Bundle;


import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.onlineshoppingapp.ChangeAddressActivity;
import com.android.onlineshoppingapp.MainActivity;
import com.android.onlineshoppingapp.R;
import com.android.onlineshoppingapp.models.UserAddress;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.divider.MaterialDivider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserAddressFragment extends Fragment {

    private TextView tvFullNameAddress1, tvFullNameAddress2, tvPhoneNumberAddress1;
    private TextView tvPhoneNumberAddress2, tvAddress1, tvAddress2, tvDefaultFAddress;
    private ImageView ivChangeUserAddress;

    private FirebaseAuth fAuth;
    private FirebaseFirestore db;
    private List<UserAddress> userAddresses;

    public UserAddressFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_address, container, false);

        // init
        tvDefaultFAddress = view.findViewById(R.id.tvDefaultFAddress);
        tvFullNameAddress1 = view.findViewById(R.id.tvFullNameAddress1);
        tvFullNameAddress2 = view.findViewById(R.id.tvFullNameAddress2);
        tvPhoneNumberAddress1 = view.findViewById(R.id.tvPhoneNumberAddress1);
        tvPhoneNumberAddress2 = view.findViewById(R.id.tvPhoneNumberAddress2);
        tvAddress1 = view.findViewById(R.id.tvAddress1);
        tvAddress2 = view.findViewById(R.id.tvAddress2);
        ivChangeUserAddress = view.findViewById(R.id.ivChangeUserAddress);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userAddresses = new ArrayList<>();

        db.collection("Users").document(fAuth.getCurrentUser().getUid())
                .collection("Addresses")
                .addSnapshotListener((value, error) -> {
                    userAddresses.clear();
                    if (error != null) Log.e("getAddress", error.getMessage());

//                    value.forEach(documentSnapshot -> {
//                        UserAddress userAddress = documentSnapshot.toObject(UserAddress.class);
//                        userAddresses.add(userAddress);
//                    });
                    if (value != null) {
                        userAddresses = value.toObjects(UserAddress.class);
                    }

                    if (userAddresses.isEmpty()) {
                        // set Address 1
                        tvFullNameAddress1.setText("Chưa thiết đặt");
                        tvPhoneNumberAddress1.setText("Chưa thiết đặt");
                        tvAddress1.setText("Chưa thiết đặt");

                        // set Address 2
                        tvFullNameAddress2.setText("Chưa thiết đặt");
                        tvPhoneNumberAddress2.setText("Chưa thiết đặt");
                        tvAddress2.setText("Chưa thiết đặt");
                    } else if (userAddresses.size() > 1) {
                        if (userAddresses.get(0).isDefaultAddress()) {
                            // set Address 1
                            tvFullNameAddress1.setText(userAddresses.get(0).getName().toString());
                            tvPhoneNumberAddress1.setText(userAddresses.get(0).getPhone().toString());
                            tvAddress1.setText(userAddresses.get(0).getFullAddress());

                            // set Address 2
                            tvFullNameAddress2.setText(userAddresses.get(1).getName().toString());
                            tvPhoneNumberAddress2.setText(userAddresses.get(1).getPhone().toString());
                            tvAddress2.setText(userAddresses.get(1).getFullAddress());
                        } else {
                            // set Address 1
                            tvFullNameAddress1.setText(userAddresses.get(1).getName().toString());
                            tvPhoneNumberAddress1.setText(userAddresses.get(1).getPhone().toString());
                            tvAddress1.setText(userAddresses.get(1).getFullAddress());

                            // set Address 2
                            tvFullNameAddress2.setText(userAddresses.get(0).getName().toString());
                            tvPhoneNumberAddress2.setText(userAddresses.get(0).getPhone().toString());
                            tvAddress2.setText(userAddresses.get(0).getFullAddress());
                        }

                    } else {
                        // set Address 1
                        tvFullNameAddress1.setText(userAddresses.get(0).getName().toString());
                        tvPhoneNumberAddress1.setText(userAddresses.get(0).getPhone().toString());
                        tvAddress1.setText(userAddresses.get(0).getFullAddress());

                        // set Address 2
                        tvFullNameAddress2.setText("Chưa thiết đặt");
                        tvPhoneNumberAddress2.setText("Chưa thiết đặt");
                        tvAddress2.setText("Chưa thiết đặt");
                    }
                });

        ivChangeUserAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ChangeAddressActivity.class));
            }
        });

        return view;
    }
}