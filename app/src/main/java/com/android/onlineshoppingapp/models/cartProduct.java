package com.android.onlineshoppingapp.models;

public class cartProduct extends Product{
    private int quantity;

    public cartProduct(Product product, int quantity, String productId) {
        this.productId = productId;
        this.productName = product.productName;
        this.seller = product.seller;
        this.description = product.description;
        this.productPrice = product.productPrice;
        this.rate = product.rate;
        this.likeNumber = product.likeNumber;
        this.quantitySold = product.quantitySold;
        this.quantity = quantity;
    }

    public cartProduct(String productId, String productName, String seller, String description, int productPrice, float rate, int likeNumber, int quantitySold, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.seller = seller;
        this.description = description;
        this.productPrice = productPrice;
        this.rate = rate;
        this.likeNumber = likeNumber;
        this.quantitySold = quantitySold;
        this.quantity = quantity;
    }

    public cartProduct(String productId, String productName, float rate, int productPrice, int quantitySold, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.rate = rate;
        this.productPrice = productPrice;
        this.quantitySold = quantitySold;
        this.quantity = quantity;
    }

    public cartProduct() {

    }

    public cartProduct(String productName, String seller, String description, int productPrice, int quantity) {
        this.productName = productName;
        this.seller = seller;
        this.description = description;
        this.productPrice = productPrice;
        this.rate = 0;
        this.quantitySold = 0;
        this.likeNumber = 0;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
