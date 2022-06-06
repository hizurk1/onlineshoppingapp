package com.android.onlineshoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.android.onlineshoppingapp.models.UserAddress;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChangeAddressActivity extends AppCompatActivity {

    private ImageView ivBack;
    private Switch switch1, switch2;
    private CheckBox cbAddress2;
    private Button btnChangeAddress;
    private LinearLayout layoutCAddress2;
    private TextInputEditText etName1, etName2, etPhone1, etPhone2, etDetail1, etDetail2;
    private TextInputLayout layoutName1, layoutName2, layoutPhone1, layoutPhone2, layoutDetail1, layoutDetail2;
    private AutoCompleteTextView ctvCity1, ctvCity2, ctvDistrict1, ctvDistrict2, ctvTown1, ctvTown2;

    private FirebaseAuth fAuth;
    private FirebaseFirestore db;
    private List<UserAddress> userAddresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address);

        // init
        ivBack = findViewById(R.id.ivBackCAddress);
        switch1 = findViewById(R.id.switchAddress1);
        switch2 = findViewById(R.id.switchAddress2);
        etName1 = findViewById(R.id.etNameCAddress1);
        etName2 = findViewById(R.id.etNameCAddress2);
        etPhone1 = findViewById(R.id.etPhoneCAddress1);
        etPhone2 = findViewById(R.id.etPhoneCAddress2);
        etDetail1 = findViewById(R.id.etDetailCAddress1);
        etDetail2 = findViewById(R.id.etDetailCAddress2);
        layoutName1 = findViewById(R.id.layoutNameCAddress1);
        layoutName2 = findViewById(R.id.layoutNameCAddress2);
        layoutPhone1 = findViewById(R.id.layoutPhoneCAddress1);
        layoutPhone2 = findViewById(R.id.layoutPhoneCAddress2);
        layoutDetail1 = findViewById(R.id.layoutDetailCAddress1);
        layoutDetail2 = findViewById(R.id.layoutDetailCAddress2);
        ctvCity1 = findViewById(R.id.tvCityCAddress1);
        ctvCity2 = findViewById(R.id.tvCityCAddress2);
        ctvDistrict1 = findViewById(R.id.tvDistrictCAddress1);
        ctvDistrict2 = findViewById(R.id.tvDistrictCAddress2);
        ctvTown1 = findViewById(R.id.tvTownCAddress1);
        ctvTown2 = findViewById(R.id.tvTownCAddress2);
        cbAddress2 = findViewById(R.id.cbCAddress2);
        layoutCAddress2 = findViewById(R.id.layoutCAddress2);
        btnChangeAddress = findViewById(R.id.btnChangeAddress);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // click on back
        ivBack.setOnClickListener(view -> {
            onBackPressed();
        });

        // click on check box
        layoutCAddress2.setVisibility(View.GONE);
        cbAddress2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (switch2.isChecked())
                        switch2.setChecked(false);
                    layoutCAddress2.setVisibility(View.VISIBLE);
                    switch2.setVisibility(View.VISIBLE);
                } else {
                    if (!switch1.isChecked())
                        switch1.setChecked(true);
                    layoutCAddress2.setVisibility(View.GONE);
                    switch2.setVisibility(View.INVISIBLE);
                }
            }
        });

        // click on switch
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    switch2.setChecked(false);
                } else {
                    switch2.setChecked(true);
                    if (switch2.getVisibility() == View.INVISIBLE) {
                        switch1.setChecked(true);
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChangeAddressActivity.this);
                        builder.setCancelable(false)
                                .setTitle("Địa chỉ mặc định")
                                .setMessage("Cần có ít nhất 1 địa chỉ là mặc định")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).show();
                    }
                }
            }
        });

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    switch1.setChecked(false);
                } else {
                    switch1.setChecked(true);
                }
            }
        });

        // check name 1
        etName1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean onFocus) {
                if (onFocus) {
                    layoutName1.setHelperTextEnabled(false);
                } else {
                    if (etName1.getText().toString().equals("")) {
                        layoutName1.setHelperText("Tên không được bỏ trống");
                    } else if (!includeCharInAlphabet(etName1.getText().toString())) {
                        layoutName1.setHelperText("Tên phải chứa ít nhất 1 ký tự chữ cái");
                    } else {
                        layoutName1.setHelperTextEnabled(false);
                    }
                }
            }
        });

        // check phone 1
        etPhone1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean onFocus) {
                if (onFocus) {
                    layoutPhone1.setHelperTextEnabled(false);
                } else {
                    if (etPhone1.getText().toString().equals("")) {
                        layoutPhone1.setHelperText("Số điện thoại không được bỏ trống");
                    } else if (etPhone1.getText().toString().length() < 6) {
                        layoutPhone1.setHelperText("Số điện thoại phải có ít nhất 6 số");
                    } else {
                        layoutPhone1.setHelperTextEnabled(false);
                    }
                }
            }
        });

        // check detail 1
        etDetail1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean onFocus) {
                if (onFocus) {
                    layoutDetail1.setHelperTextEnabled(false);
                } else {
                    if (etDetail1.getText().toString().equals("")) {
                        layoutDetail1.setHelperText("Tên đường, số nhà không được bỏ trống");
                    } else {
                        layoutDetail1.setHelperTextEnabled(false);
                    }
                }
            }
        });

        userAddresses = new ArrayList<>();
        db.collection("UserAddresses").document(fAuth.getCurrentUser().getUid())
                .collection("Addresses")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            task.getResult().getDocuments().forEach(documentSnapshot -> {
                                UserAddress userAddress = new UserAddress();
                                userAddress = documentSnapshot.toObject(UserAddress.class);
                                userAddresses.add(userAddress);
                            });

                            // set previous value for et
                            etName1.setText(userAddresses.get(0).getName());
                            etName1.setSelectAllOnFocus(true);
                            etPhone1.setText(userAddresses.get(0).getPhone());
                            etPhone1.setSelectAllOnFocus(true);

                        } else {
                            Log.e("getAddress", task.getException().getMessage());
                        }
                    }
                });

        // check name 2
        etName2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean onFocus) {
                if (onFocus) {
                    layoutName2.setHelperTextEnabled(false);
                } else {
                    if (etPhone2.getText().toString().equals("")) {
                        layoutName2.setHelperText("Tên không được bỏ trống");
                    } else if (!includeCharInAlphabet(etName2.getText().toString())) {
                        layoutName2.setHelperText("Tên phải chứa ít nhất 1 ký tự chữ cái");
                    } else {
                        layoutName2.setHelperTextEnabled(false);
                    }
                }
            }
        });

        // check phone 2
        etPhone2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean onFocus) {
                if (onFocus) {
                    layoutPhone2.setHelperTextEnabled(false);
                } else {
                    if (etPhone2.getText().toString().equals("")) {
                        layoutPhone2.setHelperText("Số điện thoại không được bỏ trống");
                    } else if (etPhone2.getText().toString().length() < 6) {
                        layoutPhone2.setHelperText("Số điện thoại phải có ít nhất 6 số");
                    } else {
                        layoutPhone2.setHelperTextEnabled(false);
                    }
                }
            }
        });

        // check detail 2
        etDetail2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean onFocus) {
                if (onFocus) {
                    layoutDetail2.setHelperTextEnabled(false);
                } else {
                    if (etDetail2.getText().toString().equals("")) {
                        layoutDetail2.setHelperText("Tên đường, số nhà không được bỏ trống");
                    } else {
                        layoutDetail2.setHelperTextEnabled(false);
                    }
                }
            }
        });

        userAddresses = new ArrayList<>();
        db.collection("UserAddresses").document(fAuth.getCurrentUser().getUid())
                .collection("Addresses")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            task.getResult().getDocuments().forEach(documentSnapshot -> {
                                UserAddress userAddress = new UserAddress();
                                userAddress = documentSnapshot.toObject(UserAddress.class);
                                userAddresses.add(userAddress);
                            });

                            // set previous value for et
                            etName2.setText(userAddresses.get(1).getName());
                            etName2.setSelectAllOnFocus(true);
                            etPhone2.setText(userAddresses.get(1).getPhone());
                            etPhone2.setSelectAllOnFocus(true);

                        } else {
                            Log.e("getAddress", task.getException().getMessage());
                        }
                    }
                });

        // click on change
        btnChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateData()) {
                    boolean defaultAddress = switch1.isChecked(); // true = 1, false = 2
                    Log.w("defaultAddress", (defaultAddress) ? "1" : "2");
                    Toast.makeText(ChangeAddressActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean validateData() {
        if (etName1.getText().toString().equals("") || etPhone1.getText().toString().equals("") ||
                etDetail1.getText().toString().equals("") || etName2.getText().toString().equals("") ||
                etPhone2.getText().toString().equals("") || etDetail2.getText().toString().equals("") ||
                layoutName1.isHelperTextEnabled() || layoutPhone1.isHelperTextEnabled() ||
                layoutDetail1.isHelperTextEnabled() || layoutName2.isHelperTextEnabled() ||
                layoutPhone2.isHelperTextEnabled() || layoutDetail2.isHelperTextEnabled()) {
            return true;
        }
        return false;
    }

    public boolean includeCharInAlphabet(String str) {
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        for (int i = 0; i < str.length(); i++) {
            for (int j = 0; j < alphabet.length; j++) {
                if (str.toLowerCase().charAt(i) == alphabet[j])
                    return true;
            }
        }
        return false;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}