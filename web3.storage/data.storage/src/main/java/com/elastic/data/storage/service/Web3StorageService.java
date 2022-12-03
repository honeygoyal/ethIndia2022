package com.elastic.data.storage.service;

import com.elastic.data.storage.model.AppData;
import com.elastic.data.storage.model.AppUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface Web3StorageService {

    File loadFile(String hash, String appName);
    AppUploadResponse uploadFolder(AppData appData);
}
