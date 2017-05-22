package com.example.diogo.discoverytrip.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.Util.Barcode_Detector.BarcodeTrackerFactory;
import com.example.diogo.discoverytrip.Util.CallBack;
import com.example.diogo.discoverytrip.Util.Camera_Views.CameraSourcePreview;
import com.example.diogo.discoverytrip.Util.Camera_Views.GraphicOverlay;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;


public class LeitorCodigoBarras extends Fragment implements CallBack {

    public static int width, heigth;
    private CameraSourcePreview mPreview;
    private CameraSource mCameraSource;
    private CameraSource.Builder cameraBuilder;
    private GraphicOverlay mOverlay;
    private BarcodeDetector barcodeDetector;
    private BarcodeTrackerFactory barcodeFactory;

    public LeitorCodigoBarras() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_leitor_codigo_barras, container, false);

        mPreview = (CameraSourcePreview) view.findViewById(R.id.leitor_codigo_barras_cameraContent);
        mOverlay = (GraphicOverlay) view.findViewById(R.id.leitor_codigo_barras_cameraView);
        barcodeDetector = new BarcodeDetector.Builder(getContext())
                .build();
        barcodeFactory = new BarcodeTrackerFactory(mOverlay, this);
        barcodeDetector.setProcessor(new MultiProcessor.Builder<>(barcodeFactory).build());
        cameraBuilder = new CameraSource.Builder(getContext(), barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setAutoFocusEnabled(true);

        mCameraSource = cameraBuilder.build();
        startCameraSource();
        getActivity().setTitle("Leitor");

        Log.d("Logger codigo de barras","heigth: "+heigth+" width: "+width);
        return view;
    }

    @Override
    public void onFound(final String cod) {
        Log.d("Logger","Codigo encontrado "+cod);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showReadDialog(cod);
            }
        });
    }

    public void startCameraSource() {
        Log.d("Logger camera","Start");
        getActivity().runOnUiThread(new Runnable() {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

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

    @Override
    public void onHiddenChanged(boolean hidden){
        if(hidden){
            releaseCameraSource();
        }
    }
}