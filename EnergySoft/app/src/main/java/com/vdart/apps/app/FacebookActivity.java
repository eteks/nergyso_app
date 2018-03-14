package com.vdart.apps.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import static com.vdart.apps.app.MainActivity.MyPREFERENCES;

/**
 * Created by ets-prabhu on 2/1/18.
 */

public class FacebookActivity extends AppCompatActivity {
    Menu menuInflate;
    String notification_count = "";
    Toolbar toolbar;
    String NOTIFICATION_COUNT_URL = "api/notification/notification_employee_unread_count";
    int NOTIFICATION_COUNT = 0;
    String SERVER_URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_facebook);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        notification_count = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).getString("nc","");

        SERVER_URL = getString(R.string.service_url);

        NOTIFICATION_COUNT_URL = SERVER_URL + NOTIFICATION_COUNT_URL;
        getNotificationCount();

//Get a reference to your WebView//
        WebView webView = (WebView) findViewById(R.id.webview);

//Specify the URL you want to display//
//        WebView webview = new WebView(this);
//        webview.loadUrl("https://www.facebook.com/VDartIncs/");
//        setContentView(webview);
        webView.setWebViewClient(new MyBrowser());
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl("https://www.facebook.com/VDartIncs/");
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                // Page loading finished
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            }
        });
        //        Click Logo to home screen

        ImageView imageButton = (ImageView) toolbar.findViewById(R.id.vdart_logo);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FacebookActivity.this, BannerActivity.class);
                startActivity(intent);
            }
        });

        registerReceiver(myReceiver, new IntentFilter(MyAndroidFirebaseMsgService.INTENT_FILTER));

    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getNotificationCount();
        }
    };

    public void onDestroy() {

        unregisterReceiver(myReceiver);
        super.onDestroy();

    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public void getNotificationCount(){
        String data = null;
        try {

            data = URLEncoder.encode("notification_employee", "UTF-8")
                    + "=" + getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).getInt("id",0);;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        final BufferedReader[] reader = {null};

        // Send data
        final String finalData = data;
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    // Defined URL  where to send data
                    URL url = new URL(NOTIFICATION_COUNT_URL);

                    // Send POST data request
                    System.out.println("URL:" + NOTIFICATION_COUNT_URL);
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(finalData);
                    wr.flush();
                    System.out.println(finalData);
                    // Get the server response

                    reader[0] = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader[0].readLine()) != null) {
                        // Append server response in string
                        sb.append(line + "\n");
                    }
                    System.out.println("Output" + sb.toString());
                    JSONObject obj = new JSONObject(sb.toString());
                    if(obj.has("unread")){
                        NOTIFICATION_COUNT = obj.getInt("unread");
                    }
                    setCount(FacebookActivity.this, String.valueOf(NOTIFICATION_COUNT));
                }catch (Exception ex) {
                    System.out.println(ex);
                } finally {
                    try {
                        reader[0].close();
                    } catch (Exception ex) {
                    }
                }
                return null;
            }

        };
        runAsyncTask(task);

//        Download_data download_data = new Download_data((Download_data.download_complete) this);
//        download_data.download_data_from_link(NOTIFICATION_URL);
    }

    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        setCount(this, String.valueOf(NOTIFICATION_COUNT));
        return  true;
    }

    // Initiating Menu XML file (menu.xml)
    public void setCount(Context context, String count) {
        MenuItem menuItem = (MenuItem) menuInflate.findItem(R.id.action_notification);
        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();

        CountDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_group_count);
        if (reuse != null && reuse instanceof CountDrawable) {
            badge = (CountDrawable) reuse;
        } else {
            badge = new CountDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_group_count, badge);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menuInflate = menu;
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        setCount(this,notification_count);
        return true;
    }

    /**
     * Event Handling for Individual menu item selected
     * Identify single menu item by it's id
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent;
        switch (item.getItemId())
        {
            case R.id.action_home:
                intent = new Intent(FacebookActivity.this,BannerActivity.class);
                startActivity(intent);
                return true;

            case R.id.profile:
                intent = new Intent(FacebookActivity.this,ProfileActivity.class);
                startActivity(intent);
                return true;

            case R.id.events:
                intent = new Intent(FacebookActivity.this,EventMain.class);
                startActivity(intent);
                return true;

            case R.id.news:
                intent = new Intent(FacebookActivity.this,NewsMain.class);
                startActivity(intent);
                return true;

            case R.id.feedback:
                intent = new Intent(FacebookActivity.this,Feedback.class);
                startActivity(intent);
                return true;

            case R.id.gallery:
                intent = new Intent(FacebookActivity.this,EventGallery.class);
                startActivity(intent);
                return true;

            case R.id.info:
                intent = new Intent(FacebookActivity.this,EventGallery.class);
                startActivity(intent);
                return true;

            case R.id.settings:
                intent = new Intent(FacebookActivity.this,Changepassword_Activity.class);
                startActivity(intent);
                return true;

            case R.id.logout:
                intent = new Intent(FacebookActivity.this,MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.facebook:
                intent = new Intent(FacebookActivity.this,FacebookActivity.class);
                startActivity(intent);
                return true;

            case R.id.twitter:
                intent = new Intent(FacebookActivity.this,TwitterActivity.class);
                startActivity(intent);
                return true;

            case R.id.ceomsg:
                intent = new Intent(FacebookActivity.this,CeomessageActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_notification:
                intent = new Intent(FacebookActivity.this,NotificationMain.class);
                startActivity(intent);
                return true;

            case R.id.shoutout:
                intent = new Intent(this,ListingMore.class);
                intent.putExtra("more","shoutout");
                startActivity(intent);
                return true;

            case R.id.live:
                intent = new Intent(this,LiveTelecast.class);
                startActivity(intent);
                return true;

            case R.id.polls:
                intent = new Intent(this,quiz_activity_frag.class);
                startActivity(intent);
                return true;

            case R.id.action_refresh:
//                intent = new Intent(this,BannerActivity.class);
                startActivity(getIntent());
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
