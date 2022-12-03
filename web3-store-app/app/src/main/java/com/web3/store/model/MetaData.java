package com.web3.store.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MetaData implements Serializable {

    @SerializedName("apkCid")
    private final String apkCid;
    @SerializedName("folderCid")
    private final String folderCid;
    @SerializedName("metaDataCid")
    private final String metaDataCid;
    @SerializedName("appIconCid")
    private final String appIconCid;
    @SerializedName("screenshotCid1")
    private final String screenshotCid1;
    @SerializedName("screenshotCid2")
    private final String screenshotCid2;
    @SerializedName("screenshotCid3")
    private final String screenshotCid3;

    public MetaData(String apkCid, String folderCid, String metaDataCid, String appIconCid, String screenshotCid1, String screenshotCid2, String screenshotCid3) {
        this.apkCid = apkCid;
        this.folderCid = folderCid;
        this.metaDataCid = metaDataCid;
        this.appIconCid = appIconCid;
        this.screenshotCid1 = screenshotCid1;
        this.screenshotCid2 = screenshotCid2;
        this.screenshotCid3 = screenshotCid3;
    }

    public String getApkCid() {
        return apkCid;
    }

    public String getFolderCid() {
        return folderCid;
    }

    public String getMetaDataCid() {
        return metaDataCid;
    }

    public String getAppIconCid() {
        return appIconCid;
    }

    public String getScreenshotCid1() {
        return screenshotCid1;
    }

    public String getScreenshotCid2() {
        return screenshotCid2;
    }

    public String getScreenshotCid3() {
        return screenshotCid3;
    }
}
