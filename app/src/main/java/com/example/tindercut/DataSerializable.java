package com.example.tindercut;

import java.io.Serializable;

/**
 * Supporting class, which implements serializable interface
 * Used for passing data between SearchPhotoFragment and ScrollingActivity
 */
public class DataSerializable implements Serializable {
    String Data;

    public DataSerializable(String Data) {
        super();
        this.Data = Data;
    }

    public String getData() {
        return this.Data;
    }

    public void setData(String Data) {
        this.Data = Data;
    }
}
