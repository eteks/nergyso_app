package com.vdart.apps.app;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import static com.google.android.gms.internal.zzagz.runOnUiThread;

//import static com.google.android.gms.internal.zzahf.runOnUiThread;

/**
 * Created by ets-prabhu on 8/1/18.
 */

public class ImageFragment extends Fragment implements Download_data.download_complete{

    PaginationAdapter adapter;
    ViewPager viewPager;
    String SERVER_URL;
    String IMAGE_URL;
    RecyclerView rv;
    View view;
    String FULL_EVENTS_URL = "api/events/";
    String FULL_NEWS_URL = "api/news/";
    String[] images = new String[20];
    int currentPage = 0;
    LinearLayoutManager linearLayoutManager;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.
    public static int NUM_PAGES = 0;
    TextView full_events_title, event_date, event_location, recent_events;
    String check = "";
    WebView full_text_events_description;
    String RECENT_URL ;
    ImageView events_photo;
    int ONE_TIME = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SERVER_URL = getString(R.string.service_url);

        view = inflater.inflate(R.layout.fragement_image, container, false);
//        full_events_title = (TextView) view.findViewById(R.id.full_events_title);
//        full_events_description = (TextView) view.findViewById(R.id.full_events_description);
//
//        int id = getActivity().getIgetIntExtra("id", 0);

        rv = (RecyclerView) view.findViewById(R.id.main_recycler);

        adapter = new PaginationAdapter(getActivity());

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);

        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);

        full_events_title = (TextView) view.findViewById(R.id.full_events_title);
        full_text_events_description = (WebView) view.findViewById(R.id.full_text_events_description);
        events_photo = (ImageView) view.findViewById(R.id.events_photo);
        event_date = (TextView) view.findViewById(R.id.event_date);
        event_location = (TextView) view.findViewById(R.id.event_location);
        recent_events = (TextView) view.findViewById(R.id.recent_events);

        int id = getActivity().getIntent().getIntExtra("id", 0);
        check = getActivity().getIntent().getStringExtra("check");
        if(check.equals("NEWS")){
            IMAGE_URL = SERVER_URL + "api/news" + "/" + id;
            RECENT_URL = SERVER_URL + "api/news/recent_news/";
            recent_events.setText("Recent NEWS");
            event_location.setVisibility(View.GONE);
        }else if(check.equals("EVENTS")){
            IMAGE_URL = SERVER_URL + "api/events" + "/" + id;
            RECENT_URL = SERVER_URL + "api/events/recent_events/";
            recent_events.setText("Recent Events");
        }

//        viewPager = (ViewPager) view.findViewById(R.id.viewPagerdash);
//
//        myCustomPagerAdapter = new ImageFragment.MyCustomPagerAdapter(getActivity(), images);
//        viewPager.setAdapter(myCustomPagerAdapter);
//
//        /*After setting the adapter use the timer */
//        final Handler handler = new Handler();
//        final Runnable Update = new Runnable() {
//
//            public void run() {
//                if (currentPage == NUM_PAGES) {
//                    currentPage = 0;
//                }
//                viewPager.setCurrentItem(currentPage++, true);
//            }
//        };
//
//        timer = new Timer(); // This will create a new Thread
//        timer .schedule(new TimerTask() { // task to be scheduled
//
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        }, DELAY_MS, PERIOD_MS);
//
        Download_data download_data = new Download_data((Download_data.download_complete) this);
        download_data.download_data_from_link(IMAGE_URL);

        return view;
    }

    public void get_data(String data)
    {
        if (ONE_TIME == 1) {
            try {
                JSONArray data_array = new JSONArray(data);
                System.out.println("Object"+data_array);
//            nextPage = object.getString("next");
                if(data_array.length() == 0){
                    createAndShowDialog("Data is Empty","No data");
                }
                int length = 0;
                if(data_array.length() == 0){
                    length = 0;
                }else if(data_array.length() >= 3){
                    length = 3;
                }else{
                    length = data_array.length();
                }
                for (int i = 0 ; i < length ; i++)
                {
                    JSONObject obj=new JSONObject(data_array.get(i).toString());
                    if(check.equals("EVENTS")){
                        final News add=new News("Title");
                        add.news_title = obj.getString("events_title");
                        add.setId(obj.getInt("id"));
                        add.news_description = obj.getString("events_description");
                        String splitted_gallery[] = obj.getString("events_image").split("%2C");
                        add.news_image = splitted_gallery[0];
//                news.add(add);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                        progressBar.setVisibility(View.GONE);
                                adapter.add(add);
                            }
                        });
                    }else if(check.equals("NEWS")){
                        final News add=new News("Title");
                        add.news_title = obj.getString("news_title");
                        add.setId(obj.getInt("id"));
                        add.news_description = obj.getString("news_description");
                        String splitted_gallery[] = obj.getString("news_image").split("%2C");
                        add.news_image = splitted_gallery[0];
//                news.add(add);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                        progressBar.setVisibility(View.GONE);
                                adapter.add(add);
                            }
                        });
                    }
