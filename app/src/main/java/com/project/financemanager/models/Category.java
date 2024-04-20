package com.project.financemanager.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Category implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("icon")
    private String icon;
    @SerializedName("categoryOf")
    private String categoryOf;
    @SerializedName("categoryChilds")
    List<Category> categoryChilds;

    public Category(int id, String name, String icon, String categoryOf, List<Category> categoryChilds) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.categoryOf = categoryOf;
        this.categoryChilds = categoryChilds;
    }

    public Category() {
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

    public String getCategoryOf() {
        return categoryOf;
    }

    public void setCategoryOf(String categoryOf) {
        this.categoryOf = categoryOf;
    }

    public List<Category> getCategoryChilds() {
        return categoryChilds;
    }

    public void setCategoryChilds(List<Category> categoryChilds) {
        this.categoryChilds = categoryChilds;
    }
}
