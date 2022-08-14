package sg.edu.rp.c346.id21040247.p12mymovies;

import java.io.Serializable;

//implement interface Serializable to intent complex objects
public class Movie implements Serializable {

    //fields
    private int id;
    private String title;
    private String genre;
    private String year;
    private String age_rating;
    private String movieImageURL;

    //constructor
    public Movie(int id, String title, String genre, String year, String age_rating, String movieImageURL) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.age_rating = age_rating;
        this.movieImageURL = movieImageURL;
    }

    //toString
    @Override
    public String toString() {
        return "\n" + "Song Title: " + title + " - " + year + "\n" +
                "Singer: " + genre + "\n" +
                "Rating: " + age_rating + "\n";
    }

    //getter, setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAge_rating() {
        return age_rating;
    }

    public void setAge_rating(String age_rating) {
        this.age_rating = age_rating;
    }

    public String getMovieImageURL() {
        return movieImageURL;
    }

    public void setMovieImageURL(String movieImageURL) {
        this.movieImageURL = movieImageURL;
    }
}
