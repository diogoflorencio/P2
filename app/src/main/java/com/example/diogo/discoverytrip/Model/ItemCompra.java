package com.example.diogo.discoverytrip.Model;

import java.io.Serializable;

/**
 * Created by renato on 28/05/17.
 */

public class ItemCompra implements Serializable{

    Produto produto;
    float quantidade;

    public ItemCompra(Produto produto, float quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public float getQuantidade() {
        return quantidade;
    }
}
