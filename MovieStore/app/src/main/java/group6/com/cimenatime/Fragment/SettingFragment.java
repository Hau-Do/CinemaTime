package group6.com.cimenatime.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import group6.com.cimenatime.Activity.MainActivity;
import group6.com.cimenatime.Other.DownloadAPI;
import group6.com.cimenatime.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by HauDT on 03/12/2017.
 */
public class SettingFragment extends Fragment implements
        YearDialogFragment.ShowYearDialogListener,
        NumberDialogFragment.ShowNumberDialogListener{

    private TextView txtFilter;
    private LinearLayout lnGone_Filter;

    private RelativeLayout relativePopularMovies;
    private ImageView imvCheckedPopular;

    private RelativeLayout relativeTopMovies;
    private ImageView imvCheckedTop;

    private RelativeLayout relativeUpcomingMovies;
    private ImageView imvCheckedUpcoming;

    private RelativeLayout relativePlayingMovies;
    private ImageView imvCheckedPlaying;

    private TextView txtRate;
    private SeekBar seekbarRate;

    private TextView txtYear;

    public static final String JSON_URL1 = "http://api.themoviedb.org/3/movie/popular?api_key=e7631ffcb8e766993e5ec0c1f4245f93&page=1";
    public static final String JSON_URL2 = "http://api.themoviedb.org/3/movie/top_rated?api_key=e7631ffcb8e766993e5ec0c1f4245f93&page=1";
    public static final String JSON_URL3 = "http://api.themoviedb.org/3/movie/upcoming?api_key=e7631ffcb8e766993e5ec0c1f4245f93&page=1";
    public static final String JSON_URL4 = "http://api.themoviedb.org/3/movie/now_playing?api_key=e7631ffcb8e766993e5ec0c1f4245f93&page=1";
    private TextView txtSortBy;
    private LinearLayout lnGone_SortBy;

    private RelativeLayout relativeReleaseDate;
    private ImageView imvCheckedReleaseDate;

    private RelativeLayout relativeRating;
    private ImageView imvCheckedRating;

    private TextView txtNumberOfLoading;
    private RelativeLayout relativeNumberOfLoading;
    private TextView txtNumber;

    private FragmentManager fragmentManager;
    DownloadAPI downloadAPI;
    private String preferenceName = "my_state";
    private SharedPreferences.Editor editor;

    /**
     * mFilterPosition: a flag to check clicked status of Filter
     * = 1: popular movies
     * = 2: top rated movies
     * = 3: upcoming movies
     * = 4: nowplaying movies
     */
    public int mFilterPosition = 0;

    /**
     * mSortByPosition: a flag to check clicked status of SortBy
     * = 1: release day
     * = 2: rating
     */
    public int mSortByPosition = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.setting_fragment, container, false);

        setHasOptionsMenu(true);
        addControlsForFilter(view);
        addControlsForSortBy(view);
        addControlsForNumberLoading(view);

        YearDialogFragment.Year.mListener = this;
        NumberDialogFragment.Number.mListener = this;

        downloadAPI = new DownloadAPI(getContext());
        addEvents();
        openListReminderFragment();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        savePreferencesState();
    }

    @Override
    public void onResume() {
        super.onResume();
        readPreferencesState();
    }


    /**
     * save the current state of SettingFragment
     */
    private void savePreferencesState() {

        SharedPreferences preferences = this.getActivity().getSharedPreferences(preferenceName, MODE_PRIVATE);
        editor = preferences.edit();

        int seekRate = seekbarRate.getProgress();
        String textRate = txtRate.getText().toString();
        String textYear = txtYear.getText().toString();
        String textNumber = txtNumber.getText().toString();

        editor.putInt("seek_rate", seekRate);
        editor.putString("text_rate", textRate);
        editor.putString("text_year", textYear);
        editor.putString("text_number", textNumber);

        if (imvCheckedPopular.getVisibility() == View.VISIBLE) {
            editor.putInt("FilterPosition", 1);
            editor.putString("URL", JSON_URL1);

        } else if (imvCheckedTop.getVisibility() == View.VISIBLE) {
            editor.putInt("FilterPosition", 2);
            editor.putString("URL", JSON_URL2);

        } else if (imvCheckedUpcoming.getVisibility() == View.VISIBLE) {
            editor.putInt("FilterPosition", 3);
            editor.putString("URL", JSON_URL3);

        } else if (imvCheckedPlaying.getVisibility() == View.VISIBLE) {
            editor.putInt("FilterPosition", 4);
            editor.putString("URL", JSON_URL4);

        }

        if (imvCheckedReleaseDate.getVisibility() == View.VISIBLE) {
            editor.putInt("SortByPosition", 1);

        } else if (imvCheckedRating.getVisibility() == View.VISIBLE) {
            editor.putInt("SortByPosition", 2);

        }

        editor.commit();
    }

    /**
     * read the saved state of SettingFragment
     */
    private void readPreferencesState() {
        downloadAPI = new DownloadAPI(getContext());
        SharedPreferences preferences = getActivity().getSharedPreferences(preferenceName, MODE_PRIVATE);
        int seekRate = preferences.getInt("seek_rate", -1);
        String textRate = preferences.getString("text_rate", null);
        String textYear = preferences.getString("text_year", null);
        String textNumber = preferences.getString("text_number", null);

        String URL = preferences.getString("URL", JSON_URL1);
        downloadAPI.readPreferencesState(URL);
        mFilterPosition = preferences.getInt("FilterPosition", -1);
        mSortByPosition = preferences.getInt("SortByPosition", -1);
        seekbarRate.setProgress(seekRate);
        txtRate.setText(textRate);
        txtYear.setText(textYear);
        txtNumber.setText(textNumber);

        if (mFilterPosition == 1) {
            imvCheckedPopular.setVisibility(View.VISIBLE);

            imvCheckedTop.setVisibility(View.INVISIBLE);
            imvCheckedUpcoming.setVisibility(View.INVISIBLE);
            imvCheckedPlaying.setVisibility(View.INVISIBLE);
        }
        else if ( mFilterPosition == 2 ){
            imvCheckedTop.setVisibility(View.VISIBLE);

            imvCheckedPopular.setVisibility(View.INVISIBLE);
            imvCheckedUpcoming.setVisibility(View.INVISIBLE);
            imvCheckedPlaying.setVisibility(View.INVISIBLE);
        }
        else if (mFilterPosition == 3){
            imvCheckedUpcoming.setVisibility(View.VISIBLE);

            imvCheckedPopular.setVisibility(View.INVISIBLE);
            imvCheckedTop.setVisibility(View.INVISIBLE);
            imvCheckedPlaying.setVisibility(View.INVISIBLE);
        }
        else if(mFilterPosition == 4){
            imvCheckedPlaying.setVisibility(View.VISIBLE);

            imvCheckedPopular.setVisibility(View.INVISIBLE);
            imvCheckedTop.setVisibility(View.INVISIBLE);
            imvCheckedUpcoming.setVisibility(View.INVISIBLE);
        }

        if(mSortByPosition == 1){
            imvCheckedReleaseDate.setVisibility(View.VISIBLE);

            imvCheckedRating.setVisibility(View.INVISIBLE);
        }
        else if(mSortByPosition == 2){
            imvCheckedRating.setVisibility(View.VISIBLE);

            imvCheckedReleaseDate.setVisibility(View.INVISIBLE);
        }

    }

    public void openListReminderFragment() {

        ((MainActivity) getActivity()).openReminder(new MainActivity.Communicator() {
            @Override
            public void setFlag(boolean isOpen) {
                if (isOpen) {
                    ReminderFragment reminderFragment = new ReminderFragment();
                    fragmentManager = getChildFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.linearSetting, reminderFragment, "reminder");
                    //fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                } else {
                    Fragment fragment = getChildFragmentManager().findFragmentByTag("reminder");
                    fragmentManager.beginTransaction().remove(fragment).commit();
                }
            }
        });

    }

    /**
     * create some controls in Number Of Loading section
     *
     * @param view
     */
    private void addControlsForNumberLoading(View view) {

        txtNumberOfLoading = (TextView) view.findViewById(R.id.txtNumberOfLoading);
        relativeNumberOfLoading = (RelativeLayout) view.findViewById(R.id.relativeNumberOfLoading);
        txtNumber = (TextView) view.findViewById(R.id.txtNumber);

    }

    /**
     * create some controls in Sort By section
     *
     * @param view
     */
    private void addControlsForSortBy(View view) {

        txtSortBy = (TextView) view.findViewById(R.id.txtSortBy);
        lnGone_SortBy = (LinearLayout) view.findViewById(R.id.lnGone_SortBy);

        relativeReleaseDate = (RelativeLayout) view.findViewById(R.id.relativeReleaseDate);
        imvCheckedReleaseDate = (ImageView) view.findViewById(R.id.imvCheckedReleaseDate);

        relativeRating = (RelativeLayout) view.findViewById(R.id.relativeRating);
        imvCheckedRating = (ImageView) view.findViewById(R.id.imvCheckedRating);

    }

    /**
     * create some controls in Filter section
     *
     * @param view
     */
    private void addControlsForFilter(View view) {

        txtFilter = (TextView) view.findViewById(R.id.txtFilter);
        lnGone_Filter = (LinearLayout) view.findViewById(R.id.lnGone_Filter);

        relativePopularMovies = (RelativeLayout) view.findViewById(R.id.relativePopularMovies);
        imvCheckedPopular = (ImageView) view.findViewById(R.id.imvCheckedPopular);

        relativeTopMovies = (RelativeLayout) view.findViewById(R.id.relativeTopMovies);
        imvCheckedTop = (ImageView) view.findViewById(R.id.imvCheckedTop);

        relativeUpcomingMovies = (RelativeLayout) view.findViewById(R.id.relativeUpComingMovies);
        imvCheckedUpcoming = (ImageView) view.findViewById(R.id.imvCheckedUpcoming);

        relativePlayingMovies = (RelativeLayout) view.findViewById(R.id.relativePlayingMovies);
        imvCheckedPlaying = (ImageView) view.findViewById(R.id.imvCheckedPlaying);

        txtRate = (TextView) view.findViewById(R.id.txtRate);
        seekbarRate = (SeekBar) view.findViewById(R.id.seekbarRate);

        txtYear = (TextView) view.findViewById(R.id.txtYear);

    }

    private void addEvents() {

        addEventsOfFilter();
        addEventsOfSortBy();
        addEventsOfNumberOfLoading();

    }

    /**
     * define events in Number Of Loading section
     */
    private void addEventsOfNumberOfLoading() {

        txtNumberOfLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtNumberOfLoading.getTag().equals("NUMBER")) {
                    relativeNumberOfLoading.setVisibility(View.VISIBLE);

                    txtNumberOfLoading.setTag("OPEN");
                } else {
                    relativeNumberOfLoading.setVisibility(View.GONE);
                    txtNumberOfLoading.setTag("NUMBER");
                }

            }
        });

        txtNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Number Loading", Toast.LENGTH_SHORT).show();
                selectNumberPage();

            }
        });

    }

    /**
     * select number of page for loading
     */
    private void selectNumberPage() {

        DialogFragment dialogFragment = new NumberDialogFragment();
        dialogFragment.show(getActivity().getSupportFragmentManager(), "");

    }

    /**
     * define events in Filter section
     */
    private void addEventsOfSortBy() {

        /**
         * click event of Sort By
         */
        txtSortBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtSortBy.getTag().equals("SORT")) {
                    lnGone_SortBy.setVisibility(View.VISIBLE);

                    txtSortBy.setTag("OPEN");
                } else {
                    lnGone_SortBy.setVisibility(View.GONE);
                    txtSortBy.setTag("SORT");
                }
            }
        });

        relativeReleaseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imvCheckedReleaseDate.getVisibility() != View.VISIBLE) {
                    onClickedReleaseDate();
                }
            }
        });

        relativeRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imvCheckedRating.getVisibility() != View.VISIBLE) {
                    onClickedRating();
                }
            }
        });
    }

    /**
     * click event of RATING relativelayout
     */
    private void onClickedRating() {

        if (relativeRating.getTag().equals("RE_RATING")) {
            imvCheckedRating.setVisibility(View.VISIBLE);
            /**
             * do something new here
             */
            SharedPreferences preferences = getActivity().getSharedPreferences(preferenceName, MODE_PRIVATE);
            editor = preferences.edit();
            editor.putInt("SortByPosition", 2);
            editor.commit();
            String URL = preferences.getString("URL", JSON_URL1);
            downloadAPI.readPreferencesState(URL);
            relativeRating.setTag("CLICKED");
            relativeReleaseDate.setTag("RE_DATE");
            imvCheckedReleaseDate.setVisibility(View.INVISIBLE);
        } else {
            imvCheckedRating.setVisibility(View.INVISIBLE);

            relativeRating.setTag("RE_RATING");
        }
    }

    /**
     * click event of RELEASE DATE relativelayout
     */
    private void onClickedReleaseDate() {

        if (relativeReleaseDate.getTag().equals("RE_DATE")) {
            imvCheckedReleaseDate.setVisibility(View.VISIBLE);

            SharedPreferences preferences = getActivity().getSharedPreferences(preferenceName, MODE_PRIVATE);
            editor = preferences.edit();
            editor.putInt("SortByPosition", 1);
            editor.commit();
            String URL = preferences.getString("URL", JSON_URL1);
            downloadAPI.readPreferencesState(URL);
            /**
             * do something new here
             */

            relativeReleaseDate.setTag("CLICKED");
            relativeRating.setTag("RE_RATING");
            imvCheckedRating.setVisibility(View.INVISIBLE);
        } else {
            imvCheckedReleaseDate.setVisibility(View.INVISIBLE);

            relativeReleaseDate.setTag("RE_DATE");
        }

    }

    /**
     * define events in Filter section
     */
    private void addEventsOfFilter() {

        /**
         * click event of Filter
         */
        txtFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // when txtFilter is clicked to open
                if (txtFilter.getTag().equals("FILTER")) {
                    lnGone_Filter.setVisibility(View.VISIBLE);

                    txtFilter.setTag("OPEN");
                }
                // when txtFilter is clicked to closs
                else {
                    lnGone_Filter.setVisibility(View.GONE);
                    txtFilter.setTag("FILTER");
                }
            }
        });

        relativePopularMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imvCheckedPopular.getVisibility() != View.VISIBLE) {
                    onClickedPopularMovies();
                }
            }
        });

        relativeTopMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imvCheckedTop.getVisibility() != View.VISIBLE) {

                    onClickedTopMovies();

                    editor.putString("URL", JSON_URL2);
                }
            }
        });

        relativeUpcomingMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imvCheckedUpcoming.getVisibility() != View.VISIBLE) {
                    onClickedUpcomingMovies();
                }
            }
        });

        relativePlayingMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imvCheckedPlaying.getVisibility() != View.VISIBLE) {
                    onClickedPlayingMovies();
                }
            }
        });

        seekbarRate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                onRateSeekBar(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        txtYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectReleaseYear();

            }
        });
    }

    /**
     * select release year from NumberPicker
     */
    private void selectReleaseYear() {

        DialogFragment dialogFragment = new YearDialogFragment();
        dialogFragment.show(getActivity().getSupportFragmentManager(), "");
    }


    /**
     * rate for every kind of movies
     */
    private void onRateSeekBar(int progress) {

        txtRate.setText(progress + "");
        SharedPreferences preferences = getActivity().getSharedPreferences(preferenceName, MODE_PRIVATE);
        editor = preferences.edit();
        editor.putInt("seek_rate", Integer.parseInt(txtRate.getText().toString()));
        editor.commit();
        String URL = preferences.getString("URL", JSON_URL1);
        downloadAPI.readPreferencesState(URL);
    }

    /**
     * click event of PLAYING MOVIES relativelayout
     */
    private void onClickedPlayingMovies() {

        if (relativePlayingMovies.getTag().equals("RE_PLAYING")) {
            imvCheckedPlaying.setVisibility(View.VISIBLE);
            /**
             * do something new here
             */

            downloadAPI.readPreferencesState(JSON_URL4);
            relativePlayingMovies.setTag("CLICKED");
            relativePopularMovies.setTag("RE_POP");
            relativeTopMovies.setTag("RE_TOP");
            relativeUpcomingMovies.setTag("RE_UP");
            imvCheckedPopular.setVisibility(View.INVISIBLE);
            imvCheckedTop.setVisibility(View.INVISIBLE);
            imvCheckedUpcoming.setVisibility(View.INVISIBLE);
        } else {
            imvCheckedPlaying.setVisibility(View.INVISIBLE);

            relativePlayingMovies.setTag("RE_PLAYING");
        }

    }

    /**
     * click event of UPCOMING MOVIES relativelayout
     */
    private void onClickedUpcomingMovies() {

        if (relativeUpcomingMovies.getTag().equals("RE_UP")) {
            imvCheckedUpcoming.setVisibility(View.VISIBLE);
            /**
             * do something new here
             */
            downloadAPI.readPreferencesState(JSON_URL3);
            relativeUpcomingMovies.setTag("CLICKED");
            relativePopularMovies.setTag("RE_POP");
            relativeTopMovies.setTag("RE_TOP");
            relativePlayingMovies.setTag("RE_PLAYING");
            imvCheckedPopular.setVisibility(View.INVISIBLE);
            imvCheckedTop.setVisibility(View.INVISIBLE);
            imvCheckedPlaying.setVisibility(View.INVISIBLE);
        } else {
            imvCheckedUpcoming.setVisibility(View.INVISIBLE);

            relativeUpcomingMovies.setTag("RE_UP");
        }
    }

    /**
     * click event of TOP RATED MOVIES relativelayout
     */
    private void onClickedTopMovies() {

        if (relativeTopMovies.getTag().equals("RE_TOP")) {
            imvCheckedTop.setVisibility(View.VISIBLE);

            /**
             * do something new here
             */
            downloadAPI.readPreferencesState(JSON_URL2);
            relativeTopMovies.setTag("CLICKED");
            relativePopularMovies.setTag("RE_POP");
            relativeUpcomingMovies.setTag("RE_UP");
            relativePlayingMovies.setTag("RE_PLAYING");
            imvCheckedPopular.setVisibility(View.INVISIBLE);
            imvCheckedUpcoming.setVisibility(View.INVISIBLE);
            imvCheckedPlaying.setVisibility(View.INVISIBLE);

        } else {
            imvCheckedTop.setVisibility(View.INVISIBLE);

            relativeTopMovies.setTag("RE_TOP");
        }
    }

    /**
     * click event of POPULAR MOVIES relativelayout
     */
    private void onClickedPopularMovies() {
        if (relativePopularMovies.getTag().equals("RE_POP")) {
            imvCheckedPopular.setVisibility(View.VISIBLE);

            /**
             * do something new here
             */

            downloadAPI.readPreferencesState(JSON_URL1);
            relativePopularMovies.setTag("CLICKED");
            relativeTopMovies.setTag("RE_TOP");
            relativeUpcomingMovies.setTag("RE_UP");
            relativePlayingMovies.setTag("RE_PLAYING");
            imvCheckedTop.setVisibility(View.INVISIBLE);
            imvCheckedUpcoming.setVisibility(View.INVISIBLE);
            imvCheckedPlaying.setVisibility(View.INVISIBLE);
        } else {
            imvCheckedPopular.setVisibility(View.INVISIBLE);
            relativePopularMovies.setTag("RE_POP");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.menu_main_item_ChangeLayout).setVisible(false);
        menu.findItem(R.id.menu_main_item_MovieList).setVisible(true);
        menu.findItem(R.id.menu_main_item_Favorite).setVisible(true);
        menu.findItem(R.id.menu_main_item_About).setVisible(true);
        menu.findItem(R.id.menu_main_item_Setting).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * listen change of Year NumberPicker
     * @param year
     */
    @Override
    public void sendYear(int year) {
        txtYear.setText(String.valueOf(year));
        YearDialogFragment.year = year;
        if (txtYear.getText() != null) {
            savePreferencesState();
            SharedPreferences preferences = getActivity().getSharedPreferences(preferenceName, MODE_PRIVATE);
            String URL = preferences.getString("URL", JSON_URL1);
            downloadAPI.readPreferencesState(URL);
            /**
             *  MovieList: show movies having year >= txtYear
             */

        }
    }

    /**
     * listen change of PageNumber NumberPicker
     * @param number
     */
    @Override
    public void sendNumber(int number) {
        txtNumber.setText(String.valueOf(number));
        NumberDialogFragment.number = number;

        if( txtNumber.getText() != null ){
            savePreferencesState();

            int currentPage = Integer.parseInt(txtNumber.getText().toString());
            SharedPreferences preferences = getActivity().getSharedPreferences(preferenceName, MODE_PRIVATE);
            String URL = preferences.getString("URL", JSON_URL1);
            String preURL = URL.substring(0, URL.length()-1);

            String updatedURL = preURL + currentPage;
            downloadAPI.readPreferencesState(updatedURL);

        }
    }


    //Remove all reminders list and back to settings
//    public void removeAllRemindersScreen() {
//        if (fragmentManager.findFragmentByTag("reminder") != null) {
//            //Toast.makeText(getContext(), "Back to settings screen", Toast.LENGTH_SHORT).show();
//            /*FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.remove(movieReminderFragment).commit();*/
//            fragmentManager.popBackStack();
//        }
//    }

}
