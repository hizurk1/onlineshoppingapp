package com.android.onlineshoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private AutoCompleteTextView textViewListDay, textViewListMonth, textViewListYear;
    private ArrayList<Integer> arrayListDay, arrayListMonth, arrayListYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // List of Day
        textViewListDay = findViewById(R.id.tvListDay);
        arrayListDay = new ArrayList<Integer>();
        for (int i = 1; i <= 31; i++) {
            arrayListDay.add(i);
        }
        ArrayAdapter<Integer> listDayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                arrayListDay
        );
        textViewListDay.setAdapter(listDayAdapter);

        // List of Month
        textViewListMonth = findViewById(R.id.tvListMonth);
        arrayListMonth = new ArrayList<Integer>();
        for (int i = 1; i <= 12; i++) {
            arrayListMonth.add(i);
        }
        ArrayAdapter<Integer> listMonthAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                arrayListMonth
        );
        textViewListMonth.setAdapter(listMonthAdapter);

        //List of Year
        textViewListYear = findViewById(R.id.tvListYear);
        arrayListYear = new ArrayList<>();
        int currentYear = LocalDateTime.now().getYear();
        for (int i = currentYear; i > currentYear - 100; i--) {
            arrayListYear.add(i);
        }
        ArrayAdapter<Integer> listYearAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                arrayListYear
        );
        textViewListYear.setAdapter(listYearAdapter);

    }


}