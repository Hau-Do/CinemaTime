package group6.com.cimenatime.Model;

/**
 * Created by HauDT on 03/28/2017.
 */

public class MoviesURL {
    public MoviesURL(String URL, String movie_title, String movie_poster) {
        this.URL = URL;
        this.movie_title = movie_title;
        this.movie_poster = movie_poster;
    }

    String URL;
    String movie_title;
    String movie_poster;

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getMovie_title() {
        return movie_title;
    }

    public void setMovie_title(String movie_title) {
        this.movie_title = movie_title;
    }

    public String getMovie_poster() {
        return movie_poster;
    }

    public void setMovie_poster(String movie_poster) {
        this.movie_poster = movie_poster;
    }


}
