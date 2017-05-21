package com.example.diogo.discoverytrip.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.diogo.discoverytrip.Service.ServiceNotificacao;

/**
 * Created by diogo on 21/05/17.
 */

public class BroadcastNotificacao extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notificacaoIntent = new Intent(context, ServiceNotificacao.class);
        context.startService(notificacaoIntent);
        Log.d("Logger","BroadcastNotificacao");
    }
}
