package com.example.diogo.discoverytrip.Fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.diogo.discoverytrip.Activities.HomeActivity;
import com.example.diogo.discoverytrip.Model.Oferta;
import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.ServerResponses.ErrorResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.Item;
import com.example.diogo.discoverytrip.REST.ServerResponses.Market;
import com.example.diogo.discoverytrip.REST.ServerResponses.ResponseGetItems;
import com.example.diogo.discoverytrip.Util.ListAdapterOferta;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Classe fragment responsavel pelo fragmento inicial (home) na aplicação
 */

public class HomeFragment extends Fragment {

    private ListView listView;
    private Button searchOK;
    private EditText searchText;
    private static DateFormat dateFormat =  new SimpleDateFormat("dd/MM/yyyy");

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Metodo responsavel por gerenciar a criacao de um objeto 'HomeFragment'
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Logger", "HomeFragment onCreate");
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle(R.string.home_label);
        listView = (ListView) rootView.findViewById(R.id.fragment_home_list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item atracao = (Item) parent.getAdapter().getItem(position);
                DetalhesEventoFragment.atracao = atracao;

                HomeActivity activity = (HomeActivity) getActivity();
                activity.changeFragment(new DetalhesEventoFragment());
            }
        });
        searchText  = (EditText) rootView.findViewById(R.id.pnt_search_edt);
        searchOK = (Button) rootView.findViewById(R.id.pnt_search_ok_btn);
//        searchOK.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                search();
//            }
//        });
        getItems();
        return rootView;
    }

    public void getItems(){
        Call<ResponseGetItems> call  = ApiClient.API_SERVICE.getDateItems(dateFormat.format(new Date()));
        call.enqueue(new Callback<ResponseGetItems>() {
            @Override
            public void onResponse(Call<ResponseGetItems> call, Response<ResponseGetItems> response) {
                if(response.isSuccessful()){
                    ResponseGetItems responseGetItems = response.body();
                    List<Item> items = responseGetItems.getItems();
                    long seed = System.nanoTime();
                    Collections.shuffle(items, new Random(seed));
                    ListAdapterOferta adapter = new ListAdapterOferta(getActivity(), items);
                    listView.setAdapter(adapter);
                }else {
                    try {
                        ErrorResponse errorResponse = ApiClient.errorBodyConverter.convert(response.errorBody());
                        Toast.makeText(getContext(), errorResponse.getErrorDescription(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseGetItems> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        Log.d("Logger", "LocalizacaoFragment onDestroy");
        super.onDestroy();
    }
}
