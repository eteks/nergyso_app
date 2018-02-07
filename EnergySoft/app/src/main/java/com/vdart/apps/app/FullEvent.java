package com.vdart.apps.app;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FullEvent extends AppCompatActivity implements Download_data.download_complete {

    MyCustomPagerAdapter myCustomPagerAdapter;
    ViewPager viewPager;
//    String[] images = new String[20];
    ArrayList<String> images = new ArrayList<String>();
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.
    public static int NUM_PAGES = 0;
    String SERVER_URL;
    String FULL_EVENTS_URL = "api/events/";
    String RECENT_EVENTS_URL = "api/events/recent_events/";
    String RECENT_NEWS_URL = "api/news/recent_news/";
    String FULL_NEWS_URL = "api/news/";
    WebView full_text_events_description;
    int TOTAL_PAGES = 0;
    LinearLayoutManager linearLayoutManager;
    Toolbar toolbar;
    int ONE_TIME = 0;
    String check = "";
    PaginationAdapter adapter;
    TextView full_events_title, event_location, event_date;
    ImageView events_photo;
    private static final int[] ID = new int[200];
    RecyclerView rv;
    public ListView list;
    public ArrayList<News> newsList = new ArrayList<News>();
    public ListAdapter NewsAdapter;
    String DOWNLOAD_URL = "", RECENT_URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Setting Page content
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_event);

        //Setting Toolbar content
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Initialising Server URL
        SERVER_URL = getString(R.string.service_url);

//        rv = (RecyclerView) findViewById(R.id.main_recycler);
//        final ProgressBar progressBar = new ProgressBar(getActivity());

//        adapter = new FullEvent.PaginationAdapter(this);
//
//        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        rv.setLayoutManager(linearLayoutManager);
//
//        rv.setItemAnimator(new DefaultItemAnimator());
//
//        rv.setAdapter(adapter);

        viewPager = (ViewPager) findViewById(R.id.viewPagerdash);

        myCustomPagerAdapter = new MyCustomPagerAdapter(this, images);

        viewPager.setAdapter(myCustomPagerAdapter);

        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {

            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer .schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

        int id = getIntent().getIntExtra("id", 0);
        check = getIntent().getStringExtra("check");
        if(check.equals("NEWS")){
            DOWNLOAD_URL = SERVER_URL + FULL_NEWS_URL + "/" + id;
            RECENT_URL = SERVER_URL + RECENT_EVENTS_URL ;
        }else if(check.equals("EVENTS")){
            DOWNLOAD_URL = SERVER_URL + FULL_EVENTS_URL + "/" + id;
            RECENT_URL = SERVER_URL + RECENT_NEWS_URL ;
        }

        //Initialising TextView and ImageView
        full_events_title = (TextView) findViewById(R.id.full_events_title);
        full_text_events_description = (WebView) findViewById(R.id.full_text_events_description);
        events_photo = (ImageView) findViewById(R.id.events_photo);
        event_date = (TextView) findViewById(R.id.event_date);
        event_location = (TextView) findViewById(R.id.event_location);

        //Call the API to get the data
        Download_data download_data = new Download_data((Download_data.download_complete) this);
        download_data.download_data_from_link(DOWNLOAD_URL);

        // Loading the slider Image Fragment (ImageFragment.java)
        loadFragment(new ImageFragment());

    }

    private void loadFragment(Fragment fragment) {
        // create a FragmentManager
        FragmentManager fm = getFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
    }

    @Override
    public void get_data(String data) {
            if (ONE_TIME == 1) {
                try {
                    JSONArray data_array = new JSONArray(data);
                    System.out.println("Object" + data_array);
//            nextPage = object.getString("next");
                    if (data_array.length() == 0) {
                        createAndShowDialog("Data is Empty", "No data");
                    }
                    int length = 0;
                    if (data_array.length() == 0) {
                        length = 0;
                    } else if (data_array.length() >= 3) {
                        length = 3;
                    } else {
                        length = data_array.length();
                    }
                    for (int i = 0; i < length; i++) {
                        JSONObject obj = new JSONObject(data_array.get(i).toString());
                        if(check.equals("EVENTS")) {
//                System.out.println("Object"+obj);
                            final News add = new News("Title");
                            add.news_title = obj.getString("events_title");
                            add.setId(obj.getInt("id"));
                            add.news_description = obj.getString("events_description");
                            add.news_image = obj.getString("events_image");
//                news.add(add);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                        progressBar.setVisibility(View.GONE);
                                    adapter.add(add);
                                }
                            });
                        }else if(check.equals("NEWS")){
                            final News add = new News("Title");
                            add.news_title = obj.getString("news_title");
                            add.setId(obj.getInt("id"));
                            add.news_description = obj.getString("news_description");
                            add.news_image = obj.getString("news_image");
//                news.add(add);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                        progressBar.setVisibility(View.GONE);
                                    adapter.add(add);
                                }
                            });
                        }

                    }
