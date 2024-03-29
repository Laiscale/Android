package com.example.common.retrofit;

import com.example.common.constants.HttpConstants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static final OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new RetrofitInterceptor()).build();
    private static RetrofitService retrofitService;
    public static RetrofitService getRetrofitService(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(HttpConstants.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        if (retrofitService == null) retrofitService = retrofit.create(RetrofitService.class);
        return retrofitService;
    }
}
