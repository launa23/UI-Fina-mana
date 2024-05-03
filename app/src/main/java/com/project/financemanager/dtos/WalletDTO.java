package com.project.financemanager.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WalletDTO implements Serializable {
    @SerializedName("name")
    private String name;
    @SerializedName("icon")
    private String icon;
    @SerializedName("money")
    private String money;

    public WalletDTO(String name, String icon, String money) {
        this.name = name;
        this.icon = icon;
        this.money = money;
    }

    public WalletDTO() {
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
