package com.android.onlineshoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.onlineshoppingapp.fragments.EnterCodeFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Random;

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
                    EnterCodeFragment enterCodeFragment = new EnterCodeFragment();

                    // prepare data
                    Bundle data = new Bundle();
                    data.putString("userEmail", textInputEditTextEmail.getText().toString());

                    // create CODE
                    Random random = new Random();
                    String verifyCode = String.valueOf(random.nextInt(999999 - 100000) + 100000);
                    data.putString("verifyCode", verifyCode);

                    System.out.println("Code: " + verifyCode);

                    // send to fragment
                    enterCodeFragment.setArguments(data);

                    // call fragment
                    callEnterCodeFragment(enterCodeFragment);
                }
            }
        });
    }

    // ------------------ Function -----------------
    private void callEnterCodeFragment(EnterCodeFragment fragment) {
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout_forgotpass, fragment)
                .commit();
    }

}