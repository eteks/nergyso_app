package com.vdart.example.app;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.internal.zzahf.runOnUiThread;

public class FifthFragment extends Fragment implements  Download_data.download_complete{


    View view;
    Button firstButton;
    private ListAdapter mAdapter;
    Toolbar toolbar;
    private static final String TAG = "NewsMain";
    String listValue = "NULL";
    FifthFragment.PaginationAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    String nextPage;
    String SERVER_URL;
    String NEWS_URL;
    RecyclerView rv;
    ProgressBar progressBar;
    String RECENT_NEWS_URL = "api/news/recent_news";
    TextView news_more ;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fifth, container, false);
//        TextView t = (TextView) view.findViewById(R.id.news_title2);
//        t.setText("Dei");
        rv = (RecyclerView) view.findViewById(R.id.main_recycler);
//        final ProgressBar progressBar = new ProgressBar(getActivity());

        adapter = new FifthFragment.PaginationAdapter(getActivity());

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);

        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);

        SERVER_URL = getString(R.string.service_url);
        RECENT_NEWS_URL = SERVER_URL + RECENT_NEWS_URL;

        news_more = (TextView) view.findViewById(R.id.news_more);
        news_more.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewsMain.class);
                startActivity(intent);
            }
        });

        Download_data download_data = new Download_data((Download_data.download_complete) this);
        download_data.download_data_from_link(RECENT_NEWS_URL);


//        final News add=new News("Title");
////        add.news_title = "News Title - 1";
//        add.setTitle("VDart Wins highest Honor");
//        add.setId(1);
//        add.news_description = "Vdart Inc was awarded by the National Mino";
//        add.news_image = "";
//        final News add1=new News("Title");
////        add1.news_title = "News Title - 2";
//        add1.setTitle("Books for Africa");
//        add1.setId(1);
//        add1.news_description = "Members of team Atlanta pitched in to celebration";
//        add1.news_image = "";
//        final News add2=new News("Title");
////        add2.news_title = "News Title - 3";
//        add2.setTitle("Vdart named top ten Asian American Company");
//        add2.setId(1);
//        add2.news_description = "At first annua; Diversity in Action awards, hosted by the USPAACC-SE";
//        add2.news_image = "";
////        System.out.println("News Id"+obj.getInt("id"));
////                news.add(add);
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
////                progressBar.setVisibility(View.GONE);
//                adapter.add(add);
//                adapter.add(add1);
//                adapter.add(add2);
//            }
//        });
//        TextView r = (TextView) getView().findViewById(R.id.news_title2);
//        r.setText("");
        // get the reference of Button
//        firstButton = (Button) view.findViewById(R.id.firstButton);
//        // perform setOnClickListener on first Button
//        firstButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // display a message by using a Toast
//                Toast.makeText(getActivity(), "First Fragment", Toast.LENGTH_LONG).show();
//            }
//        });
        return view;
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        mAdapter = new ListAdapter(this, R.layout.newslist_layout);
//        ListView listViewTodo = (ListView) findViewById(R.id.);
//        listViewTodo.setAdapter(mAdapter);
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        rv = (RecyclerView) getView().findViewById(R.id.main_recycler);
////        final ProgressBar progressBar = new ProgressBar(getActivity());
//
//        adapter = new FifthFragment.PaginationAdapter(getActivity());
//
//        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        rv.setLayoutManager(linearLayoutManager);
//
//        rv.setItemAnimator(new DefaultItemAnimator());
//
//        rv.setAdapter(adapter);
//        final News add=new News("Title");
//        add.news_title = "News Title - 1";
//        add.setTitle("News Title - 1");
//        add.setId(1);
//        add.news_description = "News Description - 1";
//        add.news_image = "";
////        System.out.println("News Id"+obj.getInt("id"));
////                news.add(add);
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
////                progressBar.setVisibility(View.GONE);
//                adapter.add(add);
//            }
//        });
//        setContentView(R.layout.activity_news_main);
//        SERVER_URL = getString(R.string.service_url);
//        NEWS_URL = SERVER_URL+ "api/news/";
//        toolbar = (Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.setTitleTextColor(0xFFFFFFFF);
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
//        rv = (RecyclerView) findViewById(R.id.main_recycler);
//        progressBar = (ProgressBar) findViewById(R.id.main_progress);
//
//        adapter = new FifthFragment.PaginationAdapter(this);
//
//        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        rv.setLayoutManager(linearLayoutManager);
//
//        rv.setItemAnimator(new DefaultItemAnimator());
//
//        rv.setAdapter(adapter);

