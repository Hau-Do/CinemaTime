package group6.com.cimenatime.Other;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import group6.com.cimenatime.Model.Movies;

/**
 * Created by HauDT on 03/22/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "MovieDb";

    // Contacts table name
    private static final String TABLE_CONTACTS = "Movie";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "Title";
    private static final String KEY_RELEASEDAY = "Releaseday";
    private static final String KEY_FAVORITE = "Favorite";
    private static final String KEY_OVERVIEW = "Overview";
    private static final String KEY_RATING = "Rating";
    private static final String KEY_ADULT = "Adult";
    private static final String KEY_POSTER = "Poster";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_RELEASEDAY + " TEXT," + KEY_FAVORITE + " INTEGER,"
                + KEY_OVERVIEW + " TEXT," + KEY_RATING + " REAL,"
                + KEY_ADULT + " Integer," + KEY_POSTER + " BLOB" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

    public void addMovie(Movies movies) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, movies.getId());
        values.put(KEY_TITLE, movies.getMovie_title());
        values.put(KEY_RELEASEDAY, movies.getMovie_releaseday());
        values.put(KEY_FAVORITE, movies.isMovie_favorite());
        values.put(KEY_OVERVIEW, movies.getMovie_overview());
        values.put(KEY_RATING, movies.getMovie_rating());
        values.put(KEY_ADULT, movies.isMovie_adult());
        values.put(KEY_POSTER, movies.getMovie_poster());

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }

    public List<Movies> getAllContacts() {
        List<Movies> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
//                \+" WHERE " + KEY_RATING +">=" +a;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Movies movies = new Movies();
                movies.setId(cursor.getInt(0));
                movies.setMovie_title(cursor.getString(1));
                movies.setMovie_releaseday(cursor.getString(2));
                movies.setMovie_favorite(cursor.getInt(3));
                movies.setMovie_overview(cursor.getString(4));
                movies.setMovie_rating(cursor.getDouble(5));
                movies.setMovie_adult(cursor.getInt(6));

                movies.setMovie_poster(cursor.getString(7));
                // Adding contact to list
                contactList.add(movies);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public Movies getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE " + KEY_ID + "=" + id;
        Cursor cursor = db.rawQuery(selectQuery, null);
        Movies movies = new Movies();
        if (cursor != null)

        {
            cursor.moveToFirst();

            try {

                movies.setId(cursor.getInt(0));
                movies.setMovie_title(cursor.getString(1));
                movies.setMovie_releaseday(cursor.getString(2));
                movies.setMovie_favorite(cursor.getInt(3));
                movies.setMovie_overview(cursor.getString(4));
                movies.setMovie_rating(cursor.getDouble(5));
                movies.setMovie_adult(cursor.getInt(6));
                movies.setMovie_poster(cursor.getString(7));
            } catch (Exception e) {
                return movies = null;
            }
        }
        // return contact
        return movies;
    }

    public List<Movies> getAllFavContacts() {
        List<Movies> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE " + KEY_FAVORITE + "=1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

                do {

                    Movies movies = new Movies();
                    movies.setId(cursor.getInt(0));
                    movies.setMovie_title(cursor.getString(1));
                    movies.setMovie_releaseday(cursor.getString(2));
                    movies.setMovie_favorite(cursor.getInt(3));
                    movies.setMovie_overview(cursor.getString(4));
                    movies.setMovie_rating(cursor.getDouble(5));
                    movies.setMovie_adult(cursor.getInt(6));

                    movies.setMovie_poster(cursor.getString(7));

                    // Adding contact to list
                    contactList.add(movies);


                } while (cursor.moveToNext());
            }



        // return contact list
        return contactList;
    }

    public List<Movies> getQueryFavContacts(String s) {
        List<Movies> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE " + KEY_FAVORITE + "=1" + " AND " + KEY_TITLE + " LIKE " + "'%" + s + "%'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Movies movies = new Movies();
                movies.setId(cursor.getInt(0));
                movies.setMovie_title(cursor.getString(1));
                movies.setMovie_releaseday(cursor.getString(2));
                movies.setMovie_favorite(cursor.getInt(3));
                movies.setMovie_overview(cursor.getString(4));
                movies.setMovie_rating(cursor.getDouble(5));
                movies.setMovie_adult(cursor.getInt(6));

                movies.setMovie_poster(cursor.getString(7));
                // Adding contact to list
                contactList.add(movies);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }


    public int updateFavorite(int i, int position) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FAVORITE, i);

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = " + position, null);
    }

    public void deleteContact(Movies movies) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[]{String.valueOf(movies.getId())});
        db.close();
    }


    /**
     * HauDT: write some functions to query
     */

    /**
     * show all of movies releasing from specified year
     *
     * @return: list of movies
     */
    public List<Movies> getMoviesFollowYear() {

        List<Movies> listMovie = new ArrayList<>();


        return listMovie;
    }

    /**
     * show all of movies follow descending release date
     *
     * @return: list of movies
     */
    public List<Movies> getMoviesFollowDescendingReleaseDate() {

        List<Movies> listMovie = new ArrayList<>();

        String query = "SELECT * FROM" + TABLE_CONTACTS + " ORDER BY " + KEY_RELEASEDAY + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Movies movies = new Movies();
                movies.setId(cursor.getInt(0));
                movies.setMovie_title(cursor.getString(1));
                movies.setMovie_releaseday(cursor.getString(2));
                movies.setMovie_favorite(cursor.getInt(3));
                movies.setMovie_overview(cursor.getString(4));
                movies.setMovie_rating(cursor.getDouble(5));
                movies.setMovie_adult(cursor.getInt(6));

                movies.setMovie_poster(cursor.getString(7));
                // Adding contact to list
                listMovie.add(movies);
            } while (cursor.moveToNext());
        }

        return listMovie;
    }


}
