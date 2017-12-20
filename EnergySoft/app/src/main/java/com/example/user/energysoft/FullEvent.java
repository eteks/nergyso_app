package com.example.user.energysoft;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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


public class FullEvent extends AppCompatActivity implements Download_data.download_complete {
    String SERVER_URL;
    String FULL_EVENTS_URL;
    String RECENT_EVENTS_URL;
    int TOTAL_PAGES = 0;
    Toolbar toolbar;
    int ONE_TIME = 0;
    private static ViewPager mPager;
    private static ScrollView mScrollView;
    TextView full_events_title, full_events_description, full_text_events_description;
    ImageView events_photo;
    private static int currentPage = 0;
    private static final String[] XMEN = new String[200];
    private static final int[] ID = new int[200];
    private ArrayList<String> XMENArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_event);
        SERVER_URL = getString(R.string.service_url);
        FULL_EVENTS_URL = SERVER_URL + "api/events/";
        RECENT_EVENTS_URL = SERVER_URL + "api/events/recent_events/";
//        init();
//        ImageView home = (ImageView) findViewById(R.id.action_home);
//        home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(FullNews.this, GridList.class);
//                startActivity(intent);
//            }
//        });
        full_events_title = (TextView) findViewById(R.id.full_events_title);
        full_events_description = (TextView) findViewById(R.id.full_events_description);
        full_text_events_description = (TextView) findViewById(R.id.full_text_events_description);
        events_photo = (ImageView) findViewById(R.id.events_photo);
        int id = getIntent().getIntExtra("id", 0);
        FULL_EVENTS_URL = FULL_EVENTS_URL + id + "/";
        Download_data download_data = new Download_data((Download_data.download_complete) this);
        download_data.download_data_from_link(FULL_EVENTS_URL);
    }

    private void init() {
//        for(int i=0;i<XMEN.length;i++){
//            XMENArray.add(XMEN[i]);
//        }
//        NestedScrollView scrollView = (NestedScrollView) findViewById (R.id.nest_scrollview);
//        scrollView.setFillViewport (true);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new MyAdapter(FullEvent.this, XMENArray));
//        mScrollView = (ScrollView) findViewById(R.id.news_scroll);
//        mScrollView.setFillViewport(true);
//        mPager.setOnTouchListener(new View.OnTouchListener() {
//            int dragthreshold = 30;
//            int downX;
//            int downY;

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
        }, 2500, 2500);
    }


    @Override
    public void get_data(String data) {
        if (ONE_TIME == 1) {
            try {
                final JSONArray object = (JSONArray) new JSONTokener(data).nextValue();
                System.out.println("Object" + object);
                for (int i = 0; i < object.length(); i++) {
                    JSONObject obj = new JSONObject(object.get(i).toString());
                    System.out.println("Images" + obj);
                    XMEN[i] = (obj.getString("events_image"));
                    ID[i] = obj.getInt("id");
                    XMENArray.add(XMEN[i]);
                    TOTAL_PAGES = i;
                }
                init();
            } catch (JSONException e) {
                e.printStackTrace();
                Download_data download_data = new Download_data((Download_data.download_complete) this);
                download_data.download_data_from_link(RECENT_EVENTS_URL);
                ONE_TIME--;
            }
        }
        if (ONE_TIME == 0) {
            try {
                final JSONObject object = (JSONObject) new JSONTokener(data).nextValue();
                System.out.println("Object" + object);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            full_events_title.setText(object.getString("events_title"));
                            full_events_description.setText(object.getString("events_description"));
                            full_text_events_description.setText(object.getString("events_description"));
                            System.out.println(SERVER_URL + object.getString("events_image"));
                            loadImageUrl(SERVER_URL + object.getString("events_image"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Download_data download_data = new Download_data((Download_data.download_complete) this);
            download_data.download_data_from_link(RECENT_EVENTS_URL);
        }
        ONE_TIME += 1;
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

    private void loadImageFromUrl(ImageView myImage, String employee_photo) {
        Picasso.with(this).load(employee_photo).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
                .into(myImage, new com.squareup.picasso.Callback() {

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
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
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_home:
                intent = new Intent(FullEvent.this, GridList.class);
                startActivity(intent);
                return true;

            case R.id.profile:
                intent = new Intent(FullEvent.this, ProfileActivity.class);
                startActivity(intent);
                return true;

            case R.id.events:
                intent = new Intent(FullEvent.this, EventMain.class);
                startActivity(intent);
                return true;

            case R.id.news:
                intent = new Intent(FullEvent.this, NewsMain.class);
                startActivity(intent);
                return true;

            case R.id.shoutout:
                intent = new Intent(FullEvent.this, Shoutout.class);
                startActivity(intent);
                return true;

            case R.id.gallery:
                Toast.makeText(FullEvent.this, "Coming soon", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.info:
                Toast.makeText(FullEvent.this, "Coming soon", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.settings:
                intent = new Intent(FullEvent.this, Changepassword_Activity.class);
                startActivity(intent);
                return true;

            case R.id.logout:
                intent = new Intent(FullEvent.this, MainActivity.class);
                startActivity(intent);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class CustomScrollView extends ScrollView {

        private GestureDetector mGestureDetector;

        public CustomScrollView(Context context, AttributeSet attrs) {
            super(context, attrs);
            mGestureDetector = new GestureDetector(context, new YScrollDetector());
            setFadingEdgeLength(0);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return super.onInterceptTouchEvent(ev)
                    && mGestureDetector.onTouchEvent(ev);
        }

        // Return false if we're scrolling in the x direction
        class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                    float distanceX, float distanceY) {
                return (Math.abs(distanceY) > Math.abs(distanceX));
            }
        }

    }
}