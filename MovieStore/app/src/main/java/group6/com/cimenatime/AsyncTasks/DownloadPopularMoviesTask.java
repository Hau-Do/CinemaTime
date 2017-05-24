package group6.com.cimenatime.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import group6.com.cimenatime.Model.Movies;
import group6.com.cimenatime.Other.DatabaseHandler;

/**
 * Created by HauDT on 05/6/2017.
 */

public class DownloadPopularMoviesTask extends AsyncTask<String, Void, String>{

    private final static String TAG = DownloadPopularMoviesTask.class.getSimpleName();


    private Context context;

    public DownloadPopularMoviesTask(Context context){
        this.context = context;
    }


    @Override
    protected String doInBackground(String... params) {

        HttpRequest httpRequest = new HttpRequest();

        // make a request for the url and get response
        String jsonString = httpRequest.makeHttpRequest(params[0]);
        Log.e(TAG, "Response from url: " + jsonString);

        if(jsonString != null){

            try {

                JSONObject object = new JSONObject(jsonString);
                JSONArray arrayResult = object.getJSONArray("results");
                for(int i = 0; i < arrayResult.length(); i++){

                    JSONObject jsonObject = arrayResult.getJSONObject(i);

                    String title = jsonObject.getString("title");
                    String releaseDay = jsonObject.getString("release_date");
                    String overview = jsonObject.getString("overview");
                    Double rating = jsonObject.getDouble("vote_average");
                    boolean adultResult = jsonObject.getBoolean("adult");
                    int adult;
                    if (adultResult) {
                        adult = 1;
                    } else {
                        adult = 0;
                    }
                    String posterSuf = jsonObject.getString("poster_path");
                    String poster = "http://image.tmdb.org/t/p/original/"+posterSuf;

                    Movies movies = new Movies();
                    movies.setMovie_title(title);
                    movies.setMovie_releaseday(releaseDay);
                    movies.setMovie_favorite(0);
                    movies.setMovie_overview(overview);
                    movies.setMovie_rating(rating);
                    movies.setMovie_adult(adult);
                    movies.setMovie_poster(poster);

                    DatabaseHandler handler = new DatabaseHandler(context);
                    handler.addMovie(movies);

                }


            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }

        }

        return jsonString;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        /**
         * update to UI Thread here
         */
    }
}
