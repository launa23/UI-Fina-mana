package com.project.financemanager.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Transaction implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("amount")
    private String amount;
    @SerializedName("description")
    private String description;
    @SerializedName("time")
    private String time;
    @SerializedName("type")
    private String type;
    @SerializedName("walletName")
    private String walletName;
    @SerializedName("categoryName")
    private String categoryName;
    @SerializedName("category_id")
    private String categoryID;
    //tus
    @SerializedName("image")
    private String image;
    @SerializedName("wallet_id")
    private String walletID;
    //sut
    public Transaction(int id, String amount, String description, String time, String type, String walletName, String categoryName) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.time = time;
        this.type = type;
        this.walletName = walletName;
        this.categoryName = categoryName;
        this.image = image;
    }

    //tus

    public Transaction(String amount, String description, String time, String categoryID, String walletID) {
        this.amount = amount;
        this.description = description;
        this.time = time;
        this.categoryID = categoryID;
        this.walletID = walletID;
    }

    //sut

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
