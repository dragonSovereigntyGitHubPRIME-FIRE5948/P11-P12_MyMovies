package sg.edu.rp.c346.id21040247.p12mymovies;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class EditActivity extends AppCompatActivity {

    /* -------------------------------------------------- Initialisation -------------------------------------------------- */
    //TextView
    //EditText
    EditText etMovieID, etEditMovieTitle, etEditYear, etMovieURL;
    //Spinner
    Spinner spnEditGenre, spnEditAgeRating;
    //Button
    Button btnUpdate, btnDelete, btnReturn;
    //Image
    ImageView ivMovieUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnReturn = findViewById(R.id.btnReturn);
        etMovieID = findViewById(R.id.etMovieID);
        etEditMovieTitle = findViewById(R.id.etEditMovieTitle);
        etEditYear = findViewById(R.id.etEditYear);
        spnEditGenre = findViewById(R.id.spnEditGenre);
        spnEditAgeRating = findViewById(R.id.spnEditAgeRating);
        ivMovieUpdate = findViewById(R.id.ivMovieUpdate);
        etMovieURL = findViewById(R.id.etMovieURL);

        //get intent selected movie
        Intent i = getIntent();
        Movie movie = (Movie) i.getSerializableExtra("getMovie");

        //get movie fields
        int id = movie.getId();
        String title = movie.getTitle();
        String genre = movie.getGenre();
        String year = movie.getYear();
        String age_rating = movie.getAge_rating();
        String imgURL = movie.getMovieImageURL();

        //set image
        if (!imgURL.isEmpty()){
            Picasso.with(this).load(imgURL).placeholder(R.drawable.noimage).into(ivMovieUpdate);
        } else if (imgURL.isEmpty()){
            ivMovieUpdate.setImageResource(R.drawable.noimage);
        }

        //set views with selected movie details
        etMovieID.setText(String.valueOf(id));
        etEditMovieTitle.setText(String.valueOf(title));
        etEditYear.setText(String.valueOf(year));
        etMovieURL.setText(imgURL);

        //set spinner Genre
        for (int count = 0; count < spnEditGenre.getCount(); count++ ){
            if (spnEditGenre.getItemAtPosition(count).equals(genre)){
                spnEditGenre.setSelection(count);
            }
        }

        //set spinner Age Rating
        for (int count = 0; count < spnEditAgeRating.getCount(); count++ ){
            if (spnEditAgeRating.getItemAtPosition(count).equals(age_rating)){
                spnEditAgeRating.setSelection(count);
            }
        }

        //Update Btn + Dialog
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //inflate custom dialog layout
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewDialog = inflater.inflate(R.layout.custom_dialog, null);

                //connect view elements from custom dialog
                final Button btnPositive = viewDialog.findViewById(R.id.btnPositive);
                final Button btnNegative = viewDialog.findViewById(R.id.btnNegative);
                final TextView tvMsg = viewDialog.findViewById(R.id.tvMsg);
                final TextView tvTitle = viewDialog.findViewById(R.id.tvTitle);

                //btn set text
                btnPositive.setText("Update");
                tvTitle.setText("Confirm Update?");

                //dialog builder
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                builder.setCancelable(false); //cannot cancel by clicking outside dialog box
                builder.setView(viewDialog);

                //create AlertDialog object by using builder
                AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();

                //get view inputs
                String editTitle = etEditMovieTitle.getText().toString();
                String editGenre = spnEditGenre.getSelectedItem().toString();
                int editYear = Integer.parseInt(etEditYear.getText().toString());
                String editAgeRating = spnEditAgeRating.getSelectedItem().toString();
                String editImageURL = etMovieURL.getText().toString();

                String msg = "\nTitle: " + editTitle + "\n" + "Genre: " + editGenre + "\n" + "Year: " + editYear + "\n" + "Age Rating: " + editAgeRating + "\n"
                            + "Image URL: " + editImageURL + "\n" ;

                tvMsg.setText("Once updated, you can still undo changes via the Recycle Bin (SHARE PREFERENCE)\n"+msg);

                //positive btn
                btnPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DBHelper dbh = new DBHelper(EditActivity.this);
                        int result = dbh.updateMovie(id, editTitle, editGenre, editYear, editAgeRating, editImageURL);
                        dbh.close();

                        if (result == 1) {
                            Toast.makeText(EditActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(EditActivity.this, "No Updates Were Made", Toast.LENGTH_SHORT).show();
                        }
                        dbh.close();
                        finish();
                    }
                });

                //negative btn
                btnNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        //Delete Btn + Dialog
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater =
                        (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewDialog = inflater.inflate(R.layout.custom_dialog, null);

                //connect view elements from custom dialog
                final Button btnPositive = viewDialog.findViewById(R.id.btnPositive);
                final Button btnNegative = viewDialog.findViewById(R.id.btnNegative);
                final TextView tvMsg = viewDialog.findViewById(R.id.tvMsg);
                final TextView tvTitle = viewDialog.findViewById(R.id.tvTitle);

                //btn set text
                btnPositive.setText("Delete");
                tvTitle.setText("Confirm Delete?");
                String msg = "Once deleted, you can still get the deleted item in the Recycle Bin";
                tvMsg.setText(msg);

                //dialog builder
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                builder.setCancelable(false); //cannot cancel by clicking outside dialog box
                builder.setView(viewDialog);

                //create AlertDialog object by using builder
                AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();

                //positive btn
                btnPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DBHelper dbh = new DBHelper(EditActivity.this);
                        int result = dbh.deleteMovie(id);

                        if (result == 1){
                            Toast.makeText(EditActivity.this, "Delete Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditActivity.this, "No Deletes Were Made", Toast.LENGTH_SHORT).show();
                        }
                        dbh.close();
                        finish();
                    }
                });

                //negative btn
                btnNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        //Return Btn
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}