package com.android.onlineshoppingapp.models;

import java.util.List;

public class Order {
    private String orderId;
    private String orderer;
    private int orderStatus;
    private int totalPrice;
    private List<OrderProduct> listOrderProduct;

    public Order(String orderId, String orderer, int orderStatus, int totalPrice, List<OrderProduct> listOrderProduct) {
        this.orderId = orderId;
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



    public List<OrderProduct> getListOrderProduct() {
        return listOrderProduct;
    }

    public void setListOrderProduct(List<OrderProduct> listOrderProduct) {
        this.listOrderProduct = listOrderProduct;
    }
}
