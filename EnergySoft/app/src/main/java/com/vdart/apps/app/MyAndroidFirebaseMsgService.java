package com.vdart.apps.app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Switch;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

public class MyAndroidFirebaseMsgService extends FirebaseMessagingService {
    private static final String TAG = "MyAndroidFCMService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Log data to Log Cat
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        System.out.println("Data : " + remoteMessage.getData());
        //create notification
        try{
            JSONObject obj = new JSONObject(remoteMessage.getData());
            String category = obj.getString("category");
            int id = 0;
            if(category.equals("events")){
                id = obj.getInt("events_id");
            }else if(category.equals("news")){
                id = obj.getInt("news_id");
            }else{
                id = 0;
            }
            switch (category){
                case "events" : {
                    createNotification(id,category,remoteMessage.getNotification().getBody());
                    break;
                }
                case "news":{
                    createNotification(id, category,remoteMessage.getNotification().getBody());
                    break;
                }
                case "shoutout":{
                    createNotification(id, category, remoteMessage.getNotification().getBody());
                    break;
                }
                case "livetelecast":{
                    createNotification(id, category, remoteMessage.getNotification().getBody());
                    break;
                }
                case "ceo":{
                    createNotification(id, category, remoteMessage.getNotification().getBody());
                    break;
                }
            }
        }catch (Exception e){

        }

//        createNotification(remoteMessage.getNotification().getBody());
    }

    private void createNotification(int id,  String category, String messageBody) {
        PendingIntent resultIntent ;
        if(category.equals("events")){
            System.out.println("Message" + messageBody);
            Intent intent = new Intent( this , FullEvent. class);
            intent.putExtra("id", id);
            String check = "EVENTS";
            intent.putExtra("check",check);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            resultIntent = PendingIntent.getActivity( this , 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }else if(category.equals("news")){
            System.out.println("Message" + messageBody);
            Intent intent = new Intent( this , FullEvent. class);
            intent.putExtra("id", id);
            intent.putExtra("check", "NEWS");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            resultIntent = PendingIntent.getActivity( this , 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }else if(category.equals("shoutout")){
            System.out.println("Message" + messageBody);
            Intent intent = new Intent( this , ListingMore. class);
            intent.putExtra("more","shoutout");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            resultIntent = PendingIntent.getActivity( this , 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

        }else if(category.equals("livetelecast")){
            System.out.println("Message" + messageBody);
            Intent intent = new Intent( this , LiveTelecast. class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            resultIntent = PendingIntent.getActivity( this , 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

        }else if(category.equals("ceo")){
            System.out.println("Message" + messageBody);
            Intent intent = new Intent( this , CeoMain. class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            resultIntent = PendingIntent.getActivity( this , 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

        }else{
            Intent intent = new Intent( this , BannerActivity. class);
            resultIntent = PendingIntent.getActivity( this , 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder( this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(messageBody)
                .setAutoCancel( true )
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mNotificationBuilder.build());
    }
}