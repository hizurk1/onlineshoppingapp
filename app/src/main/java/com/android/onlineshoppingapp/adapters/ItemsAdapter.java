package com.android.onlineshoppingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.onlineshoppingapp.R;

import java.util.List;


//Class dùng để quản lý các thuộc tính các items
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder>{
    private Context mContext;
    private List<com.android.onlineshoppingapp.adapters.Items> mListItems;

    public ItemsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<com.android.onlineshoppingapp.adapters.Items> list){
        this.mListItems = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent,false);
        return new ItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {
        com.android.onlineshoppingapp.adapters.Items item = mListItems.get(position);
        if(item == null){
            return;
        }
        holder.ItemsImg.setImageResource(item.getResourceImg());
        holder.ItemsText.setText(item.getText());
    }

    @Override
    public int getItemCount() {
        if(mListItems != null){
            return mListItems.size();
        }
        return 0;
    }

    public  class  ItemsViewHolder extends RecyclerView.ViewHolder{
        private ImageView ItemsImg;
        private TextView ItemsText;

        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);

            ItemsImg = itemView.findViewById(R.id.cv_image);
            ItemsText = itemView.findViewById(R.id.cv_text);
        }
    }
}
