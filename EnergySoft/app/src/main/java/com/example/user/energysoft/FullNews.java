package com.example.user.energysoft;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;


public class FullNews extends AppCompatActivity implements Download_data.download_complete{
    String SERVER_URL ;
    String FULL_NEWS_URL ;
    int ONE_TIME = 0, TOTAL_PAGES = 0;
    String RECENT_NEWS_URL ;
    Toolbar toolbar;
    private static ViewPager mPager;
    private static ScrollView mScrollView;
    TextView full_news_title, full_news_description, full_text_news_description;
    ImageView news_photo ;
    private static int currentPage = 0;
    private static final String[] XMEN = new String[200];
    private static final int[] ID = new int[200];
    private ArrayList<String> XMENArray = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_news);
        SERVER_URL = getString(R.string.service_url);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        FULL_NEWS_URL = SERVER_URL+ "api/news/";
        RECENT_NEWS_URL = SERVER_URL +"api/news/recent_news";
        init();
//        ImageView home = (ImageView) findViewById(R.id.action_home);
//        home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(FullNews.this, GridList.class);
//                startActivity(intent);
//            }
//        });
        full_news_title = (TextView) findViewById(R.id.full_events_title);
        full_news_description = (TextView) findViewById(R.id.full_news_description);
        full_text_news_description = (TextView) findViewById(R.id.full_text_events_description);
        news_photo = (ImageView) findViewById(R.id.events_photo);
        int id = getIntent().getIntExtra("id",0);
        FULL_NEWS_URL = FULL_NEWS_URL+id+"/";
        Download_data download_data = new Download_data((Download_data.download_complete) this);
        download_data.download_data_from_link(FULL_NEWS_URL);
//        String text = "guhsfuguig u sghusfu gsgiug siuguiosgui iiu guigsugujgsg ii,"+
//                "hjksg hk ujhfjkhg vbjkgkj gjkg gjk fd jhgjuhg jkgjk,"+
//                "jhsgujj hgsjvjhgjhdf jhgjgjgj jghsjgjgsj jhgjsgj";
//        WebView webView = (WebView) findViewById(R.id.full_text_news_description);
//                webView.loadData("<p style=\"text-align: justify\">"+ text + "</p>", "text/html", "UTF-8");
    }
    private void init() {
//        for(int i=0;i<XMEN.length;i++){
//            XMENArray.add(XMEN[i]);
//        }
//        NestedScrollView scrollView = (NestedScrollView) findViewById (R.id.nest_scrollview);
//        scrollView.setFillViewport (true);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new MyAdapter(FullNews.this,XMENArray));
//        mScrollView = (ScrollView) findViewById(R.id.news_scroll);
//        mScrollView.setFillViewport(true);
//        mPager.setOnTouchListener(new View.OnTouchListener() {
//            int dragthreshold = 30;
//            int downX;
//            int downY;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        downX = (int) event.getRawX();
//                        downY = (int) event.getRawY();
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        int distanceX = Math.abs((int) event.getRawX() - downX);
//                        int distanceY = Math.abs((int) event.getRawY() - downY);
//
//                        if (distanceY > distanceX && distanceY > dragthreshold) {
//                            mPager.getParent().requestDisallowInterceptTouchEvent(false);
//                            mScrollView.getParent().requestDisallowInterceptTouchEvent(true);
//                        } else if (distanceX > distanceY && distanceX > dragthreshold) {
//                            mPager.getParent().requestDisallowInterceptTouchEvent(true);
//                            mScrollView.getParent().requestDisallowInterceptTouchEvent(false);
//                        }
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        mScrollView.getParent().requestDisallowInterceptTouchEvent(false);
//                        mPager.getParent().requestDisallowInterceptTouchEvent(false);
//                        break;
//                }
//                return false;
//            }
//        });

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == TOTAL_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 6000, 6000);
    }



    @Override
    public void get_data(String data) {
        if(ONE_TIME == 1) {
            try {
                final JSONArray object = (JSONArray) new JSONTokener(data).nextValue();
                System.out.println("Object" + object);
                for (int i = 0; i < object.length(); i++) {
                    JSONObject obj = new JSONObject(object.get(i).toString());
                    System.out.println("Images" + obj);
                    XMEN[i] = (obj.getString("news_image"));
                    ID[i] = obj.getInt("id");
                    XMENArray.add(XMEN[i]);
                    TOTAL_PAGES = object.length();
                }
                init();
            } catch (JSONException e) {
                e.printStackTrace();
                Download_data download_data = new Download_data((Download_data.download_complete) this);
                download_data.download_data_from_link(RECENT_NEWS_URL);
                ONE_TIME--;
            }
        }
        if(ONE_TIME == 0){
            try {
                final JSONObject object = (JSONObject) new JSONTokener(data).nextValue();
                System.out.println("Object"+object);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            full_news_title.setText(object.getString("news_title"));
                            full_news_description.setText(object.getString("news_description"));
                            full_text_news_description.setText(object.getString("news_description"));
                            System.out.println(SERVER_URL+object.getString("news_image"));
                            loadImageFromUrl(SERVER_URL+object.getString("news_image"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }
            Download_data download_data = new Download_data((Download_data.download_complete) this);
            download_data.download_data_from_link(RECENT_NEWS_URL);
        }
        ONE_TIME += 1;
    }

    private void loadImageFromUrl(String employee_photo) {
        Picasso.with(this).load(employee_photo).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
                .into(news_photo, new com.squareup.picasso.Callback(){

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
            this.images=images;
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
            System.out.println("List "+images.get(position));
            loadImageFromUrl(myImage,(SERVER_URL+images.get(position)));
            myImageLayout.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
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
    private void loadImageFromUrl(ImageView myImage,String employee_photo) {
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

    public void generateList(int position){
        Intent intent = new Intent(FullNews.this,FullNews.class);
        intent.putExtra("id",ID[position]);
        finish();
        startActivity(intent);
        ONE_TIME = 0;
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
                intent = new Intent(FullNews.this,GridList.class);
                startActivity(intent);
                return true;

            case R.id.profile:
                intent = new Intent(FullNews.this,ProfileActivity.class);
                startActivity(intent);
                return true;

            case R.id.events:
                intent = new Intent(FullNews.this,EventMain.class);
                startActivity(intent);
                return true;

            case R.id.news:
                intent = new Intent(FullNews.this,NewsMain.class);
                startActivity(intent);
                return true;

            case R.id.shoutout:
                intent = new Intent(FullNews.this,Shoutout.class);
                startActivity(intent);
                return true;

            case R.id.gallery:
                Toast.makeText(FullNews.this, "Coming soon", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.info:
                Toast.makeText(FullNews.this, "Coming soon", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.settings:
                intent = new Intent(FullNews.this,Changepassword_Activity.class);
                startActivity(intent);
                return true;

            case R.id.logout:
                intent = new Intent(FullNews.this,MainActivity.class);
                startActivity(intent);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

}


