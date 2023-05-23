package com.example.tindercut.data.api;

import com.example.tindercut.data.Constants;
import com.example.tindercut.data.model.GetProfileInfoResponse;
import com.example.tindercut.data.model.UserImagesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetUserImagesApi {

    @GET(Constants.profileImgUrl)
    Call<UserImagesResponse> getProfileImages(@Path(value = "id", encoded = true) String id);

}
