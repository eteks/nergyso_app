package com.example.user.energysoft;

        import android.app.AlertDialog;
        import android.app.Fragment;
        import android.app.FragmentManager;
        import android.app.FragmentTransaction;
        import android.content.Context;
        import android.content.Intent;
        import android.os.AsyncTask;
        import android.os.Build;
        import android.os.Handler;
        import android.support.v4.view.PagerAdapter;
        import android.support.v4.view.ViewPager;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
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

        import java.util.Timer;
        import java.util.TimerTask;

public class BannerActivity extends AppCompatActivity implements Download_data.download_complete{
    Toolbar toolbar;
    ImageButton firstFragment, secondFragment, thirdFragment, fourthFragment, fifthFragment;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.
    ViewPager viewPager;
    String SERVER_URL ;
    String BANNER_URL ;
    String[] images = new String[20];
    MyCustomPagerAdapter myCustomPagerAdapter;
    public static int NUM_PAGES = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SERVER_URL = getString(R.string.service_url);
        BANNER_URL = SERVER_URL+"api/banner/";

        viewPager = (ViewPager)findViewById(R.id.viewPagerdash);

        myCustomPagerAdapter = new MyCustomPagerAdapter(BannerActivity.this, images);
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
                loadFragment(new FirstFragment());
            }
        });
        // perform setOnClickListener event on Second Button
        secondFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // load Second Fragment
                loadFragment(new SecondFragment());
            }
        });
        // perform setOnClickListener event on Third Button
        thirdFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // load First Fragment
                loadFragment(new ThirdFragment());
            }
        });
        // perform setOnClickListener event on Fourth Button
        fourthFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // load First Fragment
                loadFragment(new FourthFragment());
            }
        });
        // perform setOnClickListener event on Fifth Button
        fifthFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // load First Fragment
                loadFragment(new FifthFragment());
            }
        });
        Download_data download_data = new Download_data((Download_data.download_complete) this);
        download_data.download_data_from_link(BANNER_URL);

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
        try {
            JSONArray data_array = new JSONArray(data);
            System.out.println("Object"+data_array);
            if(data_array.length() == 0){
                createAndShowDialog("Server Error","No connection");
            }
            NUM_PAGES = data_array.length();
            for (int i = 0 ; i < data_array.length() ; i++)
            {
                JSONObject obj=new JSONObject(data_array.get(i).toString());
                images[i] = SERVER_URL+obj.getString("banner_image");
//                System.out.println("Object"+obj);
//                final Event add=new Event();
//                add.events_title = obj.getString("events_title");
//                add.setTitle(obj.getString("events_title"));
//                add.setId(obj.getInt("id"));
//                add.events_description = obj.getString("events_description");
//                add.setTitle(obj.getString("events_title"));
//                add.setDescription(obj.getString("events_description"));
//                add.events_image = obj.getString("events_image");
//                add.setImage(obj.getString("events_image"));
//                System.out.println("Events Id"+obj.getInt("id"));
//                news.add(add);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        progressBar.setVisibility(View.GONE);
//                        adapter.add(add);
                    }
                });

            }
//            if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
//            else isLastPage = true;

//            NewsAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            createAndShowDialog(e,"No connection");
//            loadFirstPage();
            e.printStackTrace();
        }

    }

    // Initiating Menu XML file (menu.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
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
        Intent intent;
        switch (item.getItemId())
        {
            case R.id.action_home:
                intent = new Intent(BannerActivity.this,BannerActivity.class);
                startActivity(intent);
                return true;

            case R.id.profile:
                intent = new Intent(BannerActivity.this,ProfileActivity.class);
                startActivity(intent);
                return true;

            case R.id.feedback:
                intent = new Intent(BannerActivity.this,Feedback.class);
                startActivity(intent);
                return true;

            case R.id.action_search:
                intent = new Intent(BannerActivity.this,SearchActivity.class);
                startActivity(intent);
                return true;

            case R.id.events:
                intent = new Intent(BannerActivity.this,EventMain.class);
                startActivity(intent);
                return true;

            case R.id.news:
                intent = new Intent(BannerActivity.this,NewsMain.class);
                startActivity(intent);
                return true;

            case R.id.shoutout:
                intent = new Intent(BannerActivity.this,Shoutout.class);
                startActivity(intent);
                return true;

            case R.id.gallery:
                intent = new Intent(BannerActivity.this,EventGallery.class);
                startActivity(intent);
                return true;

            case R.id.info:
                Toast.makeText(BannerActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.settings:
                intent = new Intent(BannerActivity.this,Changepassword_Activity.class);
                startActivity(intent);
                return true;

            case R.id.logout:
                intent = new Intent(BannerActivity.this,MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.facebook:
                intent = new Intent(BannerActivity.this,FacebookActivity.class);
                startActivity(intent);
                return true;

            case R.id.twitter:
                intent = new Intent(BannerActivity.this,TwitterActivity.class);
                startActivity(intent);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class MyCustomPagerAdapter extends PagerAdapter {
        Context context;
        String images[];
        LayoutInflater layoutInflater;


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

