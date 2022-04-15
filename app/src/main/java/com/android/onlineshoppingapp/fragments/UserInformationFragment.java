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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserInformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserInformationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView tvFullNameInfo, tvDateOfBirthInfo, tvPhoneNumberInfo, tvSexInfo, tvEmailInfo;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserInformationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserInformationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserInformationFragment newInstance(String param1, String param2) {
        UserInformationFragment fragment = new UserInformationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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