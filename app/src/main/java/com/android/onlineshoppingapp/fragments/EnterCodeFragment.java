package com.android.onlineshoppingapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.onlineshoppingapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EnterCodeFragment extends Fragment {

    private Button btnOkCode;
    private TextView textViewEnterCode;
    private TextInputLayout inputLayoutEnterCode;
    private TextInputEditText editTextEnterCode;
    private String userEmail, verifyCode;
    private Bundle data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_enter_code, container, false);

        // get data from activity
        data = getArguments();
        userEmail = data.getString("userEmail");
        verifyCode = data.getString("verifyCode");

        // set text for textview below imageview
        textViewEnterCode = view.findViewById(R.id.tvEnterCodeFP);
        textViewEnterCode.setText("Chúng tôi vừa gửi mã xác minh đến email:\n" + userEmail);
        textViewEnterCode.setTextSize(17);

        // click on OK button
        btnOkCode = view.findViewById(R.id.btnOkCodeFP);
        inputLayoutEnterCode = view.findViewById(R.id.txtFieldEnterCodeFP);
        editTextEnterCode = view.findViewById(R.id.editTxtEnterCodeFP);
        btnOkCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editTextEnterCode.getText().toString().equals(verifyCode)) {
                    inputLayoutEnterCode.setHelperText("Mã xác minh không đúng");
                } else {
                    // send data to change password fragment
                    ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
                    changePasswordFragment.setArguments(data);

                    // call fragment
                    callChangePasswordFragment(changePasswordFragment);
                }
            }
        });

        // return view
        return view;
    }

    private void callChangePasswordFragment(ChangePasswordFragment fragment) {
        // call change password fragment
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_forgotpass, fragment)
                .commit();
    }

}