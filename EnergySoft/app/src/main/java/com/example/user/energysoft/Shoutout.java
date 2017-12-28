package com.example.user.energysoft;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import static com.example.user.energysoft.MainActivity.MyPREFERENCES;

public class Shoutout extends AppCompatActivity{
    String SERVER_URL ;
    String SHOUT_URL ;
    Toolbar toolbar;
    EditText shoutout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoutout);
        SERVER_URL = getString(R.string.service_url);
        SHOUT_URL = SERVER_URL+ "api/shoutout/";
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        System.out.println("USer : "+shared.getInt("id",0));
        final int id = shared.getInt("id",0);
//        ImageView home = (ImageView) findViewById(R.id.action_home);
//        home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Shoutout.this, GridList.class);
//                startActivity(intent);
//            }
//        });
        shoutout = (EditText) findViewById(R.id.shouttext);
        Button shout = (Button) findViewById(R.id.shoutpost);
        shout.setOnClickListener(new View.OnClickListener() {
            int ONE_TIME = 0;
            @Override
            public void onClick(View view) {
                ONE_TIME++;
                if (ONE_TIME == 1) {
                String shout = shoutout.getText().toString();
                String data = null;
                try {
                    data = URLEncoder.encode("shoutout_description", "UTF-8")
                            + "=" + URLEncoder.encode(shout, "UTF-8");
                    data += "&" + URLEncoder.encode("shoutout_employee", "UTF-8") + "="
                            + id;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                final BufferedReader[] reader = {null};

                // Send data
                final String finalData = data;
                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            ONE_TIME = 0;
                            // Defined URL  where to send data
                            URL url = new URL(SHOUT_URL);

                            // Send POST data request
                            System.out.println("URL:" + SHOUT_URL);
                            URLConnection conn = url.openConnection();
                            conn.setDoOutput(true);
                            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                            wr.write(finalData);
                            wr.flush();

                            // Get the server response

                            reader[0] = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            StringBuilder sb = new StringBuilder();
                            String line = null;

                            // Read Server Response
                            while ((line = reader[0].readLine()) != null) {
                                // Append server response in string
                                sb.append(line + "\n");
                            }
                            System.out.println(sb.toString());
                            JSONObject object = new JSONObject(sb.toString());
                            if(object.has("key")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Shoutout.this, "Logging in!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                System.out.println("object"+object.getString("key"));
                                SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("key", object.getString("key"));
                                editor.putInt("id",object.getInt("user"));
                                editor.putString("username",object.getString("username"));
                                editor.putString("email",object.getString("email"));
                                editor.commit();
//                                Intent intent = new Intent(MainActivity.this, GridList.class);
////                                    intent.putExtra("key", object.getString("key"));
////                                    finish();
//                                startActivity(intent);
                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        createAndShowDialog("Coming soon", "Not Ready");
                                    }
                                });
                            }
                        } catch (Exception ex) {
                            System.out.println(ex);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    createAndShowDialog("No internet connection", "Error");
                                }
                            });
                        } finally {
                            try {

                                reader[0].close();
                            } catch (Exception ex) {
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            createAndShowDialog("Check your connection","No connection");
//                                        }
//                                    });

                            }
                        }


                        return null;
                    }

                };
                runAsyncTask(task);

            }
            }
        });
    }

    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if(exception.getCause() != null){
            ex = exception.getCause();
        }
        createAndShowDialog(ex.getMessage(), title);
    }

    /**
     * Creates a dialog and shows it
     *
     * @param message
     *            The dialog message
     * @param title
     *            The dialog title
     */
    private void createAndShowDialog(final String message, final String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }

    private void createAndShowDialogFromTask(final Exception exception, String title) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createAndShowDialog(exception, "Error");
            }
        });
    }

    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
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
                intent = new Intent(Shoutout.this,GridList.class);
                startActivity(intent);
                return true;

            case R.id.profile:
                intent = new Intent(Shoutout.this,ProfileActivity.class);
                startActivity(intent);
                return true;

            case R.id.events:
                intent = new Intent(Shoutout.this,EventMain.class);
                startActivity(intent);
                return true;

            case R.id.news:
                intent = new Intent(Shoutout.this,NewsMain.class);
                startActivity(intent);
                return true;

            case R.id.shoutout:
                intent = new Intent(Shoutout.this,Shoutout.class);
                startActivity(intent);
                return true;

            case R.id.gallery:
                Toast.makeText(Shoutout.this, "Coming soon", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.info:
                Toast.makeText(Shoutout.this, "Coming soon", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.settings:
                intent = new Intent(Shoutout.this,Changepassword_Activity.class);
                startActivity(intent);
                return true;

            case R.id.logout:
                intent = new Intent(Shoutout.this,MainActivity.class);
                startActivity(intent);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

}