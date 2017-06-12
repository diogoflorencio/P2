package com.example.diogo.discoverytrip.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
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

import com.example.diogo.discoverytrip.Fragments.Carrinho;
import com.example.diogo.discoverytrip.Fragments.HomeFragment;
import com.example.diogo.discoverytrip.Model.Market;
import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.ServerResponses.MarketResponse;
import com.example.diogo.discoverytrip.Util.WIFIManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Classe activity responsavel pela activity home (principal) na aplicação
 */
public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

//    public static final String EVENT_TYPE = "Event";
    private int currentScreen = 0;
    private NavigationView navigationView;
    public static final int REQUEST_PERMISSIONS_CODE = 128;


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

        if (!hasMarket())
            selectMarket(this);

        createHomeFragment();

        permission();
    }

    private boolean hasMarket() {
        return this.getSharedPreferences("panfleto_eletronico", MODE_PRIVATE).contains("market");
    }

    private void setMarket(String id) {
        this.getSharedPreferences("panfleto_eletronico", MODE_PRIVATE).edit().putString("market", id).apply();
    }

    private void selectMarket(final Context context) {
        final AlertDialog loading = new ProgressDialog.Builder(context)
                .setTitle(R.string.loading)
                .create();

        loading.show();
        ApiClient.API_SERVICE.mercados().enqueue(new Callback<MarketResponse>() {
            @Override
            public void onResponse(Call<MarketResponse> call, Response<MarketResponse> response) {

                loading.dismiss();

                final String[] options = new String[response.body().getMarkets().size()];
                final String[] optionsIds = new String[response.body().getMarkets().size()];

                int i = 0;
                for (Market m : response.body().getMarkets()) {
                    optionsIds[i] = m.getId();
                    options[i++] = m.getCompany();
                }

                final AlertDialog select = new AlertDialog.Builder(context)
                        .setTitle(R.string.select_market)
                        .setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setMarket(optionsIds[which]);
                            }
                        })
                        .create();
                select.show();
            }

            @Override
            public void onFailure(Call<MarketResponse> call, Throwable t) {
            }
        });


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
            case R.id.logout:
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
                Log.d("Logger", "Leitor código de barras");
                fragment = new Carrinho();
                LeitorCodigoBarrasActivity.width = Resources.getSystem().getDisplayMetrics().widthPixels;
                LeitorCodigoBarrasActivity.heigth = Resources.getSystem().getDisplayMetrics().heightPixels;
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this,  new String[]{Manifest.permission.CAMERA,Manifest.permission.CHANGE_WIFI_STATE},
                    REQUEST_PERMISSIONS_CODE);
        }
    }
}
