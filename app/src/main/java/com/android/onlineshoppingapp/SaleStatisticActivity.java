package com.android.onlineshoppingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
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
import com.android.onlineshoppingapp.models.Order;
import com.android.onlineshoppingapp.models.OrderProduct;
import com.android.onlineshoppingapp.models.Product;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class SaleStatisticActivity extends AppCompatActivity {

    private ImageView ivBack;
    private BarChart barChart;
    private Spinner spinner;
    private RecyclerView recyclerView;
    private BestSellingAdapter adapter;

    private FirebaseAuth fAuth;
    private FirebaseFirestore db;
    private List<Long> monthList = new ArrayList<>();
    private List<String> monthStringList = new ArrayList<String>();
    private Long totalQuantity = 0L;
    private Long orderQuantity = 0L;
    private List<Order> orderList = new ArrayList<>();
    private ArrayList<BarEntry> chartModel = new ArrayList<>();


    private String[] filters = {"Tháng này", "Tháng trước", "Toàn bộ"};

    @SuppressLint("SimpleDateFormat")
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
                monthStringList);
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

        for (int i = 1; i <= 31; i++) {
//            chartModel.add(new BarEntry(i, new Random().nextInt(20)));
            chartModel.add(new BarEntry(i, 0));
        }


        AsyncTask.execute(() -> {
            db.collection("Orders")
                    .get()
                    .addOnCompleteListener(task -> {
                        monthList.clear();
                        monthStringList.clear();
                        if (task.getResult() != null) {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                try {
                                    LocalDate localDate = new SimpleDateFormat("dd/MM/yyyy").parse(documentSnapshot.getString("orderTime")).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                    int month = localDate.getMonthValue();
                                    if (!monthStringList.contains("Tháng " + month)) {
                                        monthStringList.add("Tháng " + month);
                                        monthList.add((long) month);
                                        arrayAdapter.notifyDataSetChanged();
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Order order = documentSnapshot.toObject(Order.class);
                                if (order != null) {
                                    order.setOrderId(documentSnapshot.getId());
                                }
                                orderList.add(order);
                            }
                        }

                        getDataByMonth(monthList.get(monthList.size() - 1));
                    });
        });


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

    private void getDataByMonth(Long month) {
        for (int i = 0; i <= 30; i++) {
//            chartModel.add(new BarEntry(i, new Random().nextInt(20)));
            chartModel.set(i,new BarEntry(i, 0));
        }
        Map<Integer, Long> quantitySaleMap = new HashMap<>();
        for (Order order : orderList) {
            LocalDate localDate = null;
            try {
                localDate = new SimpleDateFormat("dd/MM/yyyy").parse(order.getOrderTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                int monthTemp = localDate.getMonthValue();
                if (monthTemp == month) {
                    db.collection("Orders")
                            .document(order.getOrderId())
                            .collection("Products")
                            .whereEqualTo("seller", Objects.requireNonNull(fAuth.getCurrentUser()).getUid())
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                Long temp = 0L;
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    temp += documentSnapshot.getLong("orderQuantity");
                                }
                                try {
                                    int day = new SimpleDateFormat("dd/MM/yyyy").parse(order.getOrderTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth();
                                    if (quantitySaleMap.get(day) != null)
                                        quantitySaleMap.put(day, quantitySaleMap.get(day) + temp);
                                    else
                                        quantitySaleMap.put(day, temp);
                                    chartModel.set(day, new BarEntry(day, quantitySaleMap.get(day)));
                                    BarDataSet barDataSet = new BarDataSet(chartModel, "Monthly Sales Statistics");
                                    barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                                    barDataSet.setValueTextColor(Color.BLACK);
                                    barDataSet.setValueTextSize(12f);

                                    BarData barData = new BarData(barDataSet);

                                    barChart.setFitBars(true);
                                    barChart.setData(barData);
                                    barChart.getDescription().setEnabled(false);
                                    barChart.animateY(1000);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            });
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    private void onItemSelectedHandler(AdapterView<?> adapterView, View view, int position,
                                       long id) {

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Adapter adapterSpinner = adapterView.getAdapter();
        String filter = (String) adapterSpinner.getItem(position);

        getDataByMonth(monthList.get(position));


        if (filter.equals(filters[0])) {
            Toast.makeText(this, "Tháng này", Toast.LENGTH_SHORT).show();
        } else if (filter.equals(filters[1])) {
            Toast.makeText(this, "Tháng trước", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Toàn bộ", Toast.LENGTH_SHORT).show();
        }

    }
}