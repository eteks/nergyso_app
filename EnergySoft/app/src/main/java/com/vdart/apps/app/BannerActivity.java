package com.vdart.apps.app;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static com.vdart.apps.app.MainActivity.MyPREFERENCES;

public class BannerActivity extends AppCompatActivity implements Download_data.download_complete{
    Toolbar toolbar;
    ImageButton firstFragment, secondFragment, thirdFragment, fourthFragment, fifthFragment;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 100;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.
    ViewPager viewPager;
    String SERVER_URL ;
    String BANNER_URL ;
    MenuItem menuItem;
    Menu menuInflate;
    int id = 0;
    int NOTIFICATION_COUNT = 0;
    String NOTIFICATION_URL = "api/notification/notification_list_by_employee";
//    String[] images = new String[20];
    MyCustomPagerAdapter myCustomPagerAdapter;
    public static int NUM_PAGES = 0;

    ArrayList<String> images = new ArrayList<String>();
//    images[1] = "test";
//    images.add("text"); //this adds an element to the list.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
//        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SERVER_URL = getString(R.string.service_url);
        BANNER_URL = SERVER_URL+"api/banner/";

        NOTIFICATION_URL = SERVER_URL + NOTIFICATION_URL;

        viewPager = (ViewPager)findViewById(R.id.viewPagerdash);

        myCustomPagerAdapter = new MyCustomPagerAdapter(BannerActivity.this, images);
        viewPager.setAdapter(myCustomPagerAdapter);

