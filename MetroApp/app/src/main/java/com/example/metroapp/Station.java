package com.example.metroapp;

public class Station {
    private int _id;
    private String name ;
    private int iSCommon ;
    double latitude ,longitude;
    private int line_num;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getiSCommon() {
        return iSCommon;
    }

    public void setiSCommon(int iSCommon) {
        this.iSCommon = iSCommon;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getLine_num() {
        return line_num;
    }

    public void setLine_num(int line_num) {
        this.line_num = line_num;
    }
}
