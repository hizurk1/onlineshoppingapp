package com.android.onlineshoppingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.onlineshoppingapp.models.UserInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeUserInfoActivity extends AppCompatActivity {

    private ImageView ivBack;
    private TextInputLayout layoutFName, layoutLName, layoutPhone;
    private TextInputEditText etLName, etFName, etPhone, etEmail;
    private static TextInputEditText etDateOfBirth;
    private AutoCompleteTextView ctvSex;
    private Button btnChange;

    private FirebaseAuth fAuth;
    private FirebaseFirestore db;
    private List<String> sexList;
    private UserInformation userInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_info);

        // init
        ivBack = findViewById(R.id.ivBackCUserInfo);
        layoutFName = findViewById(R.id.layoutFNameCUserInfo);
        layoutLName = findViewById(R.id.layoutLNameCUserInfo);
        layoutPhone = findViewById(R.id.layoutPhoneCUserInfo);
        etLName = findViewById(R.id.etLNameCUserInfo);
        etFName = findViewById(R.id.etFNameCUserInfo);
        etPhone = findViewById(R.id.etPhoneCUserInfo);
        etEmail = findViewById(R.id.etEmailCUserInfo);
        etDateOfBirth = findViewById(R.id.etDOBCUserInfo);
        ctvSex = findViewById(R.id.ctvSexCUserInfo);
        btnChange = findViewById(R.id.btnChangeUserInfo);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userInformation = (UserInformation) getIntent().getSerializableExtra("userInformation");

        // click on back
        ivBack.setOnClickListener(view -> {
            onBackPressed();
        });

        // set previous data
        etLName.setText(userInformation.getLastName());
        etFName.setText(userInformation.getFirstName());
        etPhone.setText(userInformation.getPhone());
        etEmail.setText(userInformation.getEmail());
        ctvSex.setText(userInformation.getSex());

        sexList = new ArrayList<>();
        sexList.add("Nam");
        sexList.add("Nữ");
        sexList.add("Khác");

        ArrayAdapter<String> sexListAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                sexList
        );
        ctvSex.setAdapter(sexListAdapter);
        ctvSex.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                closeKeyboard();
            }
        });

        etDateOfBirth.setText(new SimpleDateFormat("dd/MM/yyyy").format(userInformation.getDateOfBirth()));

        // check data
        etFName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean onFocus) {
                if (onFocus) {
                    layoutFName.setHelperTextEnabled(false);
                } else {
                    if (etFName.getText().toString().equals("")) {
                        layoutFName.setHelperText("Tên không được bỏ trống");
                    } else if (!includeCharInAlphabet(etFName.getText().toString())) {
                        layoutFName.setHelperText("Tên phải chứa ít nhất 1 ký tự chữ cái");
                    } else {
                        layoutFName.setHelperTextEnabled(false);
                    }
                }
            }
        });

        etLName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean onFocus) {
                if (onFocus) {
                    layoutLName.setHelperTextEnabled(false);
                } else {
                    if (etLName.getText().toString().equals("")) {
                        layoutLName.setHelperTextEnabled(false);
                    } else if (!includeCharInAlphabet(etLName.getText().toString())) {
                        layoutLName.setHelperText("Họ phải chứa ít nhất 1 ký tự chữ cái");
                    } else {
                        layoutLName.setHelperTextEnabled(false);
                    }
                }
            }
        });

        etPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean onFocus) {
                if (onFocus) {
                    layoutPhone.setHelperTextEnabled(false);
                } else {
                    if (etPhone.getText().toString().equals("")) {
                        layoutPhone.setHelperText("Số điện thoại không được bỏ trống");
                    } else if (etPhone.getText().toString().length() < 6) {
                        layoutPhone.setHelperText("Số điện thoại phải có ít nhất 6 số");
                    } else {
                        layoutPhone.setHelperTextEnabled(false);
                    }
                }
            }
        });

        etEmail.setFocusable(false);
        etDateOfBirth.setFocusable(false);
        etDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                showDatePickerDialog(view);
            }
        });

        // click on change
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateData()) {
                    try {
                        Map<String, Object> data = new HashMap<>();
                        data.put("firstName", etFName.getText().toString().trim());
                        data.put("lastName", etLName.getText().toString().trim());
                        data.put("phone", etPhone.getText().toString());
                        data.put("sex", ctvSex.getText().toString());
                        data.put("dateOfBirth", new SimpleDateFormat("dd/MM/yyyy").parse(etDateOfBirth.getText().toString()));

                        db.collection("Users").document(fAuth.getCurrentUser().getUid())
                                .update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ChangeUserInfoActivity.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                                            onBackPressed();
                                        } else {
                                            Log.e("updateError", task.getException().getMessage());
                                        }
                                    }
                                });

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ChangeUserInfoActivity.this, "Bạn chưa nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean validateData() {
        return etFName.getText().toString().equals("") ||
                etPhone.getText().toString().equals("") || etDateOfBirth.getText().toString().equals("") ||
                layoutFName.isHelperTextEnabled() || layoutLName.isHelperTextEnabled() ||
                layoutPhone.isHelperTextEnabled();
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {

            try {
                String dateString = String.valueOf(day) + "/" + String.valueOf(month + 1) + "/" + String.valueOf(year);
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = dateFormat.parse(dateString);
                Timestamp timeStampDate = new Timestamp(date.getTime());
                long datePicker = timeStampDate.getTime();

                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                long currentTime = timestamp.getTime();

                if (currentTime - datePicker <= 0) {
                    Toast.makeText(getActivity(), "Bạn vừa chọn ngày ở tương lai", Toast.LENGTH_SHORT).show();
                    etDateOfBirth.setText("1/1/2000");
                } else {
                    etDateOfBirth.setText(new SimpleDateFormat("dd/MM/yyyy").format(date));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean includeCharInAlphabet(String str) {
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        for (int i = 0; i < str.length(); i++) {
            for (int j = 0; j < alphabet.length; j++) {
                if (str.toLowerCase().charAt(i) == alphabet[j])
                    return true;
            }
        }
        return false;
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}