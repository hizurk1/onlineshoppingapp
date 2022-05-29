package com.android.onlineshoppingapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.android.onlineshoppingapp.R;
import com.android.onlineshoppingapp.ShoppingCartActivity;
import com.android.onlineshoppingapp.adapters.BannerImageAdapter;
import com.android.onlineshoppingapp.adapters.RecyclerViewAdapterProduct;
import com.android.onlineshoppingapp.models.BannerImage;
import com.android.onlineshoppingapp.models.Product;

import java.util.ArrayList;
import java.util.List;

public class HomePageFragment extends Fragment {

    private RecyclerView recyclerTopitem, recyclerRightItem;
    private ImageView ivShoppingCart;
    private ViewPager2 viewPager2;
    private List<BannerImage> imageList;
    private BannerImageAdapter bannerImageAdapter;
    private final Handler sliderHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        // init
        List<Product> list = getlistData();

        ivShoppingCart = view.findViewById(R.id.ivShopCartHome);

        // click on shopping cart
        ivShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ShoppingCartActivity.class));
            }
        });

        // banner image
        viewPager2 = view.findViewById(R.id.viewPagerBannerHome);
        imageList = getBannerImage();
        bannerImageAdapter = new BannerImageAdapter(imageList, viewPager2);
        viewPager2.setAdapter(bannerImageAdapter);

        viewPager2.setOffscreenPageLimit(3);
        viewPager2.setClipChildren(false);
        viewPager2.setClipToPadding(false);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(20));
        transformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        viewPager2.setPageTransformer(transformer);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });

        //Top sell item
        recyclerTopitem = (RecyclerView) view.findViewById(R.id.hgTopitem);
        recyclerTopitem.setAdapter(new RecyclerViewAdapterProduct(list));
        LinearLayoutManager layoutmangerTopitem = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerTopitem.setLayoutManager(layoutmangerTopitem);

        //For you
        recyclerRightItem = (RecyclerView) view.findViewById(R.id.hgRighPrForYou);
        RecyclerViewAdapterProduct adapterProduct = new RecyclerViewAdapterProduct(list);
        recyclerRightItem.setAdapter(adapterProduct);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerRightItem.setLayoutManager(gridLayoutManager);

        int heightOfForYou = (adapterProduct.getItemCount() % 2 == 0) ?
                (adapterProduct.getItemCount() * 350) : (adapterProduct.getItemCount() * 460);
        recyclerRightItem.setMinimumHeight(heightOfForYou);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    private List<BannerImage> getBannerImage() {
        List<BannerImage> list = new ArrayList<>();
        list.add(new BannerImage(R.drawable.banner1));
        list.add(new BannerImage(R.drawable.banner2));
        list.add(new BannerImage(R.drawable.banner3));

        return list;
    }

    private List<Product> getlistData() {
        List<Product> list = new ArrayList<Product>();
        Product item1 = new Product(String.valueOf(1), "Xe mô hình", "String seller", "Vượt mọi trở ngại, thẳng tiến vạch đích!!!", 999, 5, 174, 23);
        Product item2 = new Product(String.valueOf(2), "Xe mô hình", "String seller", "Vượt mọi trở ngại, thẳng tiến vạch đích!!!", 999, 5, 174, 23);
        Product item3 = new Product(String.valueOf(3), "Xe mô hình", "String seller", "Vượt mọi trở ngại, thẳng tiến vạch đích!!!", 999, 5, 174, 23);
        Product item4 = new Product(String.valueOf(4), "Xe mô hình", "String seller", "Vượt mọi trở ngại, thẳng tiến vạch đích!!!", 999, 5, 174, 23);
        Product item5 = new Product(String.valueOf(5), "Xe mô hình", "String seller", "Vượt mọi trở ngại, thẳng tiến vạch đích!!!", 999, 5, 174, 23);
        Product item6 = new Product(String.valueOf(6), "Xe mô hình", "String seller", "Vượt mọi trở ngại, thẳng tiến vạch đích!!!", 999, 5, 174, 23);
        Product item7 = new Product(String.valueOf(7), "Xe mô hình", "String seller", "Vượt mọi trở ngại, thẳng tiến vạch đích!!!", 999, 5, 174, 23);
        Product item8 = new Product(String.valueOf(8), "Xe mô hình", "String seller", "Vượt mọi trở ngại, thẳng tiến vạch đích!!!", 999, 5, 174, 23);
        list.add(item1);
        list.add(item2);
        list.add(item3);
        list.add(item4);
        list.add(item5);
        list.add(item6);
        list.add(item7);
        list.add(item8);
        return list;
    }

}
