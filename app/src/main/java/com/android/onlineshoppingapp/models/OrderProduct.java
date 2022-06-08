package com.android.onlineshoppingapp.models;

public class OrderProduct extends Product{
    private int orderQuantity;

    public OrderProduct(String productId, String productName, String seller, String description, String category, int productPrice, float rate, int likeNumber, int quantitySold, int quantity, int orderQuantity) {
        super(productId, productName, seller, description, category, productPrice, rate, likeNumber, quantitySold, quantity);
        this.orderQuantity = orderQuantity;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }
}
