package com.example.diogo.discoverytrip.Util.Barcode_Detector;



import android.content.res.Resources;
import android.graphics.Point;
import android.util.Log;

import com.example.diogo.discoverytrip.Fragments.LeitorCodigoBarras;
import com.example.diogo.discoverytrip.Util.CallBack;
import com.example.diogo.discoverytrip.Util.Camera_Views.GraphicOverlay;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;


/**
 * Generic tracker which is used for tracking either a face or a barcode (and can really be used for
 * any type of item).  This is used to receive newly detected items, add a graphical representation
 * to an overlay, update the graphics as the item changes, and remove the graphics when the item
 * goes away.
 */
public class GraphicTracker<T> extends Tracker<Barcode> {
    private GraphicOverlay mOverlay;
    private TrackedGraphic<Barcode> mGraphic;
    private CallBack callBack;
    private String lastBarcode;
    private static final float virtualWidth = 720, virtualHeight=960;
    private int adjustY = 50, adjustX = 5, readAreaHeight = 25;

    GraphicTracker(GraphicOverlay overlay, TrackedGraphic<Barcode> graphic, CallBack callBack) {
        mOverlay = overlay;
        mGraphic = graphic;
        this.callBack = callBack;
    }

    /**
     * Start tracking the detected item instance within the item overlay.
     */
    @Override
    public void onNewItem(int id, Barcode item) {
        float yPoint1, yPoint2, yPoint3, yPoint4;

        mGraphic.setId(id);
        adjustY = (int) (dpToPx(readAreaHeight)*virtualHeight/ LeitorCodigoBarras.heigth);
        Log.d("Logger","AdjustY = "+adjustY);
        Point ponto = item.cornerPoints[0];
        yPoint1 = ponto.y;
        Log.d("Logger codigo de barras","X1="+ponto.x+" Y1="+ponto.y);

        ponto = item.cornerPoints[1];
        yPoint2 = ponto.y;
        Log.d("Logger codigo de barras","X2="+ponto.x+" Y2="+ponto.y);

        ponto = item.cornerPoints[2];
        yPoint3 = ponto.y;
        Log.d("Logger codigo de barras","X3="+ponto.x+" Y3="+ponto.y);

        ponto = item.cornerPoints[3];
        yPoint4 = ponto.y;
        Log.d("Logger codigo de barras","X4="+ponto.x+" Y4="+ponto.y);

        if(yPoint1 >= virtualHeight/2 - adjustY &&
                yPoint2 >= virtualHeight/2 - adjustY &&
                yPoint3 <= virtualHeight/2 + adjustY &&
                yPoint4 <= virtualHeight/2 + adjustY){
            lastBarcode = item.rawValue;
            callBack.onFound(item.rawValue);
        }
    }

    /**
     * Update the position/characteristics of the item within the overlay.
     */
    @Override
    public void onUpdate(Detector.Detections<Barcode> detectionResults, Barcode item) {
        float yPoint1, yPoint2, yPoint3, yPoint4;

        mOverlay.add(mGraphic);
        mGraphic.updateItem(item);

        adjustY = (int) (dpToPx(readAreaHeight)*virtualHeight/LeitorCodigoBarras.heigth);
        Log.d("Logger","AdjustY = "+adjustY);
        Point ponto = item.cornerPoints[0];
        yPoint1 = ponto.y;
        Log.d("Logger codigo de barras","X1="+ponto.x+" Y1="+ponto.y);

        ponto = item.cornerPoints[1];
        yPoint2 = ponto.y;
        Log.d("Logger codigo de barras","X2="+ponto.x+" Y2="+ponto.y);

        ponto = item.cornerPoints[2];
        yPoint3 = ponto.y;
        Log.d("Logger codigo de barras","X3="+ponto.x+" Y3="+ponto.y);

        ponto = item.cornerPoints[3];
        yPoint4 = ponto.y;
        Log.d("Logger codigo de barras","X4="+ponto.x+" Y4="+ponto.y);

        if(yPoint1 >= virtualHeight/2 - adjustY &&
                yPoint2 >= virtualHeight/2 - adjustY &&
                yPoint3 <= virtualHeight/2 + adjustY &&
                yPoint4 <= virtualHeight/2 + adjustY){

            if(lastBarcode == null) {
                lastBarcode = item.rawValue;
                callBack.onFound(item.rawValue);
            }
            else if(!lastBarcode.equals(item.rawValue)){
                lastBarcode = item.rawValue;
                callBack.onFound(item.rawValue);
            }
        }
    }

    private int dpToPx(int dp){
        int px;
        px = (int) Math.ceil(dp * (Resources.getSystem().getDisplayMetrics().densityDpi / 160f));
        return px;
    }

    /**
     * Hide the graphic when the corresponding face was not detected.  This can happen for
     * intermediate frames temporarily, for example if the face was momentarily blocked from
     * view.
     */
    @Override
    public void onMissing(Detector.Detections<Barcode> detectionResults) {
        mOverlay.remove(mGraphic);
    }

    /**
     * Called when the item is assumed to be gone for good. Remove the graphic annotation from
     * the overlay.
     */
    @Override
    public void onDone() {
        mOverlay.remove(mGraphic);
    }
}