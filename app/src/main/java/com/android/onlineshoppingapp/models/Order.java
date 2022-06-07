package com.android.onlineshoppingapp.models;

public class Order extends Product {
    private String orderer;
    private int orderStatus;
    private int totalPrice;
    private int orderQuantity;

    public Order(String productId, String productName, String seller, String description, int productPrice, float rate, int likeNumber, int quantitySold, String orderer, int orderStatus, int totalPrice, int orderQuantity) {
        super(productId, productName, seller, description, productPrice, rate, likeNumber, quantitySold);
        this.orderer = orderer;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.orderQuantity = orderQuantity;
    }

    public Order(String productName, String seller, String description, int productPrice, String orderer, int orderStatus, int totalPrice, int orderQuantity) {
        super(productName, seller, description, productPrice);
        this.orderer = orderer;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.orderQuantity = orderQuantity;
    }

    public Order(String orderer, int orderStatus, int totalPrice, int orderQuantity) {
        this.orderer = orderer;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.orderQuantity = orderQuantity;
    }

    public Order() {
    }

    public String getOrderer() {
        return orderer;
    }

    public void setOrderer(String orderer) {
        this.orderer = orderer;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }
}
