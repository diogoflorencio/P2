package com.example.diogo.discoverytrip.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diogo.discoverytrip.Model.ItemCompra;
import com.example.diogo.discoverytrip.Model.Produto;
import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.ServerResponses.ErrorResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.ResponseProduct;
import com.example.diogo.discoverytrip.Util.Barcode_Detector.BarcodeTrackerFactory;
import com.example.diogo.discoverytrip.Util.CallBack;
import com.example.diogo.discoverytrip.Util.Camera_Views.CameraSourcePreview;
import com.example.diogo.discoverytrip.Util.Camera_Views.GraphicOverlay;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsultorDePreco extends AppCompatActivity implements CallBack {

    public static int width, heigth;
    private CameraSourcePreview mPreview;
    private CameraSource mCameraSource;
    private CameraSource.Builder cameraBuilder;
    private GraphicOverlay mOverlay;
    private BarcodeDetector barcodeDetector;
    private BarcodeTrackerFactory barcodeFactory;
    private String idMarket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultor_de_preco);
        idMarket = (String) getIntent().getExtras().get("idMarket");
        mPreview = (CameraSourcePreview) findViewById(R.id.consultor_codigo_barras_cameraContent);
        mOverlay = (GraphicOverlay) findViewById(R.id.consultor_codigo_barras_cameraView);
    }

    @Override
    public void onFound(final String cod) {
        Log.d("Logger","Codigo encontrado "+cod);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getProduct(cod);
            }
        });
        stopCameraSource();
    }

    public void startCameraSource() {
        barcodeDetector = new BarcodeDetector.Builder(this)
                .build();
        barcodeFactory = new BarcodeTrackerFactory(mOverlay, this);
        barcodeDetector.setProcessor(new MultiProcessor.Builder<>(barcodeFactory).build());
        cameraBuilder = new CameraSource.Builder(this, barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setAutoFocusEnabled(true);

        mCameraSource = cameraBuilder.build();

        Log.d("LoggerCamera","start");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    mPreview.start(mCameraSource, mOverlay);
                } catch (IOException e) {
                    mCameraSource.release();
                    mCameraSource = null;
                }
            }
        });
        Log.d("LoggerCamera","started");
    }

    public void stopCameraSource(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPreview.stop();
            }
        }).start();
    }

    private void showReadDialog(final Produto produto){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_consulta_produto,null,true);
        TextView cod = (TextView) view.findViewById(R.id.dialog_consulta_produto_codigo);
        TextView descricao = (TextView) view.findViewById(R.id.dialog_consulta_produto_descricao);
        TextView preco = (TextView) view.findViewById(R.id.dialog_consulta_produto_preco);

        cod.setText(produto.getCodigoBarras());
        descricao.setText(produto.getDescricao());
        preco.setText(String.valueOf(produto.getValorUn()));
        builder.setCancelable(false);
        builder.setView(view);
        builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                startCameraSource();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Terminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void releaseCameraSource(){
        Log.d("LoggerCamera","release");
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPreview.release();
            }
        }).start();
        Log.d("LoggerCamera","released");
    }

    private void getProduct(String cod){
        Call<ResponseProduct> call = ApiClient.API_SERVICE.getProduct(idMarket, cod);
        call.enqueue(new Callback<ResponseProduct>() {
            @Override
            public void onResponse(Call<ResponseProduct> call, Response<ResponseProduct> response) {
                if(response.isSuccessful()){
                    showReadDialog(response.body().getProduto());
                }else {
                    try {
                        ErrorResponse errorResponse = ApiClient.errorBodyConverter.convert(response.errorBody());
                        Toast.makeText(ConsultorDePreco.this,errorResponse.getErrorDescription(),Toast.LENGTH_SHORT).show();
                        Log.e("Logger",errorResponse.getErrorDescription());
                        startCameraSource();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseProduct> call, Throwable t) {
                Toast.makeText(ConsultorDePreco.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d("LoggerCamera","onPause");
        releaseCameraSource();
    }

    @Override
    public void onResume(){
        super.onResume();
        startCameraSource();
    }
}
