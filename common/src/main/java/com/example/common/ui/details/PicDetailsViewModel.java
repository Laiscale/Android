package com.example.common.ui.details;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.common.bean.CommentData;
import com.example.common.bean.FirstCommentBean;
import com.example.common.bean.PictureData;
import com.example.common.bean.ResponseData;
import com.example.common.bean.RetrofitResponse;
import com.example.common.retrofit.RetrofitClient;
import com.example.common.retrofit.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PicDetailsViewModel extends ViewModel {
    private RetrofitService retrofitService;
    private MutableLiveData<RetrofitResponse<String>> _focusRes = new MutableLiveData<>();
    public LiveData<RetrofitResponse<String>> focusRes = _focusRes;

    private MutableLiveData<RetrofitResponse<String>> _unFocusRes = new MutableLiveData<>();
    public LiveData<RetrofitResponse<String>> unFocusRes = _unFocusRes;

    private MutableLiveData<RetrofitResponse<String>> _commentRes = new MutableLiveData<>();
    public LiveData<RetrofitResponse<String>> commentRes = _commentRes;

    private MutableLiveData<List<CommentData>> _firstCommentList = new MutableLiveData<>();
    public LiveData<List<CommentData>> firstCommentList = _firstCommentList;

    private MutableLiveData<RetrofitResponse<PictureData>> _picData = new MutableLiveData<>();
    public LiveData<RetrofitResponse<PictureData>> picData = _picData;

    private MutableLiveData<RetrofitResponse<String>> _likeRes = new MutableLiveData<>();
    public LiveData<RetrofitResponse<String>> likeRes = _likeRes;

    private MutableLiveData<RetrofitResponse<String>> _disLikeRes = new MutableLiveData<>();
    public LiveData<RetrofitResponse<String>> disLikeRes = _disLikeRes;

    private MutableLiveData<RetrofitResponse<String>> _favoriteRes = new MutableLiveData<>();
    public LiveData<RetrofitResponse<String>> favoriteRes = _favoriteRes;

    private MutableLiveData<RetrofitResponse<String>> _unFavoriteRes = new MutableLiveData<>();
    public LiveData<RetrofitResponse<String>> unFavoriteRes = _unFavoriteRes;

    private boolean hasMore = false;
    private int currentPage = 0;
    private int size = 0;
    private int total = 0;

    public PicDetailsViewModel(){
        retrofitService = RetrofitClient.getRetrofitService();
    }

    public void focusUser(long focusId, long userId){
        retrofitService.subscribedUser(focusId, userId).enqueue(new Callback<RetrofitResponse<String>>() {
            @Override
            public void onResponse(@NonNull Call<RetrofitResponse<String>> call, @NonNull Response<RetrofitResponse<String>> response) {
                _focusRes.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<RetrofitResponse<String>> call, @NonNull Throwable t) {

            }
        });
    }

    public void unFocusUser(long focusId, long userId){
        retrofitService.unSubscribedUser(focusId, userId).enqueue(new Callback<RetrofitResponse<String>>() {
            @Override
            public void onResponse(@NonNull Call<RetrofitResponse<String>> call, @NonNull Response<RetrofitResponse<String>> response) {
                _unFocusRes.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<RetrofitResponse<String>> call, @NonNull Throwable t) {

            }
        });
    }

    public void releaseComment(String content, long shareId, long userId, String username){
        FirstCommentBean firstCommentBean = new FirstCommentBean(
                content,
                shareId,
                userId,
                username
        );
        retrofitService.addFirstComment(firstCommentBean).enqueue(new Callback<RetrofitResponse<String>>() {
            @Override
            public void onResponse(@NonNull Call<RetrofitResponse<String>> call, @NonNull Response<RetrofitResponse<String>> response) {
                _commentRes.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<RetrofitResponse<String>> call, @NonNull Throwable t) {

            }
        });
    }

    public void getFirstCommentList(int page, long shareId ,int size){
        retrofitService.getFirstCommentList(page, shareId, size).enqueue(new Callback<RetrofitResponse<ResponseData<CommentData>>>() {
            @Override
            public void onResponse(@NonNull Call<RetrofitResponse<ResponseData<CommentData>>> call, @NonNull Response<RetrofitResponse<ResponseData<CommentData>>> response) {
                if (response.body() == null) return;
                if (response.body().data == null) return;
                RetrofitResponse<ResponseData<CommentData>> responseBody = response.body();
                _firstCommentList.setValue(responseBody.data.records);
                if (responseBody.data == null) return;
                setHasMore(responseBody.data.current * responseBody.data.size < responseBody.data.total);
                setCurrentPage(responseBody.data.current);
                setSize(responseBody.data.size);
                setTotal(responseBody.data.total);
            }

            @Override
            public void onFailure(@NonNull Call<RetrofitResponse<ResponseData<CommentData>>> call, @NonNull Throwable t) {

            }
        });
    }

    public void getPicDetails(long shareId, long userId){
        retrofitService.getPictureDetail(shareId, userId).enqueue(new Callback<RetrofitResponse<PictureData>>() {
            @Override
            public void onResponse(@NonNull Call<RetrofitResponse<PictureData>> call, @NonNull Response<RetrofitResponse<PictureData>> response) {
                _picData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<RetrofitResponse<PictureData>> call, @NonNull Throwable t) {

            }
        });
    }

    public void likePic(long shareId, long userId){
        retrofitService.likePicture(shareId, userId).enqueue(new Callback<RetrofitResponse<String>>() {
            @Override
            public void onResponse(@NonNull Call<RetrofitResponse<String>> call, @NonNull Response<RetrofitResponse<String>> response) {
                _likeRes.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<RetrofitResponse<String>> call, @NonNull Throwable t) {

            }
        });
    }

    public void dislikePic(long likeId){
        retrofitService.disLikePicture(likeId).enqueue(new Callback<RetrofitResponse<String>>() {
            @Override
            public void onResponse(@NonNull Call<RetrofitResponse<String>> call, @NonNull Response<RetrofitResponse<String>> response) {
                _disLikeRes.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<RetrofitResponse<String>> call, @NonNull Throwable t) {

            }
        });
    }

    public void favoritePic(long shareId, long userId){
        retrofitService.favoritePicture(shareId, userId).enqueue(new Callback<RetrofitResponse<String>>() {
            @Override
            public void onResponse(@NonNull Call<RetrofitResponse<String>> call, @NonNull Response<RetrofitResponse<String>> response) {
                _favoriteRes.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<RetrofitResponse<String>> call, @NonNull Throwable t) {

            }
        });
    }

    public void unFavoritePic(long collectId){
        retrofitService.unFavoritePicture(collectId).enqueue(new Callback<RetrofitResponse<String>>() {
            @Override
            public void onResponse(@NonNull Call<RetrofitResponse<String>> call, @NonNull Response<RetrofitResponse<String>> response) {
                _unFavoriteRes.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<RetrofitResponse<String>> call, @NonNull Throwable t) {

            }
        });
    }

    public boolean getHasMore() {
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
