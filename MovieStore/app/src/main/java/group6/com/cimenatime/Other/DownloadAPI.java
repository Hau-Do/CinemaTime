package group6.com.cimenatime.Other;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import group6.com.cimenatime.Fragment.DetailMoviesFragment;
import group6.com.cimenatime.Model.CastCrew;
import group6.com.cimenatime.Model.Movies;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by HauDT on 03/23/2017.
 */
public class DownloadAPI extends AsyncTask<String, Integer, String> {
    Context context;
    public static final String JSON_URL1 = "http://api.themoviedb.org/3/movie/popular?api_key=e7631ffcb8e766993e5ec0c1f4245f93&page=1";
    public static final String JSON_URL2 = "http://api.themoviedb.org/3/movie/top_rated?api_key=e7631ffcb8e766993e5ec0c1f4245f93&page=1";
        public static final String JSON_URL3 = "http://api.themoviedb.org/3/movie/upcoming?api_key=e7631ffcb8e766993e5ec0c1f4245f93&page=1";
    public static final String JSON_URL4 = "http://api.themoviedb.org/3/movie/now_playing?api_key=e7631ffcb8e766993e5ec0c1f4245f93&page=1";
    private String preferenceName = "my_state";

    public DownloadAPI(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        return docNoiDung_Tu_URL(params[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("DemoJson", s);
        try {
            JSONObject object = new JSONObject(s);
            JSONArray arrayobject = object.getJSONArray("results");
            for (int i = 0; i < arrayobject.length(); i++) {
                JSONObject JsonKh = arrayobject.getJSONObject(i);
                String title = JsonKh.getString("title");
                String poster1 = JsonKh.getString("poster_path");
                String overview = JsonKh.getString("overview");
                String releaseday = JsonKh.getString("release_date");
                Double rating = JsonKh.getDouble("vote_average");
                boolean adult1 = JsonKh.getBoolean("adult");
                int adult;
                if (adult1) {
                    adult = 1;
                } else {
                    adult = 0;
                }
                String poster = "http://image.tmdb.org/t/p/original/" + poster1;

                Movies movies = new Movies();
                movies.setMovie_title(title);
                movies.setMovie_releaseday(releaseday);
                movies.setMovie_adult(adult);
                movies.setMovie_favorite(0);
                movies.setMovie_overview(overview);
                movies.setMovie_rating(rating);
                movies.setMovie_poster(poster);
                DatabaseHandler handler = new DatabaseHandler(context);
                handler.addMovie(movies);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static String docNoiDung_Tu_URL(String theUrl) {
        StringBuilder content = new StringBuilder();

        try {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public void sendRequest(String URL) {

        StringRequest stringRequest = new StringRequest(
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showJSON(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String json) {
        try {

            SharedPreferences preferences = context.getSharedPreferences(preferenceName, MODE_PRIVATE);
            JSONObject object = new JSONObject(json);
            JSONArray arrayobject = object.getJSONArray("results");
            List<Movies> moviesList = new ArrayList<>();
            DatabaseHandler handler = new DatabaseHandler(context);
            for (int i = 0; i < arrayobject.length(); i++) {
                JSONObject JsonKh = arrayobject.getJSONObject(i);
                int id = Integer.parseInt(JsonKh.getString("id"));
                String title = JsonKh.getString("title");
                String poster1 = JsonKh.getString("poster_path");
                String overview = JsonKh.getString("overview");
                String releaseday = JsonKh.getString("release_date");
                Double rating = JsonKh.getDouble("vote_average");
                boolean adult1 = JsonKh.getBoolean("adult");
                String poster = "http://image.tmdb.org/t/p/original/" + poster1;
                int adult;
                if (adult1) {
                    adult = 1;
                } else {
                    adult = 0;
                }
                double mRating = Double.valueOf(preferences.getInt("seek_rate", -1));
//                int textYear = Integer.parseInt(preferences.getString("text_year", null));
//                int subRelease = Integer.parseInt(releaseday.substring(0, 4));


                int sortType = preferences.getInt("SortByPosition", 1);
                String s = preferences.getString("text_year", "0");
                if (s.equals("")) {
                    s = "2000";
                }
                int textYear = Integer.parseInt(s);
                int subRelease = Integer.parseInt(releaseday.substring(0, 4));
                if (sortType == 1) {
                    if (textYear <= subRelease) {
                        Movies movies = new Movies();
                        movies.setId(id);
                        movies.setMovie_title(title);
                        movies.setMovie_releaseday(releaseday);
                        movies.setMovie_adult(adult);
                        movies.setMovie_favorite(0);
                        movies.setMovie_overview(overview);
                        movies.setMovie_rating(rating);
                        movies.setMovie_poster(poster);
                        moviesList.add(movies);
                    }
                } else if (sortType == 2) {


                    if (rating >= mRating) {
                        Movies movies = new Movies();
                        movies.setId(id);
                        movies.setMovie_title(title);
                        movies.setMovie_releaseday(releaseday);
                        movies.setMovie_adult(adult);
                        movies.setMovie_favorite(0);
                        movies.setMovie_overview(overview);
                        movies.setMovie_rating(rating);
                        movies.setMovie_poster(poster);
                        moviesList.add(movies);
                    }
                }



//                if(moviesList)
//                handler.addMovie(movies);

            }
            List<Movies> moviesFavList = handler.getAllFavContacts();
//           |
            for (int i = 0; i < moviesList.size(); i++) {
                for (int j = 0; j < moviesFavList.size(); j++) {
                    if (moviesList.get(i).getId() == moviesFavList.get(j).getId() &&
                            moviesList.get(i).isMovie_favorite() != moviesFavList.get(j).isMovie_favorite()) {
                        moviesList.set(i, moviesFavList.get(j));
                    }
                }

            }
            Events.MovieListFragmentMessage mov = new Events.MovieListFragmentMessage();
            mov.setMoviesList(moviesList);
            EventBus.getDefault().post(mov);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void readPreferencesState(String URL) {

        SharedPreferences preferences = context.getSharedPreferences(preferenceName, MODE_PRIVATE);


        sendRequest(URL);
    }

    public void sendCastRequest(String URL) {

        StringRequest stringRequest = new StringRequest(URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showCastJSON(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void showCastJSON(String json) {
        try {
            String IMAGES_URL = "http://image.tmdb.org/t/p/original";
            SharedPreferences preferences = context.getSharedPreferences(preferenceName, MODE_PRIVATE);
            JSONObject object = new JSONObject(json);
            JSONArray arrayobject = object.getJSONArray("cast");
            List<CastCrew> mCastCrew = new ArrayList<>();
            for (int i = 0; i <= 6; i++) {
                String name = arrayobject.getJSONObject(i).getString("name");
                String images = arrayobject.getJSONObject(i).getString("profile_path");
                if (!images.equals("null")) {
                    images = IMAGES_URL + images;
                    mCastCrew.add(new CastCrew(name, images));
                }

            }

            new DetailMoviesFragment(mCastCrew);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}


