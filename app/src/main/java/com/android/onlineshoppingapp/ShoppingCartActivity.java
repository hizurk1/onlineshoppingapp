package com.android.onlineshoppingapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.onlineshoppingapp.adapters.ShoppingCartAdapter;
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

public class ShoppingCartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<cartProduct> productList;
    private ShoppingCartAdapter adapter;
    private ImageView ivBack;
    private EditText etCoupon;
    private Button btnBuyNow;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // init
        recyclerView = findViewById(R.id.rvProductCart);
        ivBack = findViewById(R.id.ivBackCart);
        etCoupon = findViewById(R.id.etCouponCart);
        btnBuyNow = findViewById(R.id.btnBuyCart);
        productList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

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
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            cartProduct cartProduct = documentSnapshot.toObject(cartProduct.class);
                            Objects.requireNonNull(cartProduct).setQuantity(Integer.parseInt(String.valueOf(doc.get("quantity"))));
                            cartProduct.setProductId(documentSnapshot.getId());
                            productList.add(cartProduct);
                            adapter.notifyDataSetChanged();
                        }
                    });
//                    cartProduct.setProductId(doc.getId());
//                    productList.add(cartProduct);
                }
                Log.d(TAG, "Current products in CART: " + productList);
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

        // click on buy now
        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkCoupon(etCoupon.getText().toString())) {
                    Toast.makeText(ShoppingCartActivity.this, "Applied", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(ShoppingCartActivity.this, "Thanks", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean checkCoupon(String coupon) {
        if (coupon.equals("FREE")) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}