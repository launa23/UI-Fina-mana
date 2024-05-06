package com.project.financemanager.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StatisticByCategoryDTO implements Serializable {
    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @SerializedName("icon")
    private String icon;
    @SerializedName("total")
    private String total;

    public StatisticByCategoryDTO(long id, String name, String icon, String total) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.total = total;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
