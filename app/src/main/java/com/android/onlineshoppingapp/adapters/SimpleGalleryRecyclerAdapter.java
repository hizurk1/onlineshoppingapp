package com.android.onlineshoppingapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.onlineshoppingapp.R;
import com.android.onlineshoppingapp.models.Product;

import java.util.List;

public class SimpleGalleryRecyclerAdapter extends RecyclerView.Adapter<SimpleGalleryRecyclerAdapter.ImageViewHolder> {
    private List<Uri> listUri;
    private Context context;

    public SimpleGalleryRecyclerAdapter(List<Uri> listUri, Context context) {
        this.listUri = listUri;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imageview_simplegallery_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Uri uri = listUri.get(position);
        if (uri == null) {
            return;
        }
        setValueOfEachItem(holder, uri);
    }

    private void setValueOfEachItem(ImageViewHolder holder, Uri uri) {
        holder.imageViewSimpleGallery.setImageURI(uri);


    }

    @Override
    public int getItemCount() {
        if (listUri != null)
            return listUri.size();
        return 0;
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewSimpleGallery;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            //init
            imageViewSimpleGallery = (ImageView) itemView.findViewById(R.id.imageViewSimpleGallery);

        }
    }
}
