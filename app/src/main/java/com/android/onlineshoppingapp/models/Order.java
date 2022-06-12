package com.android.onlineshoppingapp.models;

import java.util.List;

public class Order {
    private String orderId;
    private String orderer;
    private String orderTime;
    private int orderStatus;
    private int totalPrice;
    private int confirm;
    private List<OrderProduct> listOrderProduct;
    private UserAddress address;

    public Order(String orderId, String orderer, String orderTime, int orderStatus, int totalPrice, int confirm, List<OrderProduct> listOrderProduct, UserAddress address) {
        this.orderId = orderId;
        this.orderer = orderer;
        this.orderTime = orderTime;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.confirm = confirm;
        this.listOrderProduct = listOrderProduct;
        this.address = address;
    }

    public Order(String orderId, String orderer, int orderStatus, int totalPrice, List<OrderProduct> listOrderProduct, UserAddress address) {
        this.orderId = orderId;
        this.address = address;
        this.orderer = orderer;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.listOrderProduct = listOrderProduct;
    }

    public Order(){};

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public int getConfirm() {
        return confirm;
    }

    public void setConfirm(int confirm) {
        this.confirm = confirm;
    }

    public List<OrderProduct> getListOrderProduct() {
        return listOrderProduct;
    }

    public void setListOrderProduct(List<OrderProduct> listOrderProduct) {
        this.listOrderProduct = listOrderProduct;
    }

    public UserAddress getAddress() {
        return address;
    }

    public void setAddress(UserAddress address) {
        this.address = address;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }
}
