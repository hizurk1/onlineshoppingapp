package com.android.onlineshoppingapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.onlineshoppingapp.R;
import com.android.onlineshoppingapp.adapters.Items;
import com.android.onlineshoppingapp.adapters.ItemsAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentTabCategory1 extends Fragment {
    private RecyclerView rvItems;
    private ItemsAdapter mItemAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_cate_1, container, false);

        rvItems = (RecyclerView) view.findViewById(R.id.rcv_items);
        mItemAdapter = new ItemsAdapter(getContext());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);

        rvItems.setLayoutManager(gridLayoutManager);

        //Hàm đưa dữ liệu vào
        mItemAdapter.setData(getListItems());
        rvItems.setAdapter(mItemAdapter);
        return view;
    }

    //Hàm lấy dữ liệu đưa vào list để truyền vào hàm setData ở trên
    private List<Items> getListItems() {
        List<Items> list = new ArrayList<Items>();
        for (int i = 1; i <= 8; i++) {
            list.add(new Items(R.drawable.img_sample, "Danh mục " + i));
        }

        return list;
    }
}