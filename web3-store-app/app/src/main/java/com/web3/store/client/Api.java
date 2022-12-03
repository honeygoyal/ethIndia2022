package com.web3.store.client;

import com.web3.store.model.AppElement;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;

public interface Api {
   // String BASE_URL = "https://886b-2402-e280-2145-c5-58c6-99d0-2034-66df.in.ngrok.io/index/";

    String BASE_URL = "http://54.249.38.16:8000/index/";
    @POST("getApps")
    Call<List<AppElement>> getAllApps();

    @POST("getComics")
    Call<List<AppElement>> getAllComics();

    @POST("getGames")
    Call<List<AppElement>> getAllGames();
}
