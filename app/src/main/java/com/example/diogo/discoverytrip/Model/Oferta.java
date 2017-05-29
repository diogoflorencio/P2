package com.example.diogo.discoverytrip.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by renato on 28/05/17.
 */

public class Oferta {

    @SerializedName("")
    private String supermercado;

    @SerializedName("")
    private String titulo;

    @SerializedName("")
    private String descricao;

    @SerializedName("")
    private String dataInicio;

    @SerializedName("")
    private String dataFim;

    @SerializedName("")
    private float precoNormal;

    @SerializedName("")
    private float precoOferta;

    @SerializedName("")
    private String fotoId;

    public Oferta(String titulo, String descricao, String supermercado, String dataInicio, String dataFim, float precoNormal, float precoOferta, String fotoId) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.precoNormal = precoNormal;
        this.precoOferta = precoOferta;
        this.fotoId = fotoId;
        this.supermercado = supermercado;
    }

    public String getSupermercado() {
        return supermercado;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public float getPrecoNormal() {
        return precoNormal;
    }

    public float getPrecoOferta() {
        return precoOferta;
    }

    public String getFotoId() {
        return fotoId;
    }
}
