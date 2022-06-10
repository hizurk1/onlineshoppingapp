package com.android.onlineshoppingapp.adapters;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.onlineshoppingapp.ProductDetailActivity;
import com.android.onlineshoppingapp.R;
import com.android.onlineshoppingapp.fragments.HomePageFragment;
import com.android.onlineshoppingapp.models.Product;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RecyclerViewAdapterProduct extends RecyclerView.Adapter<RecyclerViewAdapterProduct.ProductViewHolder> {

    private List<Product> listProducts;
    private FirebaseFirestore db;
    private Context context;

    public RecyclerViewAdapterProduct(List<Product> listProducts, Context context) {
        this.listProducts = listProducts;
        this.context = context.getApplicationContext();
    }

    public RecyclerViewAdapterProduct(List<Product> listProducts) {
        this.listProducts = listProducts;
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = listProducts.get(position);
        if (product == null) {
            return;
        }
        setValueOfEachItem(holder, product);
    }

    @Override
    public int getItemCount() {
        if (listProducts != null) {
            return listProducts.size();
        }
        return 0;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView ivProductLogo;
        private TextView tvProductName, tvProductPrice, tvSoldNum, tvSaleOff;
        private RatingBar ratingbarProduct;
        private CardView cardProductItem;
        private View view;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            //init
            ivProductLogo = itemView.findViewById(R.id.ivProductLogo);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvSoldNum = itemView.findViewById(R.id.tvSoldNum);
            ratingbarProduct = itemView.findViewById(R.id.ratingbarProduct);
            tvSaleOff = itemView.findViewById(R.id.tvSaleOff);
            cardProductItem = itemView.findViewById(R.id.cardProductItem);
            view = itemView;
        }
    }

    // set values for product card
    public void setValueOfEachItem(ProductViewHolder holder, Product product) {

        int soldNum = product.getQuantitySold();
        String soldNumStr = String.valueOf(soldNum);

        if (soldNum > 1000) {
            soldNumStr = String.valueOf(soldNum / 100 * 100) + "+";
        }

        // turn on and off sale off tag: change if statement
        if (product.getProductPrice() > 100000 && soldNum > 1000) {
            holder.tvSaleOff.setVisibility(View.VISIBLE);
        } else {
            holder.tvSaleOff.setVisibility(View.INVISIBLE);
        }

        // set values
        holder.tvSoldNum.setText("Đã bán " + soldNumStr);
        holder.ratingbarProduct.setRating(product.getRate());

        int longOfName = product.getProductName().toCharArray().length;
        String productName = "";
        if (longOfName > 50) {
            productName = product.getProductName().substring(0, 50) + "...";
        } else {
            productName = product.getProductName();
        }
        holder.tvProductName.setText(productName);
        holder.tvProductPrice.setText(String.format("%,d", product.getProductPrice()) + "đ");

        holder.cardProductItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ProductDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("product", product);
                intent.putExtras(bundle);
                view.getContext().startActivities(new Intent[]{intent});
            }
        });

        db = FirebaseFirestore.getInstance();
        db.collection("productImages")
                .document(product.getProductId())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error!=null) {
                            Log.e(TAG, String.valueOf(error));
                            return;
                        }

                        DocumentSnapshot documentSnapshot = value;
                        if (documentSnapshot.exists()) {
                            Map<String, Object> map = documentSnapshot.getData();
                            if (map != null) {
                                List<String> string = (List<String>) map.get("url");
                                assert string != null;
                                Glide.with(holder.view)
                                        .load(string.get(0)).into(holder.ivProductLogo);
                            } else
                                holder.ivProductLogo.setImageResource(R.drawable.logoapp);
                        }
                    }
                });

    }


}
