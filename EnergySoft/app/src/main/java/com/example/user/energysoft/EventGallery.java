package com.example.user.energysoft;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EventGallery extends AppCompatActivity implements Download_data.download_complete{

    GalleryAdapter mAdapter;
    RecyclerView mRecyclerView;

    private static final String TAG = "EventGallery";
    String listValue = "NULL";
    EventMain.PaginationAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    String nextPage;
    String SERVER_URL ;
    EventMain main;
    String GALLERY_URL ;
    RecyclerView rv;
    ProgressBar progressBar;

    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;
    public ListView list;
    public ArrayList<Event> eventList = new ArrayList<Event>();
    public ListAdapter NewsAdapter;

    ArrayList<ImageModel> images = new ArrayList<>();

    public static String IMGS[] = {
            "https://images.unsplash.com/photo-1444090542259-0af8fa96557e?q=80&fm=jpg&w=1080&fit=max&s=4b703b77b42e067f949d14581f35019b",
            "https://images.unsplash.com/photo-1439546743462-802cabef8e97?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300",
            "https://images.unsplash.com/photo-1441155472722-d17942a2b76a?q=80&fm=jpg&w=1080&fit=max&s=80cb5dbcf01265bb81c5e8380e4f5cc1",
            "https://images.unsplash.com/photo-1437651025703-2858c944e3eb?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300",
            "https://images.unsplash.com/photo-1431538510849-b719825bf08b?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300",
            "https://images.unsplash.com/photo-1434873740857-1bc5653afda8?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300",
            "https://images.unsplash.com/photo-1439396087961-98bc12c21176?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300",
            "https://images.unsplash.com/photo-1433616174899-f847df236857?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300",
            "https://images.unsplash.com/photo-1438480478735-3234e63615bb?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300",
            "https://images.unsplash.com/photo-1438027316524-6078d503224b?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_gallery);

//        for (int i = 0; i < 1; i++) {

            ImageModel imageModel = new ImageModel();
            imageModel.setName("");
            imageModel.setUrl("https://d2q79iu7y748jz.cloudfront.net/s/_logo/41e2d8289eeca06856d4d543321be3cf.png");
            images.add(imageModel);

//        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SERVER_URL = getString(R.string.service_url);
        GALLERY_URL = SERVER_URL+ "api/gallery/";

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(EventGallery.this, 3));
        mRecyclerView.setHasFixedSize(true);


        mAdapter = new GalleryAdapter(EventGallery.this, images);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(EventGallery.this, DetailActivity.class);
                        intent.putParcelableArrayListExtra("data", images);
                        intent.putExtra("pos", position);
                        startActivity(intent);

                    }
                }));

        Download_data download_data = new Download_data((Download_data.download_complete) this);
        download_data.download_data_from_link(GALLERY_URL);

    }

    public void get_data(final String data)
    {
        try {
            JSONObject object = new JSONObject(data);
            System.out.println("Object"+object);
            JSONArray data_array = object.getJSONArray("results");
            System.out.println("Object"+data_array);
            if(data_array.length() == 0){
                createAndShowDialog("Server Error","No connection");
            }
            for (int i = 0 ; i < data_array.length() ; i++)
            {
                JSONObject obj=new JSONObject(data_array.get(i).toString());
                String splitted_gallery[] = obj.getString("gallery_image").split(",");
                for(int index = 0; index < splitted_gallery.length; index++){
                    final ImageModel imageModel = new ImageModel();
//                    imageModel.setName(obj.getString("gallery_title"));
                    imageModel.setUrl(SERVER_URL+splitted_gallery[index]);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            images.add(imageModel);
                        }
                    });
                }
            }
        } catch (JSONException e) {
            createAndShowDialog(e,"No connection");
//            loadFirstPage();
            e.printStackTrace();
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
                intent = new Intent(EventGallery.this,BannerActivity.class);
                startActivity(intent);
                return true;

            case R.id.feedback:
                intent = new Intent(EventGallery.this,Feedback.class);
                startActivity(intent);
                return true;

            case R.id.action_search:
                intent = new Intent(EventGallery.this,SearchActivity.class);
                startActivity(intent);
                return true;

            case R.id.profile:
                intent = new Intent(EventGallery.this,ProfileActivity.class);
                startActivity(intent);
                return true;

            case R.id.events:
                intent = new Intent(EventGallery.this,EventMain.class);
                startActivity(intent);
                return true;

            case R.id.news:
                intent = new Intent(EventGallery.this,NewsMain.class);
                startActivity(intent);
                return true;

            case R.id.shoutout:
                intent = new Intent(EventGallery.this,Shoutout.class);
                startActivity(intent);
                return true;

            case R.id.gallery:
                intent = new Intent(EventGallery.this,EventGallery.class);
                startActivity(intent);
                return true;

            case R.id.info:
                Toast.makeText(EventGallery.this, "Coming soon", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.settings:
                intent = new Intent(EventGallery.this,Changepassword_Activity.class);
                startActivity(intent);
                return true;

            case R.id.logout:
                intent = new Intent(EventGallery.this,MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.facebook:
                intent = new Intent(EventGallery.this,FacebookActivity.class);
                startActivity(intent);
                return true;

            case R.id.twitter:
                intent = new Intent(EventGallery.this,TwitterActivity.class);
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
