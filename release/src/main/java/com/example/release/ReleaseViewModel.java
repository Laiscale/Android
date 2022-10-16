package com.example.release;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.common.bean.ImageUploadData;
import com.example.common.bean.PictureBean;
import com.example.common.bean.RetrofitResponse;
import com.example.common.retrofit.RetrofitClient;
import com.example.common.retrofit.RetrofitService;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReleaseViewModel extends ViewModel {
    private RetrofitService retrofitService;
    private MutableLiveData<RetrofitResponse<String>> _saveRes = new MutableLiveData<>();
    public LiveData<RetrofitResponse<String>> saveRes = _saveRes;

    private MutableLiveData<RetrofitResponse<String>> _shareRes = new MutableLiveData<>();
    public LiveData<RetrofitResponse<String>> shareRes = _shareRes;

    private MutableLiveData<RetrofitResponse<ImageUploadData>> _uploadData = new MutableLiveData<>();
    public LiveData<RetrofitResponse<ImageUploadData>> uploadData = _uploadData;

    public ReleaseViewModel(){
        retrofitService = RetrofitClient.getRetrofitService();
    }

    public void sharePic(String content, String title, long imageCode, long pUserId){
        PictureBean pictureBean = new PictureBean(content, imageCode, pUserId, title);
        retrofitService.addSharePicture(pictureBean).enqueue(new Callback<RetrofitResponse<String>>() {
            @Override
            public void onResponse(@NonNull Call<RetrofitResponse<String>> call, @NonNull Response<RetrofitResponse<String>> response) {
                _shareRes.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<RetrofitResponse<String>> call, @NonNull Throwable t) {

            }
        });
    }

    public void savePic(String content, String title, long imageCode, long pUserId){
        PictureBean pictureBean = new PictureBean(content, imageCode, pUserId, title);
        retrofitService.addSavePicture(pictureBean).enqueue(new Callback<RetrofitResponse<String>>() {
            @Override
            public void onResponse(@NonNull Call<RetrofitResponse<String>> call, @NonNull Response<RetrofitResponse<String>> response) {
                _saveRes.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<RetrofitResponse<String>> call, @NonNull Throwable t) {

            }
        });
    }

    public void uploadImage(List<String> imagePathList){
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (int i = 0; i < imagePathList.size() - 1; i++){
            File file = new File(imagePathList.get(i));
            RequestBody requestBody =RequestBody.create(MediaType.parse("application/json; charset=utf-8"), file);
            builder.addFormDataPart("fileList", file.getName(), requestBody);
        }
        retrofitService.uploadFile(builder.build()).enqueue(new Callback<RetrofitResponse<ImageUploadData>>() {
            @Override
            public void onResponse(@NonNull Call<RetrofitResponse<ImageUploadData>> call, @NonNull Response<RetrofitResponse<ImageUploadData>> response) {
                if (response.body() == null) return;
                _uploadData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<RetrofitResponse<ImageUploadData>> call, @NonNull Throwable t) {

            }
        });
    }
}
