package com.example.diogo.discoverytrip.REST.ServerResponses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by renato on 11/06/17.
 */

public class Market {

    @SerializedName("_id")
    private String id;

    @SerializedName("company")
    private String company;

    @SerializedName("manager")
    private String manager;

    @SerializedName("address")
    private List<Address> address;

    @SerializedName("network")
    private MarketNetwork network;

    public String getId() {
        return id;
    }

    public String getCompany() {
        return company;
    }

    public String getManager() {
        return manager;
    }

    public List<Address> getAddress() {
        return address;
    }

    public MarketNetwork getNetwork() {
        return network;
    }
}