        id = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).getInt("id",0);

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

        getNotificationCount();

        timer = new Timer(); // This will create a new Thread
        timer .schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

        loadFragment(new DefaultFragment());

        String  check = "";
        //Loading Default Fragment
        Intent intent = getIntent();
        // Get the extras (if there are any)
        Bundle extras = intent.getExtras();
        if (extras != null) {
            check = getIntent().getStringExtra("check");
        }else{
            check = "default";
        }
        switch(check){
            case "birthday":{
                changeImage("first");
                loadFragment(new FirstFragment());
                break;
            }
            case "anniversary":{
                changeImage("second");
                loadFragment(new SecondFragment());
                break;
            }
            case "shoutout":{
                changeImage("third");
                loadFragment(new ThirdFragment());
                break;
            }
            case "events":{
                changeImage("fourth");
                loadFragment(new FourthFragment());
                break;
            }
            case "news":{
                changeImage("fifth");
                loadFragment(new FifthFragment());
                break;
            }
            default:{
//                loadFragment(new FirstFragment());
            }
        }


        // get the reference of Button's
        firstFragment = (ImageButton) findViewById(R.id.firstFragment);
        secondFragment = (ImageButton) findViewById(R.id.secondFragment);
        thirdFragment = (ImageButton) findViewById(R.id.thirdFragment);
        fourthFragment = (ImageButton) findViewById(R.id.fourthFragment);
        fifthFragment = (ImageButton) findViewById(R.id.fifthFragment);
        // perform setOnClickListener event on First Button
        firstFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // load First Fragment
                changeImage("first");
                loadFragment(new FirstFragment());
            }
        });
        // perform setOnClickListener event on Second Button
        secondFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // load Second Fragment
                changeImage("second");
                loadFragment(new SecondFragment());
            }
        });
        // perform setOnClickListener event on Third Button
        thirdFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // load First Fragment
                changeImage("third");
                loadFragment(new ThirdFragment());
            }
        });
        // perform setOnClickListener event on Fourth Button
        fourthFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // load First Fragment
                changeImage("fourth");
                loadFragment(new FourthFragment());
            }
        });
        // perform setOnClickListener event on Fifth Button
        fifthFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // load First Fragment
                changeImage("fifth");
                loadFragment(new FifthFragment());
            }
        });
        Download_data download_data = new Download_data((Download_data.download_complete) this);
        download_data.download_data_from_link(BANNER_URL);

        //        Click Logo to home screen

        ImageView imageButton = (ImageView) toolbar.findViewById(R.id.vdart_logo);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BannerActivity.this, BannerActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        setCount(this, String.valueOf(NOTIFICATION_COUNT));
        return  true;
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

    public void changeImage(String image){
        switch (image){
            case "first":{
                firstFragment.setImageResource(R.drawable.bir);
                secondFragment.setImageResource(R.drawable.anni_black);
                thirdFragment.setImageResource(R.drawable.shout_black);
                fourthFragment.setImageResource(R.drawable.even_black);
                fifthFragment.setImageResource(R.drawable.newss_black);
                break;
            }
            case "second":{
                firstFragment.setImageResource(R.drawable.bir_black);
                secondFragment.setImageResource(R.drawable.ann);
                thirdFragment.setImageResource(R.drawable.shout_black);
                fourthFragment.setImageResource(R.drawable.even_black);
                fifthFragment.setImageResource(R.drawable.newss_black);
                break;
            }
            case "third":{
                firstFragment.setImageResource(R.drawable.bir_black);
                secondFragment.setImageResource(R.drawable.anni_black);
                thirdFragment.setImageResource(R.drawable.shout);
                fourthFragment.setImageResource(R.drawable.even_black);
                fifthFragment.setImageResource(R.drawable.newss_black);
                break;
            }
            case "fourth":{
                firstFragment.setImageResource(R.drawable.bir_black);
                secondFragment.setImageResource(R.drawable.anni_black);
                thirdFragment.setImageResource(R.drawable.shout_black);
                fourthFragment.setImageResource(R.drawable.even);
                fifthFragment.setImageResource(R.drawable.newss_black);
                break;
            }
            case "fifth":{
                firstFragment.setImageResource(R.drawable.bir_black);
                secondFragment.setImageResource(R.drawable.anni_black);
                thirdFragment.setImageResource(R.drawable.shout_black);
                fourthFragment.setImageResource(R.drawable.even_black);
                fifthFragment.setImageResource(R.drawable.newss);
                break;
            }
        }

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

    public void get_data(String data)
    {
        try
        {
            JSONArray data_array = new JSONArray(data);
            System.out.println("Object"+data_array);
            if(data_array.length() == 0){
                createAndShowDialog("Server Error","No connection");
            }
            NUM_PAGES = data_array.length();
            for (int i = 0 ; i < data_array.length() ; i++)
            {
                JSONObject obj=new JSONObject(data_array.get(i).toString());
//                images[i] = SERVER_URL+obj.getString("banner_image");
                images.add(SERVER_URL+obj.getString("banner_image"));
                myCustomPagerAdapter.notifyDataSetChanged();
            }
            System.out.println("Images_list"+images);
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        }
        catch (JSONException e)
        {
            createAndShowDialog(e,"No connection");
            e.printStackTrace();
        }
    }


    // Initiating Menu XML file (menu.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflate = menu;
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
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

    /**
     * Event Handling for Individual menu item selected
     * Identify single menu item by it's id
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        menuItem = item;
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

//            case R.id.shoutout:
//                intent = new Intent(this,ListingMore.class);
//                startActivity(intent);
//                return true;

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

            case R.id.action_refresh:
//                intent = new Intent(this,BannerActivity.class);
                startActivity(getIntent());
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
//            System.out.println("NUM_PAGES"+NUM_PAGES);
////            System.out.println("IMAGE_SIZE"+images.size());
////
////            System.out.println("IMAGES"+images);
////            return images.size();
////            int count =  images.size();
////            if(count == 0) {
////                System.out.println("IF");
////                return 1;
////            }
////            else {
////                return count;
////            }
////            return NUM_PAGES;
//            if(images.size() == 0)
//                return 0;
//            else
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
            System.out.println("in");
            loadImageFromUrl(imageView, images.get(position));
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

    private void loadImageFromUrl(ImageView myImage,String employee_photo) {
        Picasso.with(this).load(employee_photo).placeholder(R.mipmap.ic_loader).error(R.mipmap.ic_loader)
                .into(myImage, new com.squareup.picasso.Callback(){

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    public void getNotificationCount(){
        String data = null;
        try {

            data = URLEncoder.encode("notification_employee", "UTF-8")
                    + "=" + id;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        final BufferedReader[] reader = {null};

        // Send data
        final String finalData = data;
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    // Defined URL  where to send data
                    URL url = new URL(NOTIFICATION_URL);

                    // Send POST data request
                    System.out.println("URL:" + NOTIFICATION_URL);
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(finalData);
                    wr.flush();
                    System.out.println(finalData);
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
                    JSONArray data_array = new JSONArray(sb.toString());
                    System.out.println("Data array Length" + data_array.length());
                    for (int i = 0; i < data_array.length(); i++) {
                        final JSONObject obj = new JSONObject(data_array.get(i).toString());
                        System.out.println("Object" + obj);
                        if (!obj.getBoolean("notification_read_status")) {
                            NOTIFICATION_COUNT++;
                        }
                    }
                    System.out.println("Notification"+ NOTIFICATION_COUNT);
                    SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("nc", String.valueOf(NOTIFICATION_COUNT));
                    editor.commit();
                    setCount(BannerActivity.this, String.valueOf(NOTIFICATION_COUNT));
                }catch (Exception ex) {
                    System.out.println(ex);
                } finally {
                    try {
                        reader[0].close();
                    } catch (Exception ex) {
                    }
                }
                return null;
            }

        };
        runAsyncTask(task);

//        Download_data download_data = new Download_data((Download_data.download_complete) this);
//        download_data.download_data_from_link(NOTIFICATION_URL);
    }
}

