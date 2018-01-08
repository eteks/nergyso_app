package com.example.user.energysoft;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by ets-prabhu on 2/1/18.
 */

public class TwitterActivity extends AppCompatActivity {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        webView.loadUrl("https://twitter.com/VDartInc/");

    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            System.out.println("HI");
            view.loadUrl(url);
            return true;
        }
    }

    // Initiating Menu XML file (menu.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
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
                intent = new Intent(TwitterActivity.this,BannerActivity.class);
                startActivity(intent);
                return true;

            case R.id.profile:
                intent = new Intent(TwitterActivity.this,ProfileActivity.class);
                startActivity(intent);
                return true;

            case R.id.events:
                intent = new Intent(TwitterActivity.this,EventMain.class);
                startActivity(intent);
                return true;

            case R.id.news:
                intent = new Intent(TwitterActivity.this,NewsMain.class);
                startActivity(intent);
                return true;

            case R.id.shoutout:
                intent = new Intent(TwitterActivity.this,Shoutout.class);
                startActivity(intent);
                return true;

            case R.id.feedback:
                intent = new Intent(TwitterActivity.this,Feedback.class);
                startActivity(intent);
                return true;

            case R.id.gallery:
                intent = new Intent(TwitterActivity.this,EventGallery.class);
                startActivity(intent);
                return true;

            case R.id.info:
                intent = new Intent(TwitterActivity.this,EventGallery.class);
                startActivity(intent);
                return true;

            case R.id.settings:
                intent = new Intent(TwitterActivity.this,Changepassword_Activity.class);
                startActivity(intent);
                return true;

            case R.id.logout:
                intent = new Intent(TwitterActivity.this,MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.facebook:
                intent = new Intent(TwitterActivity.this,FacebookActivity.class);
                startActivity(intent);
                return true;

            case R.id.twitter:
                intent = new Intent(TwitterActivity.this,TwitterActivity.class);
                startActivity(intent);
                return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
