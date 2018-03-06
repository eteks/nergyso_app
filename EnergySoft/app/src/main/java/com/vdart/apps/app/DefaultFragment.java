package com.vdart.apps.app;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.google.android.gms.internal.zzagz.runOnUiThread;

/**
 * Created by ets-prabhu on 12/2/18.
 */

public class DefaultFragment extends Fragment implements  Download_data.download_complete{

    View view;
    RecyclerView main_recycler_birthday, main_recycler_anniversary, main_recycler_shoutout, main_recycler_news, main_recycler_event, main_recycler_today_birthday, main_recycler_today_anniversary;
    DefaultFragment.PaginationAdapter adapter1, adapter2, adapter4, adapter5, adapter6, adapter7 ;
    DefaultFragment.PaginationAdapter1 adapter3;
    LinearLayoutManager linearLayoutManager1, linearLayoutManager2, linearLayoutManager3, linearLayoutManager4, linearLayoutManager5, linearLayoutManager6, linearLayoutManager7;
    String SERVER_URL = "";
    String BIRTHDAY_URL = "api/employee/employee_upcoming_birthday/", ANNIVERSARY_URL = "api/employee/employee_upcoming_anniversary/", SHOUTOUT_URL = "api/shoutout_list/", NEWS_URL = "api/news/recent_news", EVENT_URL = "api/events/recent_events/", TODAY_BIRTHDAY_URL = "api/employee/employee_today_birthday/", TODAY_ANNIVERSARY_URL = "api/employee/employee_today_anniversary/";
    boolean birthday = false, anniversary = false, shoutout_ = false, news_ = false, event_ = false, birthday_today = false, anniversary_today = false;
    TextView more_upcoming_birthday, more_upcoming_anniversary, more_shoutout, more_news, more_event, more_today_anniversary, more_today_birthday;
    TextView upcoming_birthday,upcoming_anniversary, upcoming_shoutout, upcoming_news, upcoming_event, today_birthday, today_anniversary;
    View birthday_end,anniversary_end,shoutout_end,news_end,event_end, birthday_today_end, anniversary_today_end;
    Button facebook, twitter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_default, container, false);
        SERVER_URL = getString(R.string.service_url);

        BIRTHDAY_URL = SERVER_URL + BIRTHDAY_URL;
        ANNIVERSARY_URL = SERVER_URL + ANNIVERSARY_URL;
        SHOUTOUT_URL = SERVER_URL + SHOUTOUT_URL;
        NEWS_URL = SERVER_URL + NEWS_URL ;
        EVENT_URL = SERVER_URL + EVENT_URL ;
        TODAY_BIRTHDAY_URL = SERVER_URL + TODAY_BIRTHDAY_URL;
        TODAY_ANNIVERSARY_URL = SERVER_URL + TODAY_ANNIVERSARY_URL;

        main_recycler_birthday = (RecyclerView) view.findViewById(R.id.main_recycler_birthday);
        main_recycler_anniversary = (RecyclerView) view.findViewById(R.id.main_recycler_anniversary);
        main_recycler_shoutout = (RecyclerView) view.findViewById(R.id.main_recycler_shoutout);
        main_recycler_news = (RecyclerView) view.findViewById(R.id.main_recycler_news);
        main_recycler_event = (RecyclerView) view.findViewById(R.id.main_recycler_event);
        main_recycler_today_birthday = (RecyclerView) view.findViewById(R.id.main_recycler_today_birthday);
        main_recycler_today_anniversary = (RecyclerView) view.findViewById(R.id.main_recycler_today_anniversary);

        upcoming_birthday = (TextView) view.findViewById(R.id.upcoming_birthday);
        upcoming_anniversary = (TextView) view.findViewById(R.id.upcoming_birthday);
        upcoming_shoutout = (TextView) view.findViewById(R.id.upcoming_shoutout);
        upcoming_news = (TextView) view.findViewById(R.id.upcoming_news);
        upcoming_event = (TextView) view.findViewById(R.id.upcoming_event);
        today_birthday = (TextView) view.findViewById(R.id.today_birthday);
        today_anniversary = (TextView) view.findViewById(R.id.today_anniversary);

        birthday_end = (View) view.findViewById(R.id.birthday_end);
        anniversary_end = (View) view.findViewById(R.id.anniversary_end);
        shoutout_end = (View) view.findViewById(R.id.shoutout_end);
        news_end = (View) view.findViewById(R.id.news_end);
        event_end = (View) view.findViewById(R.id.event_end);
        birthday_today_end = (View) view.findViewById(R.id.birthday_today_end);
        anniversary_today_end = (View) view.findViewById(R.id.anniversary_today_end);

        adapter1 = new DefaultFragment.PaginationAdapter(getActivity());
        adapter2 = new DefaultFragment.PaginationAdapter(getActivity());
        adapter3 = new DefaultFragment.PaginationAdapter1(getActivity());
        adapter4 = new DefaultFragment.PaginationAdapter(getActivity());
        adapter5 = new DefaultFragment.PaginationAdapter(getActivity());
        adapter6 = new DefaultFragment.PaginationAdapter(getActivity());
        adapter7 = new DefaultFragment.PaginationAdapter(getActivity());

        linearLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        main_recycler_birthday.setLayoutManager(linearLayoutManager1);

        linearLayoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        main_recycler_anniversary.setLayoutManager(linearLayoutManager2);

        linearLayoutManager3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        main_recycler_shoutout.setLayoutManager(linearLayoutManager3);

        linearLayoutManager4 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        main_recycler_news.setLayoutManager(linearLayoutManager4);

        linearLayoutManager5 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        main_recycler_event.setLayoutManager(linearLayoutManager5);

        linearLayoutManager6 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        main_recycler_today_birthday.setLayoutManager(linearLayoutManager6);

        linearLayoutManager7 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        main_recycler_today_anniversary.setLayoutManager(linearLayoutManager7);

        main_recycler_birthday.setAdapter(adapter1);
        main_recycler_anniversary.setAdapter(adapter2);
        main_recycler_shoutout.setAdapter(adapter3);
        main_recycler_news.setAdapter(adapter4);
        main_recycler_event.setAdapter(adapter5);
        main_recycler_today_birthday.setAdapter(adapter6);
        main_recycler_today_anniversary.setAdapter(adapter7);

        more_upcoming_birthday = (TextView) view.findViewById(R.id.more_upcoming_birthday);
        more_upcoming_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ListingMore.class);
                intent.putExtra("more","upcoming_birthday");
                startActivity(intent);
            }
        });

        more_today_birthday = (TextView) view.findViewById(R.id.more_today_birthday);
        more_today_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ListingMore.class);
                intent.putExtra("more","today_birthday");
                startActivity(intent);
            }
        });

        more_upcoming_anniversary = (TextView) view.findViewById(R.id.more_upcoming_anniversary);
        more_upcoming_anniversary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ListingMore.class);
                intent.putExtra("more","upcoming_anniversary");
                startActivity(intent);
            }
        });

        more_today_anniversary = (TextView) view.findViewById(R.id.more_today_anniversary);
        more_today_anniversary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ListingMore.class);
                intent.putExtra("more","today_anniversary");
                startActivity(intent);
            }
        });

        more_shoutout = (TextView) view.findViewById(R.id.more_shoutout);
        more_shoutout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ListingMore.class);
                intent.putExtra("more","shoutout");
                startActivity(intent);
            }
        });
        more_news = (TextView) view.findViewById(R.id.more_news);
        more_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),NewsMain.class);
                startActivity(intent);
            }
        });
        more_event = (TextView) view.findViewById(R.id.more_event);
        more_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),EventMain.class);
                startActivity(intent);
            }
        });

        facebook = (Button) view.findViewById(R.id.facebook);
        facebook.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(),FacebookActivity.class);
                startActivity(intent);
            }
        });

        twitter = (Button) view.findViewById(R.id.twitter);
        twitter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(),TwitterActivity.class);
                startActivity(intent);
            }
        });
        Download_data download_data = new Download_data((Download_data.download_complete) this);
        download_data.download_data_from_link(TODAY_BIRTHDAY_URL);
        birthday_today = true;

        return view;
    }

    public void get_data(String data) {
        if (birthday_today) {
            Download_data download_data = new Download_data((Download_data.download_complete) this);
            download_data.download_data_from_link(BIRTHDAY_URL);
            birthday = true;
            birthday_today = false;
            try {
                JSONArray data_array = new JSONArray(data);
                System.out.println("Object" + data_array);
                for (int i = 0; i < 1; i++) {
                    JSONObject obj = new JSONObject(data_array.get(0).toString());
                    System.out.println("Object" + obj);
                    final News add = new News("Title");
                    add.news_title = obj.getString("employee_name");
                    add.setTitle(obj.getString("employee_name"));
                    add.setId(obj.getInt("id"));
                    Date date = parseDate(obj.getString("employee_dob"));
                    SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
                    String strDate = formatter.format(date);
                    add.news_description = strDate;
                    add.news_image = obj.getString("employee_photo");
                    add.setType("birthday_today");
                    System.out.println("News Id" + obj.getInt("id"));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter6.add(add);
                        }
                    });
                }
            } catch (JSONException e) {
//            createAndShowDialog(e,"No data");
                e.printStackTrace();
                today_birthday.setVisibility(View.GONE);
                more_today_birthday.setVisibility(View.GONE);
                birthday_today_end.setVisibility(View.GONE);
            }
        }else if (birthday) {
            Download_data download_data = new Download_data((Download_data.download_complete) this);
            download_data.download_data_from_link(TODAY_ANNIVERSARY_URL);
            anniversary_today = true;
            birthday = false;
            try {
                JSONArray data_array = new JSONArray(data);
                System.out.println("Object" + data_array);
                for (int i = 0; i < 1; i++) {
                    JSONObject obj = new JSONObject(data_array.get(0).toString());
                    System.out.println("Object" + obj);
                    final News add = new News("Title");
                    add.news_title = obj.getString("employee_name");
                    add.setTitle(obj.getString("employee_name"));
                    add.setId(obj.getInt("id"));
                    Date date = parseDate(obj.getString("employee_dob"));
                    SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
                    String strDate = formatter.format(date);
                    add.news_description = strDate;
                    add.news_image = obj.getString("employee_photo");
                    add.setType("birthday");
                    System.out.println("News Id" + obj.getInt("id"));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter1.add(add);
                        }
                    });
                }
            } catch (JSONException e) {
//            createAndShowDialog(e,"No data");
                e.printStackTrace();
                upcoming_birthday.setVisibility(View.GONE);
                more_upcoming_birthday.setVisibility(View.GONE);
                birthday_end.setVisibility(View.GONE);
            }

        }else if (anniversary_today) {
            Download_data download_data = new Download_data((Download_data.download_complete) this);
            download_data.download_data_from_link(ANNIVERSARY_URL);
            anniversary_today = false;
            anniversary = true;
            try {
                JSONArray data_array = new JSONArray(data);
                System.out.println("Object" + data_array);
                for (int i = 0; i < 1; i++) {
                    JSONObject obj = new JSONObject(data_array.get(0).toString());
                    System.out.println("Object" + obj);
                    final News add = new News("Title");
                    add.news_title = obj.getString("employee_name");
                    add.setTitle(obj.getString("employee_name"));
                    add.setId(obj.getInt("id"));
                    Date date = parseDate(obj.getString("employee_doj"));
                    SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
                    String strDate = formatter.format(date);
                    add.news_description = strDate;
                    add.news_image = obj.getString("employee_photo");
                    add.setType("anniversary_today");
                    System.out.println("News Id" + obj.getInt("id"));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter7.add(add);
                        }
                    });
                }
            } catch (JSONException e) {
//            createAndShowDialog(e,"No data");
                e.printStackTrace();
                today_anniversary.setVisibility(View.GONE);
                more_today_anniversary.setVisibility(View.GONE);
                anniversary_today_end.setVisibility(View.GONE);
            }
        } else if (anniversary) {
            Download_data download_data = new Download_data((Download_data.download_complete) this);
            download_data.download_data_from_link(SHOUTOUT_URL);
            shoutout_ = true;
            anniversary = false;
            try {
                JSONArray data_array = new JSONArray(data);
                System.out.println("Object" + data_array);
                for (int i = 0; i < 1; i++) {
                    JSONObject obj = new JSONObject(data_array.get(0).toString());
                    System.out.println("Object" + obj);
                    final News add = new News("Title");
                    add.news_title = obj.getString("employee_name");
                    add.setTitle(obj.getString("employee_name"));
                    add.setId(obj.getInt("id"));
                    Date date = parseDate(obj.getString("employee_doj"));
                    SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
                    String strDate = formatter.format(date);
                    add.news_description = strDate;
                    add.news_image = obj.getString("employee_photo");
                    add.setType("anniversary");
                    System.out.println("News Id" + obj.getInt("id"));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter2.add(add);
                        }
                    });
                }
            } catch (JSONException e) {
//            createAndShowDialog(e,"No data");
                e.printStackTrace();
                upcoming_anniversary.setVisibility(View.GONE);
                more_upcoming_anniversary.setVisibility(View.GONE);
                anniversary_end.setVisibility(View.GONE);
            }
        } else if (shoutout_) {
            Download_data download_data = new Download_data((Download_data.download_complete) this);
            download_data.download_data_from_link(NEWS_URL);
            news_ = true;
            shoutout_ = false;
            try {
                JSONObject object = new JSONObject(data);
                JSONArray data_array = object.getJSONArray("results");
                System.out.println("Object" + data_array);
                for (int i = 0; i < 1; i++) {
                    JSONObject obj = new JSONObject(data_array.get(0).toString());
                    final News add = new News();
                    add.news_title = obj.getString("employee_from_profile");
                    add.setTitle(obj.getString("employee_from_profile"));
                    add.setId(obj.getInt("id"));
                    add.news_description = obj.getString("shoutout_description");
                    add.setNews_video(obj.getString("employee_from_name"));
                    add.setNews_document(obj.getString("employee_to_name"));
                    add.news_image = obj.getString("employee_to_profile");
                    add.setType("shoutout");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter3.add(add);
                        }
                    });
                }
            } catch (JSONException e) {
//            createAndShowDialog(e,"No data");
                e.printStackTrace();
                upcoming_shoutout.setVisibility(View.GONE);
                more_shoutout.setVisibility(View.GONE);
                shoutout_end.setVisibility(View.GONE);
            }
        } else if (news_) {
            Download_data download_data = new Download_data((Download_data.download_complete) this);
            download_data.download_data_from_link(EVENT_URL);
            event_ = true;
            news_ = false;
            try {
                JSONArray data_array = new JSONArray(data);
                System.out.println("Object" + data_array);
                for (int i = 0; i < 1; i++) {
                    JSONObject obj = new JSONObject(data_array.get(0).toString());
                    final News add = new News("Title");
                    add.news_title = obj.getString("news_title");
                    add.setTitle(obj.getString("news_title"));
                    add.setId(obj.getInt("id"));
                    add.news_description = obj.getString("news_description");
                    add.news_image = obj.getString("news_image");
                    add.setType("news");
                    System.out.println("News Id" + obj.getInt("id"));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter4.add(add);
                        }
                    });
                }
            } catch (JSONException e) {
//            createAndShowDialog(e,"No data");
                e.printStackTrace();
                upcoming_news.setVisibility(View.GONE);
                more_news.setVisibility(View.GONE);
                news_end.setVisibility(View.GONE);
            }

        } else if (event_) {
            try {
                JSONArray data_array = new JSONArray(data);
                System.out.println("Object" + data_array);
                for (int i = 0; i < 1; i++) {
                    JSONObject obj = new JSONObject(data_array.get(0).toString());
                    final News add = new News("Title");
                    add.news_title = obj.getString("events_title");
                    add.setId(obj.getInt("id"));
                    add.news_description = obj.getString("events_description");
                    add.news_image = obj.getString("events_image");
                    add.setType("events");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter5.add(add);
                        }
                    });
                }
                event_ = false;
            } catch (JSONException e) {
//            createAndShowDialog(e,"No data");
                e.printStackTrace();
                event_end.setVisibility(View.GONE);
            }
        }
    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    private void createAndShowDialog(final String message, final String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

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
                    viewHolder = new DefaultFragment.PaginationAdapter.LoadingVH(v2);
                    break;
            }
            return viewHolder;
        }

        @NonNull
        private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
            final RecyclerView.ViewHolder viewHolder;
            View v1 = inflater.inflate(R.layout.birthdaylist_layout, parent, false);
            viewHolder = new DefaultFragment.PaginationAdapter.NewsVH(v1);
            final View.OnClickListener mOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    News news = newsList.get(viewHolder.getAdapterPosition());
                    String category = news.getType();
                    switch (category){
                        case "birthday":{
                            Intent intent = new Intent(getActivity(),ListingMore.class);
                            intent.putExtra("more","upcoming_birthday");
                            startActivity(intent);
                            break;
                        }
                        case "birthday_today":{
                            Intent intent = new Intent(getActivity(),ListingMore.class);
                            intent.putExtra("more","today_birthday");
                            startActivity(intent);
                            break;
                        }
                        case "anniversary_today":{
                            Intent intent = new Intent(getActivity(),ListingMore.class);
                            intent.putExtra("more","today_birthday");
                            startActivity(intent);
                            break;
                        }
                        case "anniversary":{
                            Intent intent = new Intent(getActivity(),ListingMore.class);
                            intent.putExtra("more","upcoming_anniversary");
                            startActivity(intent);
                            break;
                        }
                        case "events":{
                            Intent intent = new Intent(getActivity(),FullEvent.class);
                            intent.putExtra("id", news.getId());
                            intent.putExtra("check","EVENTS");
                            startActivity(intent);
                            break;
                        }
                        case "news":{
                            Intent intent = new Intent(getActivity(),FullEvent.class);
                            intent.putExtra("id", news.getId());
                            intent.putExtra("check","NEWS");
                            startActivity(intent);
                        }
                    }

                }
            };
            v1.setOnClickListener(mOnClickListener);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            News news = newsList.get(position);

            switch (getItemViewType(position)) {
                case ITEM:
                    DefaultFragment.PaginationAdapter.NewsVH newsVH = (DefaultFragment.PaginationAdapter.NewsVH) holder;
                    newsVH.news_title.setText(news.getTitle());
                    newsVH.news_description.setText(news.getNews_description());
                    loadImageFromUrl(newsVH.news_image,(SERVER_URL+news.getNews_image()));
                    break;
                case LOADING:
//                Do nothswing
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

    public class PaginationAdapter1 extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private static final int ITEM = 0;
        private static final int LOADING = 1;
        NewsMain main;
        private List<News> newsList;
        private Context context;
        private boolean isLoadingAdded = false;

        public PaginationAdapter1(Context context) {
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
                    viewHolder = new DefaultFragment.PaginationAdapter1.LoadingVH(v2);
                    break;
            }
            return viewHolder;
        }

        @NonNull
        private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
            final RecyclerView.ViewHolder viewHolder;
            View v1 = inflater.inflate(R.layout.shoutoutlist_layout, parent, false);
            viewHolder = new DefaultFragment.PaginationAdapter1.NewsVH(v1);
            final View.OnClickListener mOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),ListingMore.class);
                    intent.putExtra("more","shoutout");
                    startActivity(intent);
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
                    DefaultFragment.PaginationAdapter1.NewsVH newsVH = (DefaultFragment.PaginationAdapter1.NewsVH) holder;
                    newsVH.news_title.setText(news.getNews_description());
                    newsVH.sender.setText(news.getNews_video());
                    newsVH.receiver.setText(news.getNews_document());
                    loadImageFromUrl(newsVH.shoutout_to,(SERVER_URL+news.getTitle()));
                    loadImageFromUrl(newsVH.shoutout_from,(SERVER_URL+news.getNews_image()));
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
            TextView news_title, sender, receiver;
            ImageView shoutout_from, shoutout_to;
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
                shoutout_from = (ImageView) itemView.findViewById(R.id.shoutout_from);
                shoutout_to = (ImageView) itemView.findViewById(R.id.shoutout_to);
                sender = (TextView) itemView.findViewById(R.id.sender);
                receiver = (TextView) itemView.findViewById(R.id.receiver);
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
}
