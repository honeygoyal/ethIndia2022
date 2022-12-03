package com.elastic.data.storage.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.simple.JSONObject;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppData {

    public MultipartFile apk;
    public MultipartFile screenshots1;
    public MultipartFile screenshots2;
    public MultipartFile screenshots3;
    public MultipartFile appIcon;
    public String appName;
    public String appDesc;
    public String language;
    public String type;
    public String category;
    public String subscription;

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appName", appName);
        jsonObject.put("appDesc", appDesc);
        jsonObject.put("language", language);
        jsonObject.put("type", type);
        jsonObject.put("category", category);
        jsonObject.put("subscription", subscription);
        return jsonObject.toString();
    }
}