//            if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
//            else isLastPage = true;

//            NewsAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    createAndShowDialog(e, "No connection");
                    e.printStackTrace();
//            loadFirstPage();
                }
            }
            if (ONE_TIME == 0) {
                try {
                    JSONObject data_array = new JSONObject(data);
                    System.out.println("Object" + data_array);
                    if (check.equals("NEWS")) {
                        String splitted_gallery[] = data_array.getString("news_image").split("%2C");
                        NUM_PAGES = splitted_gallery.length;
                        for (int index = 0; index < splitted_gallery.length; index++) {
//                            images[index] = SERVER_URL + splitted_gallery[index];
                                images.add(SERVER_URL + splitted_gallery[index]);
                                myCustomPagerAdapter.notifyDataSetChanged();
                        }
                    } else if (check.equals("EVENTS")) {
                        String splitted_gallery[] = data_array.getString("events_image").split("%2C");
                        NUM_PAGES = splitted_gallery.length;
                        for (int index = 0; index < splitted_gallery.length; index++) {
//                            images[index] = SERVER_URL + splitted_gallery[index];
                            images.add(SERVER_URL + splitted_gallery[index]);
                            myCustomPagerAdapter.notifyDataSetChanged();
                        }
                    }
//                final JSONObject object = (JSONObject) new JSONTokener(data).nextValue();
//                System.out.println("Object" + object);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            full_events_title.setText(object.getString("events_title"));
//                            String events_description = object.getString("events_description");
//                            full_text_events_description.loadData("<p style=\"text-align: justify\">"+ events_description + "</p>", "text/html", "UTF-8");
//                            System.out.println(SERVER_URL + object.getString("events_image"));
//                            event_date.setText("Event Date : " + object.getString("events_date"));
//                            event_location.setText("Event Venue : " + object.getString("events_venue"));
//                            String splitted_gallery[] = object.getString("events_image").split("%2C");
//                            loadImageUrl(SERVER_URL + splitted_gallery[0]);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//            Download_data download_data = new Download_data((Download_data.download_complete) this);
//            download_data.download_data_from_link(RECENT_EVENTS_URL);
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

    private void loadImageUrl(String employee_photo) {
        Picasso.with(this).load(employee_photo).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
                .into(events_photo, new com.squareup.picasso.Callback() {

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    public class MyAdapter extends PagerAdapter {

        private ArrayList<String> images;
        private LayoutInflater inflater;
        private Context context;

        public MyAdapter(Context context, ArrayList<String> images) {
            this.context = context;
            this.images = images;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, final int position) {
            View myImageLayout = inflater.inflate(R.layout.slide, view, false);
            ImageView myImage = (ImageView) myImageLayout
                    .findViewById(R.id.image);
//            myImage.setImageResource(images.get(position));
            System.out.println("List " + images.get(position));
            loadImageFromUrl(myImage, (SERVER_URL + images.get(position)));
            myImageLayout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //this will log the page number that was click
                    Log.i("TAG", "This page was clicked: " + position);
                    generateList(position);
                }
            });
            view.addView(myImageLayout, 0);
            return myImageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }
    }

    public void generateList(int position) {
        Intent intent = new Intent(FullEvent.this, FullEvent.class);
        intent.putExtra("id", ID[position]);
        finish();
        startActivity(intent);
        ONE_TIME = 0;
    }

    // Initiating Menu XML file (menu.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

    public class MyCustomPagerAdapter extends PagerAdapter {
        Context context;
        ArrayList<String> images;
        LayoutInflater layoutInflater;

        public MyCustomPagerAdapter(Context context, ArrayList<String> images) {
            this.context = context;
            this.images = images;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = layoutInflater.inflate(R.layout.activity_banner_item, container, false);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewdash);
            System.out.println(" in");
            loadImageFromUrl(imageView, images.get(position));
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

    public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private static final int ITEM = 0;
        private static final int LOADING = 1;
        NewsMain main;
        private List<News> newsList;
        private Context context;
        private boolean isLoadingAdded = false;

        public PaginationAdapter(Context context) {
            this.context = context;
            newsList = new ArrayList<>();
        }

        public List<News> getNewsList() {
            return newsList;
        }

        public void setNewsList(List<News> newsList) {
            this.newsList = newsList;
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
            View v1 = inflater.inflate(R.layout.newslist_layout, parent, false);
            viewHolder = new NewsVH(v1);
            final View.OnClickListener mOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    News news = newsList.get(viewHolder.getAdapterPosition());
                    System.out.println("CLICKed"+news.getId());
                    int id = news.getId();
                    Intent intent = new Intent(FullEvent.this,FullEvent.class);
                    intent.putExtra("id", id);
                    intent.putExtra("check","EVENTS");
//                    finish();
                    startActivity(intent);
//                news.setPage("FullNews");
                }
            };
            v1.setOnClickListener(mOnClickListener);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        Movie movie = movies.get(position);
            News news = newsList.get(position);

            switch (getItemViewType(position)) {
                case ITEM:
                    NewsVH newsVH = (NewsVH) holder;
                    newsVH.news_title.setText(news.getTitle());
                    newsVH.news_description.setText(news.getNews_description());
                    loadImageFromUrl(newsVH.news_image,(SERVER_URL+news.getNews_image()));
                    break;
                case LOADING:
//                Do nothing
                    break;
            }

        }

        @Override
        public int getItemCount() {
            return newsList == null ? 0 : newsList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return (position == newsList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
        }

    /*
   Helpers
   _________________________________________________________________________________________________
    */

        public void add(News mc) {
            newsList.add(mc);
            notifyItemInserted(newsList.size() - 1);
        }

        public void addAll(List<News> mcList) {
            for (News mc : mcList) {
                add(mc);
            }
        }

        public void remove(News city) {
            int position = newsList.indexOf(city);
            if (position > -1) {
                newsList.remove(position);
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
            add(new News());
        }

        public void removeLoadingFooter() {
            isLoadingAdded = false;

            int position = newsList.size() - 1;
            News item = getItem(position);

            if (item != null) {
                newsList.remove(position);
                notifyItemRemoved(position);
            }
        }

        public News getItem(int position) {
            return newsList.get(position);
        }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

        /**
         * Main list's content ViewHolder
         */
        protected class NewsVH extends RecyclerView.ViewHolder {
            TextView news_title,news_description;
            ImageView news_image;
            //        ListAdapter.ViewHolderItem holder = new ListAdapter.ViewHolderItem();
            public NewsVH(View itemView) {
                super(itemView);
//            ListAdapter.ViewHolderItem holder = new ListAdapter.ViewHolderItem();
//            if (convertView == null) {
//                LayoutInflater inflater = (LayoutInflater) main.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                convertView = inflater.inflate(R.layout.newslist_layout, null);

//            holder.name = (TextView) convertView.findViewById(R.id.name);
//            holder.code = (TextView) convertView.findViewById(R.id.code);
                news_title = (TextView) itemView.findViewById(R.id.news_title2);
                news_description = (TextView) itemView.findViewById(R.id.news_description2);
                news_image = (ImageView) itemView.findViewById(R.id.news_image2);
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

    // Initiating Menu XML file (menu.xml)
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu_main, menu);
//        return true;
//    }

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
