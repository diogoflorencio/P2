package com.example.diogo.discoverytrip.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diogo.discoverytrip.Fragments.Carrinho;
import com.example.diogo.discoverytrip.Fragments.HomeFragment;
import com.example.diogo.discoverytrip.GPS.GPS;
import com.example.diogo.discoverytrip.GPS.GPSClient;
import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.ServerResponses.ErrorResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.Market;
import com.example.diogo.discoverytrip.REST.ServerResponses.ResponseAllMarkets;
import com.example.diogo.discoverytrip.Util.WIFIManager;

import java.io.IOException;

import me.drakeet.materialdialog.MaterialDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Classe activity responsavel pela activity home (principal) na aplicação
 */
public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GPSClient {

//    public static final String EVENT_TYPE = "Event";
    private int currentScreen = 0;
    private NavigationView navigationView;
    public static final int REQUEST_PERMISSIONS_CODE = 128;

    private MaterialDialog mMaterialDialog;
    private boolean get = true;
    private Location location;

    /**
     * Metodo responsavel por gerenciar a criacao de um objeto 'HomeActivity'
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Logger", "Home onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        createHomeFragment();
        permission();
        GPS.instance = new GPS(this);
        GPS.instance.addClient(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d("Logger", "Home onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

        @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("Logger", "Home onOptionsItemSelected");
        int id = item.getItemId();
        switch (id) {
            case R.id.exit:
                Log.d("Logger", "Home logout");
                WIFIManager wf = new WIFIManager(this.getApplication());
                Log.d("Logger", "isConnected() "+wf.isConnected());
//                wf.enableWifi();
//                wf.requestWIFIConnection("+_+","mini@casadebaixo");

                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //pra adicionar uma opção aqui (menu lateral), basta colocar um item no "activity_home_drawer", botar um case nesse switch
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        Log.d("Logger", "Home onNavigationItemSelected");
        int id = item.getItemId();
        Fragment fragment = null;
        switch (id) {
            case R.id.nav_home:
                Log.d("Logger", "Home localizacao");
                fragment = new HomeFragment();
                currentScreen = R.id.nav_home;
                break;
            case R.id.nav_comprar:
                Log.d("Logger", "Carrinho");
                fragment = new Carrinho();
                LeitorCodigoBarrasActivity.width = Resources.getSystem().getDisplayMetrics().widthPixels;
                LeitorCodigoBarrasActivity.heigth = Resources.getSystem().getDisplayMetrics().heightPixels;
                break;
            case R.id.nav_leitorBarcode:
                Log.d("Logger", "Leitor código de barras");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View view = getLayoutInflater().inflate(R.layout.dialog_aguarde,null,true);
                TextView status = (TextView) view.findViewById(R.id.dialog_aguarde_txtStatus);
                status.setText("Buscando mercados");
                builder.setView(view);
                builder.setCancelable(false);
                Dialog waitDialog = builder.create();
                waitDialog.show();
                getMarket(location,waitDialog);
                break;
        }

        if (fragment != null) {
            changeFragment(fragment);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void createHomeFragment() {
        Log.d("Logger", "Home createHomeFragment");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        HomeFragment fragment = new HomeFragment();
        transaction.add(R.id.content_home, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        Log.d("Logger", "Home onBackPressed");

        if(currentScreen != R.id.nav_home){
            changeFragment(new HomeFragment());
            currentScreen = R.id.nav_home;
            return;
        }
        finish();
    }

    public void changeFragment(Fragment fragment){
        if(fragment instanceof HomeFragment){
            currentScreen = R.id.nav_home;
        }
        else {
            currentScreen = 0;
        }
        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            fragment.setArguments(extras);
        }
        //fragmentManager.setCustomAnimations(R.anim.left_in, R.anim.right_out);
        fragmentManager.replace(R.id.content_home, fragment);
        fragmentManager.addToBackStack(null);
        fragmentManager.commit();
    }

    private void permission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(HomeActivity.this,  new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSIONS_CODE);
        }

    }

    private void callDialog( String message, final String[] permissions ){
        mMaterialDialog = new MaterialDialog(this)
                .setTitle("Permission")
                .setMessage( message )
                .setPositiveButton("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ActivityCompat.requestPermissions(HomeActivity.this, permissions, REQUEST_PERMISSIONS_CODE);
                        mMaterialDialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
        mMaterialDialog.show();
    }

    public void getMarket(Location location, final Dialog waitDialog){
        Call<ResponseAllMarkets> call  = ApiClient.API_SERVICE.getMarketByLocation(location.getLatitude(),
                location.getLongitude(),500);
        call.enqueue(new Callback<ResponseAllMarkets>() {
            @Override
            public void onResponse(Call<ResponseAllMarkets> call, Response<ResponseAllMarkets> response) {

                if(response.isSuccessful()){
                    waitDialog.dismiss();
                    Intent intent = new Intent(HomeActivity.this,ConsultorDePreco.class);
                    intent.putExtra("idMarket", response.body().getMarkets().get(0).getId());
                    startActivity(intent);
                }else {
                    Toast.makeText(HomeActivity.this, "Não foi possível localizar um supermercado na região", Toast.LENGTH_SHORT).show();
                    waitDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseAllMarkets> call, Throwable t) {
                Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                waitDialog.dismiss();
                Log.e("LoggerGetFail", t.getMessage());
            }
        });
    }

    @Override
    public void onDestroy() {
        Log.d("Logger", "LocalizacaoFragment onDestroy");
        GPS.instance.removeClient(this);
        super.onDestroy();
    }

    @Override
    public void locationChange(Location location) {
        this.location = location;
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
