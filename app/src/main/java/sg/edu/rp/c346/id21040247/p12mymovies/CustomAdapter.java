package sg.edu.rp.c346.id21040247.p12mymovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter {

    Context parent_context;
    int layout_id;
    ArrayList<Movie> alMovies;

    public CustomAdapter(Context context, int resource, ArrayList<Movie> objects) {
        super(context, resource, objects);
        parent_context = context;
        layout_id = resource;
        alMovies = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //create layoutinflator object
        LayoutInflater inflater = (LayoutInflater) parent_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate 'view' with the
        View rowView = inflater.inflate(layout_id, parent, false);

        //link the UI components and do the necessary binding
        TextView tvTitle = rowView.findViewById(R.id.tvTitle);
        TextView tvGenre = rowView.findViewById(R.id.tvGenre);
        TextView tvYear = rowView.findViewById(R.id.tvYear);
        ImageView ivAgeRating = rowView.findViewById(R.id.ivAgeRating);
        ImageView ivMovie = rowView.findViewById(R.id.ivMovie);

        Movie movieObject = alMovies.get(position);

        // Set values to the TextView to display the corresponding information
        tvTitle.setText(movieObject.getTitle());
        tvGenre.setText(movieObject.getGenre());
        tvYear.setText(" -  " + movieObject.getYear());

        String ageRating = movieObject.getAge_rating();

        //age rating
        switch (ageRating) {
            case "G":
                ivAgeRating.setImageResource(R.drawable.g);
                break;
            case "PG":
                ivAgeRating.setImageResource(R.drawable.pg);
                break;
            case "P13":
                ivAgeRating.setImageResource(R.drawable.pg13);
                break;
            case "NC16":
                ivAgeRating.setImageResource(R.drawable.nc16);
                break;
            case "M18":
                ivAgeRating.setImageResource(R.drawable.m18);
                break;
            case "R21":
                ivAgeRating.setImageResource(R.drawable.r21);
                break;
        }

        //movie image
        String url = movieObject.getMovieImageURL();

        if (!url.isEmpty()){
            Picasso.with(parent_context).load(url).placeholder(R.drawable.noimage).into(ivMovie);
        } else if (url.isEmpty()){
            ivMovie.setImageResource(R.drawable.noimage);
        }

        return rowView;
    }
}
