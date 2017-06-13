package com.example.diogo.discoverytrip.REST.ServerResponses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by renato on 11/06/17.
 */

public class ResponseMarketItems {

    @SerializedName("items")
    private List<Item> items;

    @SerializedName("status")
    private String status;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
