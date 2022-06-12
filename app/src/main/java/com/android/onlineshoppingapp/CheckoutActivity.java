package com.android.onlineshoppingapp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.onlineshoppingapp.models.Cart;
import com.android.onlineshoppingapp.models.UserAddress;
import com.android.onlineshoppingapp.models.cartProduct;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CheckoutActivity extends AppCompatActivity {

    private TextView tvChange, tvName, tvPhone, tvAddress, tvOrderPrice, tvShipping, tvTotal, tvFastDeliveryTxtCheckout, tvFreeOptionCheckout, tvFastOptionCheckout;
    private RadioGroup rgOptions;
    private ImageView ivBackCheckout;
    private Button btnCheckout;
    //    private final UserAddress userAddress = new UserAddress();
    private List<UserAddress> userAddressList = new ArrayList<>();
    private final int fee = 15000;
    public int total = 0;
    private Cart cart;
    private int indexAddress = 0;
    private Boolean ship = false;

    @SuppressLint("DefaultLocale")
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
        cart = (Cart) getIntent().getSerializableExtra("cart");

        // click on back
        ivBackCheckout.setOnClickListener(view -> onBackPressed());

        // set fast delivery fee
        tvFastDeliveryTxtCheckout.setText(String.format("GIAO HÀNG NHANH (%,dđ)", fee));

        // click on change
        tvChange.setOnClickListener(view -> {
            if (userAddressList.isEmpty()) {
                Toast.makeText(CheckoutActivity.this, "Navigate to edit", Toast.LENGTH_SHORT).show();
            } else {
                showDialogChangeAddress();
            }
        });

        // set date shipping
        tvFreeOptionCheckout.setText(getDateDelivery(5));
        tvFastOptionCheckout.setText(getDateDelivery(3));

        // set price order
        int priceOrder = cart.getTotalPrice();
        tvOrderPrice.setText(String.format("%,dđ", priceOrder));

        // set shipping
        total = priceOrder;
        rgOptions.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.rbFreeDeliveryCheckout) {
                tvShipping.setText("+0đ");
                total = priceOrder;
                ship = false;
                tvTotal.setText(String.format("%,dđ", total));
            }
            if (i == R.id.rbFastDeliveryCheckout) {
                List<String> sellerList = new ArrayList<>();
                for (cartProduct item : cart.getCartProductList())
                    if (!sellerList.contains(item.getSeller()))
                        sellerList.add(item.getSeller());
                tvShipping.setText(String.format("+%,dđ", fee));
                total += fee * sellerList.size();
                ship = true;
                // set total
                tvTotal.setText(String.format("%,dđ", total));
            }
        });

        // set total
        tvTotal.setText(String.format("%,dđ", total));

        // click on button checkout
        btnCheckout.setOnClickListener(view -> showDialogConfirmCheckout());

        //set address
        DocumentReference userRef = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        userRef.collection("Addresses")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    userAddressList = queryDocumentSnapshots.toObjects(UserAddress.class);
                    setAddress(userAddressList.get(0));
                }).addOnFailureListener(e -> Log.e(TAG, e.getMessage()));
    }

    private void setAddress(UserAddress userAddress) {
        tvName.setText(userAddress.getName());
        tvAddress.setText(userAddress.getFullAddress());
        tvPhone.setText(userAddress.getPhone());
    }

    @SuppressLint({"DefaultLocale", "SimpleDateFormat"})
    private void showDialogConfirmCheckout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận thanh toán")
                .setMessage("Xác nhận thanh toán số tiền " + String.format("%,dđ", total) + " bằng hình thức: Thanh toán khi nhận hàng.")
                .setCancelable(false)
                .setPositiveButton("Đồng ý", (dialogInterface, i) -> {
                    List<String> sellerList = new ArrayList<>();
                    for (cartProduct item : cart.getCartProductList())
                        if (!sellerList.contains(item.getSeller()))
                            sellerList.add(item.getSeller());

                    for (String seller : sellerList) {
                        //get total price
                        Long totalPrice = 0L;
                        for (cartProduct cartProduct : cart.getCartProductList())
                            if (cartProduct.getSeller().equals(seller))
                                totalPrice += cartProduct.getProductPrice() * cartProduct.getOrderQuantity();
                        if (ship)
                            totalPrice += 15000;
                        Map<String, Object> order = new HashMap<>();
                        order.put("orderer", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                        order.put("seller", seller);
                        order.put("address", userAddressList.get(indexAddress));
                        order.put("totalPrice", totalPrice);
                        order.put("orderStatus", 0);
                        order.put("orderTime", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
                        CollectionReference orderRef = FirebaseFirestore.getInstance().collection("Orders");
                        String orderId = orderRef.document().getId();
                        //Create order
                        orderRef.document(orderId)
                                .set(order)
                                .addOnSuccessListener(unused -> {
                                    for (cartProduct item : cart.getCartProductList()) {
                                        if (item.getSeller().equals(seller)) {
                                            Map<String, Object> productOrder = new HashMap<>();
                                            productOrder.put("isRated", false);
                                            productOrder.put("productRef",
                                                    FirebaseFirestore.getInstance()
                                                            .document("Products/" + item.getProductId() + "/"));
                                            productOrder.put("orderQuantity", item.getOrderQuantity());
                                            productOrder.put("seller", item.getSeller());
                                            productOrder.put("productPrice", item.getProductPrice());

                                            //Add product to Bought Product
                                            Map<String, Object> map = new HashMap<>();
                                            map.put("productRef", FirebaseFirestore.getInstance().collection("Products").document(item.getProductId()));
                                            FirebaseFirestore.getInstance()
                                                    .collection("Users")
                                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .collection("boughtProducts")
                                                    .document(item.getProductId())
                                                    .set(map)
                                                    .addOnFailureListener(e -> Log.e("add bought product", e.getMessage()));

                                            //add product to order
                                            orderRef.document(orderId).collection("Products")
                                                    .document(item.getProductId())
                                                    .set(productOrder)
                                                    .addOnSuccessListener(unused1 -> {
                                                        Toast.makeText(CheckoutActivity.this,
                                                                "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                                                        //Delete cart
                                                        DocumentReference cartRef = FirebaseFirestore.getInstance()
                                                                .collection("Carts")
                                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                        cartRef.collection("Products")
                                                                .document(item.getProductId())
                                                                .delete();

                                                        //Reduce quantity and increase quantitySold product
                                                        DocumentReference productRef = FirebaseFirestore.getInstance()
                                                                .collection("Products")
                                                                .document(item.getProductId());
                                                        Map<String, Object> product = new HashMap<>();
                                                        product.put("quantitySold", item.getQuantitySold() + item.getOrderQuantity());
                                                        product.put("quantity", item.getQuantity() - item.getOrderQuantity());
                                                        productRef.set(product, SetOptions.merge());
                                                    }).addOnFailureListener(e -> {
                                                        Toast.makeText(CheckoutActivity.this,
                                                                "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                                                        Log.e(TAG, e.getMessage());
                                                    });
                                        }
                                    }


                                });
                    }
                    startActivity(new Intent(CheckoutActivity.this, MainActivity.class));
                    finishAffinity();
                }).setNegativeButton("Từ chối", (dialogInterface, i) -> dialogInterface.dismiss()).show();
    }

    private void showDialogChangeAddress() {

        // get list of address info
        List<String> list = new ArrayList<>();
        for (UserAddress item : userAddressList)
            list.add("Địa chỉ: " + item.getFullAddress() + "\n" + "Tên: " + item.getName() + "\n" + "SĐT: " + item.getPhone());
        CharSequence[] charSequence = list.toArray(new CharSequence[userAddressList.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn địa chỉ thanh toán")
                .setCancelable(false)
                .setSingleChoiceItems(charSequence, indexAddress, (dialogInterface, i) -> {
                    tvName.setText(userAddressList.get(i).getName());
                    tvPhone.setText(userAddressList.get(i).getPhone());
                    tvAddress.setText(userAddressList.get(i).getFullAddress());
                    indexAddress = i;
                }).setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss()).show();
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