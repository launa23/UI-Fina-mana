package com.project.financemanager.models;

public class Transaction {
    private String categoryName;
    private String amount;
    private int imgId;

    public Transaction(String categoryName, String amount, int imgId) {
        this.categoryName = categoryName;
        this.amount = amount;
        this.imgId = imgId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
