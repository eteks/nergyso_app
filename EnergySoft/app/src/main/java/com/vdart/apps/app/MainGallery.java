package com.vdart.apps.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainGallery extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_gallery);
        System.out.println("Notification");
        Intent intent = new Intent(MainGallery.this, MyAndroidFirebaseMsgService.class);
        startActivity(intent);

    }
}
