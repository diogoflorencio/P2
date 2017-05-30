package com.example.diogo.discoverytrip.Fragments;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
import com.example.diogo.discoverytrip.DataBase.AcessToken;
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
public class HomeFragment extends Fragment implements LocationListener {
    private LocationManager locationManager;
    private static final int REQUEST_LOCATION = 2;
    private double latitude, longitude;
    private ListView listView;
    private boolean get = true;
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
        startGPS();
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
//        searchOK.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                search();
//            }
//        });

//        mokup();
        fillOfertas();
        return rootView;
    }

    private void fillOfertas() {
        String token = AcessToken.recuperar(getActivity().getSharedPreferences("acessToken", Context.MODE_PRIVATE));
        ApiClient.API_SERVICE.ofertas("Bearer " + token).enqueue(new Callback<List<Oferta>>() {
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

    private void startGPS() {
        Log.d("Logger", "LocalizacaoFragment startGPS");
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        //permissão de GPS
        if (ActivityCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) ;
                // Esperando usuário autorizar permissão
            else
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
        } else
        if(verificaConexao())
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) this);
        else
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        if(get){
//            Log.d("Logger","Location search latitude: "+latitude+" longitude: "+longitude);
//            get = false;
//            String token = AcessToken.recuperar(getActivity().getSharedPreferences("acessToken", Context.MODE_PRIVATE));
//            Call<SearchResponse> call = ApiClient.API_SERVICE.searchPontoTuristico("bearer "+token,latitude, longitude,2000);
//            call.enqueue(new Callback<SearchResponse>() {
//                @Override
//                public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
//                    if(response.isSuccessful()){
//
//                        Log.d("Logger","pesquisa de atrações realizada com sucesso");
//                        List<Atracao> atracoes = response.body().getAtracoes();
//                        if(atracoes != null){
//                            Log.d("Logger","Setting listview adapter");
//                            ListAdapterItemCompra adapter = new ListAdapterItemCompra(getActivity(),
//                                    atracoes);
//                            for (Atracao atrac:atracoes
//                                 ) {
//                                Log.d("Logger",atrac.getName());
//                                Log.d("Logger","Category "+atrac.getCategory());
//                            }
//                            listView.setAdapter(adapter);
//                        }
//                    }
//                    else{
//                        try {
//                            ErrorResponse error = ApiClient.errorBodyConverter.convert(response.errorBody());
//                            Log.e("Logger", "Pesquisa de atrações "+error.getErrorDescription());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<SearchResponse> call, Throwable t) {
//                    get = true;
//                    Log.e("Logger","Pesquisa de pontos turisticos error: "+t.toString());
//                    Toast.makeText(getActivity(),"Erro ao se conectar com o servidor!",Toast.LENGTH_SHORT).show();
//                }
//            });
        }
    }

    private boolean verificaConexao() {
        //verificando se o dispositivo tem conexão com internet
        ConnectivityManager conectivtyManager =
                (ConnectivityManager) this.getActivity().getSystemService(this.getActivity().CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected())
            return true;
        else
            return false;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onDestroy() {
        Log.d("Logger", "LocalizacaoFragment onDestroy");
        super.onDestroy();
        get = true;
        stopGPS();
    }

    private void stopGPS(){
        //permissão de GPS
        if (ActivityCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) ;
                // Esperando usuário autorizar permissão
            else
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
        } else locationManager.removeUpdates(this);
    }

//    private void search(){
//        String token = AcessToken.recuperar(getActivity().getSharedPreferences("acessToken", Context.MODE_PRIVATE));
//        Call<SearchResponse> call = ApiClient.API_SERVICE.search("bearer "+token,searchText.getText().toString());
//        call.enqueue(new Callback<SearchResponse>() {
//            @Override
//            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
//                if(response.isSuccessful()){
//                    List<Atracao> atracoes = response.body().getAtracoes();
//                    if(atracoes != null){
//                        Log.d("Logger","Setting listview adapter");
//                        ListAdapterItemCompra adapter = new ListAdapterItemCompra(getActivity(),
//                                atracoes);
//                        listView.setAdapter(adapter);
//                    }else
//                        Toast.makeText(getActivity(),"Sem resultados para pesquisa",Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SearchResponse> call, Throwable t) {
//                Toast.makeText(getActivity(),"Erro ao se conectar com o servidor!",Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
