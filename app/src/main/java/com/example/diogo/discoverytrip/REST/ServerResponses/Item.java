package com.example.diogo.discoverytrip.REST.ServerResponses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by renato on 11/06/17.
 */

public class Item {

    @SerializedName("type")
    private String type;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("produto")
    private String produto;

    @SerializedName("price")
    private String price;

    @SerializedName("minAmount")
    private String minAmount;

    @SerializedName("startDate")
    private String startDate;

    @SerializedName("endDate")
    private String endDate;

    @SerializedName("image")
    private String imageId;

    @SerializedName("preminum")
    private String premium;

    @SerializedName("market")
    private String market;

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getProduto() {
        return produto;
    }

    public String getPrice() {
        return price;
    }

    public String getMinAmount() {
        return minAmount;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getImageId() {
        return imageId;
    }

    public String getPremium() {
        return premium;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }
}
