package com.elastic.data.storage.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.simple.JSONObject;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContentMetaData {
    private String folderCid;
    private String apkCid;
    private String appIconCid;
    private String screenshotCid1;
    private String screenshotCid2;
    private String screenshotCid3;
    private String metaDataCid;

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("folderCid", folderCid);
        jsonObject.put("apkCid", apkCid);
        jsonObject.put("appIconCid", appIconCid);
        jsonObject.put("screenshotCid1", screenshotCid1);
        jsonObject.put("screenshotCid2", screenshotCid2);
        jsonObject.put("screenshotCid3", screenshotCid3);
        jsonObject.put("metaDataCid", metaDataCid);
        return jsonObject.toString();
    }
}
