package com.example.home.follow;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.common.bean.PictureData;
import com.example.common.bean.ResponseData;
import com.example.common.bean.RetrofitResponse;
import com.example.common.retrofit.RetrofitClient;
import com.example.common.retrofit.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowViewModel extends ViewModel {
    private RetrofitService retrofitService;
    private MutableLiveData<ResponseData<PictureData>> _followPicList = new MutableLiveData<>();
    public LiveData<ResponseData<PictureData>> followPicList = _followPicList;

    private boolean hasMore = false;
    private int currentPage = 0;
    private int size = 0;
    private int total = 0;

    public FollowViewModel(){
        retrofitService = RetrofitClient.getRetrofitService();
    }

    public void getFollowPicList(int current, int size, long userId){
        retrofitService.getPictureListOfCurrentSubscribed(current, size, userId).enqueue(new Callback<RetrofitResponse<ResponseData<PictureData>>>() {
            @Override
            public void onResponse(@NonNull Call<RetrofitResponse<ResponseData<PictureData>>> call, @NonNull Response<RetrofitResponse<ResponseData<PictureData>>> response) {
                if (response.body() == null) return;
                RetrofitResponse<ResponseData<PictureData>> responseBody = response.body();
                _followPicList.setValue(responseBody.data);
                if (responseBody.data == null) return;
                setHasMore(responseBody.data.current * responseBody.data.size < responseBody.data.total);
                setCurrentPage(responseBody.data.current);
                setSize(responseBody.data.size);
                setTotal(responseBody.data.total);
            }

            @Override
            public void onFailure(@NonNull Call<RetrofitResponse<ResponseData<PictureData>>> call, @NonNull Throwable t) {

            }
        });
    }

    public boolean isHasMore() {
        return hasMore;
    }

    private void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    private void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getSize() {
        return size;
    }

    private void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    private void setTotal(int total) {
        this.total = total;
    }
}