package com.example.tindercut.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Supporting class for parsing hairdresser profile response
 */
public class HairdresserDetailBody implements Serializable {

    @SerializedName("name")
    private String name;
    @SerializedName("num")
    private String num;
    @SerializedName("addr")
    private String addr;
    @SerializedName("company")
    private String company;
    @SerializedName("pic")
    private String pic;

    @SerializedName("id")
    private String id;


    public HairdresserDetailBody(String name, String num, String addr, String company, String id) {
        super();
        this.name = name;
        this.num = num;
        this.addr = addr;
        this.company = company;
        this.pic = pic;
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
