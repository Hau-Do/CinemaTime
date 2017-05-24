package group6.com.cimenatime.Fragment;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import group6.com.cimenatime.Activity.MainActivity;
import group6.com.cimenatime.Adapter.CastCrewAdapter;
import group6.com.cimenatime.Model.CastCrew;
import group6.com.cimenatime.Model.Movies;
import group6.com.cimenatime.Model.Reminder;
import group6.com.cimenatime.MyVideoPlayer.PlayerActivity;
import group6.com.cimenatime.Other.DatabaseHandler;
import group6.com.cimenatime.Other.DownloadAPI;
import group6.com.cimenatime.Other.Events;
import group6.com.cimenatime.R;
import group6.com.cimenatime.Receiver.AlarmReceiver;

/**
 * Created by HauDT on 03/11/2017.
 */
public class DetailMoviesFragment extends Fragment implements DateTimeDialogFragment.OnDateTimeSetListener {

    private LinearLayout layoutDetailMoviesFragment;
    private String title;
    private CheckBox chkbFavourite;
    private TextView txtReleaseDate;
    private TextView txtRate;
    private ImageView imvAdult;

    private ImageView imvPoster;
    private Button btnReminder,btnPlay;
    private TextView txtReminderDate;
    private TextView txtOverview;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private static List<CastCrew> mCastCrews = new ArrayList<>();
    private Object dataFromBundle;
    Movies movies = new Movies();
    private int year;
    private Reminder reminder;
    static CastCrewAdapter adapter;
    private int id; // id of the movie showing

