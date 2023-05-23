package com.example.tindercut.data.api;

import com.example.tindercut.data.Constants;
import com.example.tindercut.data.model.GetProfileInfoResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetProfileInfoApi {
    @GET(Constants.profileInfoUrl)
    Call<GetProfileInfoResponse> getProfileInfo(@Path(value = "id", encoded = true) String id);
}
