package com.example.user.energysoft;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.user.energysoft.MainActivity.MyPREFERENCES;
import static com.google.android.gms.internal.zzahf.runOnUiThread;

public class ThirdFragment extends Fragment implements Download_data.download_complete{
    String SHOUT_URL ;
    String SHOUTOUT_LIST_URL;
    EditText shoutout_description, shoutout_employee_to;
    View view;
    Button firstButton;
    private ListAdapter mAdapter;
    Toolbar toolbar;
    private static final String TAG = "NewsMain";
    String listValue = "NULL";
    ThirdFragment.PaginationAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    String nextPage;
    String SERVER_URL;
    String EMPLOYEE_URL;
    RecyclerView rv;
    ProgressBar progressBar;
    boolean employee = false;
    boolean shout_list = false;

    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;
    String[] Employees_Name = {"1", "2", "3", "4", "5","6","1", "2", "3", "4", "5","6","1", "2", "3", "4", "5","6","1", "2", "3", "4", "5","6","1", "2", "3", "4", "5","6","1", "2", "3", "4", "5","6","1", "2", "3", "4", "5","6"};
    int[] Employees_ID = new int[5];
    //    String event = "{\"events\":[ {\"id\":\"1\",\"events_title\":\"Event-1\",\"events_description\":\"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\",\"events_venue\":\"Pondicherry\",\"events_image\":\"http://www.fotothing.com/photos/df3/df309f51ae73de90d1ea09aa2e3d312d.jpg\",\"events_video\":\"http://www.fotothing.com/photos/df3/df309f51ae73de90d1ea09aa2e3d312d.jpg\",\"events_document\":\"https://www.google.co.in/url?sa=t&rct=j&q=&esrc=s&source=web&cd=4&cad=rja&uact=8&ved=0ahUKEwjM99DFtfzXAhVMqo8KHXxEDg0QFgg5MAM&url=http%3A%2F%2Funec.edu.az%2Fapplication%2Fuploads%2F2014%2F12%2Fpdf-sample.pdf&usg=AOvVaw2a4IUOgpX0IaeRxQpCN2l0\",\"events_date\":\"21-07-2017\"]}";
    public ListView list;
    public ArrayList<News> newsList = new ArrayList<News>();
    public ListAdapter NewsAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_third, container, false);
//        TextView t = (TextView) view.findViewById(R.id.news_title2);
//        t.setText("Dei");
        rv = (RecyclerView) view.findViewById(R.id.main_recycler);
//        final ProgressBar progressBar = new ProgressBar(getActivity());

        adapter = new ThirdFragment.PaginationAdapter(getActivity());

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);

        rv.setItemAnimator(new DefaultItemAnimator());

        SERVER_URL = getString(R.string.service_url);
        EMPLOYEE_URL = SERVER_URL+"api/employee/employee_tag_details/";
        SHOUT_URL = SERVER_URL+"api/shoutout_post/";
        SHOUTOUT_LIST_URL = SERVER_URL+"api/shoutout_list/";

        SharedPreferences shared = getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        System.out.println("USer : "+shared.getInt("id",0));
        final int employee_id = shared.getInt("id",0);

        rv.setAdapter(adapter);
        ArrayAdapter<String> Employee_adapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, Employees_Name);
        //Getting the instance of AutoCompleteTextView
        final AutoCompleteTextView actv = (AutoCompleteTextView) view.findViewById(R.id.shoutout_employee_to);
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(Employee_adapter);//setting the adapter data into the AutoCompleteTextView
        actv.setTextColor(getResources().getColor(R.color.blueText));
