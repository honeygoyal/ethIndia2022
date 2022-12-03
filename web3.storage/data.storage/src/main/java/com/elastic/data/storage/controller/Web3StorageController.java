package com.elastic.data.storage.controller;


import com.elastic.data.storage.model.AppData;
import com.elastic.data.storage.model.AppUploadResponse;
import com.elastic.data.storage.service.Web3StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;

@CrossOrigin
@RestController
public class Web3StorageController {

    @Autowired
    private Web3StorageService web3StorageService;

    @GetMapping(value = "file/{hash}/{fileName}", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<InputStreamResource> getFile(@PathVariable("hash") String hash, @PathVariable("fileName")String fileName) throws FileNotFoundException {
        File file = web3StorageService.loadFile(hash, fileName);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +file.getName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @PostMapping(path = "create",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<AppUploadResponse> uploadFolder(@ModelAttribute AppData appData) {
        AppUploadResponse appUploadResponse = web3StorageService.uploadFolder(appData);
        return ResponseEntity.status(HttpStatus.OK).body(appUploadResponse);
    }

}
