package com.project.financemanager.dtos;

import com.google.gson.annotations.SerializedName;
import com.project.financemanager.models.Category;

import java.io.Serializable;
import java.util.List;

public class CategoryDTO implements Serializable {
    @SerializedName("parent_id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("icon")
    private String icon;

    public CategoryDTO(String id, String name, String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public CategoryDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
}
