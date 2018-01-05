package com.example.user.energysoft;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by ets-prabhu on 2/1/18.
 */

public class TwitterActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_facebook);

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
}
