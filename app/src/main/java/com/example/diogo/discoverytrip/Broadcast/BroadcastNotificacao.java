package com.example.diogo.discoverytrip.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.example.diogo.discoverytrip.Service.ServiceNotificacao;
import com.example.diogo.discoverytrip.Util.WIFIManager;

import java.util.concurrent.Semaphore;

/**
 * Created by diogo on 21/05/17.
 */

public class BroadcastNotificacao extends BroadcastReceiver {
    private static Semaphore semaphore = new Semaphore(1);
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            semaphore.tryAcquire();
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (info != null && info.isConnected()) {
                Intent notificacaoIntent = new Intent(context, ServiceNotificacao.class);
                //context.startService(notificacaoIntent);
                Log.d("Logger", "BroadcastNotificacao");
            }
            semaphore.release();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
