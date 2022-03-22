package com.android.onlineshoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ImageView imageViewCLose;
    private TextInputLayout textInputLayoutEmail;
    private TextInputEditText textInputEditTextEmail;
    private Button btnSendCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // click on close button
        imageViewCLose = findViewById(R.id.imageViewClose);
        imageViewCLose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // navigate to login activity
                finish();
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            }
        });

        // send code
        textInputLayoutEmail = findViewById(R.id.txtFieldEmailFP);
        textInputEditTextEmail = findViewById(R.id.editTxtEmailFP);

        textInputEditTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean onFocus) {
                if (onFocus) {
                    textInputLayoutEmail.setHelperTextEnabled(false);
                }
            }
        });

        // click on send button
        btnSendCode = findViewById(R.id.btnSendCode);
        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textInputEditTextEmail.getText().toString().equals("")) {
                    textInputLayoutEmail.setHelperText("Email không được bỏ trống!");
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Send code to " +
                            textInputEditTextEmail.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}