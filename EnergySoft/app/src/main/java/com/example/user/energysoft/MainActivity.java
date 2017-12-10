package com.example.user.energysoft;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            int ONE_TIME = 0;

            @Override
            public void onClick(View view) {
                Log.i("clicks","You Clicked B1");
                Intent i=new Intent(MainActivity.this, FullNews.class);
                startActivity(i);
                ONE_TIME++;
                if (ONE_TIME == 1) {
                    EditText username = (EditText) findViewById(R.id.username);
                    EditText password = (EditText) findViewById(R.id.password);
                    String userName = username.getText().toString();
                    String passWord = password.getText().toString();
                    System.out.println(userName+passWord);
                }
            }
        });

    }

}
