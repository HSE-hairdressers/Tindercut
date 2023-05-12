package com.example.tindercut.data.api;

import com.example.tindercut.data.Constants;
import com.example.tindercut.data.model.LoginSourceBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CheckLoginInfoApi {

    String url = Constants.loginURL;

    @POST(url)
    Call<CheckLoginResponse> createPost(@Body LoginSourceBody body);

}
