package com.example.diogo.discoverytrip.Util;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.diogo.discoverytrip.Model.ItemCompra;
import com.example.diogo.discoverytrip.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by renato on 07/02/17.
 */
public class ListAdapterItemCompra extends ArrayAdapter<ItemCompra>{
    private List<View> views;
    private LayoutInflater inflater;
    private List<ItemCompra> itens;

    public ListAdapterItemCompra(Activity context, List<ItemCompra> itens){
        super(context, R.layout.item_oferta,itens);

        this.inflater = context.getLayoutInflater();
        this.itens = itens;
        this.views = new ArrayList<>();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        if(views.size() > position){
            return views.get(position);
        }

        Log.d("Logger","getView "+position);
        final ItemCompra item = itens.get(position);
        View view = inflater.inflate(R.layout.item_produto, null, true);

        TextView codItem  = (TextView) view.findViewById(R.id.item_produto_cod_item);
        TextView descricao  = (TextView) view.findViewById(R.id.item_produto_descricao);
        TextView quantidade  = (TextView) view.findViewById(R.id.item_produto_qnt);
        TextView unidade  = (TextView) view.findViewById(R.id.item_produto_unidade);
        TextView valorUn  = (TextView) view.findViewById(R.id.item_produto_valorUn);
        TextView subtotal  = (TextView) view.findViewById(R.id.item_produto_subtotal);
        float realSubTotal = item.getQuantidade()*item.getProduto().getValorUn();

        codItem.setText(String.valueOf(position));
        descricao.setText(item.getProduto().getDescricao());
        quantidade.setText(String.valueOf(item.getQuantidade()));
        unidade.setText(item.getProduto().getUnidade());
        valorUn.setText(String.valueOf(item.getProduto().getValorUn()));
        subtotal.setText(String.valueOf(realSubTotal));

        views.add(view);
        return view;
    }

    @Override
    public ItemCompra getItem(int position){
        return itens.get(position);
    }
}