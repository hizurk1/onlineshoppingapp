package com.android.onlineshoppingapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.android.onlineshoppingapp.fragments.UserAddressFragment;
import com.android.onlineshoppingapp.fragments.UserInformationFragment;
import com.android.onlineshoppingapp.fragments.UserPaymentInfoFragment;

public class ViewPagerAdapterSettings extends FragmentStateAdapter {

    private String[] titles = new String[]{"Cá nhân", "Địa chỉ", "Thanh toán"};

    public ViewPagerAdapterSettings(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new UserInformationFragment();
            case 1:
                return new UserAddressFragment();
            case 2:
                return new UserPaymentInfoFragment();
        }
        return new UserInformationFragment();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
