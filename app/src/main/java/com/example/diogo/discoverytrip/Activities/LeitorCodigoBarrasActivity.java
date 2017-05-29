package com.example.diogo.discoverytrip.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.Util.Barcode_Detector.BarcodeTrackerFactory;
import com.example.diogo.discoverytrip.Util.CallBack;
import com.example.diogo.discoverytrip.Util.Camera_Views.CameraSourcePreview;
import com.example.diogo.discoverytrip.Util.Camera_Views.GraphicOverlay;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class LeitorCodigoBarrasActivity extends AppCompatActivity implements CallBack{

    public static int width, heigth;
    private CameraSourcePreview mPreview;
    private CameraSource mCameraSource;
    private CameraSource.Builder cameraBuilder;
    private GraphicOverlay mOverlay;
    private BarcodeDetector barcodeDetector;
    private BarcodeTrackerFactory barcodeFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leitor_codigo_barras);

        mPreview = (CameraSourcePreview) findViewById(R.id.leitor_codigo_barras_cameraContent);
        mOverlay = (GraphicOverlay) findViewById(R.id.leitor_codigo_barras_cameraView);
        barcodeDetector = new BarcodeDetector.Builder(this)
                .build();
        barcodeFactory = new BarcodeTrackerFactory(mOverlay, this);
        barcodeDetector.setProcessor(new MultiProcessor.Builder<>(barcodeFactory).build());
        cameraBuilder = new CameraSource.Builder(this, barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setAutoFocusEnabled(true);

        mCameraSource = cameraBuilder.build();
        startCameraSource();
    }

    @Override
    public void onFound(final String cod) {
        Log.d("Logger","Codigo encontrado "+cod);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showReadDialog(cod);
            }
        });
    }

    public void startCameraSource() {
        Log.d("Logger camera","Start");
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
    }

    public void stopCameraSource(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPreview.stop();
            }
        }).start();
    }

    private void showReadDialog(final String codigo){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Código identificado!");
        builder.setMessage("Código de barras: "+codigo);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                startCameraSource();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startCameraSource();
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        stopCameraSource();
    }

    private void releaseCameraSource(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPreview.release();
            }
        }).start();
    }

    @Override
    public void onDestroy(){
        releaseCameraSource();
        super.onDestroy();
    }


}
