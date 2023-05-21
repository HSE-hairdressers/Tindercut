package com.example.tindercut.data.api;

import com.example.tindercut.data.model.SearchImageData;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Supporting class for pasing check login response
 */
public class CheckLoginResponse {
    @SerializedName("name")
    String name;

    @SerializedName("id")
    Long id;

    public CheckLoginResponse(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
