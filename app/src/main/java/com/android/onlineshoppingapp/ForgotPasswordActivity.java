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
        imageViewCLose = findViewById(R.id.imageViewClose);
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
                    String verificationCode = String.valueOf(random.nextInt(999999 - 100000) + 100000);
                    data.putString("verifyCode", verificationCode);

                    // send code to user email
                    sendCodeByEmail(textInputEditTextEmail.getText().toString().trim(), verificationCode);
                    System.out.println("VERIFICATION CODE: " + verificationCode);
                    fAuth.sendPasswordResetEmail(textInputEditTextEmail.getText().toString());

                    // send to fragment
                    enterCodeFragment.setArguments(data);

                    // call fragment
                    callEnterCodeFragment(enterCodeFragment);
                }
            }
        });
    }

    // ------------------ Function -----------------


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void sendCodeByEmail(String receiverEmail, String verificationCode) {

        try {
            String senderEmail = "project.onlineshoppingapp@gmail.com";
            String senderPassword = "hulwohxxyuihmesr";

            String stringHost = "smtp.gmail.com";

            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", stringHost);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, senderPassword);
                }
            });

            // send email to user: receiverEmail
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(receiverEmail));

            mimeMessage.setSubject("[OnlineShoppingApp] Mã xác minh thay đổi mật khẩu");
            mimeMessage.setText("Xin chào," +
                    "\n\nBạn vừa yêu cầu mã xác minh để thay đổi mật khẩu của bạn " +
                    "cho địa chỉ email: " + receiverEmail +
                    "\n\nMã xác minh: " + verificationCode +
                    "\n\nVui lòng nhập mã xác minh bên trên để chúng tôi có thể tiếp tục thực hiện " +
                    "các bước thay đổi mật khẩu cho tài khoản của bạn." +
                    "\n\nTrân trọng,\nĐội ngũ OnlineShoppingApp");

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    private void callEnterCodeFragment(EnterCodeFragment fragment) {
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout_forgotpass, fragment)
                .commit();
    }

}