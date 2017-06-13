package com.example.diogo.discoverytrip.REST.ServerResponses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by renato on 11/06/17.
 */

public class ResponseAllMarkets {

    @SerializedName("markets")
    private List<Market> markets;

    public List<Market> getMarkets() {
        return markets;
    }
}
