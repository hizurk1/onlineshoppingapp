package com.android.onlineshoppingapp.adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.onlineshoppingapp.ListOfProductActivity;
import com.android.onlineshoppingapp.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<String> categoryList;

    public CategoryAdapter(List<String> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        holder.tvCateName.setText(categoryList.get(position));

        switch (categoryList.get(position)) {
            case "Thiết bị điện tử":
                holder.ivImage.setImageResource(R.drawable.electronics);
                break;
            case "Sách":
                holder.ivImage.setImageResource(R.drawable.books);
                break;
            case "Thời trang nữ":
                holder.ivImage.setImageResource(R.drawable.femalefashion);
                break;
            case "Cho bé":
                holder.ivImage.setImageResource(R.drawable.forbaby);
                break;
            case "Thời trang nam":
                holder.ivImage.setImageResource(R.drawable.menfashion);
                break;
            case "Khác":
                holder.ivImage.setImageResource(R.drawable.other);
                break;
        }

        holder.cardCategoryItem.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), ListOfProductActivity.class);
            intent.putExtra("see_more_product", categoryList.get(position));
            view.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        if (categoryList.isEmpty())
            return 0;
        return categoryList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivImage;
        private TextView tvCateName;
        private MaterialCardView cardCategoryItem;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            ivImage = itemView.findViewById(R.id.ivImageCategoryItem);
            tvCateName = itemView.findViewById(R.id.tvCategoryItem);
            cardCategoryItem = itemView.findViewById(R.id.cardCategoryItem);

        }
    }
}
