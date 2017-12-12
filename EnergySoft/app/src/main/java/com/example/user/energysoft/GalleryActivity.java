package com.example.user.energysoft;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class GalleryActivity extends AppCompatActivity {

    GridView simpleGrid;
    int logos[] = {R.drawable.logo1, R.drawable.logo2, R.drawable.logo3, R.drawable.logo4,
            R.drawable.logo5, R.drawable.logo6, R.drawable.logo7, R.drawable.logo8, R.drawable.logo9,
            R.drawable.logo10, R.drawable.logo11, R.drawable.logo12, R.drawable.logo13};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        simpleGrid = (GridView) findViewById(R.id.galleryGridView); // init GridView
        // Create an object of CustomAdapter and set Adapter to GirdView
        GalleryAdapter galleryAdapter = new GalleryAdapter(getApplicationContext(), logos);
        simpleGrid.setAdapter(galleryAdapter);
        // implement setOnItemClickListener event on GridView
        simpleGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // set an Intent to Another Activity
                Intent intent = new Intent(GalleryActivity.this, GalleryZoomActivity.class);
                intent.putExtra("image", logos[position]); // put image data in Intent
                startActivity(intent); // start Intent
            }
        });
    }
}
