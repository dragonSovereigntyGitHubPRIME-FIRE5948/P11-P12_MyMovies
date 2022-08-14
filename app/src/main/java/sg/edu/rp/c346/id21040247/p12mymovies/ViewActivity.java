package sg.edu.rp.c346.id21040247.p12mymovies;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ViewActivity extends AppCompatActivity {

    /* -------------------------------------------------- Initialisation -------------------------------------------------- */

    //Text View
    TextView tvTitle;

    //Spinner
    Spinner spnSort;

    //List View
    ListView lvMovies;

    //arraylist, arrayadapter
    ArrayList<Movie> alMoviesToDisplay = new ArrayList<>();

    ArrayAdapter<Movie> aaMovie;

    //custom adapter
    CustomAdapter caMovies;

    /* -------------------------------------------------- onResume -------------------------------------------------- */
    @Override
    protected void onResume() {
        super.onResume();
        Intent i = getIntent();
        String intentTitle = i.getStringExtra("title");
        String intentGenre = i.getStringExtra("genre");
        String intentYear = i.getStringExtra("year");
        String intentAgeRating = i.getStringExtra("age_rating");

        //refreshes listview upon re-entry
        DBHelper dbh = new DBHelper(ViewActivity.this);
        alMoviesToDisplay.clear();
        alMoviesToDisplay = dbh.showMovies(intentTitle, intentGenre, intentYear, intentAgeRating, 0);
        aaMovie = new CustomAdapter(ViewActivity.this, R.layout.row, alMoviesToDisplay);
        lvMovies.setAdapter(aaMovie);
        dbh.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        /* -------------------------------------------------- Linking -------------------------------------------------- */
        lvMovies = findViewById(R.id.lvMovies);
        tvTitle = findViewById(R.id.tvTitle);
        spnSort = findViewById(R.id.spnSort);

        /* -------------------------------------------------- Setting Filter Desc-------------------------------------------------- */
        String title = "";
        String filter = "";

        //get intents
        Intent i = getIntent();
        String intentTitle = i.getStringExtra("title");
        String intentGenre = i.getStringExtra("genre");
        String intentYear = i.getStringExtra("year");
        String intentAgeRating = i.getStringExtra("age_rating");

        /* -------------------------------------------------- Set ListView -------------------------------------------------- */
        //get intent (movie arraylist from MainActivity)
        Intent intent = getIntent();
        alMoviesToDisplay = (ArrayList<Movie>) intent.getSerializableExtra("movies");

        caMovies = new CustomAdapter(this, R.layout.row, alMoviesToDisplay);
        lvMovies.setAdapter(caMovies);

        /* -------------------------------------------------- Open Individual Movie -------------------------------------------------- */
        lvMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long identity) {
                Movie getMovie = alMoviesToDisplay.get(position);
                Intent intent2 = new Intent(ViewActivity.this, EditActivity.class);
                intent2.putExtra("getMovie", getMovie);
                startActivity(intent2);
            }
        });

        /* -------------------------------------------------- Sorting ListView -------------------------------------------------- */
        spnSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //get sort id
                int sortID = position;
                switch (sortID) {
                    case 0:
                        sortID = 0;
                        break;
                    case 1:
                        sortID = 1;
                        break;
                    case 2:
                        sortID = 2;
                        break;
                    case 3:
                        sortID = 3;
                        break;
                    case 4:
                        sortID = 4;
                        break;
                    case 5:
                        sortID = 5;
                        break;
                    case 6:
                        sortID = 6;
                        break;
                    case 7:
                        sortID = 7;
                        break;
                    case 8:
                        sortID = 8;
                        break;
                }
                DBHelper dbh = new DBHelper(ViewActivity.this);
                ArrayList<Movie> alSortedMovies = new ArrayList<>();
                alSortedMovies.clear();
                alSortedMovies = dbh.showMovies(intentTitle, intentGenre, intentYear, intentAgeRating, sortID);
                caMovies = new CustomAdapter(ViewActivity.this, R.layout.row, alSortedMovies);
                lvMovies.setAdapter(caMovies);
                dbh.close();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){
            }
        });
    }
}