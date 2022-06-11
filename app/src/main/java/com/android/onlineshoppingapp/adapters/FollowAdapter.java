package com.android.onlineshoppingapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.onlineshoppingapp.MyStoreActivity;
import com.android.onlineshoppingapp.R;
import com.android.onlineshoppingapp.models.Following;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.FollowViewHolder> {

    private List<Following> followingList;
    private Context context;

    public FollowAdapter(List<Following> followingList, Context context) {
        this.followingList = followingList;
        this.context = context;
    }


    @NonNull
    @Override
    public FollowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_following, parent, false);
        return new FollowViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FollowViewHolder holder, int position) {
        Following following = followingList.get(position);
        if (following == null)
            return;
        holder.tvShopName.setText(following.getShopName());
        AsyncTask.execute(() -> {
            String seller = following.getShopId();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseAuth fAuth = FirebaseAuth.getInstance();
            db.collection("Users")
                    .document(Objects.requireNonNull(fAuth.getCurrentUser()).getUid())
                    .collection("Following")
                    .addSnapshotListener((value, error) -> {
                        if (error != null)
                            Log.e("set text", error.getMessage());

                        if (value != null) {
                            Boolean aBoolean = true;
                            holder.btnUnfollow.setText("Theo dõi");
                            for (DocumentSnapshot documentSnapshot1 : value) {
                                if (seller.equals(documentSnapshot1.getId())) {
                                    holder.btnUnfollow.setText("Bỏ theo dõi");
                                    aBoolean = false;
                                }
                            }
                            //set event follow btn
                            if (aBoolean) {
                                //follow
                                holder.btnUnfollow.setOnClickListener(view1 -> {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("sellerRef", db.collection("Users").document(seller));
                                    db.collection("Users")
                                            .document(Objects.requireNonNull(fAuth.getCurrentUser()).getUid())
                                            .collection("Following")
                                            .document(seller)
                                            .set(map)
                                            .addOnSuccessListener(unused -> {
                                                holder.btnUnfollow.setText("Bỏ theo dõi");
                                                //increase follower of seller
                                                //get follower
                                                db.collection("Users")
                                                        .document(seller)
                                                        .get()
                                                        .addOnSuccessListener(documentSnapshot -> {
                                                            if (documentSnapshot.exists()) {
                                                                Map<String, Object> map1 = new HashMap<>();
                                                                if (documentSnapshot.getLong("followers") != null)
                                                                    map1.put("followers", documentSnapshot.getLong("followers") + 1);
                                                                else
                                                                    map1.put("followers", 1);
                                                                db.collection("Users")
                                                                        .document(seller)
                                                                        .set(map1, SetOptions.merge())
                                                                        .addOnFailureListener(e -> Log.e("increase follower", e.getMessage()));
                                                            }
                                                        });

                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(context, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                                                Log.e("follow store", e.getMessage());
                                            });
                                });
                            } else {
                                //unfollow
                                holder.btnUnfollow.setOnClickListener(view1 -> {
                                    db.collection("Users")
                                            .document(fAuth.getCurrentUser().getUid())
                                            .collection("Following")
                                            .document(seller)
                                            .delete()
                                            .addOnSuccessListener(unused -> {
                                                holder.btnUnfollow.setText("Theo dõi");
                                                //decrease follower of seller
                                                //get follower
                                                db.collection("Users")
                                                        .document(seller)
                                                        .get()
                                                        .addOnSuccessListener(documentSnapshot -> {
                                                            if (documentSnapshot.exists()) {
                                                                Map<String, Object> map1 = new HashMap<>();
                                                                if (documentSnapshot.getLong("followers") != null)
                                                                    map1.put("followers", documentSnapshot.getLong("followers") - 1);
                                                                else
                                                                    map1.put("followers", 0);
                                                                db.collection("Users")
                                                                        .document(seller)
                                                                        .set(map1, SetOptions.merge())
                                                                        .addOnFailureListener(e -> Log.e("decrease follower", e.getMessage()));
                                                            }
                                                        });
                                            })
                                            .addOnFailureListener(e -> Log.e("delete seller", e.getMessage()));
                                });
                            }
                        }
                    });
        });

        if (following.getShopAvatar() != null)
            Glide.with(context)
                    .load(following.getShopAvatar())
                    .into(holder.ivAvatar);
        else
            holder.ivAvatar.setImageResource(R.drawable.logoapp);

        //set event
        holder.ivAvatar.setOnClickListener(view -> {
            Intent intent = new Intent(context, MyStoreActivity.class);
            intent.putExtra("sellerOfProduct", following.getShopId());
            context.startActivity(intent);
        });

        holder.tvShopName.setOnClickListener(view -> {
            Intent intent = new Intent(context, MyStoreActivity.class);
            intent.putExtra("sellerOfProduct", following.getShopId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (followingList.isEmpty())
            return 0;
        return followingList.size();
    }


    class FollowViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivAvatar;
        private final Button btnUnfollow;
        private final TextView tvShopName;

        public FollowViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAvatar = itemView.findViewById(R.id.ivAvatarShopFollow);
            btnUnfollow = itemView.findViewById(R.id.btnUnfollow);
            tvShopName = itemView.findViewById(R.id.tvShopNameFollow);
        }
    }
}
