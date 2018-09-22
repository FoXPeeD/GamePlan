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

        //catogory spinner init
        Spinner categorySpinner = findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        //game spinner init
        final Spinner gameSpinner = findViewById(R.id.gameSpinner);

        ArrayAdapter<CharSequence> gameAdapter = ArrayAdapter.createFromResource(createPost.this,
                R.array.ball_games_array, android.R.layout.simple_spinner_item);
        gameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameSpinner.setAdapter(gameAdapter);
//        gameSpinner.getSelectedView().setEnabled(false);
//        gameSpinner.setEnabled(false);


        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                gameSpinner.getSelectedView().setEnabled(true);
//                gameSpinner.setEnabled(true);
//                ArrayAdapter<CharSequence> gameAdapter = ArrayAdapter.createFromResource(createPost.this,
//                        R.array.ball_games_array, android.R.layout.simple_spinner_item);

                switch(position) {
                    case 0:
                        ArrayAdapter<CharSequence> gameBallAdapter = ArrayAdapter.createFromResource(createPost.this,
                                R.array.ball_games_array, android.R.layout.simple_spinner_item);
                        gameBallAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        gameSpinner.setAdapter(gameBallAdapter);
                        break;
                    case 1:
                        ArrayAdapter<CharSequence> gameVideoAdapter = ArrayAdapter.createFromResource(createPost.this,
                                R.array.video_games_array, android.R.layout.simple_spinner_item);
                        gameVideoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        gameSpinner.setAdapter(gameVideoAdapter);
                        break;
                    case 2:
                        ArrayAdapter<CharSequence> gameBoardAdapter = ArrayAdapter.createFromResource(createPost.this,
                                R.array.board_games_array, android.R.layout.simple_spinner_item);
                        gameBoardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        gameSpinner.setAdapter(gameBoardAdapter);
                        break;
                    case 3:
                        ArrayAdapter<CharSequence> gameWorkoutAdapter = ArrayAdapter.createFromResource(createPost.this,
                                R.array.workout_array, android.R.layout.simple_spinner_item);
                        gameWorkoutAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        gameSpinner.setAdapter(gameWorkoutAdapter);
                        break;
                }


//                gameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                gameSpinner.setAdapter(gameAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
