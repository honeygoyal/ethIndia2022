package com.elastic.data.storage.service;

import com.elastic.data.storage.model.AppData;
import com.elastic.data.storage.model.AppUploadResponse;
import com.elastic.data.storage.model.ContentMetaData;
import com.elastic.data.storage.model.UploadResponse;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class Web3StorageServiceImpl implements Web3StorageService {

    private static final String BASE_URL = "https://w3s.link/ipfs/";
    private static final String UPLOAD_URL = "https://api.web3.storage/upload";

    @Override
    public File loadFile(String hash, String appName) {
        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        urlBuilder.append(hash);
        try {
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            byte []bytes  = IOUtils.toByteArray(conn.getInputStream());
            File file = new File(appName);
            file.createNewFile();
            writeBytesToFile(appName, bytes);
            conn.disconnect();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public AppUploadResponse uploadFolder(AppData appData) {
        try{
            UploadResponse apkResponse = uploadFile(convert(appData.getApk()));
            UploadResponse screenShot1 = uploadFile(convert(appData.getScreenshots1()));
            UploadResponse screenShot2 = uploadFile(convert(appData.getScreenshots2()));
            UploadResponse screenShot3 = uploadFile(convert(appData.getScreenshots3()));
            UploadResponse appIconResponse = uploadFile(convert(appData.getAppIcon()));
            UploadResponse metaDataResponse = uploadFile(getFile("meta_data.json", appData.toString()));
            assert apkResponse != null;
            assert appIconResponse != null;
            assert screenShot1 != null;
            assert screenShot2 != null;
            assert screenShot3 != null;
            assert metaDataResponse != null;
            ContentMetaData contentMetaData = new ContentMetaData( "",
                    apkResponse.getCid(), appIconResponse.getCid(), screenShot1.getCid(),
                    screenShot2.getCid(), screenShot3.getCid(), metaDataResponse.getCid());
            UploadResponse uploadResponse = uploadFile(getFile("c.json", contentMetaData.toString()));
            AppUploadResponse appUploadResponse = new AppUploadResponse();
            appUploadResponse.setDataCid(uploadResponse.getCid());
            appUploadResponse.setAppIconCid(contentMetaData.getAppIconCid());
            appUploadResponse.setAppName(appData.getAppName());
            appUploadResponse.setCategory(appData.getCategory());
            appUploadResponse.setAppType(appData.getType());
            return appUploadResponse;
        }catch (Exception exception){
            System.out.println("Error: " + exception);
        }
        return null;
    }

    private UploadResponse uploadFile(File file){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(UPLOAD_URL);
        MultipartEntityBuilder builder  = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("file", new FileBody(file));
        HttpEntity httpEntity = builder.build();
        Header header = new BasicHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJkaWQ6ZXRocjoweDM1MTY0M2UwZjk4NGM2NDM0M2U2RTUxYWI1MTYzQWE5Y2NGRGRDMGIiLCJpc3MiOiJ3ZWIzLXN0b3JhZ2UiLCJpYXQiOjE2NzAwNDcyMTMxMzAsIm5hbWUiOiJ0ZXN0LXRva2VuIn0.5134IEf5SjdErhYMYyGMDr0pjJYRg0urA5vX7uF92qU");
        httpPost.setHeader(header);
        httpPost.setEntity(httpEntity);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            String responseStr = EntityUtils.toString(responseEntity);
            Gson g = new Gson();
            UploadResponse uploadResponse = g.fromJson(responseStr, UploadResponse.class);
            return uploadResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static void writeBytesToFile(String fileOutput, byte[] bytes)
            throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fileOutput)) {
            fos.write(bytes);
        }
    }

        public static File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

        public File getFile(String fileName, String content) throws IOException {
        File metaDataFile = new File(fileName);
        metaDataFile.createNewFile();
        writeInFile(fileName, content);
        return metaDataFile;
    }

        public void writeInFile(String fileName, String text) {
        try {
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write(text);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("file already exist");
        }

    }

}
