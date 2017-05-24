package group6.com.cimenatime.Model;

/**
 * Created by Phuong Doan on 03/20/2017.
 */
public class Movies {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id; // movieId
    private String movie_title;
    private String movie_poster;
    private String movie_releaseday;
    private Double movie_rating;
    private int movie_adult;
    private int movie_favorite;
    private String movie_overview;

//    public Movies(String movie_title, String movie_poster, String movie_releaseday, Double movie_rating, boolean movie_adult, boolean movie_favorite, String movie_overview) {
//        this.movie_title = movie_title;
//        this.movie_poster = movie_poster;
//        this.movie_releaseday = movie_releaseday;
//        this.movie_rating = movie_rating;
//        this.movie_adult = movie_adult;
//        this.movie_favorite = movie_favorite;
//        this.movie_overview = movie_overview;
//    }

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

    public String getMovie_releaseday() {
        return movie_releaseday;
    }

    public void setMovie_releaseday(String movie_releaseday) {
        this.movie_releaseday = movie_releaseday;
    }

    public Double getMovie_rating() {
        return movie_rating;
    }

    public void setMovie_rating(Double movie_rating) {
        this.movie_rating = movie_rating;
    }

    public int isMovie_adult() {
        return movie_adult;
    }

    public void setMovie_adult(int movie_adult) {
        this.movie_adult = movie_adult;
    }

    public int isMovie_favorite() {
        return movie_favorite;
    }

    public void setMovie_favorite(int movie_favorite) {
        this.movie_favorite = movie_favorite;
    }

    public String getMovie_overview() {
        return movie_overview;
    }

    public void setMovie_overview(String movie_overview) {
        this.movie_overview = movie_overview;
    }
}
