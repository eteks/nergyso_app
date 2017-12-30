package com.vdart.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

public class MainGallery extends AppCompatActivity {
    Toolbar toolbar;
    ImageButton events_more, news_more;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_gallery);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        events_more = (ImageButton) findViewById(R.id.events_more);
        news_more = (ImageButton) findViewById(R.id.news_more);
        events_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainGallery.this, EventGallery.class);
                startActivity(intent);
            }
        });
        news_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainGallery.this, EventGallery.class);
                startActivity(intent);
            }
        });
    }
}
