package com.android.onlineshoppingapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class SettingsActivity extends AppCompatActivity {

    private ImageView ivBack;
    private Button btnLogout;
    private RelativeLayout layoutLanguage, layoutPolicy;
    private TextView txtLanguage;
    private FirebaseAuth fAuth;
    private static String selectedLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        fAuth = FirebaseAuth.getInstance();
        ivBack = findViewById(R.id.ivBackToProfile);
        btnLogout = findViewById(R.id.btnLogout);
        txtLanguage = findViewById(R.id.txtLanguage);
        layoutLanguage = findViewById(R.id.layoutLanguage);
        layoutPolicy = findViewById(R.id.layoutPolicy);

        // click on back button
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // click on userinfo
        layoutLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] languageItems = {"Tiếng Việt", "Vietnamese"};
                if (selectedLanguage == null)
                    selectedLanguage = languageItems[0];

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);
                alertDialog.setTitle("Chọn ngôn ngữ");
                alertDialog.setSingleChoiceItems(languageItems, Arrays.asList(languageItems).indexOf(selectedLanguage), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedLanguage = languageItems[i];
                    }
                });

                alertDialog.setPositiveButton("Thay đổi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        txtLanguage.setText(selectedLanguage);
                        dialogInterface.dismiss();
                    }
                });

                alertDialog.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

        // click on policy
        layoutPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);
                alertDialog.setTitle("Chính sách")
                        .setMessage(policyDialogContent())
                        .setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        });


        // click on logout button
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //log out
                fAuth.signOut();
                // navigate to login activity
                finishAffinity();
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
            }
        });

    }

    // ------------------ Function -----------------------

    private String policyDialogContent() {
        String content = "1. Trong quá trình cung cấp cho Quý vị các Dịch Vụ (như được định nghĩa trong Điều Khoản Sử Dụng) hoặc quyền truy cập vào ứng dụng mua sắm trực tuyến (như được định nghĩa trong Điều Khoản Sử Dụng), chúng tôi sẽ thu thập, sử dụng, tiết lộ, lưu trữ và/hoặc xử lý dữ liệu, bao gồm cả dữ liệu cá nhân của Quý vị. Trong Chính Sách Bảo Mật này, ứng dụng cũng sẽ được dẫn chiếu đến (các) nền tảng của Nhà Bán Hàng (như được định nghĩa trong Quy Chế Hoạt Động) liên quan.\n" +
                "\n" +
                "2. Chính Sách Bảo Mật này thiết lập để giúp Quý vị biết được cách thức chúng tôi thu thập, sử dụng, tiết lộ, lưu trữ và/hoặc xử lý dữ liệu mà chúng tôi thu thập và nhận được trong quá trình cung cấp các Dịch Vụ hoặc cấp quyền truy cập vào Ứng dụng cho Quý vị, người dùng của chúng tôi, cho dù Quý vị đang sử dụng ứng dụng của chúng tôi với tư cách là Khách Hàng (như được định nghĩa trong Quy Chế Hoạt Động) hoặc Nhà Bán Hàng. Chúng Tôi sẽ chỉ thu thập, sử dụng, tiết lộ, lưu trữ và/hoặc xử lý dữ liệu cá nhân của Quý vị theo Chính Sách Bảo Mật này.\n" +
                "\n" +
                "3. Quý vị cần đọc Chính Sách Bảo Mật này cùng với bất kỳ thông báo được áp dụng nào khác mà chúng tôi có thể đưa ra trong các trường hợp cụ thể khi chúng tôi thu thập, sử dụng, tiết lộ và/hoặc xử lý dữ liệu cá nhân về Quý vị, để Quý vị nhận thức đầy đủ về cách thức và lý do chúng tôi sử dụng dữ liệu cá nhân của Quý vị.";
        return content;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}