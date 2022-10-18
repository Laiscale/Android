package com.example.profile.edit;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.common.bean.ImageUploadData;
import com.example.common.bean.PictureData;
import com.example.common.bean.ResponseData;
import com.example.common.bean.RetrofitResponse;
import com.example.common.bean.UserUpdateData;
import com.example.common.retrofit.RetrofitClient;
import com.example.common.retrofit.RetrofitService;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditDataViewModel extends ViewModel {
    private RetrofitService retrofitService;

    private MutableLiveData<RetrofitResponse<String>> _userUpdateRes = new MutableLiveData<>();
    public LiveData<RetrofitResponse<String>> userUpdateRes = _userUpdateRes;

    private MutableLiveData<RetrofitResponse<ImageUploadData>> _uploadData = new MutableLiveData<>();
    public LiveData<RetrofitResponse<ImageUploadData>> uploadData = _uploadData;

    public EditDataViewModel(){
        retrofitService = RetrofitClient.getRetrofitService();
    }

    public void saveUserInfo(String avatar, long userId, String introduce, int sex, String username){
        UserUpdateData userUpdateData = new UserUpdateData(avatar, userId, introduce, sex, username);
        retrofitService.userUpdate(userUpdateData).enqueue(new Callback<RetrofitResponse<String>>() {
            @Override
            public void onResponse(@NonNull Call<RetrofitResponse<String>> call, @NonNull Response<RetrofitResponse<String>> response) {
                _userUpdateRes.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<RetrofitResponse<String>> call, @NonNull Throwable t) {

            }
        });
    }

    public void uploadAvatar(String avatarPath){
        File file = new File(avatarPath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("fileList", file.getName(), requestBody);
        List<MultipartBody.Part> partList = new LinkedList<>();
        partList.add(part);

        retrofitService.uploadFile(partList).enqueue(new Callback<RetrofitResponse<ImageUploadData>>() {
            @Override
            public void onResponse(@NonNull Call<RetrofitResponse<ImageUploadData>> call, @NonNull Response<RetrofitResponse<ImageUploadData>> response) {
                _uploadData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<RetrofitResponse<ImageUploadData>> call, @NonNull Throwable t) {

            }
        });
    }
}