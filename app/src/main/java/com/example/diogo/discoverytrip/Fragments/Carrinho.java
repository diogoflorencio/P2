package com.example.diogo.discoverytrip.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diogo.discoverytrip.Activities.HomeActivity;
import com.example.diogo.discoverytrip.Activities.LeitorCodigoBarrasActivity;
import com.example.diogo.discoverytrip.GPS.GPS;
import com.example.diogo.discoverytrip.GPS.GPSClient;
import com.example.diogo.discoverytrip.Model.ItemCompra;
import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.ServerResponses.ErrorResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.Market;
import com.example.diogo.discoverytrip.Util.ListAdapterItemCompra;
import com.example.diogo.discoverytrip.Util.Util;
import com.google.android.gms.ads.formats.NativeAd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link Carrinho#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Carrinho extends Fragment implements GPSClient, View.OnClickListener {

    private List<ItemCompra> produtosLidos;
    private ListView lVProdutos;
    private TextView tVTotal, marketView;
    private float total;
    private int distance = 100, getadas = 0;
    private String idMarket;
    private boolean geteiMarket = false;
    private MaterialDialog mMaterialDialog;
    private GPS gps;

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
        marketView = (TextView) view.findViewById(R.id.carrinho_nome_supermercado);
        total = 0f;
        gps = new GPS(getActivity());
        gps.addClient(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(),LeitorCodigoBarrasActivity.class);
        intent.putExtra("idMarket", idMarket);
        startActivityForResult(intent, 465);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != 465){
            return;
        }

        ItemCompra item = (ItemCompra) data.getExtras().get("Item");
        total += item.getQuantidade()*item.getProduto().getValorUn();

        produtosLidos.add(item);
        ListAdapterItemCompra adapter = new ListAdapterItemCompra(getActivity(), produtosLidos);
        lVProdutos.setAdapter(adapter);

        tVTotal.setText(String.valueOf(total));
    }

    @Override
    public void locationChange(Location location) {
        if(!geteiMarket && getadas < 3){
            geteiMarket = true;
            getadas++;
            getMarket(location);
        }
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Util.callDialogGPS("Para o uso deste serviço o GPS precisa estar ligado. " +
                "Deseja ligar agora?", getContext());
    }

    public void getMarket(Location location){
            Call<Market> call  = ApiClient.API_SERVICE.getMarketByLocation(location.getLatitude(),
                    location.getLongitude(),distance);
            call.enqueue(new Callback<Market>() {
                @Override
                public void onResponse(Call<Market> call, Response<Market> response) {
                    if(response.isSuccessful()){
                       setMarket(response.body());
                    }else {
                            geteiMarket = false;
                            if(getadas == 3) {
                                callDialogRepeatMarketRequest("Não consiguimos identificar que você está em " +
                                        "um supermecado. Deseja tentar novamente?",getContext());
                            }
                    }
                }

                @Override
                public void onFailure(Call<Market> call, Throwable t) {
                    geteiMarket = false;
                    if(getadas >= 3)
                        Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
    }

    private  void  setMarket(Market market){
        idMarket = market.getId();
        marketView.setText(market.getCompany());
    }

    public void callDialogRepeatMarketRequest(String message, final Context context){
        mMaterialDialog = new MaterialDialog(context)
                .setTitle("MarketRequest")
                .setMessage( message )
                .setPositiveButton("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getadas = 0;
                        mMaterialDialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HomeActivity home = (HomeActivity) getActivity();
                        home.changeFragment(new HomeFragment());
                        mMaterialDialog.dismiss();
                    }
                });
        mMaterialDialog.show();
    }
}
