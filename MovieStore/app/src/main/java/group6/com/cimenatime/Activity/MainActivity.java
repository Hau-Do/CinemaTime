package group6.com.cimenatime.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import java.util.ArrayList;
import java.util.List;

import group6.com.cimenatime.Adapter.PagerAdapter;
import group6.com.cimenatime.Adapter.ReminderAdapter;
import group6.com.cimenatime.Fragment.ReminderFragment;
import group6.com.cimenatime.Model.Reminder;
import group6.com.cimenatime.Other.CircleImageView;
import group6.com.cimenatime.R;

/**
 * Created by HauDT on 03/12/2017.
 */
public class MainActivity extends AppCompatActivity /*implements
        ReminderFragment.OnReminderFragmentListener*/{

    /**
     * URL of API
     */
    public final static String urlPopular = "http://api.themoviedb.org/3/movie/popular?api_key=e7631ffcb8e766993e5ec0c1f4245f93&page=1";
    public final static String urlTopRated = "http://api.themoviedb.org/3/movie/top_rated?api_key=e7631ffcb8e766993e5ec0c1f4245f93&page=1";
    public final static String urlUpComing = "http://api.themoviedb.org/3/movie/upcoming?api_key=e7631ffcb8e766993e5ec0c1f4245f93&page=1";
    public final static String urlNowPlaying = "http://api.themoviedb.org/3/movie/now_playing?api_key=e7631ffcb8e766993e5ec0c1f4245f93&page=1";


    NavigationView nvView;
    public DrawerLayout drawer;
    public Toolbar toolbar;
    ImageView drawer_imgAvatar;
    Button drawer_btnEdt, drawer_btnShow;
    TextView drawer_txvName, drawer_txvDateTime, drawer_txvEmail, drawer_txvGender;
    AHBottomNavigation bottomNavigation;
    ViewPager viewPager;
    RecyclerView recyclerView;
    ActionBarDrawerToggle toggle;
    private RecyclerView mRecyclerViewReminder;
    private LinearLayoutManager mLinearLayoutManager;
    private List<Reminder> mReminders;
    private ReminderAdapter reminderAdapter;
    String imageString;
    /**
     * isBack == false: display Home button for Detail Movie
     * isBack == true: display Back button for Detail Movie
     */
    public static boolean isBack = false;

    /**
     * isSettingBack == false: display Home button for Reminder
     * isSettingBack == true: display Back button for Reminder
     */
    public static boolean isSettingBack = false;

    //private ReminderFragment reminderFragment;

    /**
     * isBundle =
     *      1: get bundle from MovieListFragment
     *      2: get bundle from ReminderFragment
     */
    public static int isBundle = 0;


    /**
     * Remove ReminderFragment
     */
    public interface Communicator{
        public void setFlag(boolean isOpen);
    }
    Communicator mCommunicator;
    public void openReminder(Communicator communicator){
        this.mCommunicator = communicator;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Init();
        ViewPager();
        castingNavDrawerandEvent();
        RecyclerViewManage();

//        reminderFragment = new ReminderFragment();
//
//        reminderFragment.setOnReminderFragmentListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void RecyclerViewManage() {

        mLinearLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setHasFixedSize(true);

        addData();

        reminderAdapter = new ReminderAdapter(getBaseContext(), mReminders);
        recyclerView.setAdapter(reminderAdapter);
    }

    private void addData() {

        mReminders = new ArrayList<>();

//        mReminders.add( new Reminder(String.valueOf(R.drawable.emma), "Emma Movie", 2006+"", 7.5, "20/10/2006 21:30") );
//        mReminders.add( new Reminder(String.valueOf(R.drawable.emma), "Emma Movie", 2006+"", 7.5, "20/10/2006 21:30") );

    }


    /**
     * HauDT
     * cast view and make bottombar, navigationdrawer, viewpager and set Event
     */
    private void Init() {
//castting bottombar
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("MovieList", R.drawable.ic_movie);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Favorite", R.drawable.ic_favorite);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Setting", R.drawable.ic_settings);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("About", R.drawable.ic_wallpaper);
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.setCurrentItem(0);
        bottomNavigation.setBehaviorTranslationEnabled(true);
        bottomNavigation.setInactiveColor(Color.parseColor("#717153"));
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#e9ffffff"));
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                viewPager.setCurrentItem(position);


                /**
                 *  HauDT: handle event and update state of Home/Back icon on the toolbar
                 */
                switch (position) {
                    case 0:
                        getSupportActionBar().setTitle("Popular Movies");
                        if (isBack) {
                            setToolbarIcon(true);
                        } else {
                            setToolbarIcon(false);
                        }
                        break;
                    case 1:
                        getSupportActionBar().setTitle("Favorite");
                        setToolbarIcon(false);
                        break;
                    case 2:
                        getSupportActionBar().setTitle("Settings");
                        if (isSettingBack) {
                            setToolbarIcon(true);
                        } else {
                            setToolbarIcon(false);
                        }
                        break;
                    case 3:
                        getSupportActionBar().setTitle("About");
                        setToolbarIcon(false);
                        break;
                }
                return true;
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvView = (NavigationView) findViewById(R.id.nvView);
        setSupportActionBar(toolbar);
        //set navigationdrawer with toogle on actionbar
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        drawer.addDrawerListener(toggle);


        //when select bottombaritem call function.

    }
    /**
     * HauDT
     * casting NavigationDrawer View and call event, setbittmap
     */
    private void ViewPager() {

        viewPager = (ViewPager) findViewById(R.id.pager);
        FragmentPagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), bottomNavigation.getItemsCount());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);

        //event when people changepage,bottombar set position for bottombar item
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigation.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //when select bottombaritem call function.

    }

    /**
     * HauDT: call the setToolbarIcon() method handling event and update state of Home/Back icon on the toolbar
     *
     * @param ischeck
     */
    public void setToolbarIcon(boolean ischeck) {

        if (ischeck == false) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toggle.setDrawerIndicatorEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    } else {
                        drawer.openDrawer(GravityCompat.START);
                    }
                }
            });
        } else if (ischeck == true) {

            toggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isBack) {
                        onBackPressed();
                        isBack = false;
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        toggle.setDrawerIndicatorEnabled(true);
                    } else if (isSettingBack) {
                        mCommunicator.setFlag(false);
                        isSettingBack = false;
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        toggle.setDrawerIndicatorEnabled(true);
                    } else {
                        if (drawer.isDrawerOpen(GravityCompat.START)) {
                            drawer.closeDrawer(GravityCompat.START);
                        } else {
                            drawer.openDrawer(GravityCompat.START);
                        }
                    }
                }
            });
        }
    }

    /**
     * HauDT
     * casting NavigationDrawer View and call event, setbittmap
     */

    private void castingNavDrawerandEvent() {
        View nav = nvView.getHeaderView(0);
        drawer_imgAvatar = (ImageView) nav.findViewById(R.id.Fragment_nav_item_ImageView);
        drawer_btnEdt = (Button) nav.findViewById(R.id.Fragment_nav_item_BtnEdit);
        drawer_txvName = (TextView) nav.findViewById(R.id.Fragment_nav_item_textName);
        drawer_txvDateTime = (TextView) nav.findViewById(R.id.Fragment_nav_item_DateTime);
        drawer_txvEmail = (TextView) nav.findViewById(R.id.Fragment_nav_item_Email);
        drawer_txvGender = (TextView) nav.findViewById(R.id.Fragment_nav_item_Gender);
        drawer_btnShow = (Button) nav.findViewById(R.id.Fragment_nav_item_BtnShow);
        recyclerView = (RecyclerView) nav.findViewById(R.id.view);

        drawer_btnEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, EditProfileActivity.class);
                startActivity(i);

            }
        });
        drawer_btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(nvView);
                viewPager.setCurrentItem(2);
                isSettingBack = true;
                setToolbarIcon(true);
                mCommunicator.setFlag(true);



            }
        });
        //set Circleimage
        CircleImageView circleImageView = new CircleImageView();
        Bitmap bitmap = circleImageView.decodeSampledBitmapFromResource(getResources(), R.drawable.no_img, 100, 100);
        drawer_imgAvatar.setImageBitmap(circleImageView.getCircleBitmap(bitmap));
    }


    /**
     * HauDT
     * send data from EditProfileActivity and using SharedReferences to get data
     *
     * set value for view
     */
    private void getData() {
        SharedPreferences pre = getSharedPreferences("my_data", MODE_PRIVATE);
        String Name = pre.getString("Name", "Hau Do");
        String Email = pre.getString("Email", "haudotien@gmail.com");
        String DateTime = pre.getString("DateTime", "05/11/2016");
         imageString = pre.getString("Bitmap", null);
        int Spinner = pre.getInt("Spinner", -1);
        //set text
        drawer_txvName.setText(Name);
        drawer_txvDateTime.setText(DateTime);
        drawer_txvEmail.setText(Email);
        if (Spinner == 0) {

            drawer_txvGender.setText("Men");
        }
        if (Spinner == 1) {
            drawer_txvGender.setText("Women");
        }
        if (imageString != null) {
            requestPermission();

        }


    }

    /**
     * HauDT
     * Catching event when bottomBar in tab
     * case 0: MovieList
     * case 1:Favorite
     * case 2: Setting
     * case 3 :About
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        FragmentManager fm = getSupportFragmentManager();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_item, menu);
        switch (bottomNavigation.getCurrentItem()) {
            case 0:
                menu.findItem(R.id.menu_main_item_MovieList).setVisible(false);
                menu.findItem(R.id.menu_main_item_Favorite).setVisible(true);
                menu.findItem(R.id.menu_main_item_About).setVisible(true);
                menu.findItem(R.id.menu_main_item_Setting).setVisible(true);
                menu.findItem(R.id.menu_main_item_ChangeLayout).setVisible(true);
                break;
            case 1:
                menu.findItem(R.id.menu_main_item_MovieList).setVisible(true);
                menu.findItem(R.id.menu_main_item_Favorite).setVisible(false);
                menu.findItem(R.id.menu_main_item_About).setVisible(true);
                menu.findItem(R.id.menu_main_item_Setting).setVisible(true);
                menu.findItem(R.id.menu_main_item_ChangeLayout).setVisible(false);
                break;
            case 2:
                menu.findItem(R.id.menu_main_item_ChangeLayout).setVisible(false);
                menu.findItem(R.id.menu_main_item_MovieList).setVisible(true);
                menu.findItem(R.id.menu_main_item_Favorite).setVisible(true);
                menu.findItem(R.id.menu_main_item_About).setVisible(true);
                menu.findItem(R.id.menu_main_item_Setting).setVisible(false);
                break;
            case 3:
                menu.findItem(R.id.menu_main_item_ChangeLayout).setVisible(false);
                menu.findItem(R.id.menu_main_item_MovieList).setVisible(true);
                menu.findItem(R.id.menu_main_item_Favorite).setVisible(true);
                menu.findItem(R.id.menu_main_item_About).setVisible(false);
                menu.findItem(R.id.menu_main_item_Setting).setVisible(true);
                break;

        }
        return super.onCreateOptionsMenu(menu);

    }

    /**
     * HauDT
     * when choose menuitem,event will set tab of bottomnavigation
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_main_item_MovieList:
                bottomNavigation.setCurrentItem(0);
                break;
            case R.id.menu_main_item_Favorite:
                bottomNavigation.setCurrentItem(1);
                break;
            case R.id.menu_main_item_Setting:
                bottomNavigation.setCurrentItem(2);
                break;
            case R.id.menu_main_item_About:
                bottomNavigation.setCurrentItem(3);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openReminderFragment() {

        setToolbarIcon(true);
        isSettingBack = true;
        ReminderFragment reminderFragment = new ReminderFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.linearSetting, reminderFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }

    }


    /**
     * A callback to call click event on  each row on Recyclerview of ReminderFragment
     * @param
     */
