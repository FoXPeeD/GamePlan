package com.example.foxtz.gameplan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class createPost extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        getSupportActionBar().setTitle("New Post");

        Spinner categorySpinner = findViewById(R.id.categorySpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        categorySpinner.setAdapter(categoryAdapter);

        final Spinner gameSpinner = findViewById(R.id.gameSpinner);
        gameSpinner.getSelectedView().setEnabled(false);
        gameSpinner.setEnabled(false);


        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gameSpinner.getSelectedView().setEnabled(true);
                gameSpinner.setEnabled(true);
                ArrayAdapter<CharSequence> gameAdapter;

                switch(position) {
                    case 0:
                        gameAdapter = ArrayAdapter.createFromResource(createPost.this,
                            R.array.ball_games_array, android.R.layout.simple_spinner_item);
                        break;
                    case 1:
                        gameAdapter = ArrayAdapter.createFromResource(createPost.this,
                            R.array.video_games_array, android.R.layout.simple_spinner_item);
                        break;
                    case 2:
                        gameAdapter = ArrayAdapter.createFromResource(createPost.this,
                            R.array.board_games_array, android.R.layout.simple_spinner_item);
                        break;
                    case 3:
                        gameAdapter = ArrayAdapter.createFromResource(createPost.this,
                            R.array.workout_array, android.R.layout.simple_spinner_item);
                        break;
                    default:
                        gameAdapter = ArrayAdapter.createFromResource(createPost.this,
                                R.array.ball_games_array, android.R.layout.simple_spinner_item);
                }


                gameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                gameSpinner.setAdapter(gameAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
