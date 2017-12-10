package com.example.user.energysoft;

import android.icu.math.BigDecimal;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView empId, empName, empEmail, empDob, empMobile, empDesignation, empBloodGroup, empDoj, empAddress, empAadharId, empDeptId, EmpExpYears;
    ImageView empPhoto;
    JSONObject jsonObject = new JSONObject();
    String str = "{\"employee\":{\"employee_id\":\"EMP001\",\"employee_name\":\"Harihara prabu U\",\"employee_dob\":\"24-04-1995\",\"employee_email\":\"harihara@etekchnoservices.com\",\"employee_mobile\":\"97900 22747\",\"employee_doj\":\"01-08-2017\",\"employee_designation\":\"Software Developer\",\"employee_photo\":\"http://www.lucidian.net/assets/pages/media/profile/people19.png\",\"employee_bloodgroup\":\"B +ve\",\"employee_address\":\"No: 17, Nalla thanni kinaru street, Kosapalayam, Puducherry-13.\",\"employee_aadhar_id\":\"8521 4785 2369\",\"employee_experience_in_years\":\"0.5\",\"employee_depaartment_id\":\"258\"}}";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        empAadharId = (TextView) findViewById(R.id.empAadharId);
        empDeptId = (TextView) findViewById(R.id.empDeptId);
        EmpExpYears = (TextView) findViewById(R.id.EmpExpYears);
        empPhoto = (ImageView) findViewById(R.id.empPhoto);
//        try {
//            URL url = new URL("http://");
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//
//            try{
//                urlConnection.connect();

        try {
            JSONObject object = (JSONObject) new JSONTokener(str).nextValue();
            final JSONObject obj = object.getJSONObject("employee");
            empId.setText(obj.getString("employee_id"));
            empName.setText(obj.getString("employee_name"));
            empEmail.setText(obj.getString("employee_email"));
            empDob.setText(obj.getString("employee_dob"));
            empMobile.setText(obj.getString("employee_mobile"));
            empDesignation.setText(obj.getString("employee_designation"));
            empBloodGroup.setText(obj.getString("employee_bloodgroup"));
            empDoj.setText(obj.getString("employee_doj"));
            empAddress.setText(obj.getString("employee_address"));
            empAadharId.setText(obj.getString("employee_aadhar_id"));
            empDeptId.setText(obj.getString("employee_depaartment_id"));
            String empExperience = obj.getString("employee_experience_in_years");
//            String[] mySplit = empExperience.split("\\.");
//            Double years = new Double(mySplit[0]);
//            Double months = new Double(mySplit[1]);
//
//            String experience = years+" "+"Years"+" "+months+" "+"Months";
            EmpExpYears.setText(empExperience);
            loadImageFromUrl(obj.getString("employee_photo"));
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
}
