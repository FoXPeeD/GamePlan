package com.example.foxtz.gameplan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class createPost extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        getSupportActionBar().setTitle("New Post");

        //category spinner init
        final Spinner categorySpinner = findViewById(R.id.categorySpinner);
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

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //year spinner init
        Spinner yearSpinner = findViewById(R.id.yearSpinner);
        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(createPost.this,
                R.array.years_array, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        //month spinner init

        Spinner monthSpinner = findViewById(R.id.monthSpinner);
        ArrayAdapter<CharSequence> monthsAdapter = ArrayAdapter.createFromResource(this,
                R.array.months_array, android.R.layout.simple_spinner_item);
        monthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthsAdapter);

        //day spinner init
        final Spinner daySpinner = findViewById(R.id.daySpinner);

        ArrayAdapter<CharSequence> daysAdapter = ArrayAdapter.createFromResource(createPost.this,
                R.array.day_31_array, android.R.layout.simple_spinner_item);
        daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(daysAdapter);

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position) {
                    case 0:
                    case 2:
                    case 4:
                    case 6:
                    case 7:
                    case 9:
                    case 11:
                        ArrayAdapter<CharSequence> days31Adapter = ArrayAdapter.createFromResource(createPost.this,
                                R.array.day_31_array, android.R.layout.simple_spinner_item);
                        days31Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        daySpinner.setAdapter(days31Adapter);
                        break;
                    case 3:
                    case 5:
                    case 8:
                    case 10:
                        ArrayAdapter<CharSequence> days30Adapter = ArrayAdapter.createFromResource(createPost.this,
                                R.array.day_30_array, android.R.layout.simple_spinner_item);
                        days30Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        daySpinner.setAdapter(days30Adapter);
                        break;
                    case 1:
                        ArrayAdapter<CharSequence> days28Adapter = ArrayAdapter.createFromResource(createPost.this,
                                R.array.day_28_array, android.R.layout.simple_spinner_item);
                        days28Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        daySpinner.setAdapter(days28Adapter);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = categorySpinner.getItemAtPosition(categorySpinner.getSelectedItemPosition()).toString();
                String game = gameSpinner.getItemAtPosition(gameSpinner.getSelectedItemPosition()).toString();
//                Toast.makeText(createPost.this, "category is " + category, Toast.LENGTH_SHORT).show();
                String timeString = "once upon a time";
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference refTime = database.getReference("posts/"+timeString);
                String postKey = refTime.push().getKey();
                DatabaseReference refPost = refTime.child(postKey);
                refPost.child("category").setValue(category);
                refPost.child("game").setValue(game);
            }
        });
    }
}