//        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
//            @Override
//            protected void loadMoreItems() {
//                isLoading = true;
//                currentPage += 1;
//
//                // mocking network delay for API call
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        loadNextPage();
//                    }
//                }, 1000);
//            }
//
//            @Override
//            public int getTotalPageCount() {
//                return TOTAL_PAGES;
//            }
//
//            @Override
//            public boolean isLastPage() {
//                return isLastPage;
//            }
//
//            @Override
//            public boolean isLoading() {
//                return isLoading;
//            }
//        });
//
//
//        // mocking network delay for API call
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                loadFirstPage();
//            }
//        }, 1000);

        Download_data download_data = new Download_data((Download_data.download_complete) this);
        download_data.download_data_from_link("http://10.0.0.15:8000/api/news/");
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
            JSONArray data_array = new JSONArray(data);
            System.out.println("Object"+data_array);
//            nextPage = object.getString("next");
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
                add.news_description = obj.getString("news_description");
                add.news_image = obj.getString("news_image");
                System.out.println("News Id"+obj.getInt("id"));
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

        } catch (JSONException e) {
            createAndShowDialog(e,"No connection");
            e.printStackTrace();
//            loadFirstPage();
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

    private void createAndShowDialogFromTask(final Exception exception, String title) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createAndShowDialog(exception, "Error");
            }
        });
    }

    public void clickList(){
        Intent intent = new Intent(getActivity(),FullNews.class);
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
                    viewHolder = new FifthFragment.PaginationAdapter.LoadingVH(v2);
                    break;
            }
            return viewHolder;
        }

        @NonNull
        private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
            final RecyclerView.ViewHolder viewHolder;
            View v1 = inflater.inflate(R.layout.newslist_layout, parent, false);
            viewHolder = new FifthFragment.PaginationAdapter.NewsVH(v1);
            final View.OnClickListener mOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    News news = newsList.get(viewHolder.getAdapterPosition());
                    System.out.println("CLICKed"+news.getId());
                    int id = news.getId();
                    Intent intent = new Intent(getActivity(),FullNews.class);
                    intent.putExtra("id", id);
                    intent.putExtra("check","NEWS");
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
                    FifthFragment.PaginationAdapter.NewsVH newsVH = (FifthFragment.PaginationAdapter.NewsVH) holder;
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
                intent = new Intent(getActivity(),GridList.class);
                startActivity(intent);
                return true;

            case R.id.profile:
                intent = new Intent(getActivity(),ProfileActivity.class);
                startActivity(intent);
                return true;

            case R.id.events:
                intent = new Intent(getActivity(),EventMain.class);
                startActivity(intent);
                return true;

            case R.id.news:
                intent = new Intent(getActivity(),NewsMain.class);
                startActivity(intent);
                return true;

            case R.id.shoutout:
                intent = new Intent(getActivity(),Shoutout.class);
                startActivity(intent);
                return true;

            case R.id.gallery:
                Toast.makeText(getActivity(), "Coming soon", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.info:
                Toast.makeText(getActivity(), "Coming soon", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.settings:
                intent = new Intent(getActivity(),Changepassword_Activity.class);
                startActivity(intent);
                return true;

            case R.id.logout:
                intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
                return true;


            default:
                return super.onOptionsItemSelected(item);
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
