package com.example.tindercut.data.api;

import com.example.tindercut.data.Constants;
import com.example.tindercut.data.model.SearchImageData;
import com.example.tindercut.data.model.SearchImageResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Api for sending image to server
 */
public interface UploadImageApi {
    final String url = Constants.imgUrl;

    @Multipart
    @POST(url)
    //UploadResponse response (@Body ImageUploadBody imageUploadBody);
    Call<SearchImageResponse> uploadImage(
            @Part MultipartBody.Part image
            );

}

