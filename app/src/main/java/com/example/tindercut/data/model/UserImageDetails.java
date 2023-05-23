package com.example.tindercut.data.model;

import com.google.gson.annotations.SerializedName;

public class UserImageDetails {

    @SerializedName("img_path")
    String img;

    public UserImageDetails(String img) {
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
