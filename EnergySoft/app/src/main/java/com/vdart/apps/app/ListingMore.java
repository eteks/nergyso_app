package com.vdart.apps.app;

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

import com.squareup.picasso.Picasso;
import com.vdart.apps.app.utils.PaginationScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListingMore extends AppCompatActivity implements Download_data.download_complete {
    Toolbar toolbar;
    View view;
    private static final String TAG = "EventMain";
    String listValue = "NULL";
    PaginationAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    String nextPage;
    String SERVER_URL ;
    EventMain main;
    String EVENT_URL, more;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_main);
        SERVER_URL = getString(R.string.service_url);
        EVENT_URL = SERVER_URL+ "api/events/";
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        ImageView home = (ImageView) findViewById(R.id.action_home);
//        home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(EventMain.this, GridList.class);
//                startActivity(intent);
//            }
//        });
//        list = (ListView) findViewById(R.id.newslist);
//        NewsAdapter = new com.example.user.energysoft.ListAdapter(this);
//        list.setAdapter(NewsAdapter);
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
        //        Click Logo to home screen

        ImageView imageButton = (ImageView) toolbar.findViewById(R.id.vdart_logo);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListingMore.this, BannerActivity.class);
                startActivity(intent);
            }
        });

        // mocking network delay for API call
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadFirstPage();
            }
        }, 1000);

//        Download_data download_data = new Download_data((Download_data.download_complete) this);
//        download_data.download_data_from_link("http://10.0.0.15:8000/api/news/");
        Intent intent = getIntent();
        more = intent.getStringExtra("more");
        switch (more){
            case "today_birthday" : {
                Download_data download_data = new Download_data((Download_data.download_complete) this);
                download_data.download_data_from_link(SERVER_URL+"api/employee/employee_today_birthday/");
                break;
            }
            case "upcoming_birthday":{
                Download_data download_data = new Download_data((Download_data.download_complete) this);
                download_data.download_data_from_link(SERVER_URL+"api/employee/employee_upcoming_birthday/");
                break;
            }
            case "today_anniversary":{
                Download_data download_data = new Download_data((Download_data.download_complete) this);
                download_data.download_data_from_link(SERVER_URL+"api/employee/employee_today_anniversary/");
                break;
            }
            case "upcoming_anniversary":{
                Download_data download_data = new Download_data((Download_data.download_complete) this);
                download_data.download_data_from_link(SERVER_URL+"api/employee/employee_upcoming_anniversary/");
                break;
            }
            case "shoutout":{
                Download_data download_data = new Download_data((Download_data.download_complete) this);
                download_data.download_data_from_link(SERVER_URL+"api/shoutout_list/");
                break;
            }

            default: break;
        }
    }

    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");
//        final List<News> newsList = News.createMovies(adapter.getItemCount());
//        Download_data download_data = new Download_data((Download_data.download_complete) this);
//        download_data.download_data_from_link(EVENT_URL);

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
        if(more.equals("shoutout")){
            try {
                JSONObject object= new JSONObject(data);
                JSONArray array = object.getJSONArray("results");
                for (int i = 0 ; i < array.length() ; i++){
                    JSONObject obj=new JSONObject(array.get(i).toString());
                    if(obj.has("shoutout_description")){
                        final Event add=new Event();
                        add.setTitle(obj.getString("shoutout_description"));
                        add.setId(obj.getInt("id"));
                        add.events_description = obj.getString("employee_to_profile");
                        add.events_image = obj.getString("employee_from_profile");
                        add.setEvents_video(obj.getString("employee_from_name"));
                        add.setEvents_document(obj.getString("employee_to_name"));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                progressBar.setVisibility(View.GONE);
                                adapter.add(add);
                            }
                        });
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{
            try {
                JSONArray data_array = new JSONArray(data);
                System.out.println("Object"+data_array);
                if(data_array.length() == 0){
                    createAndShowDialog("Data is Empty","No data");
                }
                for (int i = 0 ; i < data_array.length() ; i++)
                {
                    JSONObject obj=new JSONObject(data_array.get(i).toString());
                    System.out.println("Object"+obj);
                    if(obj.has("employee_name")){
                        final Event add=new Event();
                        add.events_title = obj.getString("employee_name");
                        add.setTitle(obj.getString("employee_name"));
                        add.setId(obj.getInt("id"));
                        Date date = parseDate(obj.getString("employee_dob"));
                        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
                        String strDate = formatter.format(date);
                        add.events_description = strDate;
                        add.events_image = obj.getString("employee_photo");
                        System.out.println("News Id"+obj.getInt("id"));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.add(add);
                            }
                        });
                    }
                }
            } catch (JSONException e) {
                createAndShowDialog(e,"No connection");
                e.printStackTrace();
            }
        }
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);

    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
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
        Intent intent = new Intent(ListingMore.this,FullNews.class);
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
        private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
            final RecyclerView.ViewHolder viewHolder;
            View v1;
            if(more.equals("shoutout")){
                v1 = inflater.inflate(R.layout.shoutoutlist_layout, parent, false);
            }else{
                v1 = inflater.inflate(R.layout.birthdaylist_layout, parent, false);
            }
            viewHolder = new EventVH(v1);
            final View.OnClickListener mOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Event event = eventList.get(viewHolder.getAdapterPosition());
//                    System.out.println("CLICKed"+event.getId());
//                    int id = event.getId();
//                    Intent intent = new Intent(ListingMore.this,FullEvent.class);
//                    intent.putExtra("id", id);
////                    finish();
//                    startActivity(intent);
//                news.setPage("FullNews");
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
                    EventVH eventVH = (EventVH) holder;
                    if(more.equals("shoutout")){
                        eventVH.events_title.setText(event.getTitle());
                        eventVH.sender.setText(event.getEvents_video());
                        eventVH.receiver.setText(event.getEvents_document());
                        loadImageFromUrl(eventVH.shoutout_to,(SERVER_URL+event.getEvents_description()));
                        loadImageFromUrl(eventVH.shoutout_from,(SERVER_URL+event.getEvents_image()));
                    }else{
                        eventVH.events_title.setText(event.getTitle());
                        eventVH.events_description.setText(event.getEvents_description());
                        loadImageFromUrl(eventVH.events_image,(SERVER_URL+event.getEvents_image()));
                    }
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
            TextView events_title,events_description,sender, receiver;;
            ImageView events_image, shoutout_to, shoutout_from;

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
                events_description = (TextView) itemView.findViewById(R.id.news_description2);
                events_image = (ImageView) itemView.findViewById(R.id.news_image2);
                shoutout_to = (ImageView) itemView.findViewById(R.id.shoutout_to);
                shoutout_from = (ImageView) itemView.findViewById(R.id.shoutout_from);
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
