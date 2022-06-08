package com.android.onlineshoppingapp.models;

import java.io.Serializable;

public class Product implements Serializable {
    protected String productId;
    protected String productName;
    protected String seller;
    protected String description;
    protected String category = "Khác";
    protected int productPrice;
    protected float rate;
    protected int likeNumber;
    protected int quantitySold;
    protected int quantity;

    public Product(String productId, String productName, String seller, String description, String category, int productPrice, float rate, int likeNumber, int quantitySold, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.seller = seller;
        this.description = description;
        this.category = category;
        this.productPrice = productPrice;
        this.rate = rate;
        this.likeNumber = likeNumber;
        this.quantitySold = quantitySold;
        this.quantity = quantity;
    }

    public Product(String productName, String description, int productPrice, int quantity) {
        this.productName = productName;
        this.description = description;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.rate = 0;
        this.quantitySold = 0;
        this.likeNumber = 0;
    }

    public Product() {
    }

    public String getSeller() {
        return seller;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(int likeNumber) {
        this.likeNumber = likeNumber;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
