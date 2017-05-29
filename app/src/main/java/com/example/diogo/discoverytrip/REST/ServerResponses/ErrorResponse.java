package com.example.diogo.discoverytrip.REST.ServerResponses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by renato on 28/05/17.
 */

public class ErrorResponse {

    @SerializedName("error")
    private String error;

    @SerializedName("error_description")
    private String errorDescription;

    public String getError() {
        return error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }
}
