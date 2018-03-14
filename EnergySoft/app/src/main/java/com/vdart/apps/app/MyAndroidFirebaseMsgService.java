package com.vdart.apps.app;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Switch;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import static com.vdart.apps.app.MainActivity.MyPREFERENCES;
public class MyAndroidFirebaseMsgService extends FirebaseMessagingService {
    int id = 0;
    int NOTIFICATION_COUNT = 0;
    BannerActivity banner;
    String SERVER_URL = "";
    private static final String TAG = "MyAndroidFCMService";
    public static final String INTENT_FILTER = "INTENT_FILTER";
    String NOTIFICATION_POST_URL = "api/notification/notification_status/";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        int notification_id = 0;
        //Log data to Log Cat
        SERVER_URL = getString(R.string.service_url);

        id = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).getInt("id",0);

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        System.out.println("Data : " + remoteMessage.getData());

//        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedpreferences.edit();
//        editor.putString("nc", String.valueOf(nc));
//        editor.commit();

        Intent intent = new Intent("INTENT_FILTER");
        sendBroadcast(intent);

        //create notification
        try{
            JSONObject obj = new JSONObject(remoteMessage.getData());
            String category = obj.getString("category");

            notification_id = obj.getInt("notification_id");

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
                    createNotification(id,category,remoteMessage.getNotification().getBody(),notification_id);
                    break;
                }
                case "news":{
                    createNotification(id, category,remoteMessage.getNotification().getBody(),notification_id);
                    break;
                }
                case "shoutout":{
                    createNotification(id, category, remoteMessage.getNotification().getBody(),notification_id);
                    break;
                }
                case "livetelecast":{
                    createNotification(id, category, remoteMessage.getNotification().getBody(),notification_id);
                    break;
                }
                case "ceo":{
                    createNotification(id, category, remoteMessage.getNotification().getBody(),notification_id);
                    break;
                }
            }
        }catch (Exception e){

        }

    }

    private void createNotification(int id,  String category, String messageBody,int notification_id) {
        PendingIntent resultIntent ;
        if(category.equals("events")){
            System.out.println("Message" + messageBody);
            Intent intent = new Intent( this , FullEvent. class);
            intent.putExtra("id", id);
            String check = "EVENTS";
            intent.putExtra("check",check);
            intent.putExtra("notification_id",notification_id);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            resultIntent = PendingIntent.getActivity( this , 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }else if(category.equals("news")){
            System.out.println("Message" + messageBody);
            Intent intent = new Intent( this , FullEvent. class);
            intent.putExtra("id", id);
            intent.putExtra("check", "NEWS");
            intent.putExtra("notification_id",notification_id);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            resultIntent = PendingIntent.getActivity( this , 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }else if(category.equals("shoutout")){
            System.out.println("Message" + messageBody);
            Intent intent = new Intent( this , ListingMore. class);
            intent.putExtra("more","shoutout");
            intent.putExtra("notification_id",notification_id);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            resultIntent = PendingIntent.getActivity( this , 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

        }else if(category.equals("livetelecast")){
            System.out.println("Message" + messageBody);
            Intent intent = new Intent( this , LiveTelecast. class);
            intent.putExtra("notification_id",notification_id);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            resultIntent = PendingIntent.getActivity( this , 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

        }else if(category.equals("ceo")){
            System.out.println("Message" + messageBody);
            Intent intent = new Intent( this , CeoMain. class);
            intent.putExtra("notification_id",notification_id);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            resultIntent = PendingIntent.getActivity( this , 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

        }else{
            Intent intent = new Intent( this , BannerActivity. class);
            intent.putExtra("notification_id",notification_id);
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