package com.web3.store.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AppElement implements Serializable {

    @SerializedName("name")
    private final String name;

    @SerializedName("category")
    private final String category;

    @SerializedName("fileCid")
    private final String fileCid;

    @SerializedName("iconCid")
    private final String iconCid;

    public AppElement(String appName, String appIcon, String fileCid, String category) {
        this.name = appName;
        this.iconCid = appIcon;
        this.fileCid = fileCid;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getIconCid() {
        return iconCid;
    }

    public String getFileCid() {
        return fileCid;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "AppElement{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", fileCid='" + fileCid + '\'' +
                ", iconCid='" + iconCid + '\'' +
                '}';
    }
}
