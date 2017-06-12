package com.example.diogo.discoverytrip.REST.ServerResponses;

import com.example.diogo.discoverytrip.Model.Market;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by gervasio on 6/12/17.
 */

public class MarketResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String success;

    @SerializedName("markets")
    private List<Market> markets;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<Market> getMarkets() {
        return markets;
    }

    public void setMarkets(List<Market> markets) {
        this.markets = markets;
    }
}