    /**
     * A flag to check that click Reminder button or not
     */
    private boolean isReminder = false;
    private DatabaseHandler handler;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_movies_fragment, container, false);

        handler = new DatabaseHandler(getContext());

        addControls(view);
        if ( MainActivity.isBundle == 1 ){
            getDataFromBundle(); // receive from MovieListFragment
            addCastCrewForRecyclerView(view);
            MainActivity.isBundle = 0;
        }
        else if ( MainActivity.isBundle == 2 ){
            getIdMovieFromBundle(); // receive from ReminderFragment
            addCastCrewForRecyclerView(view);
            MainActivity.isBundle = 0;
        }


        addEvents();


        return view;
    }

    public DetailMoviesFragment() {

    }

    public DetailMoviesFragment(List<CastCrew> mCastCrews) {
        this.mCastCrews.clear();
        this.mCastCrews.addAll(mCastCrews);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    // get Data in Bundle transfered from MovieList fragment
    private void getDataFromBundle() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            final String title = bundle.getString("Title", null);
            final String overView = bundle.getString("OverView", null);
            final String releaseDay = bundle.getString("ReleaseDay", null);
            final String poster = bundle.getString("Poster", null);
            final Double rating = bundle.getDouble("Rating", 0.0);
            final int adult = bundle.getInt("Adult", -1);
            final int favorite = bundle.getInt("Favorite", -1);
            id = bundle.getInt("Id", 0);

            ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);
            setHasOptionsMenu(true);
            this.title = title;
            txtReleaseDate.setText(releaseDay);
            txtRate.setText(rating + "");

            movies.setId(id);
            movies.setMovie_favorite(favorite);
            movies.setMovie_poster(poster);
            movies.setMovie_adult(adult);
            movies.setMovie_rating(rating);
            movies.setMovie_title(title);
            movies.setMovie_overview(overView);
            movies.setMovie_releaseday(releaseDay);
            movies.setMovie_favorite(favorite);
            if (favorite == 1) {
                chkbFavourite.setChecked(true);
            } else {
                chkbFavourite.setChecked(false);
            }
            chkbFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chkbFavourite.isChecked()) {
                        chkbFavourite.setChecked(true);
                        movies.setMovie_favorite(1);

                        handler.addMovie(movies);

                    } else {

                        chkbFavourite.setChecked(false);
                        movies.setMovie_favorite(0);
                        handler.deleteContact(movies);

                    }
                    Events.MovieList2FragmentMassage mov2 = new Events.MovieList2FragmentMassage();
                    mov2.setMovies(movies);
                    EventBus.getDefault().postSticky(mov2);
                    Events.FavoriteFragmentMessage fav = new Events.FavoriteFragmentMessage();
                    fav.setMoviesList(handler.getAllFavContacts());

                    EventBus.getDefault().post(fav);


                }
            });

            if (adult == 1) {
                Picasso.with(getContext())
                        .load(R.drawable.icon_18)
                        .resize(120, 100)
                        .into(imvAdult);

            }

            Picasso.with(getContext())
                    .load(poster)
                    .placeholder(R.drawable.unnamed)
                    .resize(145, 200)
                    .into(imvPoster);

            txtOverview.setText(overView.toString());
        }
    }

    /**
     * receive idMovie sended from ReminderFragment
     */
    private void getIdMovieFromBundle() {

        Bundle bundle = this.getArguments();
        if( bundle != null ){
            final int idMovie = bundle.getInt("Id_Movie", -1);

            Movies movie = handler.getContact(idMovie);

            final String title = movie.getMovie_title();
            final String overView = movie.getMovie_overview();
            final String releaseDay = movie.getMovie_releaseday();
            final String poster = movie.getMovie_poster();
            final Double rating = movie.getMovie_rating();
            final int adult = movie.isMovie_adult();
            final int favorite = movie.isMovie_favorite();
            id = movie.getId();

            ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);
            setHasOptionsMenu(true);
            this.title = title;
            txtReleaseDate.setText(releaseDay);
            txtRate.setText(rating + "");

            movies.setId(id);
            movies.setMovie_favorite(favorite);
            movies.setMovie_poster(poster);
            movies.setMovie_adult(adult);
            movies.setMovie_rating(rating);
            movies.setMovie_title(title);
            movies.setMovie_overview(overView);
            movies.setMovie_releaseday(releaseDay);
            movies.setMovie_favorite(favorite);
            if (favorite == 1) {
                chkbFavourite.setChecked(true);
            } else {
                chkbFavourite.setChecked(false);
            }
            chkbFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chkbFavourite.isChecked()) {
                        chkbFavourite.setChecked(true);
                        movies.setMovie_favorite(1);

                        handler.addMovie(movies);

                    } else {

                        chkbFavourite.setChecked(false);
                        movies.setMovie_favorite(0);
                        handler.deleteContact(movies);

                    }
                    Events.MovieList2FragmentMassage mov2 = new Events.MovieList2FragmentMassage();
                    mov2.setMovies(movies);
                    EventBus.getDefault().postSticky(mov2);
                    Events.FavoriteFragmentMessage fav = new Events.FavoriteFragmentMessage();
                    fav.setMoviesList(handler.getAllFavContacts());

                    EventBus.getDefault().post(fav);


                }
            });

            if (adult == 1) {
                Picasso.with(getContext())
                        .load(R.drawable.icon_18)
                        .resize(120, 100)
                        .into(imvAdult);

            }

            Picasso.with(getContext())
                    .load(poster)
                    .placeholder(R.drawable.unnamed)
                    .resize(149, 260)
                    .into(imvPoster);

            txtOverview.setText(overView.toString());

        }

    }

    @Subscribe
    public void onEvent(Events.DetailFragmentMessage Det) {
//        this.movies = Det.getMovies();
        if (Det.getMovies().isMovie_favorite() == 1 && movies.getId() == Det.getMovies().getId()) {
            chkbFavourite.setChecked(true);
        }
        if (Det.getMovies().isMovie_favorite() == 0 && movies.getId() == Det.getMovies().getId()) {
            chkbFavourite.setChecked(false);
        }

    }


    private void addEvents() {

        btnReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReminderDate();

            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), PlayerActivity.class);
                startActivity(i);
            }
        });

    }

    /**
     * send id of the movie displaying to Reminder
     */
    private void sendIdToReminder() {
        EventBus.getDefault().post(id);

        Toast.makeText(getActivity(), "sent ID of this movie", Toast.LENGTH_SHORT).show();
    }

    // show notification for reminder date
    private void showReminderNotification() {

        Intent intent = new Intent(getActivity(), MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(getActivity(), (int) System.currentTimeMillis(), intent, 0);

        Notification noti = new Notification.Builder(getActivity())
                .setContentTitle("Go Cinema")
                .setContentText("Time to go get the ticket for " + this.title).setSmallIcon(R.drawable.ic_notifications)
                .setContentIntent(pIntent)
                .build();
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, noti);
    }

    // show datetime picker to display watching movie date
    private void showReminderDate() {

        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        SimpleDateTimePicker simpleDateTimePicker = SimpleDateTimePicker.make(
                "Set Date & Time Title",
                new Date(),
                this,
                getFragmentManager()
        );

        simpleDateTimePicker.show();

    }

    private void addCastCrewForRecyclerView(View view) {

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        addCastCrewData();

        adapter = new CastCrewAdapter(this.mCastCrews, getContext());
        mRecyclerView.setAdapter(adapter);


    }

    private void addCastCrewData() {
//        Bundle bundle = this.getArguments();
//        if (bundle != null) {
//            id = bundle.getInt("Id", 0);
//        }

        DownloadAPI downloadAPI = new DownloadAPI(getContext());
        downloadAPI.sendCastRequest(makeURL(id));
    }

    private void addControls(View view) {

        layoutDetailMoviesFragment = (LinearLayout) view.findViewById(R.id.layoutDetailMoviesFragment);

        chkbFavourite = (CheckBox) view.findViewById(R.id.chkbFavourite);
        txtReleaseDate = (TextView) view.findViewById(R.id.txtReleaseDate);
        txtRate = (TextView) view.findViewById(R.id.txtRate);
        imvAdult = (ImageView) view.findViewById(R.id.imvAdult);
        btnPlay = (Button) view.findViewById(R.id.btnPlay);



        imvPoster = (ImageView) view.findViewById(R.id.imvPoster);

        btnReminder = (Button) view.findViewById(R.id.btnReminder);
        txtReminderDate = (TextView) view.findViewById(R.id.txtReminderDate);
        txtOverview = (TextView) view.findViewById(R.id.txtOverview);
        txtOverview.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private String makeURL(int id) {
        String URI = "http://api.themoviedb.org/3/movie/" + id + "/credits?api_key=e7631ffcb8e766993e5ec0c1f4245f93";
        return URI;
    }

    /**
     * set Datetime and Timepicker event in OK
     * @param date
     */
    @Override
    public void DateTimeSet(Date date) {
        int year = date.getYear() + 1900;
        txtReminderDate.setText(year + "-" + (date.getMonth() + 1) + "-" + date.getDate() +"  " + date.getHours() + ":" + date.getMinutes());
        setYear(year);

        calculateTime(date);

        /**
         * restore movies object into DB
         */
        isReminder = true;
        if( isReminder && movies != null){
            handler.addMovie(movies);
        }
        isReminder = false;

        /**
         * transfer a reminder object to ReminderFragment by using EventBus here
         */
        Reminder reminder = new Reminder();
        reminder.setId(movies.getId());
        reminder.setPosterId(movies.getMovie_poster());
        reminder.setMovieTitle(movies.getMovie_title());
        reminder.setReleaseDate(movies.getMovie_releaseday());
        reminder.setRating(movies.getMovie_rating());
        reminder.setReminderedDate(txtReminderDate.getText().toString());

        EventBus.getDefault().postSticky(reminder);
    }

    private void calculateTime(Date date) {
        Calendar cal = Calendar.getInstance();
        long data = 0;
        int year1 = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;      // 0 to 11
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        long time = cal.getTimeInMillis();
        if (date.getTime() >= time) {
            data = date.getTime() - time;
            scheduleAlarm(data);
        } else {
            Toast.makeText(getContext(), "Bạn Nhập Sai Ngày Giờ", Toast.LENGTH_SHORT).show();
        }
    }

    private void scheduleAlarm(long data) {
        Long time = new GregorianCalendar().getTimeInMillis() + data;
        Log.d("LogTime",time+"");

        // create an Intent and set the class which will execute when Alarm triggers, here we have
        // given AlarmReciever in the Intent, the onRecieve() method of this class will execute when
        // alarm triggers and
        //we will write the code to send SMS inside onRecieve() method pf Alarmreciever class
        Intent intentAlarm = new Intent(getContext(), AlarmReceiver.class);

        // create the object
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        //set the alarm for particular time
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(getContext(), 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
    }
}
