package com.example.diogo.discoverytrip.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.diogo.discoverytrip.Activities.HomeActivity;
import com.example.diogo.discoverytrip.R;

import java.util.concurrent.Semaphore;

/**
 * Created by diogo on 21/05/17.
 */

public class ServiceNotificacao extends Service {

    private  static boolean run = false;
    private static Semaphore semaphore =  new Semaphore(1);
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        enviaNotificacao();
        Log.d("Logger","Service Notificação enviando notificação");
        return START_STICKY;
    }

    private void enviaNotificacao() {
        Intent notificationIntent = new Intent(this, HomeActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.icon)
                .setContentTitle("Panfleto-e")
                .setContentText("Promoções")
                .setContentIntent(pendingIntent).build();

        /*vibração da notificação*/
        notification.vibrate = new long[] { 100, 250, 100, 500 };

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(R.mipmap.icon, notification);

        /*som da notificação*/
        try{
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(this,som);
            toque.play();
        }catch (Exception e){
            e.printStackTrace();
        }
        run= false;
    }
}
