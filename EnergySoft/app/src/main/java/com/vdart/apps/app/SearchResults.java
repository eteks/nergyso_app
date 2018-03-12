package com.vdart.apps.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
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

import com.squareup.picasso.Picasso;
import com.vdart.apps.app.utils.PaginationScrollListener;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.vdart.apps.app.MainActivity.MyPREFERENCES;

/**
 * Created by user on 1/23/2018.
 */

public class SearchResults extends AppCompatActivity implements Download_data.download_complete{

    Toolbar toolbar;
    String SERVER_URL = "";
    String NEWS_SEARCH = "api/news/search_news/";
    String EVENTS_SEARCH = "api/events/search_events/";
    String SHOUTOUT_SEARCH = "api/shoutout/search_shoutout/";
    String SEARCH_URL = "";
    RecyclerView rv;
    PaginationAdapter adapter;
    LinearLayoutManager linearLayoutManager;
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
    int NOTIFICATION_COUNT = 0;
    String NOTIFICATION_COUNT_URL = "api/notification/notification_employee_unread_count";
    Menu menuInflate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        SERVER_URL = getString(R.string.service_url);

        rv = (RecyclerView) findViewById(R.id.main_recycler);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);

        adapter = new PaginationAdapter(this);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);

        rv.setItemAnimator(new DefaultItemAnimator());

        NOTIFICATION_COUNT_URL = SERVER_URL + NOTIFICATION_COUNT_URL;
        getNotificationCount();

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

            }
        }, 1000);
        String data = null;

        if(getIntent().getStringExtra("category").equals("NEWS")){
            SEARCH_URL = SERVER_URL + NEWS_SEARCH + getIntent().getStringExtra("search") + "/";
        }else if(getIntent().getStringExtra("category").equals("EVENTS")){
            SEARCH_URL = SERVER_URL + EVENTS_SEARCH + getIntent().getStringExtra("search") + "/";
        }else if(getIntent().getStringExtra("category").equals("SHOUTOUT")){
            SEARCH_URL = SERVER_URL + SHOUTOUT_SEARCH + getIntent().getStringExtra("search") + "/";
        }

        //Call the API to get the data
        Download_data download_data = new Download_data((Download_data.download_complete) this);
        download_data.download_data_from_link(SEARCH_URL);

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
                    setCount(SearchResults.this, String.valueOf(NOTIFICATION_COUNT));
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
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        setCount(this, String.valueOf(NOTIFICATION_COUNT));
        return  true;
    }

    @Override
    public void get_data(String data) {
        JSONArray array = null;
        if(data.length() == 2){
            createAndShowDialog("No results found","Empty");
            Intent intent = new Intent(SearchResults.this,SearchActivity.class);
            startActivity(intent);
        }
        try {
            array = new JSONArray(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("Object" + array);
        for (int i = 0 ; i < array.length() ; i++) {
            try {
                JSONObject obj = new JSONObject(array.get(i).toString());
                if(getIntent().getStringExtra("category").equals("EVENTS")){
                    final Event add = new Event();
                    add.setTitle(obj.getString("events_title"));
                    add.setId(obj.getInt("id"));
                    Date date = parseDate(obj.getString("created_date"));
                    SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
                    String strDate = formatter.format(date);
                    add.setDescription(strDate);
                    add.setType("events");
                    String splitted_gallery[] = obj.getString("events_image").split("%2C");
                    add.setImage(splitted_gallery[0]);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.add(add);
                        }
                    });
                }else if(getIntent().getStringExtra("category").equals("NEWS")){
                    final Event add = new Event();
                    add.setTitle(obj.getString("news_title"));
                    add.setId(obj.getInt("id"));
                    Date date = parseDate(obj.getString("created_date"));
                    SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
                    String strDate = formatter.format(date);
                    add.setDescription(strDate);
                    String splitted_gallery[] = obj.getString("news_image").split("%2C");
                    add.setImage(splitted_gallery[0]);
                    add.setType("news");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.add(add);
                        }
                    });
                }else if(getIntent().getStringExtra("category").equals("SHOUTOUT")){
                    final Event add=new Event();
                    add.setTitle(obj.getString("employee_from_profile"));
                    add.setId(obj.getInt("id"));
                    add.events_description = obj.getString("shoutout_description");
                    add.setEvents_video(obj.getString("employee_from_name"));
                    add.setEvents_document(obj.getString("employee_to_name"));
                    add.events_image = obj.getString("employee_to_profile");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.add(add);
                        }
                    });
                }
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            } catch (JSONException e) {
                createAndShowDialog(e, "No connection");
                e.printStackTrace();
            }
        }
