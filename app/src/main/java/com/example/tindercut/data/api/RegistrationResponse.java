package com.example.tindercut.data.api;

import com.google.gson.annotations.SerializedName;

public class RegistrationResponse {
    @SerializedName("response")
    String name;

    public RegistrationResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
