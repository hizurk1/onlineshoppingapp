package com.android.onlineshoppingapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

//Đây là class dùng để setup cho tablayout
public class ViewPagerAdapter extends FragmentStateAdapter {
    //Khai báo các arraylist chứa fragment ở đây
    private final List<Fragment> mFragmentList = new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    //Tạo các dẫn tới các fragment tương ứng
    @NonNull
    @Override
    public Fragment createFragment(int position) { return mFragmentList.get(position); }

    public void addFrag(Fragment fragment) { mFragmentList.add(fragment); }

    //Khai báo số lượng các fragment
    //Có thể dùng arraylist để chứa các fragment rồi dùng size() để khai báo số lượng fragment
    @Override
    public int getItemCount() {
        return mFragmentList.size();
    }


}
