package com.android.onlineshoppingapp.fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.onlineshoppingapp.ListOfFollowActivity;
import com.android.onlineshoppingapp.ListOfProductActivity;
import com.android.onlineshoppingapp.LoginActivity;
import com.android.onlineshoppingapp.MainActivity;
import com.android.onlineshoppingapp.MyStoreActivity;
import com.android.onlineshoppingapp.PurchaseOrderActivity;
import com.android.onlineshoppingapp.R;
import com.android.onlineshoppingapp.SettingsActivity;
import com.android.onlineshoppingapp.ShoppingCartActivity;
import com.android.onlineshoppingapp.models.UserInformation;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.OnProgressListener;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfilePageFragment extends Fragment {

    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private FirebaseAuth fAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private TextView textViewFullname;
    private ImageView ivSettings, ivShoppingCart, ivAvatar, ivWallet, ivChecking, ivDelivery, ivFeedback;
    private CardView cardFollow, cardPurchasedProduct, cardSupport, cardMyStore, cardLove;

    private final int PICK_IMAGE_REQUEST = 71;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_page, container, false);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // init
        textViewFullname = view.findViewById(R.id.tvFullName);
        ivSettings = view.findViewById(R.id.ivSettings);
        ivShoppingCart = view.findViewById(R.id.ivShoppingCart);
        ivAvatar = view.findViewById(R.id.ivAvatar);
        ivWallet = view.findViewById(R.id.ivWallet);
        ivChecking = view.findViewById(R.id.ivChecking);
        ivDelivery = view.findViewById(R.id.ivDelivery);
        ivFeedback = view.findViewById(R.id.ivFeedback);
        cardFollow = view.findViewById(R.id.cardFollow);
        cardPurchasedProduct = view.findViewById(R.id.cardPurchasedProduct);
        cardSupport = view.findViewById(R.id.cardSupport);
        cardMyStore = view.findViewById(R.id.cardMyStore);
        cardLove = view.findViewById(R.id.cardFavourite);

        // change name
        db.collection("Users").document(user.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                UserInformation userInfo = task.getResult().toObject(UserInformation.class);
                                if (!userInfo.getLastName().equals("")) {
                                    textViewFullname.setText(String.format("%s %s", userInfo.getLastName(), userInfo.getFirstName()));
                                } else {
                                    textViewFullname.setText(userInfo.getFirstName());
                                }

                            }
                        }
                    }
                });

        // change avatar
        if (user.getPhotoUrl() != null) {
            Glide.with(getActivity())
                    .load(user.getPhotoUrl())
                    .into(ivAvatar);
        }

        // click on avatar
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }

        });

        // click on settings button
        ivSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        // click on shopping cart
        db.collection("Users").document(fAuth.getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.getString("accountType").equals("Bán hàng")) {
                            ivShoppingCart.setVisibility(View.INVISIBLE);
                            cardFollow.setVisibility(View.GONE);
                            cardLove.setVisibility(View.GONE);
                        } else {
                            ivShoppingCart.setVisibility(View.VISIBLE);
                            cardFollow.setVisibility(View.VISIBLE);
                            cardLove.setVisibility(View.VISIBLE);
                        }
                    }
                });
        ivShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ShoppingCartActivity.class));
            }
        });

        // click on wallet
        ivWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PurchaseOrderActivity.class);
                intent.putExtra("orderState", "receiveOrder");
                startActivity(intent);
            }
        });

        // click on checking
        ivChecking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PurchaseOrderActivity.class);
                intent.putExtra("orderState", "waitForProduct");
                startActivity(intent);
            }
        });

        // click on delivery
        ivDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PurchaseOrderActivity.class);
                intent.putExtra("orderState", "shipping");
                startActivity(intent);
            }
        });

        // click on feedback
        ivFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PurchaseOrderActivity.class);
                intent.putExtra("orderState", "waitForReview");
                startActivity(intent);
            }
        });

        // click on card love
        cardLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ListOfProductActivity.class);
                intent.putExtra("see_more_product", "favorite");
                startActivity(intent);
            }
        });

        // click on card follow
        cardFollow.setOnClickListener(view14 -> {
            startActivity(new Intent(getActivity(), ListOfFollowActivity.class));
        });

        // click on card purchased product
        cardPurchasedProduct.setOnClickListener(view13 -> {
            Intent intent = new Intent(getActivity(), ListOfProductActivity.class);
            intent.putExtra("see_more_product", "purchasedProduct");
            startActivity(intent);
        });

        // show card my store
        db.collection("Users")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (documentSnapshot.getString("accountType").equals("Bán hàng")) {
                        cardMyStore.setVisibility(View.VISIBLE);
                        cardPurchasedProduct.setVisibility(View.GONE);
                    } else {
                        cardMyStore.setVisibility(View.GONE);
                        cardPurchasedProduct.setVisibility(View.VISIBLE);
                    }
                });


        cardMyStore.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), MyStoreActivity.class)));

        // click on card support
        cardSupport.setOnClickListener(view12 -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "project.onlineshoppingapp@gmail.com", null));
            getActivity().startActivity(Intent.createChooser(emailIntent, null));
        });


        return view;
    }

    //--------------- Function ----------------

//    private void checkSignIn() {
//        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail().build();
//        gsc = GoogleSignIn.getClient(getActivity(), gso);
//
//        // get data
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
//        if (account != null) {
//            String userName = account.getDisplayName();
//            String userEmail = account.getEmail();
//            Toast.makeText(getActivity(), "Name: " + userName + "\nEmail: " + userEmail, Toast.LENGTH_SHORT)
//                    .show();
//        }
//    }
//
//    private void signOut() {
//        gsc.signOut().addOnCompleteListener(task -> {
//            fAuth.signOut();
//            startActivity(new Intent(getActivity(), LoginActivity.class));
//        });
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                ivAvatar.setImageBitmap(bitmap);
                uploadImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        StorageReference ref = storageReference.child("profileImages").child(user.getUid().toString());
        ref.putBytes(byteArrayOutputStream.toByteArray())
                .addOnSuccessListener(taskSnapshot -> getDownloadUri(ref))
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Có lỗi xảy ra " + e.getMessage(), Toast.LENGTH_SHORT).show());


    }

    private void getDownloadUri(StorageReference ref) {
        ref.getDownloadUrl()
                .addOnSuccessListener(this::setUserPhotoUri)
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Có lỗi xảy ra " + e.getMessage(), Toast.LENGTH_SHORT).show());
        ;
    }

    private void setUserPhotoUri(Uri uri) {
        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();
        user.updateProfile(userProfileChangeRequest)
                .addOnSuccessListener(unused -> Toast.makeText(getActivity(), "Đã cập nhật ảnh đại diện", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Có lỗi xảy ra " + e.getMessage(), Toast.LENGTH_SHORT).show());
        Map<String, Object> map = new HashMap<>();
        map.put("avatarUrl", String.valueOf(uri));
        db.collection("Users")
                .document(Objects.requireNonNull(fAuth.getCurrentUser()).getUid())
                .set(map, SetOptions.merge())
                .addOnFailureListener(e -> Log.e("setPhotoUri", e.getMessage()));
    }

}


