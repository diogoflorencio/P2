package com.example.diogo.discoverytrip.REST.ServerResponses;

import com.example.diogo.discoverytrip.Model.Produto;
import com.google.gson.annotations.SerializedName;

/**
 * Created by diogo on 17/06/17.
 */

public class ResponseProduct {
    @SerializedName("product")
    private Produto produto;

    public Produto getProduto() {
        return produto;
    }
}
