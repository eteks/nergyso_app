package com.example.user.energysoft;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.math.BigDecimal;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import static com.example.user.energysoft.MainActivity.MyPREFERENCES;

public class ProfileActivity extends AppCompatActivity implements  Download_data.download_complete{
    Toolbar toolbar;
    TextView empId, empName, empEmail, empDob, empMobile, empDesignation, empBloodGroup, empDoj, empAddress, empAadharId, empDeptId, EmpExpYears;
    ImageView empPhoto;
    String SERVER_URL;
    String EMPLOYEE_URL ;
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
//        ImageView home = (ImageView) findViewById(R.id.action_home);
//        home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ProfileActivity.this, GridList.class);
//                startActivity(intent);
//            }
//        });
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
    }

    public void get_data(String data)
    {
       try {
                JSONObject obj = (JSONObject) new JSONTokener(data).nextValue();
                System.out.println("Object"+obj);
                empName.setText(obj.getString("employee_name"));
                empEmail.setText(obj.getString("employee_email"));
                empDob.setText(obj.getString("employee_dob"));
                empMobile.setText(obj.getString("employee_mobile"));
                empDesignation.setText(obj.getString("employee_designation"));
                empBloodGroup.setText(obj.getString("employee_bloodgroup"));
                empDoj.setText(obj.getString("employee_doj"));
                empAddress.setText(obj.getString("employee_address"));
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
                editor.commit();
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
                intent = new Intent(ProfileActivity.this,GridList.class);
                startActivity(intent);
                return true;

            case R.id.profile:
                intent = new Intent(ProfileActivity.this,ProfileActivity.class);
                startActivity(intent);
                return true;

            case R.id.events:
                intent = new Intent(ProfileActivity.this,EventMain.class);
                startActivity(intent);
                return true;

            case R.id.news:
                intent = new Intent(ProfileActivity.this,NewsMain.class);
                startActivity(intent);
                return true;

            case R.id.shoutout:
                intent = new Intent(ProfileActivity.this,Shoutout.class);
                startActivity(intent);
                return true;

            case R.id.gallery:
                Toast.makeText(ProfileActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.info:
                Toast.makeText(ProfileActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.settings:
                intent = new Intent(ProfileActivity.this,Changepassword_Activity.class);
                startActivity(intent);
                return true;

            case R.id.logout:
                intent = new Intent(ProfileActivity.this,MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.facebook:
                intent = new Intent(ProfileActivity.this,FacebookActivity.class);
                startActivity(intent);
                return true;

            case R.id.twitter:
                intent = new Intent(ProfileActivity.this,TwitterActivity.class);
                startActivity(intent);
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
