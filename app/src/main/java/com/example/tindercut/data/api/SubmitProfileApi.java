package com.example.tindercut.data.api;

import com.example.tindercut.data.Constants;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface SubmitProfileApi {

    @Headers("Content-Type: application/json")
    @PATCH(Constants.profileEditUrl)
    Call<ResponseBody> sendInfo(@Path(value = "id", encoded = true) String id, @Body String body);
}
