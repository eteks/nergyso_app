package com.vdart.apps.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import static com.vdart.apps.app.MainActivity.MyPREFERENCES;

public class ProfileActivity extends AppCompatActivity implements  Download_data.download_complete{
    Toolbar toolbar;
    TextView empId, empName, empEmail, empDob, empMobile, empDesignation, empBloodGroup, empDoj, empAddress, empAadharId, empDeptId, EmpExpYears;
    ImageView empPhoto;
    String SERVER_URL;
    String EMPLOYEE_URL ;
    Menu menuInflate;
    String notification_count = "";
    int NOTIFICATION_COUNT = 0;
    String NOTIFICATION_COUNT_URL = "api/notification/notification_employee_unread_count";
    JSONObject jsonObject = new JSONObject();
    String str = "{\"employee\":{\"employee_id\":\"EMP001\",\"employee_name\":\"Harihara prabu U\",\"employee_dob\":\"24-04-1995\",\"employee_email\":\"harihara@etekchnoservices.com\",\"employee_mobile\":\"97900 22747\",\"employee_doj\":\"01-08-2017\",\"employee_designation\":\"Software Developer\",\"employee_photo\":\"http://www.lucidian.net/assets/pages/media/profile/people19.png\",\"employee_bloodgroup\":\"B +ve\",\"employee_address\":\"No: 17, Nalla thanni kinaru street, Kosapalayam, Puducherry-13.\",\"employee_aadhar_id\":\"8521 4785 2369\",\"employee_experience_in_years\":\"0.5\",\"employee_depaartment_id\":\"258\"}}";
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SERVER_URL = getString(R.string.service_url);
        EMPLOYEE_URL = SERVER_URL+ "api/employee/";
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        ImageView home = (ImageView) findViewById(R.id.action_home);
//        home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ProfileActivity.this, GridList.class);
//                startActivity(intent);
//            }
//        });
        notification_count = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).getString("nc","");

        NOTIFICATION_COUNT_URL = SERVER_URL + NOTIFICATION_COUNT_URL;
        getNotificationCount();

        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        System.out.println("USer : "+shared.getInt("id",0));
        EMPLOYEE_URL = EMPLOYEE_URL+shared.getInt("id",0)+"/";
        toolbar.setTitleTextColor(0xFFFFFFFF);
        empId = (TextView) findViewById(R.id.empId);
        empName = (TextView) findViewById(R.id.empName);
        empEmail = (TextView) findViewById(R.id.empEmail);
        empDob = (TextView) findViewById(R.id.empDob);
        empMobile = (TextView) findViewById(R.id.empMobile);
        empDesignation = (TextView) findViewById(R.id.empDesignation);
        empBloodGroup = (TextView) findViewById(R.id.empBloodGroup);
        empDoj = (TextView) findViewById(R.id.empDoj);
        empAddress = (TextView) findViewById(R.id.empAddress);
//        empAadharId = (TextView) findViewById(R.id.empAadharId);
        empDeptId = (TextView) findViewById(R.id.empDeptId);
        EmpExpYears = (TextView) findViewById(R.id.EmpExpYears);
        empPhoto = (ImageView) findViewById(R.id.empPhoto);
        Download_data download_data = new Download_data((Download_data.download_complete) this);
        download_data.download_data_from_link(EMPLOYEE_URL);

//        Click Logo to home screen

        ImageView imageButton = (ImageView) toolbar.findViewById(R.id.vdart_logo);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, BannerActivity.class);
                startActivity(intent);
            }
        });

    }

    public void getNotificationCount(){
        String data = null;
        try {

            data = URLEncoder.encode("notification_employee", "UTF-8")
                    + "=" + getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).getInt("id",0);;
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
                    // Defined URL  where to send data
                    URL url = new URL(NOTIFICATION_COUNT_URL);

                    // Send POST data request
                    System.out.println("URL:" + NOTIFICATION_COUNT_URL);
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(finalData);
                    wr.flush();
                    System.out.println(finalData);
                    // Get the server response

                    reader[0] = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader[0].readLine()) != null) {
                        // Append server response in string
                        sb.append(line + "\n");
                    }
                    System.out.println("Output" + sb.toString());
                    JSONObject obj = new JSONObject(sb.toString());
                    if(obj.has("unread")){
                        NOTIFICATION_COUNT = obj.getInt("unread");
                    }
                    setCount(ProfileActivity.this, String.valueOf(NOTIFICATION_COUNT));
                }catch (Exception ex) {
                    System.out.println(ex);
                } finally {
                    try {
                        reader[0].close();
                    } catch (Exception ex) {
                    }
                }
                return null;
            }

        };
        runAsyncTask(task);

