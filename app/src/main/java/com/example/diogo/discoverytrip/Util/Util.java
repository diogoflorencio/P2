package com.example.diogo.discoverytrip.Util;

import android.content.Context;
import android.view.View;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by diogo on 05/06/17.
 */

public class Util {

    private static MaterialDialog mMaterialDialog;

    private static void callDialog(String message, Context context){
        mMaterialDialog = new MaterialDialog(context)
                .setTitle("Permission")
                .setMessage( message )
                .setPositiveButton("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //action
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
