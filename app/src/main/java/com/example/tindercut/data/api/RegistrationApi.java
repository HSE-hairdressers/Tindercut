package com.example.tindercut.data.api;

import com.example.tindercut.data.Constants;
import com.example.tindercut.data.model.LoginSourceBody;
import com.example.tindercut.data.model.RegistrationSourceBody;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RegistrationApi {
    String url = Constants.registerURL;

    @Headers("Content-Type: application/json")
    @POST(url)
    Call<RegistrationResponse> createPost(@Body String body);
}
