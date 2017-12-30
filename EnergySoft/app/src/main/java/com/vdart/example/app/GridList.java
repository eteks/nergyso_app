package com.vdart.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;


public class GridList extends AppCompatActivity {
    Toolbar toolbar;
    GridView simpleGrid;
    int logos[] = {R.drawable.events_tile, R.drawable.news_tile, R.drawable.gallery_tile, R.drawable.shoutout_tile,
            R.drawable.feedback_tile, R.drawable.profile_tile,R.drawable.polls_tile, R.drawable.live_tile};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_list);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        simpleGrid = (GridView) findViewById(R.id.simpleGridView); // init GridView
        // Create an object of CustomAdapter and set Adapter to GirdView
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), logos);
        simpleGrid.setAdapter(customAdapter);
        // implement setOnItemClickListener event on GridView
        simpleGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0 : {
                        Intent intent = new Intent(GridList.this, EventMain.class);
                        startActivity(intent);
                        break;
                    }
                    case 1 : {
                        Intent intent = new Intent(GridList.this, NewsMain.class);
                        startActivity(intent);
                        break;
                    }
                    case 2 : {
                        Toast.makeText(GridList.this, "Coming soon", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 3 : {
                        Intent intent = new Intent(GridList.this, Shoutout.class);
                        startActivity(intent);
                        break;
                    }
                    case 4 : {
                        Intent intent = new Intent(GridList.this, Feedback.class);
                        startActivity(intent);
                        break;
                    }
                    case 5 : {
                        Intent intent = new Intent(GridList.this, ProfileActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
            }
        });
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
                intent = new Intent(GridList.this,GridList.class);
                startActivity(intent);
                return true;

            case R.id.profile:
                intent = new Intent(GridList.this,ProfileActivity.class);
                startActivity(intent);
                return true;

            case R.id.events:
                intent = new Intent(GridList.this,EventMain.class);
                startActivity(intent);
                return true;

            case R.id.news:
                intent = new Intent(GridList.this,NewsMain.class);
                startActivity(intent);
                return true;

            case R.id.shoutout:
                intent = new Intent(GridList.this,Shoutout.class);
                startActivity(intent);
                return true;

            case R.id.gallery:
                Toast.makeText(GridList.this, "Coming soon", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.info:
                Toast.makeText(GridList.this, "Coming soon", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.settings:
                intent = new Intent(GridList.this,Changepassword_Activity.class);
                startActivity(intent);
                return true;

            case R.id.logout:
                intent = new Intent(GridList.this,MainActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

