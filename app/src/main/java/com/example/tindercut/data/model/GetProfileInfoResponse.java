package com.example.tindercut.data.model;

import com.google.gson.annotations.SerializedName;

public class GetProfileInfoResponse {

    @SerializedName("pic")
    private String pic;

    @SerializedName("name")
    private String name;

    @SerializedName("company")
    private String company;

    @SerializedName("addr")
    private String address;

    @SerializedName("num")
    private String phone;

    public String getPic() {
        return pic;
    }

    public GetProfileInfoResponse(String pic, String name, String company, String address, String phone) {
        this.pic = pic;
        this.name = name;
        this.company = company;
        this.address = address;
        this.phone = phone;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
