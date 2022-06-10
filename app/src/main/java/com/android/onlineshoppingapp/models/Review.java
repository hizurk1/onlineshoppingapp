package com.android.onlineshoppingapp.models;

public class Review {
    private String productId;
    private String category;
    private String reviewer;
    private String content;
    private int rate;
    private String createdDate;

    public Review(String productId, String category, String reviewer, String content, int rate, String createdDate) {
        this.productId = productId;
        this.category = category;
        this.reviewer = reviewer;
        this.content = content;
        this.rate = rate;
        this.createdDate = createdDate;
    }

    public Review(String productId, String reviewer, String content, int rate, String createdDate) {
        this.productId = productId;
        this.reviewer = reviewer;
        this.content = content;
        this.rate = rate;
        this.createdDate = createdDate;
    }

    public Review(String productId, String reviewer, String content, int rate) {
        this.productId = productId;
        this.reviewer = reviewer;
        this.content = content;
        this.rate = rate;
    }

    public Review(String reviewer, String content, int rate) {
        this.reviewer = reviewer;
        this.content = content;
        this.rate = rate;
    }

    public Review() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
