package com.example.diogo.discoverytrip.REST.ServerResponses;

import com.google.gson.annotations.SerializedName;

import org.w3c.dom.ls.LSException;

import java.util.List;

/**
 * Created by renato on 11/06/17.
 */

public class ResponseGetItems {

    @SerializedName("items")
    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }
}
