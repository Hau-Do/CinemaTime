package group6.com.cimenatime.Model;

/**
 * Created by HauDT on 04/29/2017.
 */
public class Reminder {

    private String posterId;
    private String movieTitle;
    private String releaseDate;
    private Double rating;
    private String reminderedDate;
    private int id; // id of the movie displaying


    public Reminder() {
    }

    public Reminder(String posterId, String movieTitle, String releaseDate, Double rating, String reminderedDate) {
        this.posterId = posterId;
        this.movieTitle = movieTitle;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.reminderedDate = reminderedDate;
    }

    public String getPosterId() {
        return posterId;
    }

    public void setPosterId(String posterId) {
        this.posterId = posterId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getReminderedDate() {
        return reminderedDate;
    }

    public void setReminderedDate(String reminderedDate) {
        this.reminderedDate = reminderedDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
