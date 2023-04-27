package com.example.tindercut.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchImageResponse {

    @SerializedName("data")
    ArrayList <SearchImageData> searchImageData;

    public ArrayList<SearchImageData> getSearchImageData() {
        return searchImageData;
    }

    public void setSearchImageData(ArrayList<SearchImageData> searchImageData) {
        this.searchImageData = searchImageData;
    }

    public SearchImageResponse(ArrayList<SearchImageData> searchImageData) {
        this.searchImageData = searchImageData;
    }
}
