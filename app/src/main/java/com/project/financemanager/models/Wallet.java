package com.project.financemanager.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Wallet implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("icon")
    private String icon;
    @SerializedName("money")
    private String money;
    @SerializedName("belongUser")
    private String belongUser;

    public Wallet(int id, String name, String icon, String money) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.money = money;
    }

    public String getBelongUser() {
        return belongUser;
    }

    public void setBelongUser(String belongUser) {
        this.belongUser = belongUser;
    }

    public Wallet(int id, String name, String icon, String money, String belongUser) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.money = money;
        this.belongUser = belongUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
