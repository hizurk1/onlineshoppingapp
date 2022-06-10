package com.android.onlineshoppingapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.onlineshoppingapp.adapters.ManageProductAdapter;
import com.android.onlineshoppingapp.adapters.SimpleGalleryRecyclerAdapter;
import com.android.onlineshoppingapp.models.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ManageProductActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ManageProductAdapter adapter;
    private SimpleGalleryRecyclerAdapter simpleGalleryRecyclerAdapter;
    private Toolbar toolbar;
    private TextView tvToolBar;
    private ImageView ivBack, ivClose;
    private Button btnAddImage, btnEditProduct;

    private FirebaseAuth fAuth;
    private FirebaseFirestore db;

    private List<Product> productList;
    private List<Product> selectionProduct = new ArrayList<>();
    private List<String> categoryList = new ArrayList<>();
    public static List<String> imageUriList = new ArrayList<>();
    private List<Uri> imageList = new ArrayList<>();

    private String productId;
    private int count = 0;
    public boolean isActionMode = false;
    public int pos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_product);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // init
        recyclerView = findViewById(R.id.rvListOfManageProduct);
        toolbar = findViewById(R.id.toolBarManage);
        tvToolBar = findViewById(R.id.tvToolBarManage);
        ivBack = findViewById(R.id.ivBackManage);
        ivClose = findViewById(R.id.ivCloseManage);

        ivBack.setOnClickListener(view -> {
            onBackPressed();
        });

        setSupportActionBar(toolbar);
        tvToolBar.setText("Quản lý sản phẩm");

        productList = new ArrayList<>();

        AsyncTask.execute(() -> {
            db.collection("Products")
                    .whereEqualTo("seller", Objects.requireNonNull(fAuth.getCurrentUser()).getUid())
                    .addSnapshotListener((value, error) -> {
                        if (error != null) return;
                        productList.clear();
                        assert value != null;
                        for (QueryDocumentSnapshot document : value) {
                            Product product = document.toObject(Product.class);
                            product.setProductId(document.getId());
                            productList.add(product);
                        }
                        adapter = new ManageProductAdapter(productList, ManageProductActivity.this);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(ManageProductActivity.this, LinearLayoutManager.VERTICAL, false);
//                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);
                    });

        });

        ivClose.setOnClickListener(view -> {
            clearActionMode();
        });

    }

    private void clearActionMode() {
        isActionMode = false;
        tvToolBar.setText("Quản lý sản phẩm");
        ivClose.setVisibility(View.GONE);
        ivBack.setVisibility(View.VISIBLE);
        count = 0;
        selectionProduct.clear();
        toolbar.getMenu().clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void startSelection(int position) {
        if (!isActionMode) {
            isActionMode = true;
            selectionProduct.add(productList.get(position));
            count++;
            updateToolbarText(count);
            ivBack.setVisibility(View.GONE);
            ivClose.setVisibility(View.VISIBLE);
            toolbar.inflateMenu(R.menu.manage_product_menu);
            pos = position;
            adapter.notifyDataSetChanged();
        }
    }

    private void updateToolbarText(int count) {
        tvToolBar.setText(String.format("%d sản phẩm được chọn", count));
    }

    public void check(View view, int position) {
        if (((CheckBox) view).isChecked()) {
            selectionProduct.add(productList.get(position));
            count++;
            updateToolbarText(count);
        } else {
            selectionProduct.remove(productList.get(position));
            count--;
            updateToolbarText(count);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_item_product_delete && selectionProduct.size() > 0) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Bạn có chắc chắn muốn xoá " + selectionProduct.size() + " sản phẩm?")
                    .setTitle("Xoá sản phẩm")
                    .setPositiveButton("Xoá", (dialogInterface, i) -> {
                        for (Product product : selectionProduct) {
                            productList.remove(product);
                            deleteProduct(product);
                        }
                        updateToolbarText(0);
                        clearActionMode();
                    }).setNegativeButton("Huỷ", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    }).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteProduct(Product product) {
        db = FirebaseFirestore.getInstance();
        db.collection("Products")
                .document(product.getProductId())
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ManageProductActivity.this, "Xoá thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("deleteError", Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

    public void showSheetToEdit(int position) {
        View sheetView = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_edit_product, null);
        BottomSheetDialog sheetDialog = new BottomSheetDialog(ManageProductActivity.this);
        sheetDialog.setContentView(sheetView);
        sheetDialog.setCancelable(false);
        sheetDialog.show();
        btnEditProduct = sheetView.findViewById(R.id.btnBottomSheetEditProduct);
        btnAddImage = sheetView.findViewById(R.id.btnAddImageEditProduct);

        Product currentProduct = productList.get(position);
//        Log.d("currentProduct", currentProduct.getProductName());

        sheetView.findViewById(R.id.bottomSheetEditProductClose).setOnClickListener(view -> {
            sheetDialog.dismiss();
        });

        TextInputLayout layoutProductName = sheetView.findViewById(R.id.layout_bottomSheetEditProductName);
        TextInputEditText etProductName = sheetView.findViewById(R.id.bottomSheetEditProductName);
        etProductName.setText(currentProduct.getProductName());
        etProductName.setOnFocusChangeListener((view, onFocus) -> {
            if (!onFocus) {
                if (etProductName.getText().toString().equals("")) {
                    layoutProductName.setHelperText("Tên sản phẩm không được để trống");
                }
            } else {
                layoutProductName.setHelperTextEnabled(false);
            }
        });

        // check product description
        TextInputEditText etProductDescription = sheetView.findViewById(R.id.bottomSheetEditProductDescription);
        TextInputLayout layoutProductDescription = sheetView.findViewById(R.id.layout_bottomSheetEditProductDescription);
        etProductDescription.setText(currentProduct.getDescription());
        etProductDescription.setOnFocusChangeListener((view, onFocus) -> {
            if (!onFocus) {
                if (etProductDescription.getText().toString().equals("")) {
                    layoutProductDescription.setHelperText("Mô tả sản phẩm không được để trống");
                }
            } else {
                layoutProductDescription.setHelperTextEnabled(false);
            }
        });

        // check product price
        TextInputEditText etProductPrice = sheetView.findViewById(R.id.bottomSheetEditProductPrice);
        TextInputLayout layoutProductPrice = sheetView.findViewById(R.id.layout_bottomSheetEditProductPrice);
        etProductPrice.setText(String.valueOf(currentProduct.getProductPrice()));
        etProductPrice.setOnFocusChangeListener((view, onFocus) -> {
            if (!onFocus) {
                if (etProductPrice.getText().toString().equals("")) {
                    layoutProductPrice.setHelperText("Giá sản phẩm không được để trống");
                }
            } else {
                layoutProductPrice.setHelperTextEnabled(false);
            }
        });

        // check quantity
        TextInputEditText etProductQuantity = sheetView.findViewById(R.id.bottomSheetEditProductQuantity);
        TextInputLayout layoutProductQuantity = sheetView.findViewById(R.id.layout_bottomSheetEditProductQuantity);
        etProductQuantity.setText(String.valueOf(currentProduct.getQuantity()));
        etProductQuantity.setOnFocusChangeListener((view, onFocus) -> {
            if (!onFocus) {
                if (etProductQuantity.getText().toString().equals("")) {
                    layoutProductQuantity.setHelperText("Số lượng không được để trống");
                }
            } else {
                layoutProductQuantity.setHelperTextEnabled(false);
            }
        });

        //set category
        AutoCompleteTextView ctvCategory = sheetView.findViewById(R.id.tvCategoryEditProduct);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                categoryList
        );
        ctvCategory.setAdapter(adapter);
        AsyncTask.execute(() -> {
            categoryList.clear();
            db.collection("Categories").get().addOnSuccessListener(queryDocumentSnapshots -> {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    categoryList.add(documentSnapshot.getString("name"));
                }
            });
        });
        ctvCategory.setText(productList.get(position).getCategory(), false);

        // add image
        RecyclerView rvImages = sheetView.findViewById(R.id.rvImagesEditProduct);
        //set image
        simpleGalleryRecyclerAdapter = new SimpleGalleryRecyclerAdapter(imageList, productList.get(position), ManageProductActivity.this);
        // setup recyclerview:
        rvImages.setLayoutManager(new LinearLayoutManager(ManageProductActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rvImages.setAdapter(simpleGalleryRecyclerAdapter);
        AsyncTask.execute(() -> {
            db.collection("productImages")
                    .document(productList.get(position).getProductId())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        imageList.clear();
                        imageUriList.clear();
                        if (documentSnapshot.exists()) {
                            imageUriList = (List<String>) documentSnapshot.getData().get("url");
                            for (String item : imageUriList)
                                imageList.add(Uri.parse(item));
                            simpleGalleryRecyclerAdapter.notifyDataSetChanged();
                        }
                        if (imageUriList.size() > 0)
                            rvImages.setVisibility(View.VISIBLE);
                    });

        });

        rvImages.setVisibility(View.VISIBLE);

        btnAddImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Chọn một (nhiều) hình ảnh"), 1);
        });

        // click on add button
        btnEditProduct.setOnClickListener(view -> {
//            newProductId = db.collection("Products").document().getId();
            productId = currentProduct.getProductId();
            uploadImage();
            updateProduct(etProductName.getText().toString(),
                    etProductDescription.getText().toString(),
                    Integer.parseInt(etProductPrice.getText().toString()),
                    Integer.parseInt(etProductQuantity.getText().toString()),
                    String.valueOf(ctvCategory.getText()), currentProduct);
            sheetDialog.dismiss();
        });

    }

    public void removeImage(int position, Product product) {
        imageList.remove(position);
        imageUriList.remove(position);
        Log.e("", String.valueOf(position));
//        Task<ListResult> storageReference = FirebaseStorage.getInstance()
//                .getReference()
//                .child("productImages")
//                .child(product.getProductId())
//                .listAll()
//                .addOnSuccessListener(listResult -> {
//                    int i = 0;
//                    listResult.getItems().get(position).delete();
//                    for (StorageReference item : listResult.getItems()) {
//                        if (i >= position) {
////                            item.putFile();
//                        }
//                        i++;
//                    }
//                });
        if (imageUriList.size() > 0) {
            Map<String, Object> image = new HashMap<>();
            image.put("url", imageUriList);
            db.collection("productImages").document(product.getProductId()).set(image);
        } else {
            db.collection("productImages").document(product.getProductId()).delete();
        }
    }

    private void updateProduct(String etProductName, String etProductDescription, int etProductPrice, int etProductQuantity, String category, Product cProduct) {
        Map<String, Object> product = new HashMap<>();
        product.put("productName", etProductName);
        product.put("seller", fAuth.getCurrentUser().getUid());
        product.put("description", etProductDescription);
        product.put("category", category);
        product.put("productPrice", etProductPrice);
        product.put("quantity", etProductQuantity);
        db.collection("Products").document(productId).update(product).addOnSuccessListener(unused -> {
            Toast.makeText(ManageProductActivity.this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();

                    int CurrentImageSelect = 0;

                    while (CurrentImageSelect < count) {
                        Uri imageuri = data.getClipData().getItemAt(CurrentImageSelect).getUri();
                        imageList.add(imageuri);
                        CurrentImageSelect = CurrentImageSelect + 1;
                    }
//                    textView.setVisibility(View.VISIBLE);
//                    textView.setText("You Have Selected "+ ImageList.size() +" Pictures" );
//                    choose.setVisibility(View.GONE);
                } else {
                    Uri imageuri = data.getData();
                    imageList.add(imageuri);
                }
                Log.e("add", String.valueOf(imageList));
            }
            simpleGalleryRecyclerAdapter.notifyDataSetChanged();
        }

    }

    private void uploadImage() {
        for (int i = 0; i < imageList.size(); i++) {
            StorageReference ref = FirebaseStorage.getInstance()
                    .getReference()
                    .child("productImages")
                    .child(productId).child(String.valueOf(i));
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageList.get(i));
                Log.e("", String.valueOf(bitmap));
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                ref.putBytes(byteArrayOutputStream.toByteArray())
                        .addOnSuccessListener(taskSnapshot -> getDownloadUri(ref))
                        .addOnFailureListener(e -> {
                            Toast.makeText(ManageProductActivity.this,
                                    "Có lỗi xảy ra " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage());
                        });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getDownloadUri(StorageReference ref) {
        ref.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    imageUriList.add(String.valueOf(uri));
                    Map<String, Object> image = new HashMap<>();
                    image.put("url", imageUriList);
                    db.collection("productImages").document(productId).set(image);
                })
                .addOnFailureListener(e -> Toast.makeText(this,
                        "Có lỗi xảy ra " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

}