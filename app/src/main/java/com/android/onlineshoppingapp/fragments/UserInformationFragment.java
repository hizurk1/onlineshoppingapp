package com.android.onlineshoppingapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.onlineshoppingapp.MainActivity;
import com.android.onlineshoppingapp.R;
import com.android.onlineshoppingapp.SettingsActivity;
import com.android.onlineshoppingapp.adapters.ViewPagerAdapterSettings;
import com.android.onlineshoppingapp.models.UserInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

public class UserInformationFragment extends Fragment {

    private TextView tvFullNameInfo, tvDateOfBirthInfo, tvPhoneNumberInfo, tvSexInfo, tvEmailInfo;

    public UserInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_information, container, false);

        tvFullNameInfo = (TextView) view.findViewById(R.id.tvFullNameInfo);
        tvEmailInfo = (TextView) view.findViewById(R.id.tvEmailInfo);
        tvSexInfo = (TextView) view.findViewById(R.id.tvSexInfo);
        tvDateOfBirthInfo = (TextView) view.findViewById(R.id.tvDateOfBirthInfo);
        tvPhoneNumberInfo = (TextView) view.findViewById(R.id.tvPhoneNumberInfo);


        tvFullNameInfo.setText(MainActivity.userInformation.getLastName() + " " + MainActivity.userInformation.getFirstName());
        tvEmailInfo.setText(MainActivity.userInformation.getEmail());
        tvSexInfo.setText(MainActivity.userInformation.getSex());
        tvDateOfBirthInfo.setText(new SimpleDateFormat("dd/MM/yyyy").format(MainActivity.userInformation.getDateOfBirth()));
        tvPhoneNumberInfo.setText(MainActivity.userInformation.getPhone());



        return view;
    }

}