package com.android.onlineshoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.onlineshoppingapp.models.UserInformation;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private TextInputEditText editTextEmail, editTextPassword;
    private TextInputLayout layoutEmail, layoutPassword;
    private TextView textViewForgotPass, textViewRegister;
    private ImageView imageViewGoogle, imageViewFacebook;

    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth fAuth;
    private FirebaseFirestore db;
    private ArrayList<UserInformation> userList = new ArrayList<UserInformation>();
    ;
    public ArrayList<String> userEmailList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // init
        btnLogin = findViewById(R.id.btnLogin);
        editTextEmail = findViewById(R.id.etEmailLogin);
        editTextPassword = findViewById(R.id.etPassLogin);
        layoutEmail = findViewById(R.id.tfEmailLogin);
        layoutPassword = findViewById(R.id.tfPassLogin);
        textViewForgotPass = findViewById(R.id.tvForgotPassword);
        textViewRegister = findViewById(R.id.txtRegister);
        imageViewGoogle = findViewById(R.id.ivGoogle);
        imageViewFacebook = findViewById(R.id.ivFacebook);
        fAuth = FirebaseAuth.getInstance();

        // Check login status if the user is logged in or not
        checkGoogleSignIn();

        if (fAuth.getCurrentUser() != null) {
            // navigate to main activity if user is logged in
            finish();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }

        // check email
        editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean onFocus) {
                if (!onFocus) {
                    // get user
                    userList = new ArrayList<UserInformation>();
                    db = FirebaseFirestore.getInstance();

                    db.collection("Users").orderBy("email", Query.Direction.ASCENDING)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                    if (error != null) {
                                        Log.e("Firestore error ", error.getMessage());
                                    }

                                    for (DocumentChange doc : value.getDocumentChanges()) {
                                        if (doc.getType() == DocumentChange.Type.ADDED) {
                                            userList.add(doc.getDocument().toObject(UserInformation.class));
                                        }
                                    }

                                    userList.forEach(user -> {
                                        userEmailList.add(user.getEmail().toString());
                                    });

                                    boolean check = false;
                                    for (int i = 0; i < userEmailList.size(); i++) {
                                        if (editTextEmail.getText().toString().trim().equals(userEmailList.get(i))) {
                                            check = true;
                                        }
                                    }
                                    if (!check) {
                                        layoutEmail.setHelperText("Email chưa được đăng ký");
                                    }
                                }
                            });
                } else {
                    layoutEmail.setHelperTextEnabled(false);
                }
            }
        });

        // check password: null
        editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean onFocus) {
                if (!onFocus) {
                    if (editTextPassword.getText().toString().equals("")) {
                        layoutPassword.setHelperText("Mật khẩu không được để trống");
                    }
                } else {
                    layoutPassword.setHelperTextEnabled(false);
                }
            }
        });

        // Click on LOGIN button
        btnLogin.setOnClickListener(view -> {

            if (!checkNullInputData()) {
                fAuth.signInWithEmailAndPassword(editTextEmail.getText().toString().trim(),
                        editTextPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            layoutPassword.setHelperTextEnabled(false);
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }
                            }, 1000);
                        } else {
                            layoutPassword.setHelperText("Mật khẩu bạn nhập không đúng");
                        }
                    }
                });
            }

        });


        // Click on Forgot password
        textViewForgotPass.setOnClickListener(view -> {
            // navigate to Forgot password activity
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });

        // Click on Register
        textViewRegister.setOnClickListener(view -> {
            //navigate to register activity
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        // Click on Google
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

//        imageViewGoogle.setOnClickListener(view -> {
////                Toast.makeText(LoginActivity.this, "Google", Toast.LENGTH_SHORT).show();
//            googleSignIn();
//        });

        // CLick on Facebook
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
        return editTextEmail.getText().toString().equals("") || editTextPassword.getText().toString().equals("");
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