package com.vdart.example.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vdart.example.app.utils.PaginationScrollListener;
import com.squareup.picasso.Picasso;

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
import java.util.ArrayList;
import java.util.List;

import static com.vdart.example.app.MainActivity.MyPREFERENCES;

public class NotificationMain extends AppCompatActivity implements Download_data.download_complete {
    Toolbar toolbar;
    View view;
    private static final String TAG = "NotificationMain";
    String listValue = "NULL";
    PaginationAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    String nextPage;
    String SERVER_URL ;
    String NOTIFICATION_URL = "api/notification/notification_list_by_employee" ;
    RecyclerView rv;
    ProgressBar progressBar;
    String DOWNLOAD_URL, category = "" ;
    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;
    public ListView list;
    public ArrayList<Event> eventList = new ArrayList<Event>();
    public ListAdapter NewsAdapter;
    String notification[] = new String[100];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_main);
        SERVER_URL = getString(R.string.service_url);
        NOTIFICATION_URL = SERVER_URL+ NOTIFICATION_URL;
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        toolbar.setTitleTextColor(0xFFFFFFFF);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        int id = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).getInt("id",0);
        rv = (RecyclerView) findViewById(R.id.main_recycler);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);

        adapter = new PaginationAdapter(this);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);

        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);

        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });


        // mocking network delay for API call
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadFirstPage();
            }
        }, 1000);
        String data = null;
        try {

            data = URLEncoder.encode("notification_employee", "UTF-8")
                    + "=" + id;
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
                    URL url = new URL(NOTIFICATION_URL);

                    // Send POST data request
                    System.out.println("URL:" + NOTIFICATION_URL);
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
                    System.out.println("Output"+sb.toString());
                    JSONArray data_array = new JSONArray(sb.toString());

                    for (int i = 0 ; i < data_array.length() ; i++)
                    {
                        final JSONObject obj=new JSONObject(data_array.get(i).toString());
                        System.out.println("Object"+obj);
                        notification[i] = obj.getString("notification_created_date");
                        category = obj.getString("notification_cateogry").toLowerCase();
                        switch(category) {
                            case "events": {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            showEvents(obj.getInt("notification_cateogry_id"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                break;
                            }
                            case "news": {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            showNews(obj.getInt("notification_cateogry_id"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                break;
                            }
                            case "live": {
//                                showLive();
                                break;
                            }
                            case "birthday":{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                            showBirthday();
                                    }
                                });
                                break;
                            }
                            case "anniversary":{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showAnniversary();
                                    }
                                });
                                break;
                            }
                            case "shoutout":{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            showShoutout(obj.getInt("notification_cateogry_id"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                break;
                            }
                            case "ceo":{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            showCeoMessage(obj.getInt("notification_cateogry_id"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                break;
                            }
                        }
                    }
                } catch (Exception ex) {
                    System.out.println(ex);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            mProgressBar.setVisibility(ProgressBar.GONE);
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

//        Download_data download_data = new Download_data((Download_data.download_complete) this);
//        download_data.download_data_from_link(NOTIFICATION_URL);
    }

    // To display the events from Notification
    private void showEvents(int id){
        DOWNLOAD_URL = SERVER_URL + "api/events/" + id + "/";
        Download_data download_data = new Download_data((Download_data.download_complete) this);
        download_data.download_data_from_link(DOWNLOAD_URL);
    }

    // To display the shoutout from Notification
    private void showShoutout(int id){
        DOWNLOAD_URL = SERVER_URL + "api/shoutout_list/" + id + "/";
        Download_data download_data = new Download_data((Download_data.download_complete) this);
        download_data.download_data_from_link(DOWNLOAD_URL);
    }

    // To display the CEO's message from Notification
    private void showCeoMessage(int id){
        DOWNLOAD_URL = SERVER_URL + "api/ceo_message_list/" + id + "/";
        Download_data download_data = new Download_data((Download_data.download_complete) this);
        download_data.download_data_from_link(DOWNLOAD_URL);
    }

    // To display the NEWS from Notification
    private void showNews(int id){
        DOWNLOAD_URL = SERVER_URL + "api/news/" + id + "/";
        Download_data download_data = new Download_data((Download_data.download_complete) this);
        download_data.download_data_from_link(DOWNLOAD_URL);
    }

    // To display the birthday from Notification
    private void showBirthday(){
        final Event add=new Event();
        add.setTitle("Happy Birthday! Wish you many more happy returns of the day. Have a wonderful year ahead.");
        add.setDescription("");
        add.setImage("1");
        add.setType("birthday");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                        progressBar.setVisibility(View.GONE);
                adapter.add(add);
            }
        });
    }

    // To display the anniversary from Notification
    private void showAnniversary(){
        final Event add=new Event();
        add.setTitle("Happy Anniversary! Your hard work and dedication are vital to the success of our organization.");
        add.setDescription("");
        add.setImage("2");
        add.setType("anniversary");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                        progressBar.setVisibility(View.GONE);
                adapter.add(add);
            }
        });
    }


    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");
