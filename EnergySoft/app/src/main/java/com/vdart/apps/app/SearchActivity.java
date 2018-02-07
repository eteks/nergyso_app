package com.vdart.apps.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText searchText;
    RadioButton news, events, shoutout;
    String category = "";
    Button searchbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        news = (RadioButton) findViewById(R.id.news);
        events = (RadioButton) findViewById(R.id.events);
        shoutout = (RadioButton) findViewById(R.id.shoutout);
        searchText = (EditText) findViewById(R.id.searchText);

        searchbutton = (Button) findViewById(R.id.searchbutton);
        searchbutton.setOnClickListener(new View.OnClickListener(){
            int ONE_TIME = 0;
            @Override
            public void onClick(View view) {
                String search = searchText.getText().toString();
                if(!search.isEmpty() && (!category.isEmpty())){
                    Intent intent = new Intent(SearchActivity.this,SearchResults.class);
                    intent.putExtra("search",search);
                    intent.putExtra("category",category);
                    startActivity(intent);
                }else if(search.isEmpty()){
                    createAndShowDialog("Please insert the search text","Empty");
                }else if(category.isEmpty()){
                    createAndShowDialog("Please select any category","Empty");
                }
            }
        });
    }

    public void getRadioChecked(View view){
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.news:
                if (checked)
                    category = "NEWS";
                break;
            case R.id.events:
                if (checked)
                    category = "EVENTS";
                break;
            case R.id.shoutout:
                if (checked)
                    category = "SHOUTOUT";
                break;
        }
    }

    private void createAndShowDialog(final String message, final String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
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
                intent = new Intent(SearchActivity.this,BannerActivity.class);
                startActivity(intent);
                return true;

            case R.id.profile:
                intent = new Intent(SearchActivity.this,ProfileActivity.class);
                startActivity(intent);
                return true;

            case R.id.events:
                intent = new Intent(SearchActivity.this,EventMain.class);
                startActivity(intent);
                return true;

            case R.id.news:
                intent = new Intent(SearchActivity.this,NewsMain.class);
                startActivity(intent);
                return true;

            case R.id.shoutout:
                intent = new Intent(SearchActivity.this,Shoutout.class);
                startActivity(intent);
                return true;

            case R.id.feedback:
                intent = new Intent(SearchActivity.this,Feedback.class);
                startActivity(intent);
                return true;

            case R.id.gallery:
                intent = new Intent(SearchActivity.this,EventGallery.class);
                startActivity(intent);
                return true;

            case R.id.ceomsg:
                intent = new Intent(SearchActivity.this,CeomessageActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_notification:
                intent = new Intent(SearchActivity.this,NotificationMain.class);
                startActivity(intent);
                return true;

            case R.id.info:
                Toast.makeText(SearchActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.settings:
                intent = new Intent(SearchActivity.this,Changepassword_Activity.class);
                startActivity(intent);
                return true;

            case R.id.logout:
                intent = new Intent(SearchActivity.this,MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.facebook:
                intent = new Intent(SearchActivity.this,FacebookActivity.class);
                startActivity(intent);
                return true;

            case R.id.twitter:
                intent = new Intent(SearchActivity.this,TwitterActivity.class);
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

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
