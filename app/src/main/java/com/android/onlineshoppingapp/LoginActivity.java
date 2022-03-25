package com.android.onlineshoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText editTextUsername, editTextPassword;
    private TextView textViewForgotPass, textViewRegister;
    private ImageView imageViewGoogle, imageViewFacebook;

    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Check login status if the user is logged in or not
        checkGoogleSignIn();

        // Click on LOGIN button
        btnLogin = findViewById(R.id.btnLogin);
        editTextUsername = findViewById(R.id.etUsername);
        editTextPassword = findViewById(R.id.etPassword);

        btnLogin.setOnClickListener(view -> {

            if (checkNullInputData()) {
                Toast.makeText(LoginActivity.this,
                        "Tên đăng nhập và mật khẩu\n\t\t không được để trống!",
                        Toast.LENGTH_SHORT).show();
            } else {

                // Notice: for testing purpose
                if (editTextUsername.getText().toString().equals("admin")
                        && editTextPassword.getText().toString().equals("admin")) {

                    // Access to main activity
                    finish();
                    Intent directToMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(directToMainActivity);
                } else {
                    Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu \n\t\t\t\t\t\t\t không đúng!",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        // Click on Forgot password
        textViewForgotPass = findViewById(R.id.tvForgotPassword);

        textViewForgotPass.setOnClickListener(view -> {
            // navigate to Forgot password activity
            finish();
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });

        // Click on Register
        textViewRegister = findViewById(R.id.txtRegister);

        textViewRegister.setOnClickListener(view -> {
            //navigate to register activity
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        // Click on Google
        imageViewGoogle = findViewById(R.id.ivGoogle);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        imageViewGoogle.setOnClickListener(view -> {
//                Toast.makeText(LoginActivity.this, "Google", Toast.LENGTH_SHORT).show();
            googleSignIn();
        });

        // CLick on Facebook
        imageViewFacebook = findViewById(R.id.ivFacebook);

        imageViewFacebook.setOnClickListener(view -> Toast.makeText(LoginActivity.this, "Facebook", Toast.LENGTH_SHORT).show());

    }

    // -------------- Function -----------------


    private void checkGoogleSignIn() {

        // Get data from Google sign in account
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null) {
            // navigate to main activity if user is logged in
            finish();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    private boolean checkNullInputData() {
        return editTextUsername.getText().toString().equals("") || editTextPassword.getText().toString().equals("");
    }

    private void googleSignIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                // navigate to main activity
                finish();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            } catch (ApiException e) {
                Toast.makeText(this, "Đã xảy ra lỗi. Vui lòng thử lại!", Toast.LENGTH_LONG).show();
                Log.e("Error: ", e.getMessage());
            }
        }
    }
}