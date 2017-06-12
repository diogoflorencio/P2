package com.example.diogo.discoverytrip.REST.ServerResponses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by renato on 11/06/17.
 */

public class MarketNetwork {

    @SerializedName("name")
    private String ssid;

    @SerializedName("password")
    private String password;

    public String getSsid() {
        return ssid;
    }

    public String getPassword() {
        return password;
    }
}
