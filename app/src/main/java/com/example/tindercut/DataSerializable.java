package com.example.tindercut;

import java.io.Serializable;

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
