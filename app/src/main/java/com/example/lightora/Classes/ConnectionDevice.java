package com.example.lightora.Classes;

import android.graphics.drawable.Drawable;

public class ConnectionDevice<getStatus> {
    String name = "Lightora", ssid, bssid;
    boolean status = false;
    Drawable connectionImg;


    public ConnectionDevice(String name, String ssid, String bssid, Drawable imageDrawable){
        this.name = name;
        this.ssid = ssid;
        this.bssid = bssid;
        this.connectionImg = imageDrawable;
    }

    public ConnectionDevice(String ssid, Drawable imageDrawable){
        this.name = name;
        this.ssid = ssid;
        this.connectionImg = imageDrawable;
    }


    public Drawable getConnectionImg() {
        return connectionImg;
    }

    public String getName() {
        return name;
    }

    public String getSsid() {
        return ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus(){
        return status;
    }
}