//        } else if (obj.has("news_title")) {
//            try {
//                final Event add = new Event();
//                add.setTitle(obj.getString("news_title"));
//                add.setId(obj.getInt("id"));
//                add.setDescription(obj.getString("news_description"));
//                String splitted_gallery[] = obj.getString("news_image").split("%2C");
//                add.setImage(splitted_gallery[0]);
//                add.setType("news");
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        adapter.add(add);
//                    }
//                });
//            } catch (JSONException e) {
//                createAndShowDialog(e, "No connection");
//                e.printStackTrace();
//            }
//        } else if (obj.has("shoutout_description")) {
//            try {
//                final Event add = new Event();
//                add.setTitle(obj.getString("shoutout_description"));
//                add.setId(obj.getInt("id"));
//                add.setImage(obj.getString("employee_from_profile"));
//                add.setType("shoutout");
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        adapter.add(add);
//                    }
//                });
//            } catch (JSONException e) {
//                createAndShowDialog(e, "No connection");
//                e.printStackTrace();
//            }
//        }
    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
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
                    viewHolder = new SearchResults.PaginationAdapter.LoadingVH(v2);
                    break;
            }
            return viewHolder;
        }

        @NonNull
        private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, final LayoutInflater inflater) {
            final RecyclerView.ViewHolder viewHolder;
            View v1;
            if(getIntent().getStringExtra("category").equals("SHOUTOUT")){
                v1 = inflater.inflate(R.layout.shoutoutlist_layout, parent, false);
            }else{
                v1 = inflater.inflate(R.layout.notificationlist_layout, parent, false);
            }
            viewHolder = new SearchResults.PaginationAdapter.EventVH(v1);
            final View.OnClickListener mOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Event event = eventList.get(viewHolder.getAdapterPosition());
                    if(!getIntent().getStringExtra("category").equals("SHOUTOUT")){
                        Intent intent = new Intent(SearchResults.this,FullEvent.class);
                        String check = "";
                        if(getIntent().getStringExtra("category").equals("NEWS")){
                            check = "NEWS";
                        }else{
                            check = "EVENTS";
                        }
                        int id = event.getId();
                        intent.putExtra("id", id);
                        intent.putExtra("check",check);
                        startActivity(intent);
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
                    SearchResults.PaginationAdapter.EventVH eventVH = (SearchResults.PaginationAdapter.EventVH) holder;
                    if(getIntent().getStringExtra("category").equals("SHOUTOUT")){
                        eventVH.events_title.setText(event.getEvents_description());
                        eventVH.sender.setText(event.getEvents_video());
                        eventVH.receiver.setText(event.getEvents_document());
                        loadImageFromUrl(eventVH.shoutout_to,(SERVER_URL+event.getTitle()));
                        loadImageFromUrl(eventVH.shoutout_from,(SERVER_URL+event.getEvents_image()));
                    }else{
                        eventVH.events_title.setText(event.getTitle());
                        loadImageFromUrl(eventVH.events_image, (SERVER_URL + event.getEvents_image()));
                        eventVH.events_description.setText("Posted on " + event.getEvents_description());
                    }
//                    if(event.getEvents_image().equals("1")){
//                        eventVH.events_image.setImageResource(R.drawable.birthday_blue);
//                    }else if(event.getEvents_image().equals("2")){
//                        eventVH.events_image.setImageResource(R.drawable.anniversary_blue);
//                    }else{
//
//                    }
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
            TextView events_title,events_description, sender, receiver;
            ImageView events_image, shoutout_from, shoutout_to ;
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
                shoutout_from = (ImageView) itemView.findViewById(R.id.shoutout_from);
                shoutout_to = (ImageView) itemView.findViewById(R.id.shoutout_to);
                sender = (TextView) itemView.findViewById(R.id.sender);
                receiver = (TextView) itemView.findViewById(R.id.receiver);
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
