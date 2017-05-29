package com.example.diogo.discoverytrip.Model;

/**
 * Created by renato on 28/05/17.
 */

public class ItemCompra {

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
