package com.example.diogo.discoverytrip.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by renato on 28/05/17.
 */

public class Produto implements Serializable{

    @SerializedName("price")
    private float valorUn;

    @SerializedName("unit")
    private String unidade;

    @SerializedName("name")
    private  String nome;

    @SerializedName("description")
    private String descricao;

    @SerializedName("barCode")
    private String codigoBarras;

    @SerializedName("marketId")
    private String idMarket;

    public float getValorUn() {
        return valorUn;
    }

    public String getUnidade() {
        return unidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public String getNome() {
        return nome;
    }

    public String getIdMarket() {
        return idMarket;
    }
}
