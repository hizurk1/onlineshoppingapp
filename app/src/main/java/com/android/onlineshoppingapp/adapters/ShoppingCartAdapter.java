package com.android.onlineshoppingapp.adapters;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.provider.DocumentsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.onlineshoppingapp.R;
import com.android.onlineshoppingapp.models.Product;
import com.android.onlineshoppingapp.models.cartProduct;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ShoppingCartViewHolder> {

    private List<cartProduct> productList;
    private Context context;
    public int totalAmount = 0;
    public Intent intent = new Intent("TotalAmountOfProduct");
    public ArrayList<CheckBox> checkBoxes = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;

    public ShoppingCartAdapter(List<cartProduct> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ShoppingCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_product_in_cart, parent, false);
        return new ShoppingCartViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ShoppingCartViewHolder holder, int position) {

        cartProduct product = productList.get(position);
        if (product == null) {
            return;
        }

        checkBoxes.add(holder.cbProduct);

        adjustBtn(holder.ivMinus, true);
        adjustBtn(holder.ivAdd, true);
        if (Integer.parseInt(String.valueOf(holder.etNumProduct.getText())) == 1) {
            adjustBtn(holder.ivMinus, false);
            adjustBtn(holder.ivAdd, true);
        }
        if (Integer.parseInt(holder.etNumProduct.getText().toString()) >= product.getQuantity()) {
            adjustBtn(holder.ivMinus, true);
            adjustBtn(holder.ivAdd, false);
        }

        holder.etNumProduct.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adjustBtn(holder.ivMinus, true);
                adjustBtn(holder.ivAdd, true);

                if (Integer.parseInt(charSequence.toString()) == 1) {
                    adjustBtn(holder.ivMinus, false);
                    adjustBtn(holder.ivAdd, true);
                }

                if (Integer.parseInt(charSequence.toString()) >= product.getQuantity()) {
                    adjustBtn(holder.ivMinus, true);
                    adjustBtn(holder.ivAdd, false);
                }

                setTextOfPrice(holder.tvPrice, product, holder.numProduct);

            }


            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        // click on minus
        holder.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.numProduct - 1 >= 1) {
                    holder.numProduct -= 1;
                    holder.etNumProduct.setText(String.valueOf(holder.numProduct));
                    product.setOrderQuantity(holder.numProduct);
                    if (!holder.cbProduct.isChecked())
                        holder.cbProduct.setChecked(true);
                    else {
                        if (totalAmount - product.getProductPrice() >= 0)
                            totalAmount -= product.getProductPrice();
                        else
                            totalAmount = 0;
                        sendDataToTotal(totalAmount);
                    }
                }
            }
        });

        // click on add
        holder.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.numProduct + 1 <= product.getQuantity()) {
                    holder.numProduct += 1;
                    product.setOrderQuantity(holder.numProduct);
                    holder.etNumProduct.setText(String.valueOf(holder.numProduct));
                    if (!(holder.cbProduct.isChecked()))
                        holder.cbProduct.setChecked(true);
                    else {
                        totalAmount += product.getProductPrice();
                        sendDataToTotal(totalAmount);
                    }
                }
            }
        });

        // long click on minus
        holder.ivMinus.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.w("numProduct", String.valueOf(holder.numProduct));
                if (holder.numProduct - 10 >= 1) {
                    holder.numProduct -= 10;
                    holder.etNumProduct.setText(String.valueOf(holder.numProduct));
                    product.setOrderQuantity(holder.numProduct);
                    if (!holder.cbProduct.isChecked())
                        holder.cbProduct.setChecked(true);
                    else {
                        if (totalAmount - product.getProductPrice() * 10 >= 0)
                            totalAmount -= product.getProductPrice() * 10;
                        else
                            totalAmount = 0;
                        sendDataToTotal(totalAmount);
                    }
                } else {
                    int temp = holder.numProduct;
                    holder.numProduct = 1;

                    holder.etNumProduct.setText(String.valueOf(holder.numProduct));
                    product.setOrderQuantity(holder.numProduct);
                    if (!holder.cbProduct.isChecked())
                        holder.cbProduct.setChecked(true);
                    else {
                        if (totalAmount - product.getProductPrice() * (temp - holder.numProduct) >= 0)
                            totalAmount -= product.getProductPrice() * (temp - holder.numProduct);
                        else
                            totalAmount = 0;
                        sendDataToTotal(totalAmount);
                    }
                }
                return true;
            }
        });

        // long click on add
        holder.ivAdd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (holder.numProduct + 10 <= product.getQuantity()) {
                    holder.numProduct += 10;
                    product.setOrderQuantity(holder.numProduct);
                    holder.etNumProduct.setText(String.valueOf(holder.numProduct));
                    if (!(holder.cbProduct.isChecked()))
                        holder.cbProduct.setChecked(true);
                    else {
                        totalAmount += product.getProductPrice() * 10;
                        sendDataToTotal(totalAmount);
                    }
                } else {
                    int temp = holder.numProduct;
                    holder.numProduct = product.getQuantity();

                    product.setOrderQuantity(holder.numProduct);
                    holder.etNumProduct.setText(String.valueOf(holder.numProduct));
                    if (!(holder.cbProduct.isChecked()))
                        holder.cbProduct.setChecked(true);
                    else {
                        totalAmount += product.getProductPrice() * (holder.numProduct - temp);
                        sendDataToTotal(totalAmount);
                    }
                }
                return true;
            }
        });

        // set information of product
        String productTitle = product.getProductName();
        String productSeller = product.getSeller();
        String productPrice = String.format("%,d", product.getProductPrice()) + "đ";

        Log.d("Product title", productTitle);

        // set title
        if (productTitle.length() > 15) {
            productTitle = productTitle.substring(0, 15) + "...";
        }
        holder.tvTitle.setText(productTitle);

        //set seller
        setSellerName(productSeller, holder);

        // set price
        holder.tvPrice.setText(productPrice);

        //set quantity
        holder.etNumProduct.setText(String.valueOf(product.getOrderQuantity()));
        holder.numProduct = product.getOrderQuantity();


        //set image
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("productImages")
                .document(product.getProductId()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        Map<String, Object> map = documentSnapshot.getData();

                        if (map != null) {
                            List<String> string = (List<String>) map.get("url");
                            assert string != null;
                            Glide.with(context)
                                    .load(string.get(0)).into(holder.ivProductImg);
                        }
                        else
                            holder.ivProductImg.setImageResource(R.drawable.logoapp);
                    }
                });

        // click on checkbox
        holder.cbProduct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                int priceOfProduct = Integer.parseInt(holder.tvPrice.getText().toString().replaceAll("[^0-9]", ""));
                if (isCheck) {
                    totalAmount += priceOfProduct;
                } else {
                    if (totalAmount - priceOfProduct > 0)
                        totalAmount -= priceOfProduct;
                    else totalAmount = 0;
                }
                Log.w("totalProduct", holder.tvPrice.getText().toString());
                sendDataToTotal(totalAmount);
                product.setChecked(isCheck);
            }
        });

        // click on delete
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeProductFromCart(product);
            }
        });

    }

    private void removeProductFromCart(Product product) {
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        db.collection("Carts").document(firebaseAuth.getCurrentUser().getUid())
                .collection("Products").document(product.getProductId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            notifyDataSetChanged();
                        } else {
                            Log.e("removeProduct", task.getException().toString());
                        }
                    }
                });
    }

    public void sendDataToTotal(int total) {
        Log.w("totalAmount", String.valueOf(total));
        intent.putExtra("totalAmount", total);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        notifyDataSetChanged();
    }

    public void setTextOfPrice(TextView textView, Product productSample, int num) {
        textView.setText(String.format("%,dđ", priceOfProduct(productSample, num)));
    }

    public int priceOfProduct(Product product, int num) {
        return product.getProductPrice() * num;
    }

    public void adjustBtn(ImageView imageView, boolean condition) {
        imageView.setClickable(condition);
        if (condition) {
            imageView.setImageTintList(ColorStateList.valueOf(Color.parseColor("#22B473")));
        } else {
            imageView.setImageTintList(ColorStateList.valueOf(Color.parseColor("#D2D5DD")));
        }
    }

    @Override
    public int getItemCount() {
        if (productList.size() != 0)
            return productList.size();
        return 0;
    }

    class ShoppingCartViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle, tvSeller, tvPrice;
        private ImageView ivProductImg, ivAdd, ivMinus, ivDelete;
        private CheckBox cbProduct;
        private EditText etNumProduct;
        private int numProduct = 1;

        public ShoppingCartViewHolder(@NonNull View itemView) {
            super(itemView);

            cbProduct = itemView.findViewById(R.id.cbProductICart);
            ivDelete = itemView.findViewById(R.id.ivDeleteProductICart);
            tvTitle = itemView.findViewById(R.id.tvProductTitleICart);
            tvSeller = itemView.findViewById(R.id.tvProductSellerICart);
            tvPrice = itemView.findViewById(R.id.tvProductPriceICart);
            ivProductImg = itemView.findViewById(R.id.ivProductImageICart);
            ivAdd = itemView.findViewById(R.id.ivAddICart);
            ivMinus = itemView.findViewById(R.id.ivMinusICart);
            etNumProduct = itemView.findViewById(R.id.etNumProductICart);

//            Log.d("Number", (etNumProduct.getText().toString().equals(null)) ? "null" : etNumProduct.getText().toString());
        }
    }

    private void setSellerName(String sellerId, @NonNull ShoppingCartViewHolder holder) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Users").document(sellerId);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        String productSeller = document.getString("firstName");

                        // set seller
                        if (productSeller.length() > 15) {
                            productSeller = productSeller.substring(0, 15) + "...";
                        }
                        holder.tvSeller.setText(productSeller);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}
