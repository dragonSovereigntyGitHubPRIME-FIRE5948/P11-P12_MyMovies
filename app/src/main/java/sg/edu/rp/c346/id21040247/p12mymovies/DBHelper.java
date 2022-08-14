package sg.edu.rp.c346.id21040247.p12mymovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_MOVIES = "movies";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_GENRE = "genre";
    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_AGE_RATING = "age_rating";
    private static final String COLUMN_IMAGE = "movie_image_URL";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /* -------------------------------------------------- CRUD: CREATE, READ, UPDATE, DELETE -------------------------------------------------- */

    //onCreate Database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryCreate = "CREATE TABLE " + TABLE_MOVIES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_GENRE + " TEXT, "
                + COLUMN_YEAR + " TEXT, "
                + COLUMN_AGE_RATING + " TEXT, "
                + COLUMN_IMAGE + " TEXT ) ";
        db.execSQL(queryCreate);
    }

    //onUpgrade Database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        onCreate(db);
    }

    //Insert
    public long insertMovie(String title, String genre, Integer year, String age_rating, String imgURL) {
        //create or open database for reading/editing, creates database if it does not exist (check device file explorer)
        SQLiteDatabase db = this.getWritableDatabase();

        //content object to store insert items
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_GENRE, genre);
        values.put(COLUMN_YEAR, year);
        values.put(COLUMN_AGE_RATING, age_rating);
        values.put(COLUMN_IMAGE, imgURL);

        //returns row id of inserted row, otherwise -1
        long result = db.insert(TABLE_MOVIES, null, values);
        db.close();

        //row id
        return result;
    }

    //Update
    public int updateMovie(int id, String title, String genre, int year, String age_rating, String imageURL){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_GENRE, genre);
        values.put(COLUMN_YEAR, year);
        values.put(COLUMN_AGE_RATING, age_rating);
        values.put(COLUMN_IMAGE, imageURL);

        //prepared statement for update query
        String condition = COLUMN_ID + "= ?";
        String[] args = {String.valueOf(id)};
        int result = db.update(TABLE_MOVIES, values, condition, args);
        db.close();
        return result;
    }

    //Delete
    public int deleteMovie(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String condition = COLUMN_ID + "= ?";
        String[] args = {String.valueOf(id)};
        int result = db.delete(TABLE_MOVIES, condition, args);
        db.close();
        return result;
    }

    //Read
    //array add method
    public static String[] add(String[] original, String add) {
        String[] added = new String[original.length+1];

        for(int i = 0; i<original.length; i++) {
            added[i] = original[i];
        }
        added[added.length-1] = add;
        return added;
    }

    //show movies
    public ArrayList<Movie> showMovies(String filterTitle, String filterGenre, String filterYear, String filterAgeRating, int sortID) {
        //create arraylist to store all row objects
        ArrayList<Movie> moviesList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns= {COLUMN_ID, COLUMN_TITLE, COLUMN_GENRE, COLUMN_YEAR, COLUMN_AGE_RATING, COLUMN_IMAGE};
        String condition = "";
        String[] args = new String[0];

        //check parameter empty or not, if not conditions and args are filled
        if (!filterTitle.isEmpty()) {
            condition = COLUMN_TITLE + " Like ? ";
            args = add(args, "%"+filterTitle+"%");
        }

        if (!filterGenre.isEmpty()) {
            Log.d(filterGenre, filterGenre);
            if(condition != "") {
                condition += "AND ";
            }
            condition += COLUMN_GENRE + " Like ? ";
            args = add(args, "%"+filterGenre+"%" );
        }

        if (!filterYear.isEmpty()) {
            if(!condition.equals("")) {
                condition += "AND ";
            }
            condition += COLUMN_YEAR + " Like ? ";
            args = add(args, "%"+filterYear+"%" );
        }

        if (!filterAgeRating.isEmpty()) {
            if(!condition.equals("")) {
                condition += "AND ";
            }
            condition += COLUMN_AGE_RATING + " Like ? ";
            args = add(args, "%"+filterAgeRating+"%");
        }

//       if no filter parameters, show entire list
        if (filterTitle.isEmpty() && filterGenre.isEmpty() && filterYear.isEmpty() && filterAgeRating.isEmpty() ){
            condition = null;
            args = null;
        }

        //order by conditions depending on sort id
        String order = null;
        switch (sortID){
            case 0: order = null;
                break;
            case 1: order = COLUMN_TITLE+" ASC";
                break;
            case 2: order = COLUMN_TITLE+" DESC";
                break;
            case 3: order = COLUMN_GENRE+" ASC";
                break;
            case 4: order = COLUMN_GENRE+" DESC";
                break;
            case 5: order = COLUMN_YEAR+" ASC";
                break;
            case 6: order = COLUMN_YEAR+" DESC";
                break;
            case 7: order = COLUMN_AGE_RATING+" ASC";
                break;
            case 8: order = COLUMN_AGE_RATING+" DESC";
                break;
        }

        //run prepared query, store row in cursor object
        Cursor cursor = db.query(TABLE_MOVIES, columns, condition, args,
                null, null, order, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String genre = cursor.getString(2);
                String year = cursor.getString(3);
                String age_rating = cursor.getString(4);
                String imgURL = cursor.getString(5);

                Movie movie = new Movie(id, title, genre, year, age_rating, imgURL);
                moviesList.add(movie);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return moviesList;
    }

    //Read
    //get years for spinner
    public ArrayList<String> showYears() {

        ArrayList<String> alYears = new ArrayList<>();
        //empty selection
        alYears.add("");
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns= {COLUMN_YEAR};
        Cursor cursor = db.query(TABLE_MOVIES, columns, null, null,
                null, null, COLUMN_YEAR+" DESC", null);

        if (cursor.moveToFirst()) {
            do {
                String year = cursor.getString(0);
                //if not year inside arraylist yet, add it
                if(!alYears.contains(year)) {
                    alYears.add(year);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return alYears;
    }
}
