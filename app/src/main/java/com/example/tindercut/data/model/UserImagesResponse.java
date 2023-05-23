package com.example.tindercut.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserImagesResponse {

    @SerializedName("images")
    ArrayList<UserImageDetails> images;

    public UserImagesResponse(ArrayList<UserImageDetails> images) {
        this.images = images;
    }

    public ArrayList<UserImageDetails> getImages() {
        return images;
    }

    public void setImages(ArrayList<UserImageDetails> images) {
        this.images = images;
    }
}
