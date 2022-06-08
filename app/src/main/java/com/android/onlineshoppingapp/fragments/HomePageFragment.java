package com.android.onlineshoppingapp.fragments;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.android.onlineshoppingapp.ListOfProductActivity;
import com.android.onlineshoppingapp.R;
import com.android.onlineshoppingapp.ShoppingCartActivity;
import com.android.onlineshoppingapp.adapters.BannerImageAdapter;
import com.android.onlineshoppingapp.adapters.RecyclerViewAdapterProduct;
import com.android.onlineshoppingapp.models.BannerImage;
import com.android.onlineshoppingapp.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomePageFragment extends Fragment {

    private RecyclerView recyclerTopSaleItem, recyclerForYouItem;
    private ImageView ivShoppingCart, ivSearchBtn;
    private EditText etSearch;
    private ViewPager2 viewPager2;
    private BannerImageAdapter bannerImageAdapter;
    private final Handler sliderHandler = new Handler();
    private List<BannerImage> imageList;
    public List<Product> listTopSale, listForYou;

    private FirebaseAuth fAuth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        // init
        ivShoppingCart = view.findViewById(R.id.ivShopCartHome);
        ivSearchBtn = view.findViewById(R.id.ivSearchBtn);
        etSearch = view.findViewById(R.id.etSearch);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // click on shopping cart
        ivShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ShoppingCartActivity.class));
            }
        });

        //set event for search function
        ivSearchBtn.setOnClickListener(view1 -> {
            String searchString = String.valueOf(etSearch.getText());
            Intent intent = new Intent(getContext(), ListOfProductActivity.class);
            intent.putExtra("see_more_product", "search");
            intent.putExtra("searchString", searchString);
            startActivity(intent);
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
                sliderHandler.postDelayed(sliderRunnable, 5000);
            }
        });

        listTopSale = new ArrayList<>();
        db.collection("Products")
                .orderBy("quantitySold", Query.Direction.DESCENDING)
                .limit(10)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w(TAG, "Listen failed.", error);
                            return;
                        }
                        listTopSale.clear();
                        for (QueryDocumentSnapshot document : value) {
                            Product product = document.toObject(Product.class);
                            product.setProductId(document.getId());
                            listTopSale.add(product);
                        }
                        //Top sell item
                        recyclerTopSaleItem = (RecyclerView) view.findViewById(R.id.hgTopitem);
                        recyclerTopSaleItem.setAdapter(new RecyclerViewAdapterProduct(listTopSale));
                        LinearLayoutManager layoutmangerTopitem = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        recyclerTopSaleItem.setLayoutManager(layoutmangerTopitem);
                    }
                });

        listForYou = new ArrayList<>();
        db.collection("Products")
                .orderBy("likeNumber", Query.Direction.DESCENDING)
                .limit(20)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w(TAG, "Listen failed.", error);
                            return;
                        }
                        listForYou.clear();
                        for (QueryDocumentSnapshot document : value) {
                            Product product = document.toObject(Product.class);
                            product.setProductId(document.getId());
                            listForYou.add(product);
                        }

                        //For you
                        recyclerForYouItem = (RecyclerView) view.findViewById(R.id.hgRighPrForYou);
                        RecyclerViewAdapterProduct adapterProduct = new RecyclerViewAdapterProduct(listForYou);
                        recyclerForYouItem.setAdapter(adapterProduct);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                        recyclerForYouItem.setLayoutManager(gridLayoutManager);

                        int heightOfForYou = (adapterProduct.getItemCount() % 2 == 0) ?
                                (adapterProduct.getItemCount() * 350) : (adapterProduct.getItemCount() * 460);
                        recyclerForYouItem.setMinimumHeight(heightOfForYou);
                    }
                });


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
        sliderHandler.postDelayed(sliderRunnable, 5000);
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

}