//        final List<News> newsList = News.createMovies(adapter.getItemCount());
//        Download_data download_data = new Download_data((Download_data.download_complete) this);
//        download_data.download_data_from_link(NOTIFICATION_URL);

//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                adapter.addAll(newsList);
//                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
//                else isLastPage = true;
//            }
//        });


    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);
//        final List<News> newsList = News.createMovies(adapter.getItemCount());
        Download_data download_data = new Download_data((Download_data.download_complete) this);
        System.out.println(nextPage);
        download_data.download_data_from_link(nextPage);
        isLoading = false;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                adapter.removeLoadingFooter();
//                adapter.addAll(newsList);
//                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
//                else isLastPage = true;
            }
        });

    }

    public void get_data(String data)
    {
        JSONObject obj = null;
        try {
            obj = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("Object"+obj);
        if(obj.has("events_title")){
            try {
                final Event add=new Event();
                add.setTitle(obj.getString("events_title"));
                add.setId(obj.getInt("id"));
                add.setDescription(obj.getString("events_description"));
                add.setType("events");
                String splitted_gallery[] = obj.getString("events_image").split("%2C");
                add.setImage(splitted_gallery[0]);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.add(add);
                    }
                });
            } catch (JSONException e) {
                createAndShowDialog(e,"No connection");
                loadFirstPage();
                e.printStackTrace();
            }
        }else if(obj.has("news_title")){
            try {
                final Event add=new Event();
                add.setTitle(obj.getString("news_title"));
                add.setId(obj.getInt("id"));
                add.setDescription(obj.getString("news_description"));
                String splitted_gallery[] = obj.getString("news_image").split("%2C");
                add.setImage(splitted_gallery[0]);
                add.setType("news");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.add(add);
                    }
                });
            } catch (JSONException e) {
                createAndShowDialog(e,"No connection");
                loadFirstPage();
                e.printStackTrace();
            }
        }else if(obj.has("shoutout_description")){
            try {
                final Event add=new Event();
                add.setTitle(obj.getString("shoutout_description"));
                add.setId(obj.getInt("id"));
                add.setImage(obj.getString("employee_from_profile"));
                add.setType("shoutout");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.add(add);
                    }
                });
            } catch (JSONException e) {
                createAndShowDialog(e,"No connection");
                loadFirstPage();
                e.printStackTrace();
            }
        }else if(obj.has("ceo_message")){
            try {
                final Event add=new Event();
                add.setTitle(obj.getString("ceo_message"));
                add.setId(obj.getInt("id"));
                add.setImage(obj.getString("ceo_employee_photo"));
                add.setType("ceo");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.add(add);
                    }
                });
            } catch (JSONException e) {
                createAndShowDialog(e,"No connection");
                loadFirstPage();
                e.printStackTrace();
            }
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

    public void clickList(){
        Intent intent = new Intent(NotificationMain.this,FullNews.class);
        startActivity(intent);
    }

    public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private static final int ITEM = 0;
        private static final int LOADING = 1;
        EventMain main;
        private List<Event> eventList;
        private Context context;
        private boolean isLoadingAdded = false;
        ImageView events_image;

        public PaginationAdapter(Context context) {
            this.context = context;
            eventList = new ArrayList<>();
        }

        public List<Event> getEventsList() {
            return eventList;
        }

        public void setEventsList(List<News> newsList) {
            this.eventList = eventList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());


            switch (viewType) {
                case ITEM:
                    viewHolder = getViewHolder(parent, inflater);
                    break;
                case LOADING:
                    View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                    viewHolder = new LoadingVH(v2);
                    break;
            }
            return viewHolder;
        }

        @NonNull
        private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, final LayoutInflater inflater) {
            final RecyclerView.ViewHolder viewHolder;
            View v1 = inflater.inflate(R.layout.notificationlist_layout, parent, false);
            viewHolder = new EventVH(v1);
            final View.OnClickListener mOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Event event = eventList.get(viewHolder.getAdapterPosition());
                    System.out.println("CLICKed"+event.getId());
                    int id = event.getId();
                    String check ;
                    String category = event.getType();
                    switch(category){
                        case "events":{
                            Intent intent = new Intent(NotificationMain.this,FullEvent.class);
                            check = "EVENTS";
                            intent.putExtra("id", id);
                            intent.putExtra("check",check);
                            startActivity(intent);
                            break;
                        }
                        case "news":{
                            Intent intent = new Intent(NotificationMain.this,FullNews.class);
                            check = "NEWS";
                            intent.putExtra("id", id);
                            intent.putExtra("check",check);
                            startActivity(intent);
                            break;
                        }
                        case "birthday":{
                            Intent intent = new Intent(NotificationMain.this,BannerActivity.class);
                            check = "birthday";
                            intent.putExtra("id", id);
                            intent.putExtra("check",check);
                            startActivity(intent);
                            break;
                        }
                        case "anniversary":{
                            Intent intent = new Intent(NotificationMain.this,BannerActivity.class);
                            check = "anniversary";
                            intent.putExtra("id", id);
                            intent.putExtra("check",check);
                            startActivity(intent);
                            break;
                        }
                        case "live" :{
                            break;
                        }
                        case "shoutout":{
                            break;
                        }
                        case "ceo" : {
                            break;
                        }
                    }

                }
            };
            v1.setOnClickListener(mOnClickListener);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        Movie movie = movies.get(position);
            Event event = eventList.get(position);

            switch (getItemViewType(position)) {
                case ITEM:
//                    if(category.equals("birthday")){
//                        EventVH eventVH = (EventVH) holder;
//                        eventVH.events_title.setText("Happy Birthday! Wish you many more happy returns of the day. Have a wonderful year ahead.");
//                        eventVH.events_image.setImageResource(R.drawable.birthday_blue);
//                    }
//                    else if(category.equals("anniversary")){
//                        EventVH eventVH = (EventVH) holder;
//                        eventVH.events_title.setText("Happy Anniversary! Your hard work and dedication are vital to the success of our organization.");
//                        eventVH.events_image.setImageResource(R.drawable.anniversary_blue);
//                    }else {
                        EventVH eventVH = (EventVH) holder;
                        eventVH.events_title.setText(event.getTitle());
                        eventVH.events_description.setText("Created on " + notification[position]);
                        if(event.getEvents_image().equals("1")){
                            eventVH.events_image.setImageResource(R.drawable.birthday_blue);
                        }else if(event.getEvents_image().equals("2")){
                            eventVH.events_image.setImageResource(R.drawable.anniversary_blue);
                        }else{
                            loadImageFromUrl(eventVH.events_image, (SERVER_URL + event.getEvents_image()));
                        }
//                    }
                    break;
                case LOADING:
//                Do nothing
                    break;
            }

        }

        @Override
        public int getItemCount() {
            return eventList == null ? 0 : eventList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return (position == eventList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
        }

    /*
   Helpers
   _________________________________________________________________________________________________
    */

        public void add(Event mc) {
            eventList.add(mc);
            notifyItemInserted(eventList.size() - 1);
        }

        public void addAll(List<Event> mcList) {
            for (Event mc : mcList) {
                add(mc);
            }
        }

        public void remove(Event city) {
            int position = eventList.indexOf(city);
            if (position > -1) {
                eventList.remove(position);
                notifyItemRemoved(position);
            }
        }

        public void clear() {
            isLoadingAdded = false;
            while (getItemCount() > 0) {
                remove(getItem(0));
            }
        }

        public boolean isEmpty() {
            return getItemCount() == 0;
        }


        public void addLoadingFooter() {
            isLoadingAdded = true;
            add(new Event());
        }

        public void removeLoadingFooter() {
            isLoadingAdded = false;

            int position = eventList.size() - 1;
            Event item = getItem(position);

            if (item != null) {
                eventList.remove(position);
                notifyItemRemoved(position);
            }
        }

        public Event getItem(int position) {
            return eventList.get(position);
        }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

        /**
         * Main list's content ViewHolder
         */
        protected class EventVH extends RecyclerView.ViewHolder {
            TextView events_title,events_description;
            ImageView events_image;
            //        ListAdapter.ViewHolderItem holder = new ListAdapter.ViewHolderItem();
            public EventVH(View itemView) {
                super(itemView);
//            ListAdapter.ViewHolderItem holder = new ListAdapter.ViewHolderItem();
//            if (convertView == null) {
//                LayoutInflater inflater = (LayoutInflater) main.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                convertView = inflater.inflate(R.layout.newslist_layout, null);

//            holder.name = (TextView) convertView.findViewById(R.id.name);
//            holder.code = (TextView) convertView.findViewById(R.id.code);
                events_title = (TextView) itemView.findViewById(R.id.news_title2);
                events_description = (TextView) itemView.findViewById(R.id.tvnewsreadmore);
                events_image = (ImageView) itemView.findViewById(R.id.news_image2);
//                news_description = (TextView) itemView.findViewById(R.id.news_description);
                System.out.println(itemView);
//                news_image = (ImageView) convertView.findViewById(R.id.news_image);
//            TextView news = (TextView) convertView.findViewById(R.id.news_title);
//                convertView.setTag(holder);
//            System.out.println("View "+this.main.news.get(position).news_title);
//            }
//            else
//            {
//                holder = (ListAdapter.ViewHolderItem) convertView.getTag();
//            }

            }
        }


        protected class LoadingVH extends RecyclerView.ViewHolder {

            public LoadingVH(View itemView) {
                super(itemView);
            }
        }



    }

    private void loadImageFromUrl(ImageView myImage,String employee_photo) {
        System.out.println("Image "+myImage+employee_photo);
        Picasso.with(this).load(employee_photo).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
                .into(myImage, new com.squareup.picasso.Callback(){

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
    }


}
