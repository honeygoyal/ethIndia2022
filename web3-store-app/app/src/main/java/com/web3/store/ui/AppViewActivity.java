package com.web3.store.ui;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.web3.store.R;
import com.web3.store.VolleySingleton;
import com.web3.store.model.AppElement;
import com.web3.store.model.MetaData;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.List;

public class AppViewActivity extends AppCompatActivity {

    private String BASE_URL = "http://54.249.38.16:8080/file/";
    private TextView appDescTv, appNameTv, appLanguageTV, appSubscription;
    private NetworkImageView appIconView;
    VolleySingleton volleySingleton;
    private MetaData metaData;
    RecyclerView recyclerView;
    Button button;
    DownloadManager downloadManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_app_layout);
        volleySingleton = VolleySingleton.getInstance(getApplicationContext());
        AppElement appElement = (AppElement) getIntent().getSerializableExtra("appData");
        appIconView = findViewById(R.id.app_image);
        recyclerView = findViewById(R.id.recycler_screenshots);
        recyclerView.setLayoutManager(new GridLayoutManager(AppViewActivity.this,
                1, GridLayoutManager.HORIZONTAL, false));
        button = findViewById(R.id.get_app);
        appDescTv = findViewById(R.id.app_desc_id);
        appNameTv = findViewById(R.id.app_title);
        appLanguageTV = findViewById(R.id.language);
        appSubscription = findViewById(R.id.subscription_id);
        StringBuilder imageUrl = new StringBuilder(BASE_URL);
        imageUrl.append(appElement.getIconCid());
        imageUrl.append("/icon.png");
        appIconView.setImageUrl(imageUrl.toString(), volleySingleton.getImageLoader());
        StringBuilder metaDataURL = new StringBuilder(BASE_URL);
        metaDataURL.append(appElement.getFileCid());
        metaDataURL.append("/meta.json");
        loadMetaData(metaDataURL.toString(), true);
        appNameTv.setText(appElement.getName());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(metaData!=null){
                    StringBuilder apkURL = new StringBuilder(BASE_URL);
                    apkURL.append(metaData.getApkCid());
                    apkURL.append("/");
                    apkURL.append(appElement.getName());
                    apkURL.append(".apk");
                    downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(apkURL.toString());
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                    long reference = downloadManager.enqueue(request);
                }
            }
        });

    }

    private class AppAdapter extends RecyclerView.Adapter<AppDataHolder> {

        List<String> screenshotCidList;
        Context context;

        public AppAdapter(List<String> screenshotCidList, Context context) {
            this.screenshotCidList = screenshotCidList;
            this.context = context;
        }
        @NonNull
        @Override
        public AppDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View itemView = layoutInflater.inflate(R.layout.screens, parent, false);
            return new AppDataHolder(itemView, parent);
        }

        @Override
        public void onBindViewHolder(AppDataHolder appDataHolder, int index) {
            String screenCid = screenshotCidList.get(index);
            StringBuilder imageUrl = new StringBuilder(BASE_URL);
            imageUrl.append(screenCid);
            imageUrl.append("/icon.png");
            appDataHolder.screenShot.setImageUrl(imageUrl.toString(), volleySingleton.getImageLoader());
        }

        @Override
        public int getItemCount() {
            return screenshotCidList.size();
        }

    }

    private class AppDataHolder extends RecyclerView.ViewHolder {
        NetworkImageView screenShot;

        public AppDataHolder(View itemView, ViewGroup parent) {
            super(itemView);
            screenShot = itemView.findViewById(R.id.screen_image);
        }

    }

    private void loadMetaData(String URL, boolean isMetaData) {
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        try {
                            if(isMetaData) {
                                JSONObject jsonObject = new JSONObject(response);
                                String apkCid = jsonObject.getString("apkCid");
                                String folderCid = jsonObject.getString("folderCid");
                                String metaDataCid = jsonObject.getString("metaDataCid");
                                String screenshotCid1 = jsonObject.getString("screenshotCid1");
                                String screenshotCid2 = jsonObject.getString("screenshotCid2");
                                String screenshotCid3 = jsonObject.getString("screenshotCid3");
                                String appIconCid = jsonObject.getString("appIconCid");
                                metaData = new MetaData(apkCid, folderCid, metaDataCid, appIconCid, screenshotCid1, screenshotCid2, screenshotCid3);
                                if(metaData != null){
                                    AppAdapter appAdapter = new AppAdapter(Arrays.asList(metaData.getScreenshotCid1(), metaData.getScreenshotCid2(), metaData.getScreenshotCid3()),
                                            getApplicationContext());
                                    recyclerView.setAdapter(appAdapter);
                                    StringBuilder contentMetaDataURL = new StringBuilder(BASE_URL);
                                    contentMetaDataURL.append(metaData.getMetaDataCid());
                                    contentMetaDataURL.append("/cmeta.json");
                                    loadMetaData(contentMetaDataURL.toString(), false);
                                }
                            }else {
                                JSONObject jsonObject = new JSONObject(response);
                                String appDesc = jsonObject.getString("appDesc");
                                String language = jsonObject.getString("language");
                                String subscription = jsonObject.getString("subscription");
                                appLanguageTV.setText(language);
                                appSubscription.setText(subscription);
                                appDescTv.setText(appDesc);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
