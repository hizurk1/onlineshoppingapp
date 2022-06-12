package com.android.onlineshoppingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.onlineshoppingapp.adapters.BestSellingAdapter;
import com.android.onlineshoppingapp.models.Order;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SaleStatisticActivity extends AppCompatActivity {

    private ImageView ivBack;
    private BarChart barChart;
    private Spinner spinner;
    private RecyclerView recyclerView;
    private BestSellingAdapter adapter;
    private TextView tvTotalThisMonth, tvTotalLastMonth;

    private FirebaseAuth fAuth;
    private FirebaseFirestore db;
    private List<Long> monthList = new ArrayList<>();
    private List<String> monthStringList = new ArrayList<String>();
    private Long totalQuantity = 0L;
    private Long orderQuantity = 0L;
    private List<Order> orderList = new ArrayList<>();
    private ArrayList<BarEntry> chartModel = new ArrayList<>();
    private Long saleThisMonth = 0L, saleLastMonth = 0L;


    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_statistic);

        ivBack = findViewById(R.id.ivBackSaleStatistic);
        barChart = findViewById(R.id.barChartSaleStatistic);
        spinner = findViewById(R.id.spinSaleStatistic);
        tvTotalThisMonth = findViewById(R.id.tvTotalThisMonth);
        tvTotalLastMonth = findViewById(R.id.tvTotalLastMonth);
//        recyclerView = findViewById(R.id.rvBestSellingStatistic);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        ivBack.setOnClickListener(view -> onBackPressed());

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

        for (int month = 1; month <= 12; month++) {
            monthStringList.add("Tháng " + month);
            monthList.add((long) month);
        }
        arrayAdapter.notifyDataSetChanged();

        AsyncTask.execute(() -> {
            db.collection("Orders")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.getResult() != null) {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                Order order = documentSnapshot.toObject(Order.class);
                                if (order != null) {
                                    order.setOrderId(documentSnapshot.getId());
                                }
                                orderList.add(order);
                            }
                        }
                        getDataByMonth(monthList.get(0));
                    });
        });

        saleThisMonth = 0L;
        saleLastMonth = 0L;
        tvTotalLastMonth.setText(String.format("%,dđ", saleLastMonth));
        tvTotalThisMonth.setText(String.format("%,dđ", saleThisMonth));


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

    @SuppressLint("DefaultLocale")
    private void getDataByMonth(Long month) {
        saleThisMonth = 0L;
        saleLastMonth = 0L;
        tvTotalLastMonth.setText(String.format("%,dđ", saleLastMonth));
        tvTotalThisMonth.setText(String.format("%,dđ", saleThisMonth));

        for (int i = 0; i <= 30; i++)
            chartModel.set(i, new BarEntry(i, 0));


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
                                    saleThisMonth += documentSnapshot.getLong("productPrice");
                                }
                                try {
                                    int day = new SimpleDateFormat("dd/MM/yyyy").parse(order.getOrderTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth();
                                    if (quantitySaleMap.get(day) != null)
                                        quantitySaleMap.put(day, quantitySaleMap.get(day) + temp);
                                    else
                                        quantitySaleMap.put(day, temp);
                                    chartModel.set(day-1, new BarEntry(day, quantitySaleMap.get(day)));
                                    BarDataSet barDataSet = new BarDataSet(chartModel, "Monthly Sales Statistics");
                                    barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                                    barDataSet.setValueTextColor(Color.BLACK);
                                    barDataSet.setValueTextSize(12f);

                                    BarData barData = new BarData(barDataSet);

                                    barChart.setFitBars(true);
                                    barChart.setData(barData);
                                    barChart.getDescription().setEnabled(false);
                                    barChart.animateY(1000);
                                    tvTotalThisMonth.setText(String.format("%,dđ", saleThisMonth));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            });
                } else if (monthTemp == (month - 1)) {
                    db.collection("Orders")
                            .document(order.getOrderId())
                            .collection("Products")
                            .whereEqualTo("seller", Objects.requireNonNull(fAuth.getCurrentUser()).getUid())
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                                    saleLastMonth += documentSnapshot.getLong("productPrice");
                                tvTotalLastMonth.setText(String.format("%,dđ", saleLastMonth));
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

        resetChart();
        getDataByMonth(monthList.get(position));


    }

    private void resetChart() {
        for (int i = 0; i <= 30; i++)
            chartModel.set(i, new BarEntry(i, 0));
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
}