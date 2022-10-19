package com.example.IntegratedDesign.login;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.common.bean.RetrofitResponse;
import com.example.common.bean.UserBean;
import com.example.common.retrofit.RetrofitClient;
import com.example.common.retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Response;


public class LoginViewModel extends ViewModel {
    private RetrofitService retrofitService;
    private MutableLiveData<RetrofitResponse<UserBean>> _loginRes = new MutableLiveData<>();
    public LiveData<RetrofitResponse<UserBean>> loginRes = _loginRes;

    public LoginViewModel(){
        retrofitService = RetrofitClient.getRetrofitService();
    }

    public void login(String username, String password) {
        retrofitService.userLogin(username, password).enqueue(new retrofit2.Callback<RetrofitResponse<UserBean>>() {
            @Override
            public void onResponse(@NonNull Call<RetrofitResponse<UserBean>> call, @NonNull Response<RetrofitResponse<UserBean>> response) {
                if (response.body() == null) return;
                _loginRes.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<RetrofitResponse<UserBean>> call, @NonNull Throwable t) {
                
            }
        });
    }

}
