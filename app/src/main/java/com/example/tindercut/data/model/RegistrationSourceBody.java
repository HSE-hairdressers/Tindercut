package com.example.tindercut.data.model;

import org.json.JSONObject;

/**
 * Supporting class for creating registration request body
 */
public class RegistrationSourceBody {

    private JSONObject object;

    public RegistrationSourceBody(JSONObject object) {
        this.object = object;
    }

    public JSONObject getObject() {
        return object;
    }

    public void setObject(JSONObject object) {
        this.object = object;
    }
}
