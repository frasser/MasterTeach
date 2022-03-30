package com.example.frasser.masterteach.messagging;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.frasser.masterteach.NotificacionesActivity;
import com.example.frasser.masterteach.PerfilActivity;
import com.example.frasser.masterteach.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Juan Pablo on 23/03/2018.
 */

public class MiFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = "NOTICIAS";
    String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String from = remoteMessage.getFrom(); // linea para ver el remitente
        Log.d(TAG,"MENSAJE RECIBIDO DE: "+ from);

       if (remoteMessage.getNotification() !=null){
           Log.d(TAG,"Notificacion: "+ remoteMessage.getNotification().getBody());
          mostraNotificacion(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
       }
       if (remoteMessage.getData().size()>0){
           Log.d(TAG,"Data: "+remoteMessage.getData());
       }
    }

    private void mostraNotificacion(String title, String body) {

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(this, NotificacionesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.usu)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());
    }
}
