package com.android.onlineshoppingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.onlineshoppingapp.R;
import com.android.onlineshoppingapp.models.Review;

import java.util.List;

public class WriteReviewAdapter extends RecyclerView.Adapter<WriteReviewAdapter.WriteReviewViewHolder> {

    private List<Review> reviewList;

    public WriteReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public WriteReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_write_review, parent, false);
        return new WriteReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WriteReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        if (review == null)
            return;

        String productName = "Tên sản phẩm"; // get product name
        if (productName.length() > 50) {
            holder.tvProductName.setText(String.format("%s...", productName.substring(0, 50)));
        } else {
            holder.tvProductName.setText(productName);
        }

        String category = "AAA"; // get category
        holder.tvCategory.setText(String.format("Loại: %s", category));

        float ratePoint = holder.ratingBar.getRating();

        holder.btnSubmit.setOnClickListener(view -> {
            String content = holder.etWriteReview.getText().toString();
            if (content.equals("")) {
                Toast.makeText(view.getContext(), "Đánh giá không được để trống", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(view.getContext(), "Gửi đánh giá", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        if (reviewList.isEmpty())
            return 0;
        return reviewList.size();
    }

    class WriteReviewViewHolder extends RecyclerView.ViewHolder {

        private Button btnSubmit;
        private TextView tvProductName, tvCategory;
        private EditText etWriteReview;
        private RatingBar ratingBar;

        public WriteReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            tvProductName = itemView.findViewById(R.id.tvProductNameReview);
            tvCategory = itemView.findViewById(R.id.tvCategoryReview);
            etWriteReview = itemView.findViewById(R.id.etWriteReview);
            btnSubmit = itemView.findViewById(R.id.btnWriteReview);
            ratingBar = itemView.findViewById(R.id.ratingWriteReview);

        }
    }
}
