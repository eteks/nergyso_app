package com.vdart.apps.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import static com.vdart.apps.app.MainActivity.MyPREFERENCES;

public class CeomessageActivity extends AppCompatActivity implements Download_data.download_complete{
    Toolbar toolbar;
    EditText ceo_message;
    Button ceo_post;
    String CEO_POST_URL = "api/ceo_message_post/";
    String CEO_LIST_URL = "api/ceo_message_list/";
    String SERVER_URL;
    RecyclerView rv;
    CeomessageActivity.PaginationAdapter adapter;
    ProgressBar progressBar;
    LinearLayoutManager linearLayoutManager;
    TextView ceo_more, ceomsgtitle;
    RelativeLayout ceo_messaging;
    Menu menuInflate;
    String notification_count = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ceomessage);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SERVER_URL = getString(R.string.service_url);
        CEO_POST_URL = SERVER_URL + CEO_POST_URL ;

        CEO_LIST_URL = SERVER_URL + CEO_LIST_URL;

        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final int employee_id = shared.getInt("id",0);

        notification_count = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).getString("nc","");

        rv = (RecyclerView) findViewById(R.id.main_recycler);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);

        ceo_messaging = (RelativeLayout) findViewById(R.id.ceo_messaging);

        adapter = new CeomessageActivity.PaginationAdapter(this);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);

        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);

        ceo_more = (TextView) findViewById(R.id.ceo_more);
        ceo_more.setVisibility(View.GONE);
        ceo_more.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CeomessageActivity.this, CeoMain.class);
                startActivity(intent);
            }
        });

        Download_data download_data = new Download_data((Download_data.download_complete) this);
        download_data.download_data_from_link(CEO_LIST_URL);

        ceo_message = (EditText) findViewById(R.id.ceo_message);
        ceo_post = (Button) findViewById(R.id.ceo_post);
        ceomsgtitle = (TextView) findViewById(R.id.ceomsgtitle);

        String designation = shared.getString("employee_designation","");
        if(designation.toUpperCase().equals("CEO")){
            ceo_messaging.setVisibility(View.VISIBLE);
        }
        ceo_post.setOnClickListener(new View.OnClickListener() {
            int ONE_TIME = 0;
            @Override
            public void onClick(View view) {
                String message = ceo_message.getText().toString();
                String data = null;
                try {
                    data = URLEncoder.encode("ceo_message", "UTF-8")
                            + "=" + URLEncoder.encode(message, "UTF-8");
                    data += "&" + URLEncoder.encode("ceo_employee", "UTF-8")
                            + "=" + employee_id;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                final BufferedReader[] reader = {null};

                // Send data
                final String finalData = data;
                System.out.println("Data" + finalData);
                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            ONE_TIME = 0;
                            // Defined URL  where to send data
                            URL url = new URL(CEO_POST_URL);

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
                            System.out.println("Output" + sb.toString());
                            JSONObject object = new JSONObject(sb.toString());
                            if (object.has("id")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(CeomessageActivity.this, "Posted successfully!", Toast.LENGTH_SHORT).show();
                                        ceo_message.setText("");
                                    }
                                });
//                                    System.out.println("object"+object.getString("key"));
//                                    SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//                                    SharedPreferences.Editor editor = sharedpreferences.edit();
//                                    editor.putString("key", object.getString("key"));
//                                    editor.putInt("id",object.getInt("user"));
//                                    editor.putString("username",object.getString("username"));
//                                    editor.putString("email",object.getString("email"));
//                                    editor.commit();
//                                Intent intent = new Intent(MainActivity.this, GridList.class);
////                                    intent.putExtra("key", object.getString("key"));
////                                    finish();
//                                startActivity(intent);
//                                }else{
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            createAndShowDialog("Coming soon", "Not Ready");
//                                        }
//                                    });
                            }
                        } catch (Exception ex) {
                            System.out.println("Error" + ex);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    createAndShowDialog("Message already exists", "Error");
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

            }
        });

    }

    public void get_data(String data)
    {
        try {
//            JSONObject object = new JSONObject(data);
//            System.out.println("Object"+object);
            JSONArray data_array = new JSONArray(data);
            int length = data_array.length();
//            JSONArray data_array = object.getJSONArray("results");
            System.out.println("Object"+data_array);
//            nextPage = object.getString("next");
            if(data_array.length() == 0){
                createAndShowDialog("Server Error","No connection");
            }else if(data_array.length() > 3){
                ceo_more.setVisibility(View.VISIBLE);
                length = 3;
            }
            for (int i = 0 ; i < length ; i++)
            {
                JSONObject obj=new JSONObject(data_array.get(i).toString());
//                System.out.println("Object"+obj);
                final Event add=new Event();
//                add.events_title = obj.getString("events_title");
                add.setTitle(obj.getString("ceo_message"));
                add.setId(obj.getInt("id"));
                add.setImage(SERVER_URL + obj.getString("ceo_employee_photo"));
//                add.events_description = obj.getString("events_description");
//                add.setTitle(obj.getString("events_title"));
//                add.setDescription(obj.getString("events_description"));
//                add.events_image = obj.getString("events_image");
//                add.setImage("");
                System.out.println("Events Id"+obj.getInt("id"));
//                news.add(add);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        progressBar.setVisibility(View.GONE);
                        adapter.add(add);
                    }
                });

            }
//            if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
//            else isLastPage = true;

//            NewsAdapter.notifyDataSetChanged();
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        }
        catch (JSONException e) {
            createAndShowDialog(e,"No connection");
//            loadFirstPage();
            e.printStackTrace();
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
        setCount(this,notification_count);
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
                    viewHolder = new CeomessageActivity.PaginationAdapter.LoadingVH(v2);
                    break;
            }
            return viewHolder;
        }

        @NonNull
        private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, final LayoutInflater inflater) {
            final RecyclerView.ViewHolder viewHolder;
            View v1 = inflater.inflate(R.layout.ceolist_layout, parent, false);
            viewHolder = new CeomessageActivity.PaginationAdapter.EventVH(v1);
            final View.OnClickListener mOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Event event = eventList.get(viewHolder.getAdapterPosition());
//                    System.out.println("CLICKed"+event.getId());
//                    int id = event.getId();
//                    String check = "EVENTS";
//                    Intent intent = new Intent(main,FullEvent.class);
//                    intent.putExtra("id", id);
//                    intent.putExtra("check",check);
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
                    CeomessageActivity.PaginationAdapter.EventVH eventVH = (CeomessageActivity.PaginationAdapter.EventVH) holder;
                    eventVH.events_title.setText(event.getTitle());
                    loadImageFromUrl(eventVH.events_image,event.getEvents_image());
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
            TextView events_title,events_description, readMore;
            ImageView events_image;
            int ONE = 1;
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
                readMore = (TextView) itemView.findViewById(R.id.readMore);
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
                readMore.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        if(ONE == 1){
                            events_title.setMaxLines(10);
                            ONE = 0;
                        }else if(ONE == 0){
                            events_title.setMaxLines(1);
                            ONE = 1;
                        }
                    }
                });
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