//        Download_data download_data = new Download_data((Download_data.download_complete) this);
//        download_data.download_data_from_link(NOTIFICATION_URL);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        setCount(this, String.valueOf(NOTIFICATION_COUNT));
        return  true;
    }

    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }

    public void get_data(String data)
    {
       try {
                JSONObject obj = (JSONObject) new JSONTokener(data).nextValue();
                System.out.println("Object"+obj);
                empName.setText(obj.getString("employee_id").toUpperCase());
                empEmail.setText(obj.getString("employee_email"));
                empDob.setText(obj.getString("employee_dob"));
                empMobile.setText(obj.getString("employee_mobile"));
                empDesignation.setText(obj.getString("employee_designation"));
                empBloodGroup.setText(obj.getString("employee_bloodgroup"));
                empDoj.setText(obj.getString("employee_doj"));
                empAddress.setText(obj.getString("employee_address"));
                empId.setText(obj.getString("employee_name"));
//                empAadharId.setText(obj.getString("employee_aadhar_id"));
                empDeptId.setText(obj.getString("employee_department_name"));
                int empExperience = obj.getInt("employee_experience_in_years");
//            String[] mySplit = empExperience.split("\\.");
//            Double years = new Double(mySplit[0]);
//            Double months = new Double(mySplit[1]);
//
//            String experience = years+" "+"Years"+" "+months+" "+"Months";
                EmpExpYears.setText("\""+empExperience+"\"");
                loadImageFromUrl((SERVER_URL+obj.getString("employee_photo")));
                SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("employee_dob", obj.getString("employee_dob"));
                editor.putString("employee_photo", obj.getString("employee_photo"));
                editor.putString("employee_doj",obj.getString("employee_doj"));
                editor.putString("employee_designation",obj.getString("employee_designation"));
                editor.commit();
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    private void loadImageFromUrl(String employee_photo) {
        Picasso.with(this).load(employee_photo).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
                .into(empPhoto, new com.squareup.picasso.Callback(){

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    // Initiating Menu XML file (menu.xml)
    public void setCount(Context context, String count) {
        MenuItem menuItem = (MenuItem) menuInflate.findItem(R.id.action_notification);
        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();

        CountDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_group_count);
        if (reuse != null && reuse instanceof CountDrawable) {
            badge = (CountDrawable) reuse;
        } else {
            badge = new CountDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_group_count, badge);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menuInflate = menu;
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        setCount(this,notification_count);
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
                intent = new Intent(this,BannerActivity.class);
                startActivity(intent);
                return true;

            case R.id.profile:
                intent = new Intent(this,ProfileActivity.class);
                startActivity(intent);
                return true;

            case R.id.feedback:
                intent = new Intent(this,Feedback.class);
                startActivity(intent);
                return true;

            case R.id.action_search:
                intent = new Intent(this,SearchActivity.class);
                startActivity(intent);
                return true;

            case R.id.events:
                intent = new Intent(this,EventMain.class);
                startActivity(intent);
                return true;

            case R.id.news:
                intent = new Intent(this,NewsMain.class);
                startActivity(intent);
                return true;

            case R.id.gallery:
                intent = new Intent(this,EventGallery.class);
                startActivity(intent);
                return true;

            case R.id.info:
                Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.settings:
                intent = new Intent(this,Changepassword_Activity.class);
                startActivity(intent);
                return true;

            case R.id.logout:
                intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.facebook:
                intent = new Intent(this,FacebookActivity.class);
                startActivity(intent);
                return true;

            case R.id.twitter:
                intent = new Intent(this,TwitterActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_notification:
                intent = new Intent(this,NotificationMain.class);
                startActivity(intent);
                return true;

            case R.id.ceomsg:
                intent = new Intent(this,CeomessageActivity.class);
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

            case R.id.vdart_logo:
                intent = new Intent(this,BannerActivity.class);
                startActivity(intent);
                return true;

            case R.id.shoutout:
                intent = new Intent(this,ListingMore.class);
                intent.putExtra("more","shoutout");
                startActivity(intent);
                return true;

            case R.id.action_refresh:
//                intent = new Intent(this,BannerActivity.class);
                startActivity(getIntent());
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createAndShowDialog(final String message, final String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }

    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if(exception.getCause() != null){
            ex = exception.getCause();
        }
        createAndShowDialog(ex.getMessage(), title);
    }

    private void createAndShowDialogFromTask(final Exception exception, String title) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createAndShowDialog(exception, "Error");
            }
        });
    }


}
