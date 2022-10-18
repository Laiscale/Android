package com.example.IntegratedDesign;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.common.bean.RetrofitResponse;
import com.example.common.bean.UserData;
import com.example.common.retrofit.RetrofitClient;
import com.example.common.retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterViewModel extends ViewModel {
    private RetrofitService retrofitService;
    private MutableLiveData<RetrofitResponse<String>> _registerRes = new MutableLiveData<>();
    public LiveData<RetrofitResponse<String>> registerRes = _registerRes;

    public RegisterViewModel() {
        retrofitService = RetrofitClient.getRetrofitService();
    }

    public void register(String username, String password){
        UserData userData = new UserData(username, password);
        retrofitService.userRegister(userData).enqueue(new Callback<RetrofitResponse<String>>() {
            @Override
            public void onResponse(@NonNull Call<RetrofitResponse<String>> call, @NonNull Response<RetrofitResponse<String>> response) {
                if (response.body() == null) return;
                _registerRes.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<RetrofitResponse<String>> call, @NonNull Throwable t) {

            }
        });
    }
}
