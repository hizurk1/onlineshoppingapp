package com.android.onlineshoppingapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.android.onlineshoppingapp.R;
import com.android.onlineshoppingapp.adapters.RecyclerViewAdapterProduct;
import com.android.onlineshoppingapp.models.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePageFragment extends Fragment {


    private ViewFlipper viewFlipper;
    private RecyclerView recyclerTopitem;
    private RecyclerView recyclerRightItem;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomePageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomePageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomePageFragment newInstance(String param1, String param2) {
        HomePageFragment fragment = new HomePageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int images[] = {R.drawable.banner1,R.drawable.banner2};
        List<Product> list = getlistData();

        View view   = inflater.inflate(R.layout.fragment_home_page,container,false);

        //View flipper
        viewFlipper = view.findViewById(R.id.v_flipper);



        //Top item
        recyclerTopitem = (RecyclerView) view.findViewById(R.id.hgTopitem);
        recyclerTopitem.setAdapter(new RecyclerViewAdapterProduct(list));
        LinearLayoutManager layoutmangerTopitem = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerTopitem.setLayoutManager(layoutmangerTopitem);



        //Right item
        recyclerRightItem = (RecyclerView) view.findViewById(R.id.hgRighPrForYou);
        recyclerRightItem.setAdapter(new RecyclerViewAdapterProduct(list));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerRightItem.setLayoutManager(gridLayoutManager);
        return view;
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

    public void flipperImages(int image){
        ImageView imageView = new ImageView(getActivity());
        imageView.setBackgroundResource(image);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(400);
        viewFlipper.setAutoStart(true);
    }

}
