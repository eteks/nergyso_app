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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import static com.example.user.energysoft.R.drawable.user;

public class MainActivity extends AppCompatActivity {
    String SERVER_URL,LOGIN_URL;
    Toolbar toolbar;
    TextView forgot_password ;
    public static final String MyPREFERENCES = "MyPrefs" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SERVER_URL = getString(R.string.service_url);
        LOGIN_URL = SERVER_URL+ "api/rest-auth/login/";
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        forgot_password = (TextView) findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ForgetpasswordActivity.class);
                startActivity(intent);
            }
        });
        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            int ONE_TIME = 0;

            @Override
            public void onClick(View view) {
                Log.i("clicks", "You Clicked B1");
                ONE_TIME++;
                if (ONE_TIME == 1) {
                    EditText username = (EditText) findViewById(R.id.username);
                    EditText password = (EditText) findViewById(R.id.password);
                    String userName = username.getText().toString();
                    String passWord = password.getText().toString();
                    System.out.println("Username:" + userName + "Password:" + passWord);

                    String data = null;
                    try {
                        data = URLEncoder.encode("username", "UTF-8")
                                + "=" + URLEncoder.encode(userName, "UTF-8");
                        data += "&" + URLEncoder.encode("password", "UTF-8") + "="
                                + URLEncoder.encode(passWord, "UTF-8");
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
                                URL url = new URL(LOGIN_URL);

                                // Send POST data request
                                System.out.println("URL:" + LOGIN_URL);
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
                                            Toast.makeText(MainActivity.this, "Logging in!", Toast.LENGTH_SHORT).show();
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
                                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
//                                    intent.putExtra("key", object.getString("key"));
//                                    finish();
                                    startActivity(intent);
                                }else{
                                    System.out.println("Password failed");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            createAndShowDialog("Drunk?! Please check your credentials", "Error");
                                        }
                                    });
                                }
                            } catch (Exception ex) {
                                System.out.println(ex);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        createAndShowDialog("Drunk?! Please check your credentials", "Error");
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
}
