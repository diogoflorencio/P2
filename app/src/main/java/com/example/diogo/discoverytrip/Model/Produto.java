package com.example.diogo.discoverytrip.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by renato on 28/05/17.
 */

public class Produto implements Serializable{

    @SerializedName("")
    private float valorUn;

    @SerializedName("")
    private String unidade;

    @SerializedName("")
    private String descricao;

    @SerializedName("")
    private String codigoBarras;

    public Produto(String codigoBarras, String descricao, float valorUn, String unidade){
        this.descricao = descricao;
        this.valorUn = valorUn;
        this.unidade = unidade;
        this.codigoBarras = codigoBarras;
    }

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
}