//    @Override
//    public void onOpenDetail(int idMovie) {
//        Toast.makeText(getApplicationContext(), "MainActivity", Toast.LENGTH_SHORT).show();
//
//
//        Bundle bundle = new Bundle();
//        bundle.putInt("Id_Movie", idMovie);
//
//        DetailMoviesFragment detail = new DetailMoviesFragment();
//        detail.setArguments(bundle);
//
////
////        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
////        trans.replace(R.id.root_container, detail);
////        trans.addToBackStack(null);
////        trans.commit();
//
//
//    }


    private void requestPermission() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){


            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                Toast.makeText(this, "request permission", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        EditProfileActivity.REQUEST_READ_PERMISSION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
            else{
                Bitmap bitmap = BitmapFactory.decodeFile(imageString);
                drawer_imgAvatar.setImageBitmap(new CircleImageView().getCircleBitmap(bitmap));
            }

        }
        else{
            Bitmap bitmap = BitmapFactory.decodeFile(imageString);
            drawer_imgAvatar.setImageBitmap(new CircleImageView().getCircleBitmap(bitmap));
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == EditProfileActivity.REQUEST_READ_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            readdata();
            Bitmap bitmap = BitmapFactory.decodeFile(imageString);
            drawer_imgAvatar.setImageBitmap(new CircleImageView().getCircleBitmap(bitmap));
        }
        else{
            Toast.makeText(this, "You're Deny", Toast.LENGTH_SHORT).show();
        }
    }


}