//                System.out.println("Object"+obj);


                }
//            if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
//            else isLastPage = true;

//            NewsAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                createAndShowDialog(e,"No connection");
                e.printStackTrace();
//            loadFirstPage();
            }
        }if(ONE_TIME == 0) {
        try {
            final JSONObject object = (JSONObject) new JSONTokener(data).nextValue();
            System.out.println("Object" + object);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (check.equals("EVENTS")) {
                        try {
                            full_events_title.setText(object.getString("events_title"));
                            String events_description = object.getString("events_description");
                            full_text_events_description.loadData("<p style=\"text-align: justify\">" + events_description + "</p>", "text/html", "UTF-8");
                            System.out.println(SERVER_URL + object.getString("events_image"));
                            Date date = parseDate(object.getString("created_date"));
                            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
                            String strDate = formatter.format(date);
                            event_date.setText("Event Date : " + strDate);
                            event_location.setText("Event Venue : " + object.getString("events_venue"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (check.equals("NEWS")){
                        try {
                            full_events_title.setText(object.getString("news_title"));
                            String events_description = object.getString("news_description");
                            full_text_events_description.loadData("<p style=\"text-align: justify\">" + events_description + "</p>", "text/html", "UTF-8");
                            System.out.println(SERVER_URL + object.getString("news_image"));
                            Date date = parseDate(object.getString("created_date"));
                            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
                            String strDate = formatter.format(date);
                            event_date.setText("Date : " + strDate);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
        } catch (JSONException e) {
            createAndShowDialog(e, "No connection");
            e.printStackTrace();
        }
        ONE_TIME++;
        Download_data download_data = new Download_data((Download_data.download_complete) this);
        download_data.download_data_from_link(RECENT_URL);

    }

    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    private void loadImageUrl(String employee_photo) {
        Picasso.with(getActivity()).load(employee_photo).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
                .into(events_photo, new com.squareup.picasso.Callback() {

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    private void createAndShowDialog(final String message, final String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

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

    public class MyCustomPagerAdapter extends PagerAdapter {
        Context context;
        String images[];
        LayoutInflater layoutInflater;

//        public MyCustomPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }


        public MyCustomPagerAdapter(Context context, String images[]) {
            this.context = context;
            this.images = images;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = layoutInflater.inflate(R.layout.activity_banner_item, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewdash);
//        imageView.setImageResource(images[position]);
            System.out.println(" in");
            loadImageFromUrl(imageView,images[position]);
            container.addView(itemView);

            //listening to image click
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
                }
            });

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

    private void loadImageFromUrl(ImageView myImage,String employee_photo) {
        System.out.println("In LoadImageFromURL");
        Picasso.with(getActivity()).load(employee_photo).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
                .into(myImage, new com.squareup.picasso.Callback(){

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
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
                    Intent intent = new Intent(getActivity(),FullEvent.class);
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
}
