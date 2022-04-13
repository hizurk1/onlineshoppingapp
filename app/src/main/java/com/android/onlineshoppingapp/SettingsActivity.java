package com.android.onlineshoppingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.onlineshoppingapp.adapters.ViewPagerAdapterSettings;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    private ImageView ivBack;
    private CardView cardLogout, cardChangePass, cardDeleteAccount, cardPolicy, cardVersion;
    private ViewPagerAdapterSettings viewPagerAdapterSettings;
    private TabLayout tabLayoutSettings;
    private ViewPager2 viewPager2Settings;
    private String[] titles = new String[]{"Cá nhân", "Địa chỉ", "Thanh toán"};

    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        fAuth = FirebaseAuth.getInstance();
        viewPagerAdapterSettings = new ViewPagerAdapterSettings(this);

        //init
        ivBack = findViewById(R.id.ivBackToProfile);
        viewPager2Settings = findViewById(R.id.viewpagerSettings);
        tabLayoutSettings = findViewById(R.id.tablayoutSettings);
        cardChangePass = findViewById(R.id.cardChangePass);
        cardDeleteAccount = findViewById(R.id.cardDeleteAccount);
        cardPolicy = findViewById(R.id.cardPolicy);
        cardVersion = findViewById(R.id.cardVersion);
        cardLogout = findViewById(R.id.cardLogout);

        // click on back button
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // viewpager and tab layout
        viewPager2Settings.setAdapter(viewPagerAdapterSettings);
        new TabLayoutMediator(tabLayoutSettings, viewPager2Settings,
                ((tab, position) -> tab.setText(titles[position]))).attach();

        // click on change password
        cardChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // click on delete account
        cardDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // click on policy
        cardPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);
                alertDialog.setTitle("Chính sách")
                        .setMessage(policyContent())
                        .setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });

        // click on version
        cardVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingsActivity.this, "Bạn đã cập nhật phiên bản mới nhất", Toast.LENGTH_SHORT).show();
            }
        });

        // click on logout
        cardLogout.setOnClickListener(new View.OnClickListener() {
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

    // ------------------ Function --------------------------

    private String policyContent() {
        String content = "1. Trong quá trình cung cấp cho Quý vị các Dịch Vụ (như được định nghĩa trong Điều Khoản Sử Dụng) hoặc quyền truy cập vào Nền Tảng Ứng dụng mua sắm trực tuyến (như được định nghĩa trong Điều Khoản Sử Dụng), chúng tôi sẽ thu thập, sử dụng, tiết lộ, lưu trữ và/hoặc xử lý dữ liệu, bao gồm cả dữ liệu cá nhân của Quý vị. Trong Chính Sách Bảo Mật này, ứng dụng cũng sẽ được dẫn chiếu đến (các) nền tảng của Nhà Bán Hàng (như được định nghĩa trong Quy Chế Hoạt Động) liên quan.\n" +
                "\n" +
                "2. Chính Sách Bảo Mật này thiết lập để giúp Quý vị biết được cách thức chúng tôi thu thập, sử dụng, tiết lộ, lưu trữ và/hoặc xử lý dữ liệu mà chúng tôi thu thập và nhận được trong quá trình cung cấp các Dịch Vụ hoặc cấp quyền truy cập vào ứng dụng cho Quý vị, người dùng của chúng tôi, cho dù Quý vị đang sử dụng ứng dụng của chúng tôi với tư cách là Khách Hàng (như được định nghĩa trong Quy Chế Hoạt Động) hoặc Nhà Bán Hàng. Chúng Tôi sẽ chỉ thu thập, sử dụng, tiết lộ, lưu trữ và/hoặc xử lý dữ liệu cá nhân của Quý vị theo Chính Sách Bảo Mật này.\n" +
                "\n" +
                "3. Quý vị cần đọc Chính Sách Bảo Mật này cùng với bất kỳ thông báo được áp dụng nào khác mà chúng tôi có thể đưa ra trong các trường hợp cụ thể khi chúng tôi thu thập, sử dụng, tiết lộ và/hoặc xử lý dữ liệu cá nhân về Quý vị, để Quý vị nhận thức đầy đủ về cách thức và lý do chúng tôi sử dụng dữ liệu cá nhân của Quý vị.";
        return content;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}