package com.project.financemanager.dtos;

import com.google.gson.annotations.SerializedName;

public class UserLogin {
    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    public UserLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
