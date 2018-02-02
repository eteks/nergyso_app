package com.vdart.apps.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import static com.vdart.apps.app.MainActivity.MyPREFERENCES;

public class SplashActivity extends Activity {

protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

// decide here whether to navigate to Login or Main Activity 

    SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

    if (sharedpreferences.getBoolean("activity_executed", false)) {
        Intent intent = new Intent(this, BannerActivity.class);
        startActivity(intent);
        finish();
    } else {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

}