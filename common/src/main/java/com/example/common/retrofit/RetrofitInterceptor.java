package com.example.common.retrofit;

import android.util.Log;

import com.example.common.constants.HttpConstants;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RetrofitInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest = request.newBuilder()
                .addHeader("appId", HttpConstants.APP_ID)
                .addHeader("appSecret", HttpConstants.APP_SECRET)
                .build();

        Log.e("retrofit","----------Request Start----------");
        Log.e("retrofit","" + newRequest.method() + " " + newRequest.url());

        assert newRequest.body() != null;
        Log.e("retrofit", "request content type" + Objects.requireNonNull(newRequest.body().contentType()).toString() );

        Log.e("retrofit", "request body: " + newRequest.body());

        Response response = chain.proceed(newRequest);

        Log.e("retrofit", "response body: " + response.body());

        Log.e("retrofit","----------Request End----------");


        return response;
    }
}
