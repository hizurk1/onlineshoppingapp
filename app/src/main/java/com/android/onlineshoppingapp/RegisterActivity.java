package com.android.onlineshoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private ImageView imageViewBack;
    private AutoCompleteTextView textViewListDay, textViewListMonth, textViewListYear;
    private ArrayList<Integer> arrayListDay, arrayListMonth, arrayListYear;
    private TextInputEditText editTextUsername, editTextPassword, editTextRePassword, editTextPhone, editTextEmail;
    private TextInputLayout txtLayoutUsername, txtLayoutPassword, txtLayoutRePassword, txtLayoutPhone, txtLayoutEmail;
    private Button btnRegister;
    private RadioGroup radioGroupAccountType, radioGroupSex;
    private RadioButton radioButtonPurchase, radioButtonSell, radioButtonMale, radioButtonFemale, radioButtonOther;
    private CheckBox checkBoxAgree;
    private String accountType, sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Click on back button
        imageViewBack = findViewById(R.id.ivBackRegister);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // navigate to login activity
                finish();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        // Check null input data: username
        editTextUsername = findViewById(R.id.editTxtUsername);
        txtLayoutUsername = findViewById(R.id.txtFieldUsername);
        editTextUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean onFocus) {
                if (!onFocus) {
                    if (editTextUsername.getText().toString().equals("")) {
                        txtLayoutUsername.setHelperText("Tên đăng nhập không được để trống!");
                    } else if (!includeCharInAlphabet(editTextUsername.getText().toString())) {
                        txtLayoutUsername.setHelperText("Tên đăng nhập phải chứa ít nhất 1 ký tự chữ");
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
                    } else if (!hasCharacter(editTextEmail.getText().toString(), '@'))
                        txtLayoutEmail.setHelperText("Email bạn vừa nhập không đúng định dạng");
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
                    } else if (!isLongEnough(editTextPassword.getText().toString(), 8)) {
                        txtLayoutPassword.setHelperText("Mật khẩu phải có ít nhất 8 kí tự");
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
                    if (editTextRePassword.getText().toString().equals(editTextPassword.getText().toString())) {
                        txtLayoutRePassword.setHelperTextEnabled(false);
                    }
                    else {
                        txtLayoutRePassword.setHelperText("Xác nhận mật khẩu không đúng!");
                    }
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
                    } else if (!isLongEnough(editTextPhone.getText().toString(), 6))
                        txtLayoutPhone.setHelperText("Số điện thoại bạn vừa nhập không hợp lệ");
                } else {
                    txtLayoutPhone.setHelperTextEnabled(false);
                }
            }
        });

        // Choose sex
        radioGroupSex = findViewById(R.id.radioGroupSex);
        radioButtonMale = findViewById(R.id.radioBtnMale);
        radioButtonFemale = findViewById(R.id.radioBtnFemale);
        radioButtonOther = findViewById(R.id.radioBtnOther);

        sex = radioButtonMale.getText().toString();
        radioGroupSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioBtnMale:
                        sex = radioButtonMale.getText().toString();
                        break;
                    case R.id.radioBtnFemale:
                        sex = radioButtonFemale.getText().toString();
                        break;
                    case R.id.radioBtnOther:
                        sex = radioButtonOther.getText().toString();
                        break;
                    default:
                        break;
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

        // List of Year
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

        // Choose type of account
        radioGroupAccountType = findViewById(R.id.rgAccType);
        radioButtonPurchase = findViewById(R.id.radioBtnPurchase);
        radioButtonSell = findViewById(R.id.radioBtnSell);

        accountType = radioButtonPurchase.getText().toString();
        radioGroupAccountType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioBtnPurchase:
                        accountType = radioButtonPurchase.getText().toString();
                        break;
                    case R.id.radioBtnSell:
                        accountType = radioButtonSell.getText().toString();
                        break;
                    default:
                        break;
                }
            }
        });

        // Click on check box
        checkBoxAgree = findViewById(R.id.checkBoxAgree);

        // Click on Register button
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // validate date of birth
                if (validateDateOfBirth()) {
                    if (editTextUsername.getText().toString().equals("") ||
                            editTextEmail.getText().toString().equals("") ||
                            editTextPassword.getText().toString().equals("")) {
                        Toast.makeText(RegisterActivity.this, "Bạn chưa điền đầy đủ thông tin!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        if (checkBoxAgree.isChecked()) {
                            Toast.makeText(RegisterActivity.this,
                                    "Username: " + editTextUsername.getText().toString() +
                                            "\nEmail: " + editTextEmail.getText().toString() +
                                            "\nPassword: " + editTextPassword.getText().toString() +
                                            "\nPhone: " + editTextPhone.getText().toString() +
                                            "\nGiới tính: " + sex +
                                            "\nSN: " + textViewListDay.getText().toString() +
                                            "/" + textViewListMonth.getText().toString() +
                                            "/" + textViewListYear.getText().toString() +
                                            "\nLoại tài khoản: " + accountType +
                                            "\nĐăng ký thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this,
                                    "Bạn phải đồng ý với các Điều khoản dịch vụ \n\t\t\t\t\t\t\t\tvà Chính sách bảo mật!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });
    }

    // ----------------------- Function ------------------------

    private boolean includeCharInAlphabet(String str) {
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        for (int i = 0; i < str.length(); i++) {
            for (int j = 0; j < alphabet.length; j++) {
                if (str.toLowerCase().charAt(i) == alphabet[j])
                    return true;
            }
        }
        return false;
    }

    private boolean hasCharacter(String str, char target) {
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == target)
                return true;
        }
        return false;
    }

    private boolean isLongEnough(String str, int num) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            count++;
        }
        if (count >= num) return true;
            else return false;
    }

    private boolean validateDateOfBirth() {
        int day = Integer.valueOf(textViewListDay.getText().toString());
        int month = Integer.valueOf(textViewListMonth.getText().toString());
        int year = Integer.valueOf(textViewListYear.getText().toString());

        // check leap year
        if (!checkLeapYear(year)) {
            if (day > dayOfMonth(month)) {
                Toast.makeText(this, "Bạn vừa nhập ngày không tồn tại!", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            if (month == 2 && day > 29) {
                Toast.makeText(this, "Bạn vừa nhập ngày không tồn tại!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private int dayOfMonth(int month) {
        int m = 0;
        switch (month) {
            case 2:
                m = 28;
                break;
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                m = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                m = 30;
                break;
            default:
                break;
        }
        return m;
    }

    private boolean checkLeapYear(int year) {
        return (((year % 4 == 0) && (year % 100 != 0)) ||
                (year % 400 == 0));
    }

}