package com.example.diogo.discoverytrip.Util.Barcode_Detector;



import com.example.diogo.discoverytrip.Util.Camera_Views.GraphicOverlay;
import com.google.android.gms.vision.barcode.Barcode;

/**
 * Common base class for defining graphics for a particular item type.  This along with
 * {@link GraphicTracker} avoids the need to duplicate this code for both the face and barcode
 * instances.
 */
public abstract class TrackedGraphic<T> extends GraphicOverlay.Graphic {
    private int mId;

    TrackedGraphic(GraphicOverlay overlay) {
        super(overlay);
    }

    void setId(int id) {
        mId = id;
    }

    protected int getId() {
        return mId;
    }

    abstract void updateItem(Barcode item);
}