package com.android.onlineshoppingapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.onlineshoppingapp.R;
import com.android.onlineshoppingapp.adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class CategoryPageFragment extends Fragment {
    private TabLayout mTablayout;
    private ViewPager2 mViewpager;
    private ViewPagerAdapter mViewPageAdapter;
    private int []TabIcons = {
            R.drawable.ic_categories,
            R.drawable.ic_teddy,
            R.drawable.ic_highheels,
            R.drawable.ic_tshirt
    };

    //Hàm thêm các fragment vào trong list bên ViewPagerAdapter
    private void setupViewPager(ViewPager2 mViewpager) {
        mViewPageAdapter = new ViewPagerAdapter(getActivity());
        mViewPageAdapter.addFrag(new FragmentTabCategory1());
        mViewPageAdapter.addFrag(new FragmentTabCategory2());
        mViewPageAdapter.addFrag(new FragmentTabCategory3());
        mViewPageAdapter.addFrag(new FragmentTabCategory4());
        mViewpager.setAdapter(mViewPageAdapter);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
//    }
//    @Override
//    public void onStop() {
//        super.onStop();
//        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        View view = inflater.inflate(R.layout.fragment_category_page, container, false);

        mTablayout = (TabLayout) view.findViewById(R.id.tab_layout1);
        mViewpager = view.findViewById(R.id.view_pager);
        setupViewPager(mViewpager);

        new TabLayoutMediator(mTablayout, mViewpager, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Danh mục hot");
                    tab.setIcon(TabIcons[0]);
                    break;
                case 1:
                    tab.setText("Doktah kun");
                    tab.setIcon(TabIcons[1]);
                    break;
                case 2:
                    tab.setText("Gacha không xấu");
                    tab.setIcon(TabIcons[2]);
                    break;
                case 3:
                    tab.setText("Người nghiện xấu");
                    tab.setIcon(TabIcons[3]);
                    break;
            }
        }).attach();
        return view;
    }
}