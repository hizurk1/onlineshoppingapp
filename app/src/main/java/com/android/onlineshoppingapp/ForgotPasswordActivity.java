package com.android.onlineshoppingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.onlineshoppingapp.fragments.EnterCodeFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ImageView imageViewCLose;
    private TextInputLayout textInputLayoutEmail;
    private TextInputEditText textInputEditTextEmail;
    private Button btnSendCode;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        fAuth = FirebaseAuth.getInstance();

        // click on close button
        imageViewCLose = findViewById(R.id.imageViewCloseFP);
        imageViewCLose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // navigate to login activity
                onBackPressed();
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
        btnSendCode = findViewById(R.id.btnSendCodeFP);
        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textInputEditTextEmail.getText().toString().equals("")) {
                    textInputLayoutEmail.setHelperText("Email không được bỏ trống!");
                } else {
                    // send code to user email
                    fAuth.sendPasswordResetEmail(textInputEditTextEmail.getText().toString());
                    Toast.makeText(ForgotPasswordActivity.this, "Đã gửi mail đặt lại mật khẩu", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                }
            }
        });
    }

    // ------------------ Function -----------------


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}