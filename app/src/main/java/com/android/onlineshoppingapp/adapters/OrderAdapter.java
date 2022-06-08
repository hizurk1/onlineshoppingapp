package com.android.onlineshoppingapp.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.onlineshoppingapp.R;
import com.android.onlineshoppingapp.models.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {

        Order order = orderList.get(position);
        if (order == null) return;
        if (order.getOrderStatus() == 2 || order.getOrderStatus() == 4) {
            holder.btnCancel.setVisibility(View.INVISIBLE);
        }

        holder.tvShopName.setText(order.getSeller());
        String orderStatus = getOrderStatus(order);
        holder.tvOrderStatus.setText(orderStatus);
        if (order.getOrderStatus() == 4) {
            holder.tvOrderStatus.setTextColor(holder.itemView.getResources().getColor(R.color.red_error));
        }

        if (order.getProductName().length() > 45) {
            holder.tvProductName.setText(String.format("%s...", order.getProductName().substring(0, 45)));
        } else {
            holder.tvProductName.setText(order.getProductName());
        }

        holder.tvCategory.setText("Loại:");
        holder.tvPrice.setText(String.format("đ%,d", order.getProductPrice()));
        holder.tvNumber.setText(String.format("x%d", order.getOrderQuantity()));
        holder.tvPriceTotal.setText(String.format("đ%,d", order.getTotalPrice()));

        if (order.getOrderStatus() == 3) {
            holder.btnCancel.setText("Đánh giá");
            holder.btnCancel.setOnClickListener(view -> {
                Toast.makeText(view.getContext(), "Đánh giá sản phẩm", Toast.LENGTH_SHORT).show();
            });
        } else {
            holder.btnCancel.setOnClickListener(view -> {
                Toast.makeText(view.getContext(), "Huỷ", Toast.LENGTH_SHORT).show();
            });
        }

    }

    @Override
    public int getItemCount() {
        if (orderList.isEmpty()) {
            return 0;
        }
        return orderList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {

        private TextView tvShopName, tvOrderStatus, tvProductName, tvCategory, tvPrice, tvNumber, tvPriceTotal;
        private Button btnCancel;
        private ImageView ivProductImg;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            tvShopName = itemView.findViewById(R.id.tvShopNameOrder);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatusOrder);
            tvProductName = itemView.findViewById(R.id.tvProductNameOrder);
            tvCategory = itemView.findViewById(R.id.tvCategoryOrder);
            tvPrice = itemView.findViewById(R.id.tvProductPriceOrder);
            tvPriceTotal = itemView.findViewById(R.id.tvPriceTotalOrder);
            tvNumber = itemView.findViewById(R.id.tvNumberOrder);
            btnCancel = itemView.findViewById(R.id.btnCancelOrder);
            ivProductImg = itemView.findViewById(R.id.ivProductImageOrder);

        }
    }

    public String getOrderStatus(Order order) {
        String orderStatus = "";
        switch (order.getOrderStatus()) {
            case 0:
                orderStatus = "Chờ xác nhận";
                break;
            case 1:
                orderStatus = "Đang lấy hàng";
                break;
            case 2:
                orderStatus = "Đang giao hàng";
                break;
            case 3:
                orderStatus = "Chờ đánh giá";
                break;
            case 4:
                orderStatus = "Đã huỷ";
                break;
        }
        return orderStatus;
    }
}
