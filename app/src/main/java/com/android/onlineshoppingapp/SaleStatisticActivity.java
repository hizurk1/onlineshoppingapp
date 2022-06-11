package com.android.onlineshoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.onlineshoppingapp.adapters.BestSellingAdapter;
import com.android.onlineshoppingapp.models.Product;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SaleStatisticActivity extends AppCompatActivity {

    private ImageView ivBack;
    private BarChart barChart;
    private Spinner spinner;
    private RecyclerView recyclerView;
    private BestSellingAdapter adapter;

    private FirebaseAuth fAuth;
    private FirebaseFirestore db;

    private String[] filters = {"Tháng này", "Tháng trước", "Toàn bộ"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_statistic);

        ivBack = findViewById(R.id.ivBackSaleStatistic);
        barChart = findViewById(R.id.barChartSaleStatistic);
        spinner = findViewById(R.id.spinSaleStatistic);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        ivBack.setOnClickListener(view -> {
            onBackPressed();
        });

        //set spinner data
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                filters);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                onItemSelectedHandler(adapterView, view, position, id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayList<BarEntry> chartModel = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            chartModel.add(new BarEntry(i, new Random().nextInt(20)));
        }
        BarDataSet barDataSet = new BarDataSet(chartModel, "Monthly Sales Statistics");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(12f);

        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        barChart.animateY(1000);

    }


    private void onItemSelectedHandler(AdapterView<?> adapterView, View view, int position, long id) {

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Adapter adapterSpinner = adapterView.getAdapter();
        String filter = (String) adapterSpinner.getItem(position);

        if (filter.equals(filters[0])) {
            Toast.makeText(this, "Tháng này", Toast.LENGTH_SHORT).show();
        } else if (filter.equals(filters[1])) {
            Toast.makeText(this, "Tháng trước", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Toàn bộ", Toast.LENGTH_SHORT).show();
        }

    }
}