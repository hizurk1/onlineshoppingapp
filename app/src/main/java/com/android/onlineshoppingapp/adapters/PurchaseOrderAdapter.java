package com.android.onlineshoppingapp.adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.android.onlineshoppingapp.fragments.CancelOrderFragment;
import com.android.onlineshoppingapp.fragments.ReceiveOrderFragment;
import com.android.onlineshoppingapp.fragments.ShippingFragment;
import com.android.onlineshoppingapp.fragments.WaitForProductFragment;
import com.android.onlineshoppingapp.fragments.WaitForReviewFragment;

public class PurchaseOrderAdapter extends FragmentStateAdapter {

    public PurchaseOrderAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return new ReceiveOrderFragment();
            case 1:
                return new WaitForProductFragment();
            case 2:
                return new ShippingFragment();
            case 3:
                return new WaitForReviewFragment();
            case 4:
                return new CancelOrderFragment();
            default:
                return new ReceiveOrderFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
