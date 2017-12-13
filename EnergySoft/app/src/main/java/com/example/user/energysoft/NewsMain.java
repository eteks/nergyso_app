package com.example.user.energysoft;

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

import com.example.user.energysoft.utils.PaginationScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

public class NewsMain extends AppCompatActivity implements Download_data.download_complete {
    Toolbar toolbar;
    private static final String TAG = "NewsMain";
    String listValue = "NULL";
    PaginationAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    String nextPage;
    String SERVER_URL;
    String NEWS_URL;
    RecyclerView rv;
    ProgressBar progressBar;

    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;

    //    String event = "{\"events\":[ {\"id\":\"1\",\"events_title\":\"Event-1\",\"events_description\":\"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\",\"events_venue\":\"Pondicherry\",\"events_image\":\"http://www.fotothing.com/photos/df3/df309f51ae73de90d1ea09aa2e3d312d.jpg\",\"events_video\":\"http://www.fotothing.com/photos/df3/df309f51ae73de90d1ea09aa2e3d312d.jpg\",\"events_document\":\"https://www.google.co.in/url?sa=t&rct=j&q=&esrc=s&source=web&cd=4&cad=rja&uact=8&ved=0ahUKEwjM99DFtfzXAhVMqo8KHXxEDg0QFgg5MAM&url=http%3A%2F%2Funec.edu.az%2Fapplication%2Fuploads%2F2014%2F12%2Fpdf-sample.pdf&usg=AOvVaw2a4IUOgpX0IaeRxQpCN2l0\",\"events_date\":\"21-07-2017\"]}";
    public ListView list;
    public ArrayList<News> newsList = new ArrayList<News>();
    public ListAdapter NewsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_main);
        SERVER_URL = getString(R.string.service_url);
        NEWS_URL = SERVER_URL+ "api/news/";
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
//        ImageView home = (ImageView) findViewById(R.id.action_home);
//        home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(NewsMain.this, GridList.class);
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


        // mocking network delay for API call
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadFirstPage();
            }
        }, 1000);

//        Download_data download_data = new Download_data((Download_data.download_complete) this);
//        download_data.download_data_from_link("http://10.0.0.15:8000/api/news/");
    }

    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");
//        final List<News> newsList = News.createMovies(adapter.getItemCount());
        Download_data download_data = new Download_data((Download_data.download_complete) this);
        download_data.download_data_from_link(NEWS_URL);

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
        try {
            JSONObject object = new JSONObject(data);
            System.out.println("Object"+object);
            JSONArray data_array = object.getJSONArray("results");
            System.out.println("Object"+data_array);
            nextPage = object.getString("next");
            if(data_array.length() == 0){
                createAndShowDialog("Server Error","No connection");
            }
            for (int i = 0 ; i < data_array.length() ; i++)
            {
                JSONObject obj=new JSONObject(data_array.get(i).toString());
//                System.out.println("Object"+obj);
                final News add=new News("Title");
                add.news_title = obj.getString("news_title");
                add.setTitle(obj.getString("news_title"));
                add.setId(obj.getInt("id"));
//                add.news_description = obj.getString("news_description");
//                add.news_image = obj.getString("news_image");
                System.out.println("News Id"+obj.getInt("id"));
//                news.add(add);
                runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    adapter.add(add);
                }
            });

            }
//            if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
//            else isLastPage = true;

//            NewsAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            createAndShowDialog(e,"No connection");
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
        Intent intent = new Intent(NewsMain.this,FullNews.class);
        startActivity(intent);
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
                    Intent intent = new Intent(NewsMain.this,FullNews.class);
                    intent.putExtra("id", id);
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
//                newsVH.news_description.setText("Description");
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
            //        ListAdapter.ViewHolderItem holder = new ListAdapter.ViewHolderItem();
            public NewsVH(View itemView) {
                super(itemView);
//            ListAdapter.ViewHolderItem holder = new ListAdapter.ViewHolderItem();
//            if (convertView == null) {
//                LayoutInflater inflater = (LayoutInflater) main.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                convertView = inflater.inflate(R.layout.newslist_layout, null);

//            holder.name = (TextView) convertView.findViewById(R.id.name);
//            holder.code = (TextView) convertView.findViewById(R.id.code);
                news_title = (TextView) itemView.findViewById(R.id.news_title);
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


}
