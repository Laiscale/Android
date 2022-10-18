package com.example.common.retrofit;

import android.util.Log;

import com.example.common.constants.HttpConstants;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

import okhttp3.Interceptor;
import okhttp3.MediaType;
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

        Log.e("retrofit", "request body: " + newRequest.body());

        Response response = chain.proceed(newRequest);

        Log.e("retrofit", "response body: " + response.body());

        if (isPlainText(response.body().contentType())){
            String body = response.peekBody(Long.MAX_VALUE).string();
            Log.e("retrofit", "response Body: " + body);
        }

        Log.e("retrofit","----------Request End----------");


        return response;
    }

    private Boolean isPlainText(MediaType mediaType){
        if (mediaType == null) return false;
        String mediaTypeString = mediaType.toString();
        if (!mediaTypeString.isEmpty()){
            mediaTypeString = mediaTypeString.toLowerCase(Locale.ROOT);
            if (mediaTypeString.contains("text") || mediaTypeString.contains("application/json")){
                return true;
            }
        }
        return false;
    }
}