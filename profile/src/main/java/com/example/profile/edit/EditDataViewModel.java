package com.example.profile.edit;

import android.telecom.Call;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.common.bean.PictureData;
import com.example.common.bean.ResponseData;
import com.example.common.bean.RetrofitResponse;
import com.example.common.bean.UserUpdateData;
import com.example.common.retrofit.RetrofitClient;
import com.example.common.retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditDataViewModel extends ViewModel {
    private RetrofitService retrofitService;
    private MutableLiveData<RetrofitResponse<String>> _userUpdateRes = new MutableLiveData<>();
    public LiveData<RetrofitResponse<String>> userUpdateRes = _userUpdateRes;
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
}
