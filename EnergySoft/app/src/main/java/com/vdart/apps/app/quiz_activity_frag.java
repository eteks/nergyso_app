package com.vdart.apps.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import static com.vdart.apps.app.MainActivity.MyPREFERENCES;

public class quiz_activity_frag extends AppCompatActivity implements Download_data.download_complete{

    Toolbar toolbar;
    String SERVER_URL, POLLS_URL = "api/polls/";
    String POLLS_POST_URL = "api/pollsresult_post/";
    String POLLS_CHECK_URL = "api/polls_employee_result_exists/";
    TextView questionText;
    RadioButton answerA, answerB, answerC, answerD, answerE, answer;
    RadioGroup answers;
    int[] answersId = new int[5];
    Button vote, next, previous;
    int questionId =0;
    int answerId = 0;
    int NEXT_QUESTION_ID = 0, CURRENT_QUESTION_ID = 0, PREVIOUS_QUESTION_ID = 0, TOTAL_QUESTIONS = 0;
    boolean answered = false;
    int employee_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_one);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SERVER_URL = getString(R.string.service_url);
        POLLS_URL = SERVER_URL + POLLS_URL;
        POLLS_POST_URL = SERVER_URL + POLLS_POST_URL ;
        POLLS_CHECK_URL = SERVER_URL + POLLS_CHECK_URL;

        questionText = (TextView) findViewById(R.id.questionText);
        answerA = (RadioButton) findViewById(R.id.answerA);
        answerA.setVisibility(View.GONE);
        answerB = (RadioButton) findViewById(R.id.answerB);
        answerB.setVisibility(View.GONE);
        answerC = (RadioButton) findViewById(R.id.answerC);
        answerC.setVisibility(View.GONE);
        answerD = (RadioButton) findViewById(R.id.answerD);
        answerD.setVisibility(View.GONE);
        answerE = (RadioButton) findViewById(R.id.answerE);
        answerE.setVisibility(View.GONE);

        answers = (RadioGroup) findViewById(R.id.answers);

        employee_id = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).getInt("id",0);

        answer = (RadioButton) findViewById(answers.getCheckedRadioButtonId());

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            if(extras.getInt("next",0) > 0){
                CURRENT_QUESTION_ID = extras.getInt("next",0);
            }else if(extras.getInt("previous",0) > 0){
                CURRENT_QUESTION_ID = extras.getInt("previous",0);
            }
            Download_data download_data = new Download_data((Download_data.download_complete) this);
            download_data.download_data_from_link(POLLS_URL);
        }else {
            Download_data download_data = new Download_data((Download_data.download_complete) this);
            download_data.download_data_from_link(POLLS_URL);
        }

        next = (Button) findViewById(R.id.next);
        next.setVisibility(View.GONE);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(answered){
                    Intent intent = new Intent(quiz_activity_frag.this,quiz_activity_frag.class);
                    intent.putExtra("next", NEXT_QUESTION_ID);
                    startActivity(intent);
                }else{
                    createAndShowDialog("Please vote the current one","Error");
                }
            }
        });

        previous = (Button) findViewById(R.id.previous);
        previous.setVisibility(View.GONE);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(answered){
                    Intent intent = new Intent(quiz_activity_frag.this,quiz_activity_frag.class);
                    intent.putExtra("next", PREVIOUS_QUESTION_ID);
                    startActivity(intent);
                }else{
                    createAndShowDialog("Please vote the current one","Error");
                }
            }
        });

        vote = (Button) findViewById(R.id.vote);
        vote.setVisibility(View.GONE);
        vote.setOnClickListener(new View.OnClickListener(){
            int ONE_TIME = 0;
            @Override
            public void onClick(View view) {
                if((questionId > 0) && (answerId > 0)) {
                    String data = null;
                    try {

                        data = URLEncoder.encode("pollsresult_question", "UTF-8")
                                + "=" + questionId;
                        data += "&" + URLEncoder.encode("pollsresult_answer", "UTF-8") + "="
                                + answerId;
                        data += "&" + URLEncoder.encode("pollsresult_employee", "UTF-8") + "="
                                + employee_id;

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
                                URL url = new URL(POLLS_POST_URL);

                                // Send POST data request
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
                                if (object.has("success")) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            answered = true;
                                            Toast.makeText(quiz_activity_frag.this, "Voted successfully!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            createAndShowDialog("Please check your connections", "Error");
                                        }
                                    });
                                }
                            } catch (Exception ex) {
                                System.out.println(ex);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        createAndShowDialog("Please check your connections", "Error");
                                    }
                                });
                            } finally {
                                try {
                                    reader[0].close();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                            return null;
                        }

                    };
                    runAsyncTask(task);
                }else {
                    createAndShowDialog("Please try later!","Error");
                }
            }
        });

    }

    public void getRadioChecked(View view){
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.answerA:
                if (checked)
                    answerId = answersId[0];
                    break;
            case R.id.answerB:
                if (checked)
                    answerId = answersId[1];
                    break;
            case R.id.answerC:
                if (checked)
                    answerId = answersId[2];
                break;
            case R.id.answerD:
                if (checked)
                    answerId = answersId[3];
                break;
            case R.id.answerE:
                if (checked)
                    answerId = answersId[4];
                break;
        }
    }

    public void get_data(String data)
    {
        try {
            JSONArray data_array = new JSONArray(data);
            System.out.println("Object"+data_array);
            TOTAL_QUESTIONS = data_array.length();
            if(CURRENT_QUESTION_ID < TOTAL_QUESTIONS && CURRENT_QUESTION_ID >= 0){
                final JSONObject obj=new JSONObject(data_array.get(CURRENT_QUESTION_ID).toString());
                PREVIOUS_QUESTION_ID = CURRENT_QUESTION_ID - 1 ;
                NEXT_QUESTION_ID = CURRENT_QUESTION_ID + 1;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getData(obj);
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        createAndShowDialog("No further questions","Over!");
                        Intent intent = new Intent(quiz_activity_frag.this,BannerActivity.class);
                        startActivity(intent);
                    }
                });
            }
        } catch (JSONException e) {
            createAndShowDialog(e,"No connection");
            e.printStackTrace();
        }

    }

    public void getData (JSONObject obj){
        try{
            questionText.setText(obj.getString("question"));
            questionId = obj.getInt("id");
            checkAnswered(questionId);
            vote.setVisibility(View.VISIBLE);
            next.setVisibility(View.VISIBLE);
            previous.setVisibility(View.VISIBLE);
            JSONArray answersArray = obj.getJSONArray("answers");
            for(int j = 0; j < answersArray.length(); j++){
                switch(j){
                    case 0 :{
                        JSONObject answers = new JSONObject(answersArray.get(j).toString());
                        answerA.setVisibility(View.VISIBLE);
                        answerA.setText(answers.getString("answer"));
                        answersId[j] = answers.getInt("id");
                        break;
                    }
                    case 1 :{
                        JSONObject answers = new JSONObject(answersArray.get(j).toString());
                        answerB.setVisibility(View.VISIBLE);
                        answerB.setText(answers.getString("answer"));
                        answersId[j] = answers.getInt("id");
                        break;
                    }
                    case 2 :{
                        JSONObject answers = new JSONObject(answersArray.get(j).toString());
                        answerC.setVisibility(View.VISIBLE);
                        answerC.setText(answers.getString("answer"));
                        answersId[j] = answers.getInt("id");
                        break;
                    }
                    case 3 :{
                        JSONObject answers = new JSONObject(answersArray.get(j).toString());
                        answerD.setVisibility(View.VISIBLE);
                        answerD.setText(answers.getString("answer"));
                        answersId[j] = answers.getInt("id");
                        break;
                    }
                    case 4 :{
                        JSONObject answers = new JSONObject(answersArray.get(j).toString());
                        answerE.setVisibility(View.VISIBLE);
                        answerE.setText(answers.getString("answer"));
                        answersId[j] = answers.getInt("id");
                        break;
                    }
                }
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }

    }

    public void checkAnswered(int questionId){
        String data = null;
        try {

            data = URLEncoder.encode("pollsresult_question", "UTF-8")
                    + "=" + questionId;
            data += "&" + URLEncoder.encode("pollsresult_employee", "UTF-8") + "="
                    + employee_id;

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
                    URL url = new URL(POLLS_CHECK_URL);

                    // Send POST data request
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
                    final JSONObject object = new JSONObject(sb.toString());
                    if (object.getInt("exists") == 1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                answered = true;
                                vote.setText("REVOTE");
                                for(int i = 0; i < answersId.length; i++){
                                    try {
                                        if(answersId[i] == object.getInt("answer_id")){
                                            answerId = object.getInt("answer_id");
                                            switch (i){
                                                case 0 : answerA.toggle(); break;
                                                case 1 : answerB.toggle(); break;
                                                case 2 : answerC.toggle(); break;
                                                case 3 : answerD.toggle(); break;
                                                case 4 : answerE.toggle(); break;
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                createAndShowDialog("Please check your connections", "Error");
                            }
                        });
                    }
                } catch (Exception ex) {
                    System.out.println(ex);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            createAndShowDialog("Please check your connections", "Error");
                        }
                    });
                } finally {
                    try {

                        reader[0].close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                return null;
            }

        };
        runAsyncTask(task);

    }

    private void createAndShowDialog(final String message, final String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }

    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }

    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if(exception.getCause() != null){
            ex = exception.getCause();
        }
        createAndShowDialog(ex.getMessage(), title);
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

            case R.id.shoutout:
                intent = new Intent(this,ListingMore.class);
                intent.putExtra("more","shoutout");
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
