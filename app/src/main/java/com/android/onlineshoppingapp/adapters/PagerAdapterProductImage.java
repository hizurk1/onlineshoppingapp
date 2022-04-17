package com.android.onlineshoppingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.android.onlineshoppingapp.R;
import com.android.onlineshoppingapp.models.ProductImage;
import com.bumptech.glide.Glide;

import java.util.List;

public class PagerAdapterProductImage extends PagerAdapter {

    private Context context;
    private List<String> productImageList;

    public PagerAdapterProductImage(Context context, List<String> productImageList) {
        this.context = context;
        this.productImageList = productImageList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.slide_image_product_detail, container, false);
        ImageView ivPhoto = view.findViewById(R.id.ivImgSlideProduct);

        String productImage = productImageList.get(position);
        if (productImage != null) {
            Glide.with(context)
                    .load(productImage)
                    .into(ivPhoto);
        }
        // add view to View Group
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        if (productImageList != null) {
            return productImageList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // remove view from View Group
        container.removeView((View) object);
    }
}
