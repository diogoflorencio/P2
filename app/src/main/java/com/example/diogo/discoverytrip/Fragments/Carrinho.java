package com.example.diogo.discoverytrip.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.diogo.discoverytrip.Activities.LeitorCodigoBarrasActivity;
import com.example.diogo.discoverytrip.Model.ItemCompra;
import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.Util.ListAdapterItemCompra;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link Carrinho#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Carrinho extends Fragment implements View.OnClickListener {

    private List<ItemCompra> produtosLidos;
    private ListView lVProdutos;
    private TextView tVTotal;
    private float total;

    public Carrinho() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Carrinho.
     */
    // TODO: Rename and change types and number of parameters
    public static Carrinho newInstance(String param1, String param2) {
        Carrinho fragment = new Carrinho();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_carrinho, container, false);
        FloatingActionButton btnAdd = (FloatingActionButton) view.findViewById(R.id.carrinho_addItem);
        btnAdd.setOnClickListener(this);

        produtosLidos = new ArrayList<>();
        lVProdutos = (ListView) view.findViewById(R.id.carrinho_list_produtos);
        tVTotal =(TextView) view.findViewById(R.id.carrinho_total);
        total = 0f;

        return view;
    }

    @Override
    public void onClick(View v) {
        startActivityForResult(new Intent(getContext(),LeitorCodigoBarrasActivity.class), 465);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ItemCompra item = (ItemCompra) data.getExtras().get("Item");
        total += item.getQuantidade()*item.getProduto().getValorUn();

        produtosLidos.add(item);
        ListAdapterItemCompra adapter = new ListAdapterItemCompra(getActivity(), produtosLidos);
        lVProdutos.setAdapter(adapter);

        tVTotal.setText(String.valueOf(total));
    }
}
