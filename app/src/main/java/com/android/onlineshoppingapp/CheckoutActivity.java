package com.android.onlineshoppingapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.onlineshoppingapp.models.UserAddress;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private TextView tvChange, tvName, tvPhone, tvAddress, tvOrderPrice, tvShipping, tvTotal, tvFastDeliveryTxtCheckout;
    private RadioGroup rgOptions;
    private RadioButton rbFreeDelivery, rbFastDelivery;
    private final UserAddress userAddress = new UserAddress();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // init
        tvChange = findViewById(R.id.tvChangeAddressCheckout);
        tvFastDeliveryTxtCheckout = findViewById(R.id.tvFastDeliveryTxtCheckout);
        tvName = findViewById(R.id.tvNameCheckout);
        tvPhone = findViewById(R.id.tvPhoneCheckout);
        tvAddress = findViewById(R.id.tvAddressCheckout);
        tvOrderPrice = findViewById(R.id.tvOrderPriceCheckout);
        tvShipping = findViewById(R.id.tvShippingFeeCheckout);
        tvTotal = findViewById(R.id.tvTotalCheckout);
        rgOptions = findViewById(R.id.rgOptionsCheckout);
        rbFreeDelivery = findViewById(R.id.rbFreeDeliveryCheckout);
        rbFastDelivery = findViewById(R.id.rbFastDeliveryCheckout);

        // set fast delivery fee
        int fee = 15000;
        tvFastDeliveryTxtCheckout.setText(String.format("GIAO HÀNG NHANH (%,d", String.valueOf(fee) + "đ)"));

        // click on change
        tvChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userAddress.getListOfAddress().isEmpty()) {
                    Toast.makeText(CheckoutActivity.this, "Navigate to edit", Toast.LENGTH_SHORT).show();
                } else {
                    showDialogChangeAddress();
                }
            }
        });

    }

    private void showDialogChangeAddress() {

        // get list of address info
        String name = userAddress.getFirstName() + " " + userAddress.getLastName();
        String phone = userAddress.getPhone();
        CharSequence[] charSequence = userAddress.getListOfAddress().toArray(new CharSequence[userAddress.getListOfAddress().size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn địa chỉ thanh toán")
                .setCancelable(false)
                .setSingleChoiceItems(charSequence, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                tvName.setText(name);
                                tvPhone.setText(phone);
                                tvAddress.setText(userAddress.getListOfAddress().get(0));
                                break;
                            case 1:
                                tvName.setText(name);
                                tvPhone.setText(phone);
                                tvAddress.setText(userAddress.getListOfAddress().get(1));
                                break;
                        }
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();

    }


}