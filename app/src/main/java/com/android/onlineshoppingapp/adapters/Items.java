package com.android.onlineshoppingapp.adapters;
//Tạo 1 class Items chứa các thuộc tính của 1 item thể hiện trong cardview
public class Items {
    private int resourceImg;
    private String text;

    public Items(int resourceImg, String text) {
        this.resourceImg = resourceImg;
        this.text = text;
    }

    public int getResourceImg() {
        return resourceImg;
    }

    public String getText() {
        return text;
    }

    public void setResourceImg(int resourceImg) {
        this.resourceImg = resourceImg;
    }

    public void setText(String text) {
        this.text = text;
    }
}
