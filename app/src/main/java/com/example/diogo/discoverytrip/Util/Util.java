package com.example.diogo.discoverytrip.Util;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by diogo on 05/06/17.
 */

public class Util {

    private static MaterialDialog mMaterialDialog;

    public static void callDialogGPS(String message, final Context context){
        mMaterialDialog = new MaterialDialog(context)
                .setTitle("Permission")
                .setMessage( message )
                .setPositiveButton("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
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
}
