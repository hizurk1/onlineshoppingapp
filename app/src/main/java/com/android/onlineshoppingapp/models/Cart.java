package com.android.onlineshoppingapp.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {
    private List<cartProduct> cartProductList;
    private int totalPrice;

    public Cart(List<cartProduct> cartProductList, int totalPrice) {
        this.cartProductList = cartProductList;
        this.totalPrice = totalPrice;
    }

    public List<cartProduct> getCartProductList() {
        return cartProductList;
    }

    public void setCartProductList(List<cartProduct> cartProductList) {
        this.cartProductList = cartProductList;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
