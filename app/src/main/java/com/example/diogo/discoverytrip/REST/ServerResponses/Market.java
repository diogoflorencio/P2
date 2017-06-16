package com.example.diogo.discoverytrip.REST.ServerResponses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by renato on 11/06/17.
 */

public class Market {

    @SerializedName("id")
    private String id;

    @SerializedName("company")
    private String company;

    @SerializedName("manager")
    private String manager;

    @SerializedName("address")
    private Address address;

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

    public Address getAddress() {
        return address;
    }

    public MarketNetwork getNetwork() {
        return network;
    }
}
