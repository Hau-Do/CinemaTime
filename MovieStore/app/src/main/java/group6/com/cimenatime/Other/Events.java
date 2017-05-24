package group6.com.cimenatime.Other;

import java.util.List;

import group6.com.cimenatime.Model.Movies;

/**
 * Created by HauDT on 03/30/2017.
 */
public class Events {

    public static class MovieListFragmentMessage {
        private List<Movies> moviesList;
        public List<Movies> getMoviesList() {
            return moviesList;
        }
        public void setMoviesList(List<Movies> moviesList) {
            this.moviesList = moviesList;
        }
    }

    public static class FavoriteFragmentMessage {

        private List<Movies> moviesList;
        public List<Movies> getMoviesList() {
            return moviesList;
        }

        public void setMoviesList(List<Movies> moviesList) {
            this.moviesList = moviesList;
        }
    }

    public static class DetailFragmentMessage {

        public Movies getMovies() {
            return movies;
        }

        public void setMovies(Movies movies) {
            this.movies = movies;
        }

        private Movies movies;
    }
    public static class MovieList2FragmentMassage {

        public Movies getMovies() {
            return movies;
        }

        public void setMovies(Movies movies) {
            this.movies = movies;
        }

        private Movies movies;
    }
}

