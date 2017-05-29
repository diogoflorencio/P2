package com.example.diogo.discoverytrip.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by renato on 28/05/17.
 */

public class Produto {

    @SerializedName("")
    private float valorUn;

    @SerializedName("")
    private String unidade;

    @SerializedName("")
    private String descricao;

    public Produto(String descricao, float valorUn, String unidade){
        this.descricao = descricao;
        this.valorUn = valorUn;
        this.unidade = unidade;

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
}