//        final News add=new News("Title");
//        add.news_title = "Shoutout - 1";
//        add.setTitle("Hi everyone be ready for the treat.");
//        add.setId(1);
//        add.news_description = "at com.android.okhttp.internal.huc.HttpURLConnectionImpl.getInputStream(HttpURLConnectionImpl.java:250)";
//        add.news_image = "";
//        final News add1=new News("Title");
//        add1.news_title = "Shoutout - 2";
//        add1.setTitle("Hi everyone be ready for the treat.");
//        add1.setId(1);
//        add1.news_description = "at com.android.okhttp.internal.huc.HttpURLConnectionImpl.getInputStream(HttpURLConnectionImpl.java:250)";
//        add1.news_image = "";
//        final News add2=new News("Title");
//        add2.news_title = "Shoutout - 3";
//        add2.setTitle("Hi everyone be ready for the treat.");
//        add2.setId(1);
//        add2.news_description = "at com.android.okhttp.internal.huc.HttpURLConnectionImpl.getInputStream(HttpURLConnectionImpl.java:250)";
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

        Download_data download_data = new Download_data((Download_data.download_complete) this);
        download_data.download_data_from_link(EMPLOYEE_URL);
        employee = true;

        shoutout_description = (EditText) view.findViewById(R.id.shoutout_description);
        shoutout_employee_to = (EditText) view.findViewById(R.id.shoutout_employee_to);
        Button shout = (Button) view.findViewById(R.id.shoutpost);
        shout.setOnClickListener(new View.OnClickListener() {
            int ONE_TIME = 0;
            @Override
            public void onClick(View view) {
                ONE_TIME++;
                if (ONE_TIME == 1) {
                    String shoutout_text = shoutout_description.getText().toString();
                    String shoutout_to = actv.getText().toString();
                    int shoutout_to_employee = 0;
                    for(int i = 0; i < Employees_Name.length; i++){
                        if(shoutout_to.equals(Employees_Name[i])){
                            shoutout_to_employee = Employees_ID[i];
                            System.out.println("Shout out employee"+shoutout_to_employee);
                        }
                    }
                    String data = null;
                    try {
                        data = URLEncoder.encode("shoutout_description", "UTF-8")
                                + "=" + URLEncoder.encode(shoutout_text, "UTF-8");
                        data += "&" + URLEncoder.encode("shoutout_employee_to", "UTF-8")
                                + "=" + shoutout_to_employee;
                        data += "&" + URLEncoder.encode("shoutout_employee_from", "UTF-8")
                                + "=" + employee_id;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    final BufferedReader[] reader = {null};

                    // Send data
                    final String finalData = data;
                    System.out.println("Data"+finalData);
                    AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                ONE_TIME = 0;
                                // Defined URL  where to send data
                                URL url = new URL(SHOUT_URL);

                                // Send POST data request
                                System.out.println("URL:" + SHOUT_URL);
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
                                System.out.println("Output"+sb.toString());
                                JSONObject object = new JSONObject(sb.toString());
                                if(object.has("success")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getActivity(), "Posted successfully!", Toast.LENGTH_SHORT).show();
                                            shoutout_description.setText("");
                                            shoutout_employee_to.setText("");
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
                                System.out.println("Error"+ex);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        createAndShowDialog("No internet connection", "Error");
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
            }
        });
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


//        Download_data download_data = new Download_data((Download_data.download_complete) this);
//        download_data.download_data_from_link("http://10.0.0.15:8000/api/news/");
    }

    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");
//        final List<News> newsList = News.createMovies(adapter.getItemCount());
//        Download_data download_data = new Download_data((Download_data.download_complete) this);
//        download_data.download_data_from_link(NEWS_URL);

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
        //For storing employee data
        if(employee){
            try {
                JSONArray object = new JSONArray(data);
                System.out.println("Object"+object);

                if(object.length() == 0){
                    createAndShowDialog("Server Error","No connection");
                }
                for (int i = 0 ; i < object.length() ; i++)
                {
                    JSONObject obj=new JSONObject(object.get(i).toString());
                    Employees_Name[i] = obj.getString("employee_name");
                    Employees_ID[i] = obj.getInt("id");
                }

//            if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
//            else isLastPage = true;

//            NewsAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                createAndShowDialog(e,"No connection");
                e.printStackTrace();
//            loadFirstPage();
            }
            employee = false;
            Download_data download_data = new Download_data((Download_data.download_complete) this);
            download_data.download_data_from_link(SHOUTOUT_LIST_URL);
            shout_list = true;

        }else if(shout_list){ //For storing shoutout lists
            try {
                JSONObject object = new JSONObject(data);
                System.out.println("Object"+object);
                JSONArray data_array = object.getJSONArray("results");
                System.out.println("Object"+data_array);
//                nextPage = object.getString("next");
                if(data_array.length() == 0){
                    createAndShowDialog("Server Error","No connection");
                }
                for (int i = 0 ; i < data_array.length() ; i++)
                {
                    JSONObject obj=new JSONObject(data_array.get(i).toString());
//                System.out.println("Object"+obj);
                    final News add=new News();
                    add.news_title = obj.getString("employee_from_profile");
                    add.setTitle(obj.getString("employee_from_profile"));
                    add.setId(obj.getInt("id"));
                    add.news_description = obj.getString("shoutout_description");
//                    add.setTitle(obj.getString("events_title"));
//                    add.setDescription(obj.getString("events_description"));
                    add.news_image = obj.getString("employee_to_profile");
//                    add.setImage(obj.getString("events_image"));
//                    System.out.println("Events Id"+obj.getInt("id"));
//                news.add(add);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            progressBar.setVisibility(View.GONE);
                            adapter.add(add);
                        }
                    });

                }
//            if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
//            else isLastPage = true;

//            NewsAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                createAndShowDialog(e,"No connection");
                loadFirstPage();
                e.printStackTrace();
            }

            shout_list = false;
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
                    viewHolder = new ThirdFragment.PaginationAdapter.LoadingVH(v2);
                    break;
            }
            return viewHolder;
        }

        @NonNull
        private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
            final RecyclerView.ViewHolder viewHolder;
            View v1 = inflater.inflate(R.layout.shoutoutlist_layout, parent, false);
            viewHolder = new ThirdFragment.PaginationAdapter.NewsVH(v1);
            final View.OnClickListener mOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    News news = newsList.get(viewHolder.getAdapterPosition());
//                    System.out.println("CLICKed"+news.getId());
//                    int id = news.getId();
//                    Intent intent = new Intent(getActivity(),FullNews.class);
//                    intent.putExtra("id", id);
////                    finish();
//                    startActivity(intent);
////                news.setPage("FullNews");
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
                    ThirdFragment.PaginationAdapter.NewsVH newsVH = (ThirdFragment.PaginationAdapter.NewsVH) holder;
                    newsVH.news_title.setText(news.getNews_description());
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
            TextView news_title;
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
