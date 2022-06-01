package com.android.onlineshoppingapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.onlineshoppingapp.models.UserAddress;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class CheckoutActivity extends AppCompatActivity {

    private TextView tvChange, tvName, tvPhone, tvAddress, tvOrderPrice, tvShipping, tvTotal, tvFastDeliveryTxtCheckout, tvFreeOptionCheckout, tvFastOptionCheckout;
    private RadioGroup rgOptions;
    private ImageView ivBackCheckout;
    private Button btnCheckout;
    private final UserAddress userAddress = new UserAddress();
    private final int fee = 15000;
    public int total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // init
        ivBackCheckout = findViewById(R.id.ivBackCheckout);
        tvChange = findViewById(R.id.tvChangeAddressCheckout);
        tvFastDeliveryTxtCheckout = findViewById(R.id.tvFastDeliveryTxtCheckout);
        tvName = findViewById(R.id.tvNameCheckout);
        tvPhone = findViewById(R.id.tvPhoneCheckout);
        tvAddress = findViewById(R.id.tvAddressCheckout);
        tvFreeOptionCheckout = findViewById(R.id.tvFreeOptionCheckout);
        tvFastOptionCheckout = findViewById(R.id.tvFastOptionCheckout);
        tvOrderPrice = findViewById(R.id.tvOrderPriceCheckout);
        tvShipping = findViewById(R.id.tvShippingFeeCheckout);
        tvTotal = findViewById(R.id.tvTotalCheckout);
        rgOptions = findViewById(R.id.rgOptionsCheckout);
        btnCheckout = findViewById(R.id.btnCheckout);

        // click on back
        ivBackCheckout.setOnClickListener(view -> {
            onBackPressed();
        });

        // set fast delivery fee
        tvFastDeliveryTxtCheckout.setText(String.format("GIAO HÀNG NHANH (%,dđ)", fee));

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

        // set date shipping
        tvFreeOptionCheckout.setText(getDateDelivery(5));
        tvFastOptionCheckout.setText(getDateDelivery(3));

        // set price order
        int priceOrder = getIntent().getIntExtra("totalPrice", 0);
        tvOrderPrice.setText(String.format("%,dđ", priceOrder));

        // set shipping
        total = priceOrder;
        rgOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rbFreeDeliveryCheckout) {
                    tvShipping.setText("+0đ");
                    total = priceOrder;
                    tvTotal.setText(String.format("%,dđ", total));
                }
                if (i == R.id.rbFastDeliveryCheckout) {
                    tvShipping.setText(String.format("+%,dđ", fee));
                    total += fee;

                    // set total
                    tvTotal.setText(String.format("%,dđ", total));
                }
            }
        });

        // set total
        tvTotal.setText(String.format("%,dđ", total));

        // click on button checkout
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogConfirmCheckout();
            }
        });

    }

    private void showDialogConfirmCheckout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận thanh toán")
                .setMessage("Xác nhận thanh toán số tiền " + String.format("%,dđ", total) + " bằng hình thức: Thanh toán khi nhận hàng.")
                .setCancelable(false)
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(CheckoutActivity.this, "Đang xử lý...", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(() -> {
                            Toast.makeText(CheckoutActivity.this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CheckoutActivity.this, MainActivity.class));
                            finishAffinity();
                        }, 2000);
                    }
                }).setNegativeButton("Từ chối", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
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

    public String getDateDelivery(int delay) {

        LocalDate currentDate = LocalDate.now();
        LocalDate dateAfter = currentDate.plus(delay, ChronoUnit.DAYS);

        return "Giao hàng trước " + convertDateOfWeek(dateAfter.getDayOfWeek().getValue()) +
                ", ngày " + dateAfter.getDayOfMonth() + "/" + dateAfter.getMonth().getValue() + "/" + dateAfter.getYear();
    }

    public String convertDateOfWeek(int dow) {
        switch (dow) {
            case 1:
                return "thứ 2";
            case 2:
                return "thứ 3";
            case 3:
                return "thứ 4";
            case 4:
                return "thứ 5";
            case 5:
                return "thứ 6";
            case 6:
                return "thứ 7";
            case 7:
                return "Chủ Nhật";
        }
        return "";
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}