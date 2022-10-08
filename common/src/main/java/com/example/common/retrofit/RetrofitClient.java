package com.example.common.retrofit;

import com.example.common.constants.HttpConstants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static final OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new RetrofitInterceptor()).build();
    public static RetrofitService getRetrofitService(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(HttpConstants.BASE_URL)
                    .client(okHttpClient)
                    .build();
        }
        return retrofit.create(RetrofitService.class);
    }
}
