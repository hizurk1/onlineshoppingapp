package com.android.onlineshoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText editTextUsername, editTextPassword;
    private TextView textViewForgotPass, textViewRegister;
    private ImageView imageViewGoogle, imageViewFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Click on LOGIN button
        btnLogin = findViewById(R.id.btnLogin);
        editTextUsername = findViewById(R.id.etUsername);
        editTextPassword = findViewById(R.id.etPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkNullInputData()) {
                    Toast.makeText(LoginActivity.this,
                            "Tên đăng nhập và mật khẩu\n\t\t không được để trống!",
                            Toast.LENGTH_SHORT).show();
                } else {

                    // Notice: for testing purpose
                    if (editTextUsername.getText().toString().equals("admin")
                            && editTextPassword.getText().toString().equals("admin")) {

                        // Access to main activity
                        Intent directToMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(directToMainActivity);
                    } else {
                        Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu \n\t\t\t\t\t\t\t không đúng!",
                                Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        // Click on Forgot password
        textViewForgotPass = findViewById(R.id.tvForgotPassword);

        textViewForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Forgot password", Toast.LENGTH_SHORT).show();
            }
        });

        // Click on Register
        textViewRegister = findViewById(R.id.txtRegister);

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //direct to register activity
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        // Click on Google
        imageViewGoogle = findViewById(R.id.ivGoogle);

        imageViewGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Google", Toast.LENGTH_SHORT).show();
            }
        });

        // CLick on Facebook
        imageViewFacebook = findViewById(R.id.ivFacebook);

        imageViewFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Facebook", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean checkNullInputData() {
        if (editTextUsername.getText().toString().equals("") || editTextPassword.getText().toString().equals(""))
            return true;
        return false;
    }

}