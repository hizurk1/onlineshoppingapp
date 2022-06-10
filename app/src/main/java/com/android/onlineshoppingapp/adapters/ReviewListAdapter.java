package com.android.onlineshoppingapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.onlineshoppingapp.R;
import com.android.onlineshoppingapp.models.Review;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ReviewListViewHolder> {

    private List<Review> reviewList;
    private Context context;

    public ReviewListAdapter(List<Review> reviewList, Context context) {
        this.reviewList = reviewList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_review_product, parent, false);
        return new ReviewListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewListViewHolder holder, int position) {
        Review review = reviewList.get(position);
        if (review == null)
            return;
        // show data
        holder.ratingBar.setRating((float) review.getRate());
        holder.tvContent.setText(review.getContent());
        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(review.getReviewer())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    holder.tvReviewer.setText(String.format("bởi %s", documentSnapshot.getString("lastName") + " " + documentSnapshot.getString("firstName")));
                });


        try {
            holder.tvTime.setText(timeAgo(new Date(),
                    new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(review.getCreatedDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        if (reviewList.isEmpty()) {
            return 0;
        }
        return reviewList.size();
    }

    class ReviewListViewHolder extends RecyclerView.ViewHolder {

        private RatingBar ratingBar;
        private TextView tvContent, tvReviewer, tvTime;
        private ImageView ivDelete;

        public ReviewListViewHolder(@NonNull View itemView) {
            super(itemView);

            ratingBar = itemView.findViewById(R.id.ratingReviewItem);
            tvContent = itemView.findViewById(R.id.tvReviewContentItem);
            tvReviewer = itemView.findViewById(R.id.tvCustomerReviewItem);
            tvTime = itemView.findViewById(R.id.tvTimeReviewItem);
            ivDelete = itemView.findViewById(R.id.ivDeleteReviewItem);

        }
    }


    public String timeAgo(java.util.Date currentDate, java.util.Date pastDate) {

        long milliSecPerMinute = 60 * 1000; //Milliseconds Per Minute
        long milliSecPerHour = milliSecPerMinute * 60; //Milliseconds Per Hour
        long milliSecPerDay = milliSecPerHour * 24; //Milliseconds Per Day
        long milliSecPerMonth = milliSecPerDay * 30; //Milliseconds Per Month
        long milliSecPerYear = milliSecPerDay * 365; //Milliseconds Per Year
        //Difference in Milliseconds between two dates
        long msExpired = currentDate.getTime() - pastDate.getTime();
        //Second or Seconds ago calculation
        if (msExpired < milliSecPerMinute) {
            return String.valueOf(Math.round(msExpired / 1000) + " giây trước");
        }
        //Minute or Minutes ago calculation
        else if (msExpired < milliSecPerHour) {
            return String.valueOf(Math.round(msExpired / milliSecPerMinute)) + " phút trước";
        }
        //Hour or Hours ago calculation
        else if (msExpired < milliSecPerDay) {
            return String.valueOf(Math.round(msExpired / milliSecPerHour)) + " giờ trước";
        }
        //Day or Days ago calculation
        else if (msExpired < milliSecPerMonth) {
            return String.valueOf(Math.round(msExpired / milliSecPerDay)) + " ngày trước";
        }
        //Month or Months ago calculation
        else if (msExpired < milliSecPerYear) {
            return String.valueOf(Math.round(msExpired / milliSecPerMonth)) + "  tháng trước";
        }
        //Year or Years ago calculation
        else {
            return String.valueOf(Math.round(msExpired / milliSecPerYear)) + " năm trước";
        }
    }
}
