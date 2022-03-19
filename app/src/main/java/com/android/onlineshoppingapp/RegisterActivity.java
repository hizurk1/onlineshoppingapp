package com.android.onlineshoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private AutoCompleteTextView textViewListDay, textViewListMonth, textViewListYear;
    private ArrayList<Integer> arrayListDay, arrayListMonth, arrayListYear;
    private TextInputEditText editTextUsername, editTextPassword, editTextRePassword, editTextPhone, editTextEmail;
    private TextInputLayout txtLayoutUsername, txtLayoutPassword, txtLayoutRePassword, txtLayoutPhone, txtLayoutEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Check null input data: username
        editTextUsername = findViewById(R.id.editTxtUsername);
        txtLayoutUsername = findViewById(R.id.txtFieldUsername);
        editTextUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean onFocus) {
                if (!onFocus) {
                    if (editTextUsername.getText().toString().equals("")) {
                        txtLayoutUsername.setHelperText("Tên đăng nhập không được để trống!");
                    }
                } else {
                    txtLayoutUsername.setHelperTextEnabled(false);
                }
            }
        });

        // Check null input data: email
        editTextEmail = findViewById(R.id.editTxtEmail);
        txtLayoutEmail = findViewById(R.id.txtFieldEmail);
        editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean onFocus) {
                if (!onFocus) {
                    if (editTextEmail.getText().toString().equals("")) {
                        txtLayoutEmail.setHelperText("Email không được để trống!");
                    }
                } else {
                    txtLayoutEmail.setHelperTextEnabled(false);
                }
            }
        });

        // Check null input data: password
        editTextPassword = findViewById(R.id.editTxtPassword);
        txtLayoutPassword = findViewById(R.id.txtFieldPassword);
        editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean onFocus) {
                if (!onFocus) {
                    if (editTextPassword.getText().toString().equals("")) {
                        txtLayoutPassword.setHelperText("Mật khẩu không được để trống!");
                    }
                } else {
                    txtLayoutPassword.setHelperTextEnabled(false);
                }
            }
        });

        // Check null input data: Re-password
        editTextRePassword = findViewById(R.id.editTxtRePassword);
        txtLayoutRePassword = findViewById(R.id.txtFieldRePassword);
        editTextRePassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean onFocus) {
                if (!onFocus) {
                    if (editTextRePassword.getText().toString().equals(editTextPassword.getText().toString()))
                        txtLayoutRePassword.setHelperTextEnabled(false);
                    else
                        txtLayoutRePassword.setHelperText("Xác nhận mật khẩu không khớp!");
                }
            }
        });

        // Check null input data: phone
        editTextPhone = findViewById(R.id.editTxtPhone);
        txtLayoutPhone = findViewById(R.id.txtFieldPhone);
        editTextPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean onFocus) {
                if (!onFocus) {
                    if (editTextPhone.getText().toString().equals("")) {
                        txtLayoutPhone.setHelperText("Số điện thoại không được để trống!");
                    }
                } else {
                    txtLayoutPhone.setHelperTextEnabled(false);
                }
            }
        });

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