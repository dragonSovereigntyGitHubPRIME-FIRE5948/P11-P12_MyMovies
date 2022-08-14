package sg.edu.rp.c346.id21040247.p12mymovies;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /* -------------------------------------------------- Initialisation -------------------------------------------------- */

    //tv, et
    TextView tvShowMovies;
    EditText etMovieTitle, etYear, etFilterTitle, etImageURL;

    //btns
    Button btnInsert, btnCancel, btnShowList;

    //spinners
    Spinner spnGenre, spnAgeRating, spnFilterGenre, spnFilterYear, spnFilterAgeRating;

    //arraylist, arrayadapter
    ArrayList<String> alYears = new ArrayList<>(); //year spinner
    ArrayAdapter<String> aaYears; //year spinner

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* -------------------------------------------------- Linking -------------------------------------------------- */

        /* -------------------------------------------------- Alert Dialog -------------------------------------------------- */
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewDialog = inflater.inflate(R.layout.custom_dialog, null);

        //connect view elements from custom dialog
        final Button btnPositive = viewDialog.findViewById(R.id.btnPositive);
        final Button btnNegative = viewDialog.findViewById(R.id.btnNegative);
        final TextView tvMsg = viewDialog.findViewById(R.id.tvMsg);
        final TextView tvTitle = viewDialog.findViewById(R.id.tvTitle);

        //btn set text
        btnPositive.setText("Update");
        btnNegative.setText("Cancel");
        tvTitle.setText("Update");

        //dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(viewDialog);

        //create AlertDialog object by using builder
        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        }

        dialog.show();
        /* -------------------------------------------------- Alert Dialog -------------------------------------------------- */

        etMovieTitle = findViewById(R.id.etMovieTitle);
        etYear = findViewById(R.id.etYear);
        btnInsert = findViewById(R.id.btnInsert);
        btnShowList = findViewById(R.id.btnShowList);
        spnGenre = findViewById(R.id.spnGenre);
        spnAgeRating = findViewById(R.id.spnAgeRating);
        spnFilterYear = findViewById(R.id.spnFilterYear);
        spnFilterGenre = findViewById(R.id.spnFilterGenre);
        spnFilterAgeRating = findViewById(R.id.spnFilterAgeRating);
        etFilterTitle = findViewById(R.id.etFilterTitle);
        etImageURL = findViewById(R.id.etImageURL);
        tvShowMovies = findViewById(R.id.tvShowMovies);
        btnCancel = findViewById(R.id.btnCancel);

        /* -------------------------------------------------- Insert -------------------------------------------------- */

         /*
         condition 1 - no empty entry for title and singers
         condition 2 - no empty entry for year, at least 4 digits
         condition 3 - between year 1000 to 2022, cannot be negative so e.g. -2000 not allowed
         */

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = 0;

                String title = etMovieTitle.getText().toString();
                String genre = spnGenre.getSelectedItem().toString();
                String stringYear = etYear.getText().toString();
                String agerating = spnAgeRating.getSelectedItem().toString();
                String movieImageURL = etImageURL.getText().toString();

                //condition 2
                if (stringYear.trim().length() == 4){
                    year = Integer.parseInt(stringYear);
                }else{
                    Toast.makeText(MainActivity.this, "Year at least 4 digits", Toast.LENGTH_SHORT).show();
                }

                //condition 1
                if (!title.isEmpty() && !genre.isEmpty() && !agerating.isEmpty() &&
                        //condition 3
                        year <= 2022 && year > -1 ) {

                    DBHelper dbh = new DBHelper(MainActivity.this);
                    long inserted_id = dbh.insertMovie(title, genre, year, agerating, movieImageURL);

                    if (inserted_id != -1) {
                        Toast.makeText(MainActivity.this, "Insert Successful", Toast.LENGTH_SHORT).show();
                        //resets the fields if inserted
                        etMovieTitle.setText("");
                        spnGenre.setSelection(0);
                        etYear.setText("");
                        spnAgeRating.setSelection(0);
                        etImageURL.setText("");
                    }
                    dbh.close();
                } else {
                    Toast.makeText(MainActivity.this, "Missing/Wrong Inputs", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etMovieTitle.setText("");
                etYear.setText("");
                spnAgeRating.setSelection(0);
                spnGenre.setSelection(0);
                etImageURL.setText("");
            }
        });

        /* -------------------------------------------------- Show -------------------------------------------------- */
        tvShowMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //inflate custom dialog layout
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewDialog = inflater.inflate(R.layout.dialog_view_movies, null);
                //

                //connect view elements from custom dialog
                final EditText etFilterTitle = viewDialog.findViewById(R.id.etFilterTitle);
                final Spinner spnFilterGenre = viewDialog.findViewById(R.id.spnFilterGenre);
                final Spinner spnFilterYear = viewDialog.findViewById(R.id.spnFilterYear);
                final Spinner spnFilterAgeRating = viewDialog.findViewById(R.id.spnFilterAgeRating);
                final Button btnShowList = viewDialog.findViewById(R.id.btnShowList);
                final Button btnCancel = viewDialog.findViewById(R.id.btnCancel);
                //

                final ImageView ivTitle = viewDialog.findViewById(R.id.ivTitle);

                //image view
                RotateAnimation rotateAnimation = new RotateAnimation(0, 360f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);

                rotateAnimation.setInterpolator(new LinearInterpolator());
                rotateAnimation.setDuration(3000);
                rotateAnimation.setRepeatCount(Animation.INFINITE);

                ivTitle.startAnimation(rotateAnimation);
                //

                //year spinner
                DBHelper dbh = new DBHelper(MainActivity.this);

                alYears.clear();
                alYears.addAll(dbh.showYears());

                //populate arrayadapter object with -spinner layout- and years arraylist
                aaYears = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, alYears);
                aaYears.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                //set adapter
                spnFilterYear.setAdapter(aaYears);
                dbh.close();
                //

                //dialog builder
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false); //cannot cancel by clicking outside dialog box
                builder.setView(viewDialog);

                //create AlertDialog object by using builder
                AlertDialog dialog = builder.create();

                //dialog animation
                if (dialog.getWindow() != null) {
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                }
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                dialog.show();
                //

                btnShowList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String title = etFilterTitle.getText().toString();
                        String genre = spnFilterGenre.getSelectedItem().toString();
                        String year = spnFilterYear.getSelectedItem().toString();
                        String agerating = spnFilterAgeRating.getSelectedItem().toString();
                        String movieImageURL = etImageURL.getText().toString();

                        //intent object
                        Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                        //db object
                        DBHelper dbh = new DBHelper(MainActivity.this);
                        //create arraylist
                        ArrayList<Movie> movies = new ArrayList<>();

                        //add movies using READ in DBHelper
                        //parameters are POSSIBLE filter conditions (check showMovies method to see how it works)
                        movies.addAll(dbh.showMovies(title, genre, year, agerating, 0));

                        //intent movies arraylist for display in arraydapter for ViewActivity
                        intent.putExtra("movies", movies);
                        //intent user entries for setting TextView in ViewAcitvity
                        intent.putExtra("title", title);
                        intent.putExtra("genre", genre);
                        intent.putExtra("year", year);
                        intent.putExtra("age_rating", agerating);
                        intent.putExtra("movieImageURL", movieImageURL);

                        dbh.close();
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

    }
}