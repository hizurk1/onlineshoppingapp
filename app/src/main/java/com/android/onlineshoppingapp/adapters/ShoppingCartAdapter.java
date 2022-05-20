package com.android.onlineshoppingapp.adapters;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.onlineshoppingapp.R;
import com.android.onlineshoppingapp.models.Product;

import java.util.List;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ShoppingCartViewHolder> {

    private List<Product> productList;

    public ShoppingCartAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ShoppingCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_product_in_cart, parent, false);
        return new ShoppingCartViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ShoppingCartViewHolder holder, int position) {
        Product product = productList.get(position);
        if (product == null) {
            return;
        }

        holder.etNumProduct.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (Integer.parseInt(charSequence.toString()) == 1) {
                    adjustBtn(holder.ivMinus, false);
                    adjustBtn(holder.ivAdd, true);
                } else if (Integer.parseInt(charSequence.toString()) >= 999) {
                    adjustBtn(holder.ivMinus, false);
                    adjustBtn(holder.ivAdd, true);
                } else {
                    adjustBtn(holder.ivMinus, true);
                    adjustBtn(holder.ivAdd, true);
                }
            }

            public void adjustBtn(ImageView imageView, boolean condition) {
                imageView.setClickable(condition);
                if (condition) {
                    imageView.setImageTintList(ColorStateList.valueOf(Color.parseColor("#22B473")));
                } else {
                    imageView.setImageTintList(ColorStateList.valueOf(Color.parseColor("#D2D5DD")));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // click on minus
        holder.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.numProduct -= 1;
                if (holder.numProduct < 1) {
                    holder.numProduct = 1;
                }
                holder.etNumProduct.setText(String.valueOf(holder.numProduct));
            }
        });

        // click on add
        holder.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.numProduct += 1;
                holder.etNumProduct.setText(String.valueOf(holder.numProduct));
            }
        });

        // long click on minus
        holder.ivMinus.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                holder.numProduct -= 10;
                if (holder.numProduct < 1) {
                    holder.numProduct = 1;
                }
                holder.etNumProduct.setText(String.valueOf(holder.numProduct));
                return true;
            }
        });

        // long click on add
        holder.ivAdd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                holder.numProduct += 10;
                holder.etNumProduct.setText(String.valueOf(holder.numProduct));
                return true;
            }
        });

        // set information of product
        String productTitle = product.getProductName();
        String productSeller = product.getSeller();
        String productPrice = String.format("%,d", product.getProductPrice()) + "đ";

        Log.d("Product title", productTitle);
        Log.d("Product title num", String.valueOf(productTitle.length()));

        // set title
        if (productTitle.length() > 15) {
            productTitle = productTitle.substring(0, 15) + "...";
        }
        holder.tvTitle.setText(productTitle);

        // set seller
        if (productSeller.length() > 15) {
            productSeller = productSeller.substring(0, 15) + "...";
        }
        holder.tvSeller.setText(productSeller);

        // set price
        holder.tvPrice.setText(productPrice);

    }

    @Override
    public int getItemCount() {
        if (productList.size() != 0)
            return productList.size();
        return 0;
    }

    class ShoppingCartViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle, tvSeller, tvPrice;
        private ImageView ivProductImg, ivAdd, ivMinus, ivDelete;
        private EditText etNumProduct;
        private CheckBox cbProduct;
        private int numProduct = 1;

        public ShoppingCartViewHolder(@NonNull View itemView) {
            super(itemView);

            cbProduct = itemView.findViewById(R.id.cbProductICart);
            ivDelete = itemView.findViewById(R.id.ivDeleteProductICart);
            tvTitle = itemView.findViewById(R.id.tvProductTitleICart);
            tvSeller = itemView.findViewById(R.id.tvProductSellerICart);
            tvPrice = itemView.findViewById(R.id.tvProductPriceICart);
            ivProductImg = itemView.findViewById(R.id.ivProductImageICart);
            ivAdd = itemView.findViewById(R.id.ivAddICart);
            ivMinus = itemView.findViewById(R.id.ivMinusICart);
            etNumProduct = itemView.findViewById(R.id.etNumProductICart);

//            Log.d("Number", (etNumProduct.getText().toString().equals(null)) ? "null" : etNumProduct.getText().toString());
            numProduct = Integer.parseInt(etNumProduct.getText().toString());
        }
    }
}
