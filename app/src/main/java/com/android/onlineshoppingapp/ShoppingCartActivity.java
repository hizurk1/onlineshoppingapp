package com.android.onlineshoppingapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.onlineshoppingapp.adapters.ShoppingCartAdapter;
import com.android.onlineshoppingapp.models.Cart;
import com.android.onlineshoppingapp.models.Product;
import com.android.onlineshoppingapp.models.cartProduct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ShoppingCartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<cartProduct> productList;
    private ShoppingCartAdapter adapter;
    private ImageView ivBack, ivCheckCouponCart;
    private EditText etCoupon;
    private CheckBox cbSelectAll;
    private TextView tvTotalCart;
    private Button btnBuyNow;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    public int totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // init
        recyclerView = findViewById(R.id.rvProductCart);
        ivBack = findViewById(R.id.ivBackCart);
        ivCheckCouponCart = findViewById(R.id.ivCheckCouponCart);
        cbSelectAll = findViewById(R.id.cbSelectAllCart);
        etCoupon = findViewById(R.id.etCouponCart);
        btnBuyNow = findViewById(R.id.btnBuyCart);
        tvTotalCart = findViewById(R.id.tvTotalCart);
        productList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        // click on select all
        cbSelectAll.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                setCbSelectAll(true);
            } else {
                setCbSelectAll(false);
            }
        });

        // get data from adapter
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("TotalAmountOfProduct"));

        // add data
        final CollectionReference collectionRef = db.collection("Carts").document(firebaseAuth.getCurrentUser().getUid()).collection("Products");
        collectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                productList = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    DocumentReference documentReference = (DocumentReference) doc.get("productRef");
                    if (documentReference != null) {
                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                cartProduct cartProduct = documentSnapshot.toObject(cartProduct.class);
                                Objects.requireNonNull(cartProduct).setOrderQuantity(Integer.parseInt(String.valueOf(doc.get("orderQuantity"))));
                                cartProduct.setProductId(documentSnapshot.getId());
                                cartProduct.setChecked(false);
                                productList.add(cartProduct);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
//                Log.d(TAG, "Current products in CART: " + productList);
                // set layout and adapter
                adapter = new ShoppingCartAdapter(productList, ShoppingCartActivity.this);
                LinearLayoutManager layoutManager = new LinearLayoutManager(ShoppingCartActivity.this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }
        });


        // click on back
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // click on check coupon
        ivCheckCouponCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkCoupon(etCoupon.getText().toString()) != 0) {
                    totalPrice = totalPrice * (100 - checkCoupon(etCoupon.getText().toString())) / 100;
                    tvTotalCart.setText(String.format("%,dđ", totalPrice));

                    ivCheckCouponCart.setClickable(false);
                    etCoupon.clearFocus();
                    etCoupon.setText("");
                    ivCheckCouponCart.setImageTintList(ColorStateList.valueOf(Color.parseColor("#D2D5DD")));
                    Toast.makeText(ShoppingCartActivity.this, "Đã áp dụng mã giảm giá", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ShoppingCartActivity.this, "Mã giảm giá không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // click on buy now
        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etCoupon.getText().toString().isEmpty()) {
                    totalPrice = totalPrice * (100 - checkCoupon(etCoupon.getText().toString())) / 100;
                }

                Cart cart = new Cart(productList.stream().filter(cartProduct::isChecked).collect(Collectors.toList()), totalPrice);
                if (cart.getCartProductList().isEmpty())
                    Toast.makeText(ShoppingCartActivity.this, "Bạn chưa lựa chọn sản phẩm", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(ShoppingCartActivity.this, CheckoutActivity.class);
                    intent.putExtra("cart", cart);
                    startActivity(intent);
                }
            }
        });

    }

    public BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            totalPrice = intent.getIntExtra("totalAmount", 0);
            // set total
            tvTotalCart.setText(String.format("%,dđ", totalPrice));
        }
    };

    public void setCbSelectAll(boolean condition) {
        for (int i = 0; i < adapter.checkBoxes.size(); i++) {
            CheckBox currentCheckBox = adapter.checkBoxes.get(i);
            currentCheckBox.setChecked(condition);
        }
    }

    public int checkCoupon(String coupon) {
        if (coupon.equals("SALEOFF10")) {
            return 10;
        }
        if (coupon.equals("SALEOFF20")) {
            return 20;
        }
        if (coupon.equals("SALEOFF50")) {
            return 50;
        }
        return 0;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}