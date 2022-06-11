package com.android.onlineshoppingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.onlineshoppingapp.R;
import com.android.onlineshoppingapp.models.Following;

import java.util.List;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.FollowViewHolder> {

    private List<Following> followingList;

    public FollowAdapter(List<Following> followingList) {
        this.followingList = followingList;
    }

    @NonNull
    @Override
    public FollowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_following, parent, false);
        return new FollowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowViewHolder holder, int position) {
        Following following = followingList.get(position);
        if (following == null)
            return;
        holder.tvShopName.setText(following.getShopName());
        holder.btnUnfollow.setOnClickListener(view -> {
            Toast.makeText(view.getContext(), "Đã bỏ theo dõi cửa hàng", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        if (followingList.isEmpty())
            return 0;
        return followingList.size();
    }


    class FollowViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivAvatar;
        private Button btnUnfollow;
        private TextView tvShopName;

        public FollowViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAvatar = itemView.findViewById(R.id.ivAvatarShopFollow);
            btnUnfollow = itemView.findViewById(R.id.btnUnfollow);
            tvShopName = itemView.findViewById(R.id.tvShopNameFollow);
        }
    }
}
