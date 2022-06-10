package com.android.onlineshoppingapp.adapters;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.onlineshoppingapp.ManageProductActivity;
import com.android.onlineshoppingapp.R;
import com.android.onlineshoppingapp.models.Product;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ManageProductAdapter extends RecyclerView.Adapter<ManageProductAdapter.ManageProductViewHolder> {

    private List<Product> productList;
    private Context context;
    private ActionMode actionMode;
    private ManageProductActivity activity;

    private FirebaseAuth fAuth;
    private FirebaseFirestore db;

    public ManageProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
        this.activity = (ManageProductActivity) context;
    }

    @NonNull
    @Override
    public ManageProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_manage_product, parent, false);
        return new ManageProductViewHolder(view, activity);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageProductViewHolder holder, int position) {
        setItemInfo(productList.get(position), holder);

        if (activity.pos == position) {
            holder.checkbox.setChecked(true);
            activity.pos = -1;
        }

        if (activity.isActionMode) {
            holder.checkbox.setVisibility(View.VISIBLE);
        } else {
            holder.checkbox.setVisibility(View.INVISIBLE);
            holder.checkbox.setChecked(false);
        }

        holder.cardProduct.setOnLongClickListener(view -> {
            activity.startSelection(position);
            return true;
        });

        holder.checkbox.setOnClickListener(view -> {
            activity.check(view, position);
        });

        holder.cardProduct.setOnClickListener(view -> {
            activity.showSheetToEdit(position);
        });

    }

    private void setItemInfo(Product product, @NonNull ManageProductViewHolder holder) {
        if (product == null)
            return;
        db = FirebaseFirestore.getInstance();

        AsyncTask.execute(() -> db.collection("productImages")
                .document(product.getProductId())
                .addSnapshotListener((value, error) -> {
                    if (value != null && value.exists()) {
                        Map<String, Object> map = value.getData();
                        if (map != null) {
                            List<String> string = new ArrayList<>();
                            string = (List<String>) map.get("url");
                            Glide.with(Objects.requireNonNull(holder.itemView.getContext()))
                                    .load(string.get(0)).into(holder.ivProductImg);
                        }
                    } else
                        holder.ivProductImg.setImageResource(R.drawable.logoapp);

                }));


        if (product.getProductName().length() > 45) {
            holder.tvProductName.setText(String.format("%s...", product.getProductName().substring(0, 45)));
        } else {
            holder.tvProductName.setText(product.getProductName());
        }

        holder.tvPrice.setText(String.format("%,dđ", product.getProductPrice()));
        holder.tvNumber.setText(String.valueOf(product.getQuantity()));
    }

    @Override
    public int getItemCount() {
        if (productList.isEmpty())
            return 0;
        return productList.size();
    }

    class ManageProductViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivProductImg;
        private TextView tvProductName, tvPrice, tvNumber;
        private MaterialCardView cardProduct;
        private ManageProductActivity activity;
        private CheckBox checkbox;

        public ManageProductViewHolder(@NonNull View itemView, ManageProductActivity activity) {
            super(itemView);

            ivProductImg = itemView.findViewById(R.id.ivProductLogoManage);
            tvProductName = itemView.findViewById(R.id.tvProductNameManage);
            tvPrice = itemView.findViewById(R.id.tvProductPriceManage);
            tvNumber = itemView.findViewById(R.id.tvNumberProductManage);
            cardProduct = itemView.findViewById(R.id.cardProductManage);
            checkbox = itemView.findViewById(R.id.cbManageProduct);
            this.activity = activity;

        }
    }
}
