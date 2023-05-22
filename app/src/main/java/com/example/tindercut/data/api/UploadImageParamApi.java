package com.example.tindercut.data.api;

import com.example.tindercut.data.Constants;
import com.example.tindercut.data.model.SearchImageResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Api for sending image with params to server
 */
public interface UploadImageParamApi {
    final String url = Constants.uploadUrl;

    @Multipart
    @POST(url)
    //UploadResponse response (@Body ImageUploadBody imageUploadBody);
    Call<ResponseBody> uploadImage(
            @Part MultipartBody.Part image,
            @Part("id") RequestBody id
            );

}

