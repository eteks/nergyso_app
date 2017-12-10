package com.example.user.energysoft;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

public class NewsMain extends AppCompatActivity implements Download_data.download_complete {
    Toolbar toolbar;
    String event = "{\"events\":[ {\"id\":\"1\",\"events_title\":\"Event-1\",\"events_description\":\"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\",\"events_venue\":\"Pondicherry\",\"events_image\":\"http://www.fotothing.com/photos/df3/df309f51ae73de90d1ea09aa2e3d312d.jpg\",\"events_video\":\"http://www.fotothing.com/photos/df3/df309f51ae73de90d1ea09aa2e3d312d.jpg\",\"events_document\":\"https://www.google.co.in/url?sa=t&rct=j&q=&esrc=s&source=web&cd=4&cad=rja&uact=8&ved=0ahUKEwjM99DFtfzXAhVMqo8KHXxEDg0QFgg5MAM&url=http%3A%2F%2Funec.edu.az%2Fapplication%2Fuploads%2F2014%2F12%2Fpdf-sample.pdf&usg=AOvVaw2a4IUOgpX0IaeRxQpCN2l0\",\"events_date\":\"21-07-2017\"]}";
    public ListView list;
    public ArrayList<news> news = new ArrayList<news>();
    public ListAdapter NewsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println(event);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        list = (ListView) findViewById(R.id.newslist);
        NewsAdapter = new com.example.user.energysoft.ListAdapter(this);
        list.setAdapter(NewsAdapter);
        Download_data download_data = new Download_data((Download_data.download_complete) this);
        download_data.download_data_from_link("http://www.kaleidosblog.com/tutorial/tutorial.json");
    }

    public void get_data(String data)
    {

        try {
            JSONArray data_array=new JSONArray(data);

            for (int i = 0 ; i < data_array.length() ; i++)
            {
                JSONObject obj=new JSONObject(data_array.get(i).toString());

                news add=new news();
                add.news_title = obj.getString("news_title");
                add.news_description = obj.getString("news_decription");
                System.out.println("News title"+obj.getString("news_title"));
                news.add(add);

            }

//            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }

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

//    private void refreshItemsFromTable() {
//        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
//            @Override
//            protected Void doInBackground(Void... params) {
//
//                try {
//                    JSONObject object = (JSONObject) new JSONTokener(event).nextValue();
//                    final List<news> results = (List<news>) object;
//
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            NewsAdapter.clear();
//
//                            for (news item : results) {
//                                NewsAdapter.add(item);
//                            }
//                        }
//                    });
//                } catch (final Exception e){
//                    createAndShowDialogFromTask(e, "Error");
//                }
//
//                return null;
//            }
//        };
//
//        runAsyncTask(task);
//    }

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

    private void createAndShowDialogFromTask(final Exception exception, String title) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createAndShowDialog(exception, "Error");
            }
        });
    }

}
