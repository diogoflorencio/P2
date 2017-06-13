package com.example.diogo.discoverytrip.REST.ServerResponses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by renato on 11/06/17.
 */

public class Address {

    @SerializedName("street")
    private String street;

    @SerializedName("number")
    private String number;

    @SerializedName("postalCode")
    private String postalCode;

    @SerializedName("city")
    private String city;

    @SerializedName("province")
    private String province;

    @SerializedName("country")
    private String country;

    @SerializedName("coordinates")
    private Coordinates coordenates;

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public String getCountry() {
        return country;
    }

    public Coordinates getCoordenates() {
        return coordenates;
    }
}
