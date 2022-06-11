package com.android.onlineshoppingapp.models;

public class Following {
    private String shopId;
    private String shopName;

    public Following(String shopId, String shopName) {
        this.shopId = shopId;
        this.shopName = shopName;
    }

    public Following() {
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
