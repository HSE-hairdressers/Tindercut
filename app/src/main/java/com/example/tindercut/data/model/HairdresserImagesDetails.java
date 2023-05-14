package com.example.tindercut.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Supporting class for hairdresser profile pics pasing
 */
public class HairdresserImagesDetails implements Serializable {
    @SerializedName("img_path")
    String img_path;

    public HairdresserImagesDetails(String img_path) {
        this.img_path = img_path;
    }


    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }
}
