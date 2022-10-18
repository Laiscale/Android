package com.example.common.retrofit;

import com.example.common.bean.FirstCommentBean;
import com.example.common.bean.CommentData;
import com.example.common.bean.ImageUploadData;
import com.example.common.bean.PictureData;
import com.example.common.bean.PictureBean;
import com.example.common.bean.ResponseData;
import com.example.common.bean.RetrofitResponse;
import com.example.common.bean.SecondCommentBean;
import com.example.common.bean.UserBean;
import com.example.common.bean.UserData;
import com.example.common.bean.UserUpdateData;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface RetrofitService {
    @GET("photo/collect")
    Call<RetrofitResponse<ResponseData<PictureData>>> getPictureListOfCurrentFavorite(@Query(value = "current") int current, @Query(value = "size") int size, @Query(value = "userId") long userId);

    @POST("photo/collect")
    Call<RetrofitResponse<String>> favoritePicture(@Query(value = "shareId") long shareId, @Query(value = "userId") long userId);

    @POST("photo/collect/cancel")
    Call<RetrofitResponse<String>> unFavoritePicture(@Query(value = "collectId") long collectId);

    @GET("photo/comment/first")
    Call<RetrofitResponse<ResponseData<CommentData>>> getFirstCommentList(@Query(value = "current") int current, @Query(value = "shareId") long shareId, @Query(value = "size") int size);

    @POST("photo/comment/first")
    Call<RetrofitResponse<String>> addFirstComment(@Body FirstCommentBean firstCommentBean);

    @GET("photo/comment/second")
    Call<RetrofitResponse<ResponseData<CommentData>>> getSecondCommentList(@Query(value = "commentId") int commentId,@Query(value = "current") int current, @Query(value = "shareId") int shareId, @Query(value = "size") int size);

    @POST("photo/comment/second")
    Call<RetrofitResponse<String>> addSecondComment(@Body SecondCommentBean secondCommentBean);

    @GET("photo/focus")
    Call<RetrofitResponse<ResponseData<PictureData>>> getPictureListOfCurrentSubscribed(@Query(value = "current") int current, @Query(value = "size") int size, @Query(value = "userId") long userId);

    @POST("photo/focus")
    Call<RetrofitResponse<String>> subscribedUser(@Query(value = "focusUserId") long focusUserId, @Query(value = "userId") long userId);

    @POST("photo/focus/cancel")
    Call<RetrofitResponse<String>> unSubscribedUser(@Query(value = "focusUserId") long focusUserId, @Query(value = "userId") long userId);

    @POST("photo/image/upload")
    @Multipart
    Call<RetrofitResponse<ImageUploadData>> uploadFile(@Part List<MultipartBody.Part> fileList);

    @GET("photo/like")
    Call<RetrofitResponse<ResponseData<PictureData>>> getPictureListOfCurrentLike(@Query(value = "current") int current, @Query(value = "size") int size, @Query(value = "userId") long userId);

    @POST("photo/like")
    Call<RetrofitResponse<String>> likePicture(@Query(value = "shareId") long shareId, @Query(value = "userId") long userId);

    @POST("photo/like/cancel")
    Call<RetrofitResponse<String>> disLikePicture(@Query(value = "likeId") long likeId);

    @GET("photo/share")
    Call<RetrofitResponse<ResponseData<PictureData>>> getPictureListOfShare(@Query(value = "current") int current, @Query(value = "size") int size, @Query(value = "userId") long userId);

    @POST("photo/share/add")
    Call<RetrofitResponse<String>> addSharePicture(@Body PictureBean pictureBean);

    @POST("photo/share/change")
    Call<RetrofitResponse<String>> changePictureToShare(@Body PictureBean pictureBean);

    @POST("photo/share/delete")
    Call<RetrofitResponse<String>> deletePicture(@Query(value = "shareId") long shareId, @Query(value = "userId") long userId);

    @GET("photo/share/detail")
    Call<RetrofitResponse<PictureData>> getPictureDetail(@Query(value = "shareId") long shareId, @Query(value = "userId") long userId);

    @GET("photo/share/myself")
    Call<RetrofitResponse<ResponseData<PictureData>>> getPictureListOfCurrentShare(@Query(value = "current") int current, @Query(value = "size") int size, @Query(value = "userId") long userId);

    @GET("photo/share/save")
    Call<RetrofitResponse<ResponseData<PictureData>>> getPictureListOfCurrentSave(@Query(value = "current") int current, @Query(value = "size") int size, @Query(value = "userId") long userId);

    @POST("photo/share/save")
    Call<RetrofitResponse<String>> addSavePicture(@Body PictureBean pictureBean);

    @POST("photo/user/login")
    Call<RetrofitResponse<UserBean>> userLogin(@Query(value = "username") String username, @Query(value = "password") String password);

    @POST("photo/user/register")
    Call<RetrofitResponse<String>> userRegister(@Body UserData userData);

    @POST("photo/user/update")
    Call<RetrofitResponse<String>> userUpdate(@Body UserUpdateData userUpdateData);
}
