package com.elastic.data.storage.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUploadResponse {
    String appName;
    String category;
    String dataCid;
    String appIconCid;
    String appType;
}
