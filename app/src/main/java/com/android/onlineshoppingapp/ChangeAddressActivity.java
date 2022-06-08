package com.android.onlineshoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.android.onlineshoppingapp.models.UserAddress;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class ChangeAddressActivity extends AppCompatActivity {

    private ImageView ivBack;
    private Switch switch1, switch2;
    private CheckBox cbAddress2;
    private Button btnChangeAddress;
    private LinearLayout layoutCAddress2;
    private TextInputEditText etName1, etName2, etPhone1, etPhone2, etDetail1, etDetail2;
    private TextInputLayout layoutName1, layoutName2, layoutPhone1, layoutPhone2, layoutDetail1, layoutDetail2;
    private AutoCompleteTextView ctvCity1, ctvCity2, ctvDistrict1, ctvDistrict2, ctvTown1, ctvTown2;

    private FirebaseAuth fAuth;
    private FirebaseFirestore db;
    private List<UserAddress> userAddresses;
    private ArrayList<String> cityNameList, districtNameList, townNameList, districtNameList2, townNameList2;
    private List<Map<String, Object>> cityList, districtList, townList, districtList2, townList2;
    private Map<String, Object> data1 = new HashMap<>();
    private Map<String, Object> data2 = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address);

        // init
        ivBack = findViewById(R.id.ivBackCAddress);
        switch1 = findViewById(R.id.switchAddress1);
        switch2 = findViewById(R.id.switchAddress2);
        etName1 = findViewById(R.id.etNameCAddress1);
        etName2 = findViewById(R.id.etNameCAddress2);
        etPhone1 = findViewById(R.id.etPhoneCAddress1);
        etPhone2 = findViewById(R.id.etPhoneCAddress2);
        etDetail1 = findViewById(R.id.etDetailCAddress1);
        etDetail2 = findViewById(R.id.etDetailCAddress2);
        layoutName1 = findViewById(R.id.layoutNameCAddress1);
        layoutName2 = findViewById(R.id.layoutNameCAddress2);
        layoutPhone1 = findViewById(R.id.layoutPhoneCAddress1);
        layoutPhone2 = findViewById(R.id.layoutPhoneCAddress2);
        layoutDetail1 = findViewById(R.id.layoutDetailCAddress1);
        layoutDetail2 = findViewById(R.id.layoutDetailCAddress2);
        ctvCity1 = findViewById(R.id.tvCityCAddress1);
        ctvCity2 = findViewById(R.id.tvCityCAddress2);
        ctvDistrict1 = findViewById(R.id.tvDistrictCAddress1);
        ctvDistrict2 = findViewById(R.id.tvDistrictCAddress2);
        ctvTown1 = findViewById(R.id.tvTownCAddress1);
        ctvTown2 = findViewById(R.id.tvTownCAddress2);
        cbAddress2 = findViewById(R.id.cbCAddress2);
        layoutCAddress2 = findViewById(R.id.layoutCAddress2);
        btnChangeAddress = findViewById(R.id.btnChangeAddress);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // click on back
        ivBack.setOnClickListener(view -> {
            onBackPressed();
        });

        // click on check box
        layoutCAddress2.setVisibility(View.GONE);
        cbAddress2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (switch2.isChecked())
                        switch2.setChecked(false);
                    layoutCAddress2.setVisibility(View.VISIBLE);
                    switch2.setVisibility(View.VISIBLE);
                } else {
                    if (!switch1.isChecked())
                        switch1.setChecked(true);
                    layoutCAddress2.setVisibility(View.GONE);
                    switch2.setVisibility(View.INVISIBLE);
                }
            }
        });

        // click on switch
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    switch2.setChecked(false);
                } else {
                    switch2.setChecked(true);
                    if (switch2.getVisibility() == View.INVISIBLE) {
                        switch1.setChecked(true);
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChangeAddressActivity.this);
                        builder.setCancelable(false)
                                .setTitle("Địa chỉ mặc định")
                                .setMessage("Cần có ít nhất 1 địa chỉ là mặc định")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).show();
                    }
                }
            }
        });

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switch1.setChecked(!b);
            }
        });

        // check name 1
        etName1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean onFocus) {
                if (onFocus) {
                    layoutName1.setHelperTextEnabled(false);
                } else {
                    if (etName1.getText().toString().equals("")) {
                        layoutName1.setHelperText("Tên không được bỏ trống");
                    } else if (!includeCharInAlphabet(etName1.getText().toString())) {
                        layoutName1.setHelperText("Tên phải chứa ít nhất 1 ký tự chữ cái");
                    } else {
                        layoutName1.setHelperTextEnabled(false);
                    }
                }
            }
        });

        // check name 2
        etName2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean onFocus) {
                if (onFocus) {
                    layoutName2.setHelperTextEnabled(false);
                } else {
                    if (etName2.getText().toString().equals("")) {
                        layoutName2.setHelperText("Tên không được bỏ trống");
                    } else if (!includeCharInAlphabet(etName2.getText().toString())) {
                        layoutName2.setHelperText("Tên phải chứa ít nhất 1 ký tự chữ cái");
                    } else {
                        layoutName2.setHelperTextEnabled(false);
                    }
                }
            }
        });

        // check phone 1
        etPhone1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean onFocus) {
                if (onFocus) {
                    layoutPhone1.setHelperTextEnabled(false);
                } else {
                    if (etPhone1.getText().toString().equals("")) {
                        layoutPhone1.setHelperText("Số điện thoại không được bỏ trống");
                    } else if (etPhone1.getText().toString().length() < 6) {
                        layoutPhone1.setHelperText("Số điện thoại phải có ít nhất 6 số");
                    } else {
                        layoutPhone1.setHelperTextEnabled(false);
                    }
                }
            }
        });

        // check phone 2
        etPhone2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean onFocus) {
                if (onFocus) {
                    layoutPhone2.setHelperTextEnabled(false);
                } else {
                    if (etPhone2.getText().toString().equals("")) {
                        layoutPhone2.setHelperText("Số điện thoại không được bỏ trống");
                    } else if (etPhone2.getText().toString().length() < 6) {
                        layoutPhone2.setHelperText("Số điện thoại phải có ít nhất 6 số");
                    } else {
                        layoutPhone2.setHelperTextEnabled(false);
                    }
                }
            }
        });

        // set value on city
        cityList = new ArrayList<>();
        cityNameList = new ArrayList<>();

        ArrayAdapter<String> cityListAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                cityNameList
        );
        ctvCity1.setAdapter(cityListAdapter);
        ctvCity1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                closeKeyboard();
            }
        });

        ctvCity2.setAdapter(cityListAdapter);
        ctvCity2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                closeKeyboard();
            }
        });

        // set value on district
        districtList = new ArrayList<>();
        districtNameList = new ArrayList<>();
        districtList2 = new ArrayList<>();
        districtNameList2 = new ArrayList<>();

        ArrayAdapter<String> districtListAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                districtNameList
        );
        ctvDistrict1.setAdapter(districtListAdapter);
        ctvDistrict1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                closeKeyboard();
            }
        });
        ctvDistrict2.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                districtNameList2));
        ctvDistrict2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                closeKeyboard();
            }
        });

        // set value on district
        townList = new ArrayList<>();
        townNameList = new ArrayList<>();
        townList2 = new ArrayList<>();
        townNameList2 = new ArrayList<>();

        ArrayAdapter<String> townListAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                townNameList
        );
        ctvTown1.setAdapter(townListAdapter);
        ctvTown1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                closeKeyboard();
            }
        });
        ctvTown2.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                townNameList2));
        ctvTown2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                closeKeyboard();
            }
        });

        //set city/province
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                //set city/province
                try {
                    //Create URL
                    URL url = new URL("https://provinces.open-api.vn/api/");

                    try {
                        //Create Connection
                        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();

                        if (httpsURLConnection.getResponseCode() == 200) {
                            InputStreamReader inputStreamReader = new InputStreamReader(httpsURLConnection.getInputStream(), StandardCharsets.UTF_8);
                            JsonReader jsonReader = new JsonReader(inputStreamReader);
                            jsonReader.beginArray();
                            while (jsonReader.hasNext()) {
                                jsonReader.beginObject();
                                Map<String, Object> map = new HashMap<>();
                                while (jsonReader.hasNext()) {
                                    String name = String.valueOf(jsonReader.nextName());
                                    if (name.equals("name"))
                                        map.put("name", jsonReader.nextString());
                                    else if (name.equals("code"))
                                        map.put("code", jsonReader.nextInt());
                                    else if (name.equals("division_type"))
                                        map.put("division_type", jsonReader.nextString());
                                    else if (name.equals("codename"))
                                        map.put("codename", jsonReader.nextString());
                                    else if (name.equals("phone_code"))
                                        map.put("phone_code", jsonReader.nextString());
                                    else
                                        jsonReader.skipValue();
                                }
                                cityList.add(map);
                                jsonReader.endObject();
                            }
                            jsonReader.endArray();
                            for (Map<String, Object> item : cityList) {
                                cityNameList.add(String.valueOf(item.get("name")));
                            }
                            jsonReader.close();
                        } else {
                            Toast.makeText(ChangeAddressActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                            Log.e("API address", httpsURLConnection.getResponseMessage());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        //set district
        ctvCity1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> map = cityList.get(i);
                districtList.clear();
                districtNameList.clear();
                getDistrict(String.valueOf(map.get("code")), districtList, districtNameList, data1);
            }
        });

        ctvCity2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> map = cityList.get(i);
                districtList2.clear();
                districtNameList2.clear();
                getDistrict(String.valueOf(map.get("code")), districtList2, districtNameList2, data2);
            }
        });
        //set ward
        ctvDistrict1.setOnItemClickListener((adapterView, view, i, l) -> {
            Map<String, Object> map = districtList.get(i);
            townList.clear();
            townNameList.clear();
            getTown(String.valueOf(map.get("code")), townList, townNameList, data1);
        });
        ctvDistrict2.setOnItemClickListener((adapterView, view, i, l) -> {
            Map<String, Object> map = districtList2.get(i);
            townList2.clear();
            townNameList2.clear();
            getTown(String.valueOf(map.get("code")), townList2, townNameList2, data2);
        });

        //get ward code
        ctvTown1.setOnItemClickListener((adapterView, view, i, l) -> {
            Map<String, Object> map = townList.get(i);
            data1.put("townCode", map.get("code"));
        });
        ctvTown2.setOnItemClickListener((adapterView, view, i, l) -> {
            Map<String, Object> map = townList2.get(i);
            data2.put("townCode", map.get("code"));
        });
        // check detail 1
        etDetail1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean onFocus) {
                if (onFocus) {
                    layoutDetail1.setHelperTextEnabled(false);
                } else {
                    if (etDetail1.getText().toString().equals("")) {
                        layoutDetail1.setHelperText("Tên đường, số nhà không được bỏ trống");
                    } else {
                        layoutDetail1.setHelperTextEnabled(false);
                    }
                }
            }
        });

        // check detail 2
        etDetail2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean onFocus) {
                if (onFocus) {
                    layoutDetail2.setHelperTextEnabled(false);
                } else {
                    if (etDetail2.getText().toString().equals("")) {
                        layoutDetail2.setHelperText("Tên đường, số nhà không được bỏ trống");
                    } else {
                        layoutDetail2.setHelperTextEnabled(false);
                    }
                }
            }
        });

        userAddresses = new ArrayList<>();
        db.collection("Users").document(fAuth.getCurrentUser().getUid())
                .collection("Addresses")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            task.getResult().getDocuments().forEach(documentSnapshot -> {
                                UserAddress userAddress = documentSnapshot.toObject(UserAddress.class);
                                userAddresses.add(userAddress);
                            });


                            if (!userAddresses.isEmpty()) {
                                //set which address is default
                                if (!userAddresses.get(0).isDefaultAddress() && userAddresses.size() > 1) {
                                    UserAddress temp = userAddresses.get(0);
                                    userAddresses.set(0, userAddresses.get(1));
                                    userAddresses.set(1, temp);
                                }
                                // set previous value for et
                                etName1.setText(userAddresses.get(0).getName());
                                etName1.setSelectAllOnFocus(true);
                                etPhone1.setText(userAddresses.get(0).getPhone());
                                etPhone1.setSelectAllOnFocus(true);
                                etDetail1.setText(userAddresses.get(0).getDetail());
                                etDetail1.setSelectAllOnFocus(true);
                                ctvCity1.setText(userAddresses.get(0).getCity(), false);
                                data1.put("city", userAddresses.get(0).getCity());
                                data1.put("cityCode", userAddresses.get(0).getCityCode());
                                getDistrict(String.valueOf(userAddresses.get(0).getCityCode()), districtList, districtNameList, data1);
                                ctvDistrict1.setText(userAddresses.get(0).getDistrict(),false);
                                data1.put("district", userAddresses.get(0).getDistrict());
                                data1.put("districtCode", userAddresses.get(0).getDistrictCode());
                                getTown(String.valueOf(userAddresses.get(0).getDistrictCode()), townList, townNameList, data1);
                                ctvTown1.setText(userAddresses.get(0).getTown(),false);
                                data1.put("town", userAddresses.get(0).getTown());
                                data1.put("townCode", userAddresses.get(0).getTownCode());


                                if (userAddresses.size() > 1) {
                                    // set previous value for et
                                    etName2.setText(userAddresses.get(1).getName());
                                    etName2.setSelectAllOnFocus(true);
                                    etPhone2.setText(userAddresses.get(1).getPhone());
                                    etPhone2.setSelectAllOnFocus(true);
                                    etDetail2.setText(userAddresses.get(1).getDetail());
                                    etDetail2.setSelectAllOnFocus(true);
                                    ctvCity2.setText(userAddresses.get(1).getCity(), false);
                                    data2.put("city", userAddresses.get(1).getCity());
                                    data2.put("cityCode", userAddresses.get(1).getCityCode());
                                    getDistrict(String.valueOf(userAddresses.get(1).getCityCode()), districtList2, districtNameList2, data2);
                                    ctvDistrict2.setText(userAddresses.get(1).getDistrict(),false);
                                    data2.put("district", userAddresses.get(1).getDistrict());
                                    data2.put("districtCode", userAddresses.get(1).getDistrictCode());
                                    getTown(String.valueOf(userAddresses.get(1).getDistrictCode()), townList2, townNameList2, data2);
                                    ctvTown2.setText(userAddresses.get(1).getTown(),false);
                                    data2.put("town", userAddresses.get(1).getTown());
                                    data2.put("townCode", userAddresses.get(1).getTownCode());
                                }
                            }

                        } else {
                            Log.e("getAddress", task.getException().getMessage());
                        }
                    }
                });


        // click on change
        btnChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateData()) {
                    db.collection("Users").document(fAuth.getCurrentUser().getUid())
                            .collection("Addresses")
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
//                                    Log.w("defaultAddress", (defaultAddress) ? "1" : "2");
                                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                                boolean defaultAddress = switch1.isChecked(); // true = 1, false = 2
                                data1.put("name", etName1.getText().toString());
                                data1.put("phone", etPhone1.getText().toString());
                                data1.put("city", ctvCity1.getText().toString());
                                data1.put("district", ctvDistrict1.getText().toString());
                                data1.put("town", ctvTown1.getText().toString());
                                data1.put("detail", etDetail1.getText().toString());
                                data1.put("defaultAddress", defaultAddress); // add city district town

                                data2.put("name", etName2.getText().toString());
                                data2.put("phone", etPhone2.getText().toString());
                                data2.put("city", ctvCity2.getText().toString());
                                data2.put("district", ctvDistrict2.getText().toString());
                                data2.put("town", ctvTown2.getText().toString());
                                data2.put("detail", etDetail2.getText().toString());
                                data2.put("defaultAddress", !defaultAddress); // add city district town
                                list.add(data1);
                                list.add(data2);
                                if (queryDocumentSnapshots.isEmpty()) {
                                    db.collection("Users")
                                            .document(fAuth.getCurrentUser().getUid())
                                            .collection("Addresses")
                                            .add(data1);
                                    if (cbAddress2.isChecked())
                                        db.collection("Users")
                                                .document(fAuth.getCurrentUser().getUid())
                                                .collection("Addresses")
                                                .add(data2);
                                } else {
                                    int i = 0;
                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        Log.e("", String.valueOf(documentSnapshot.getData()));
                                        if ((i == 1 && cbAddress2.isChecked()) || i == 0)
                                            db.collection("Users")
                                                    .document(fAuth.getCurrentUser().getUid())
                                                    .collection("Addresses")
                                                    .document(documentSnapshot.getId())
                                                    .set(list.get(i))
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.e("change address", e.getMessage());
                                                        }
                                                    });
                                        i++;
                                    }
                                    //add address 2 when user just have 1 address before
                                    if (i == 1 && cbAddress2.isChecked()) {
                                        db.collection("Users")
                                                .document(fAuth.getCurrentUser().getUid())
                                                .collection("Addresses")
                                                .add(list.get(i))
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.e("change address", e.getMessage());
                                                    }
                                                });
                                    }
                                }
                            });
                    Toast.makeText(ChangeAddressActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    Toast.makeText(ChangeAddressActivity.this, "Bạn chưa điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getTown(String code, List<Map<String, Object>> townList, List<String> townNameList, Map<String, Object> data) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                data.put("districtCode", Long.valueOf(code));
                try {
                    //Create URL
                    URL url = new URL("https://provinces.open-api.vn/api/d/" + code + "?depth=2");

                    try {
                        //Create Connection
                        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                        httpsURLConnection.setRequestMethod("GET");

                        if (httpsURLConnection.getResponseCode() == 200) {
                            InputStreamReader inputStreamReader = new InputStreamReader(httpsURLConnection.getInputStream(), StandardCharsets.UTF_8);
                            JsonReader jsonReader = new JsonReader(inputStreamReader);
                            jsonReader.beginObject();
                            while (jsonReader.hasNext()) {
                                String name = jsonReader.nextName();
                                if (name.equals("wards") && jsonReader.peek() != JsonToken.NULL) {
                                    jsonReader.beginArray();
                                    while (jsonReader.hasNext()) {
                                        jsonReader.beginObject();
                                        Map<String, Object> map = new HashMap<>();
                                        while (jsonReader.hasNext()) {
                                            String key = jsonReader.nextName();
                                            if (key.equals("name"))
                                                map.put("name", jsonReader.nextString());
                                            else if (key.equals("code"))
                                                map.put("code", jsonReader.nextInt());
                                            else if (key.equals("division_type"))
                                                map.put("division_type", jsonReader.nextString());
                                            else jsonReader.skipValue();
                                        }
                                        townList.add(map);
                                        jsonReader.endObject();
                                    }
                                    jsonReader.endArray();
                                } else
                                    jsonReader.skipValue();

                            }
                            jsonReader.endObject();
                            jsonReader.close();
                            httpsURLConnection.disconnect();
                            for (Map<String, Object> item : townList) {
                                townNameList.add(String.valueOf(item.get("name")));
                            }
                        } else {
                            Toast.makeText(ChangeAddressActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                            Log.e("API address", httpsURLConnection.getResponseMessage());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getDistrict(String code, List<Map<String, Object>> districtList, List<String> districtNameList, Map<String, Object> data) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                data.put("cityCode", Long.valueOf(code));
                try {
                    //Create URL
                    URL url = new URL("https://provinces.open-api.vn/api/p/" + code + "?depth=2");

                    try {
                        //Create Connection
                        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                        httpsURLConnection.setRequestMethod("GET");

                        if (httpsURLConnection.getResponseCode() == 200) {
                            InputStreamReader inputStreamReader = new InputStreamReader(httpsURLConnection.getInputStream(), StandardCharsets.UTF_8);
                            JsonReader jsonReader = new JsonReader(inputStreamReader);
                            jsonReader.beginObject();
                            while (jsonReader.hasNext()) {
                                String name = jsonReader.nextName();
                                if (name.equals("districts") && jsonReader.peek() != JsonToken.NULL) {
                                    jsonReader.beginArray();
                                    while (jsonReader.hasNext()) {
                                        jsonReader.beginObject();
                                        Map<String, Object> map = new HashMap<>();
                                        while (jsonReader.hasNext()) {
                                            String key = jsonReader.nextName();
                                            if (key.equals("name"))
                                                map.put("name", jsonReader.nextString());
                                            else if (key.equals("code"))
                                                map.put("code", jsonReader.nextInt());
                                            else if (key.equals("division_type"))
                                                map.put("division_type", jsonReader.nextString());
                                            else jsonReader.skipValue();
                                        }
                                        districtList.add(map);
                                        jsonReader.endObject();
                                    }
                                    jsonReader.endArray();
                                } else
                                    jsonReader.skipValue();

                            }
                            jsonReader.endObject();
                            jsonReader.close();
                            httpsURLConnection.disconnect();
                            for (Map<String, Object> item : districtList) {
                                districtNameList.add(String.valueOf(item.get("name")));
                            }
                        } else {
                            Toast.makeText(ChangeAddressActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                            Log.e("API address", httpsURLConnection.getResponseMessage());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean validateData() {
        if (cbAddress2.isChecked())
            return etName1.getText().toString().equals("") || etPhone1.getText().toString().equals("") ||
                    etDetail1.getText().toString().equals("") || etName2.getText().toString().equals("") ||
                    etPhone2.getText().toString().equals("") || etDetail2.getText().toString().equals("") ||
                    ctvCity1.getText().toString().equals("Chọn thành phố / tỉnh") ||
                    ctvCity2.getText().toString().equals("Chọn thành phố / tỉnh") ||
                    ctvDistrict1.getText().toString().equals("Chọn quận / huyện") ||
                    ctvDistrict2.getText().toString().equals("Chọn quận / huyện") ||
                    ctvTown1.getText().toString().equals("Chọn phường / xã") ||
                    ctvTown2.getText().toString().equals("Chọn phường / xã") ||
                    layoutName1.isHelperTextEnabled() || layoutPhone1.isHelperTextEnabled() ||
                    layoutDetail1.isHelperTextEnabled() || layoutName2.isHelperTextEnabled() ||
                    layoutPhone2.isHelperTextEnabled() || layoutDetail2.isHelperTextEnabled();
        else
            return etName1.getText().toString().equals("") || etPhone1.getText().toString().equals("") ||
                    etDetail1.getText().toString().equals("") || layoutName1.isHelperTextEnabled() ||
                    ctvCity1.getText().toString().equals("Chọn thành phố / tỉnh") ||
                    ctvDistrict1.getText().toString().equals("Chọn quận / huyện") ||
                    ctvTown1.getText().toString().equals("Chọn phường / xã") ||
                    layoutPhone1.isHelperTextEnabled() || layoutDetail1.isHelperTextEnabled();
    }

    public boolean includeCharInAlphabet(String str) {
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        for (int i = 0; i < str.length(); i++) {
            for (int j = 0; j < alphabet.length; j++) {
                if (str.toLowerCase().charAt(i) == alphabet[j])
                    return true;
            }
        }
        return false;
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}