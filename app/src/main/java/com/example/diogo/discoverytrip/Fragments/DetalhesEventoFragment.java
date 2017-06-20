package com.example.diogo.discoverytrip.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diogo.discoverytrip.Model.Oferta;
import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.REST.ServerResponses.Item;
import com.example.diogo.discoverytrip.Util.ListAdapterOferta;

import java.text.ParseException;
import java.text.SimpleDateFormat;


public class DetalhesEventoFragment extends Fragment{

    public static Item atracao;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
    private static SimpleDateFormat normalDateFormat = new SimpleDateFormat("dd/M/yyyy HH:mm");

    private TextView titulo, descricao, endereco, tipo, inicio, fim, preco;
    private ImageView foto;
    
    public DetalhesEventoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalhes_evento, container, false);

        titulo = (TextView) view.findViewById(R.id.detalhe_evento_titulo);
        descricao = (TextView) view.findViewById(R.id.detalhe_evento_descricao);
        endereco = (TextView) view.findViewById(R.id.detalhe_evento_endereco);
        tipo = (TextView) view.findViewById(R.id.detalhe_evento_categoria);
        inicio = (TextView) view.findViewById(R.id.detalhe_evento_inicio);
        fim = (TextView) view.findViewById(R.id.detalhe_evento_fim);
        preco = (TextView) view.findViewById(R.id.detalhe_evento_preco);
        foto = (ImageView) view.findViewById(R.id.detalhe_evento_img);

        titulo.setText(atracao.getName());
        descricao.setText(atracao.getDescription());

        if(atracao.getImageId() != null && !atracao.getImageId().equals("")){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ListAdapterOferta.loadImage(foto,atracao.getImageId(),getContext());
                }
            }).start();
        }

        return view;
    }
}