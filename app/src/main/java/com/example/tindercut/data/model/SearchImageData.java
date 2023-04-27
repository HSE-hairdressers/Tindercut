package com.example.tindercut.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchImageData implements Serializable {

    @SerializedName("hairdresser")
    HairdresserDetailBody hairdresser;

    @SerializedName("images")
    ArrayList<HairdresserImagesDetails> images;

    public SearchImageData(HairdresserDetailBody hairdresser, ArrayList<HairdresserImagesDetails> images) {

        super();
        this.hairdresser = hairdresser;
        this.images = images;
    }


    public HairdresserDetailBody getHairdresser() {
        return hairdresser;
    }

    public void setHairdresser(HairdresserDetailBody hairdresser) {
        this.hairdresser = hairdresser;
    }

    public ArrayList<HairdresserImagesDetails> getImages() {
        return images;
    }

    public void setImages(ArrayList<HairdresserImagesDetails> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "ImageUploadBody{" +
                ", hairdresser=" + hairdresser +
                ", images=" + images +
                '}';
    }
}
