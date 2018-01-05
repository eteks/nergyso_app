package com.example.user.energysoft;

        import android.app.Fragment;
        import android.app.FragmentManager;
        import android.app.FragmentTransaction;
        import android.content.Intent;
        import android.os.Handler;
        import android.support.v4.view.ViewPager;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.Toolbar;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.ImageButton;
        import android.widget.Toast;

        import java.util.Timer;
        import java.util.TimerTask;

public class BannerActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageButton firstFragment, secondFragment, thirdFragment, fourthFragment, fifthFragment;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.
    ViewPager viewPager;
    int images[] = {R.drawable.image_11, R.drawable.image_22, R.drawable.image_33, R.drawable.image_44};
    MyCustomPagerAdapter myCustomPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);

        viewPager = (ViewPager)findViewById(R.id.viewPagerdash);

        myCustomPagerAdapter = new MyCustomPagerAdapter(BannerActivity.this, images);
        viewPager.setAdapter(myCustomPagerAdapter);

        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public static final int NUM_PAGES = 5;

            public void run() {
                if (currentPage == NUM_PAGES-1) {
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
                intent = new Intent(BannerActivity.this,GridList.class);
                startActivity(intent);
                return true;

            case R.id.profile:
                intent = new Intent(BannerActivity.this,ProfileActivity.class);
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
                Toast.makeText(BannerActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
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


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}