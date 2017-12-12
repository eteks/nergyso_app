package com.example.user.energysoft;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EventMain extends AppCompatActivity implements Download_data.download_complete{
    Toolbar toolbar;
    public ListView eventList;
    public ArrayList<Event> event = new ArrayList<Event>();
    public android.widget.ListAdapter EventAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
//        eventList = (ListView) findViewById(R.id.event_list);
//        EventAdapter = new com.example.user.energysoft.ListEventAdapter(this);
//        eventList.setAdapter(EventAdapter);
        Download_data download_data = new Download_data((Download_data.download_complete)this);
        download_data.download_data_from_link("http://10.0.0.15:8000/api/events/");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int res_id = item.getItemId();
        if(res_id == R.id.action_home)
        {
            Toast.makeText(getApplicationContext(),"You selet Home",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void get_data(String data)
    {

        try {
            JSONObject object = new JSONObject(data);
            System.out.println("Object"+object);
            JSONArray data_array = object.getJSONArray("results");
            System.out.println("Object"+data_array);
            for (int i = 0 ; i < data_array.length() ; i++)
            {
                JSONObject obj=new JSONObject(data_array.get(i).toString());
//                System.out.println("Object"+obj);
                final Event add=new Event();
                add.events_title = obj.getString("news_title");
                add.events_description = obj.getString("news_description");
//                add.news_image = obj.getString("news_image");
//                System.out.println("News title"+obj.getString("news_title"));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        event.add(add);
                    }
                });

            }

//            NewsAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
