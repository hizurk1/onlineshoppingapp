package com.android.onlineshoppingapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.onlineshoppingapp.R;
import com.android.onlineshoppingapp.fragments.HomePageFragment;
import com.android.onlineshoppingapp.models.Product;

import java.util.List;
import java.util.Random;

public class RecyclerViewAdapterProduct extends RecyclerView.Adapter<RecyclerViewAdapterProduct.ProductViewHolder> {

    private List<Product> mProducts;

    public RecyclerViewAdapterProduct(List<Product> mProducts) {
        this.mProducts = mProducts;
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = mProducts.get(position);
        if (product == null) {
            return;
        }
        setValueOfEachItem(holder, product);
    }

    @Override
    public int getItemCount() {
        if (mProducts != null) {
            return mProducts.size();
        }
        return 0;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivProductLogo;
        private TextView tvProductName, tvProductPrice, tvSoldNum, tvSaleOff;
        private RatingBar ratingbarProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            //init
            ivProductLogo = itemView.findViewById(R.id.ivProductLogo);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvSoldNum = itemView.findViewById(R.id.tvSoldNum);
            ratingbarProduct = itemView.findViewById(R.id.ratingbarProduct);
            tvSaleOff = itemView.findViewById(R.id.tvSaleOff);
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
        holder.tvProductName.setText(product.getProductName());
        holder.tvProductPrice.setText(String.format("%,d", product.getProductPrice()) + "đ");
    }

}
