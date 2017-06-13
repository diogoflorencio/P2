package com.example.diogo.discoverytrip.Fragments;

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
import com.example.diogo.discoverytrip.Util.ListAdapterOferta;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Classe fragment responsavel pelo fragmento inicial (home) na aplicação
 */

public class HomeFragment extends Fragment {

    private ListView listView;
    private Button searchOK;
    private EditText searchText;

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
                Oferta atracao = (Oferta) parent.getAdapter().getItem(position);
                DetalhesEventoFragment.atracao = atracao;

                HomeActivity activity = (HomeActivity) getActivity();
                activity.changeFragment(new DetalhesEventoFragment());

            }
        });
        searchText  = (EditText) rootView.findViewById(R.id.pnt_search_edt);
        searchOK = (Button) rootView.findViewById(R.id.pnt_search_ok_btn);
        searchOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                search();
                fillOfertas();
            }
        });
//        mokup();
        return rootView;
    }

    private void fillOfertas() {
        String marketId = "592d06c555d2ff000495efb5"; // FIXME precisa de refatoração
        ApiClient.API_SERVICE.ofertas(marketId).enqueue(new Callback<List<Oferta>>() {
            @Override
            public void onResponse(Call<List<Oferta>> call, Response<List<Oferta>> response) {
                listView.setAdapter(new ListAdapterOferta(getActivity(), response.body()));
            }

            @Override
            public void onFailure(Call<List<Oferta>> call, Throwable t) {
                Toast.makeText(getContext(), "Não foi possível encontrar as ofertas no momento.", Toast.LENGTH_SHORT).show();
                Log.e("GET ofertas", t.getMessage());
            }
        });
    }

    private void mokup(){
        List<Oferta> atracoes = new ArrayList<>();
        Oferta atracao = new Oferta("Teste","Só testando","Testando",null,null,10.9f,9f,null);

        atracoes.add(atracao);
        atracoes.add(atracao);
        atracoes.add(atracao);
        ListAdapterOferta adapter = new ListAdapterOferta(getActivity(),
                atracoes);
        listView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        Log.d("Logger", "LocalizacaoFragment onDestroy");
        super.onDestroy();
    }
}
