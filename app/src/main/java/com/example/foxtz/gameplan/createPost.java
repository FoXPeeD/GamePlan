package com.example.foxtz.gameplan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

public class createPost extends AppCompatActivity {

    String userName = "N/A";
    int age = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

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
        final Spinner yearSpinner = findViewById(R.id.yearSpinner);
        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(createPost.this,
                R.array.years_array, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        //month spinner init

        final Spinner monthSpinner = findViewById(R.id.monthSpinner);
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

        //hour spinner init
        final Spinner hourSpinner = findViewById(R.id.hourSpinner);
        ArrayAdapter<CharSequence> hourAdapter = ArrayAdapter.createFromResource(this,
                R.array.hour_array, android.R.layout.simple_spinner_item);
        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hourSpinner.setAdapter(hourAdapter);

        //minute spinner init
        final Spinner minuteSpinner = findViewById(R.id.minuteSpinner);
        ArrayAdapter<CharSequence> minuteAdapter = ArrayAdapter.createFromResource(this,
                R.array.minute_array, android.R.layout.simple_spinner_item);
        minuteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minuteSpinner.setAdapter(minuteAdapter);

        //city spinner init
        final Spinner citySpinner = findViewById(R.id.citySpinner);
        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(this,
                R.array.city_array, android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);


        final EditText currentNumPlayersText = findViewById(R.id.currentPlayersEditTxt);
        final EditText desiredNumPlayersText = findViewById(R.id.desiredPlayersEditTxt);

//
        final EditText descriptionText = findViewById(R.id.descriptionEditText);

        Button createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //verify all text fields are not empty
                if (currentNumPlayersText.getText().toString().matches("") ||
                        desiredNumPlayersText.getText().toString().matches("") ||
                        descriptionText.getText().toString().matches("")){
                    Toast.makeText(createPost.this, "Some of the fields are empty",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                //get info
                String category = categorySpinner.getItemAtPosition(categorySpinner.getSelectedItemPosition()).toString();
                String game = gameSpinner.getItemAtPosition(gameSpinner.getSelectedItemPosition()).toString();
                String city = citySpinner.getItemAtPosition(citySpinner.getSelectedItemPosition()).toString();
                String day = daySpinner.getItemAtPosition(daySpinner.getSelectedItemPosition()).toString();
                String month = monthSpinner.getItemAtPosition(monthSpinner.getSelectedItemPosition()).toString();
                String year = yearSpinner.getItemAtPosition(yearSpinner.getSelectedItemPosition()).toString();
                String hour = hourSpinner.getItemAtPosition(hourSpinner.getSelectedItemPosition()).toString();
                String minute = minuteSpinner.getItemAtPosition(minuteSpinner.getSelectedItemPosition()).toString();
                String dateString = day+"-"+month+"-"+year;
                String timeString = hour+":"+minute;
                int currentNumPlayers = Integer.parseInt(currentNumPlayersText.getText().toString());
                int desiredNumPlayers = Integer.parseInt(desiredNumPlayersText.getText().toString());
                String description = descriptionText.getText().toString();

                //verify all fields are valid
                if(currentNumPlayers <= 0){
                    Toast.makeText(createPost.this, "Current number of players" +
                                    " need to be at least 1 (including you)",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if(desiredNumPlayers <= currentNumPlayers){
                    Toast.makeText(createPost.this, "Desired number of players" +
                                    " need to be less than current number of players",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                DateTime today = new org.joda.time.DateTime();
                int thisYear = today.getYear();
                int thisMonth = today.getMonthOfYear();
                int thisDay = today.getDayOfMonth();
                StringsClass strings = new StringsClass();
                String monthsList[] = strings.getMonths();
                int monthInt = 0;
                for (int i = 0; i < monthsList.length; i++){
                    if(monthsList[i].equals(month)){
                        monthInt = i+1;
                    }
                }
                if((thisYear >= Integer.valueOf(year)) && (thisMonth >= monthInt) && (thisDay > Integer.valueOf(day))){
                    Toast.makeText(createPost.this, "Invalid date",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                //prepare database reference
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                String leadingZeroDay = String.format("%02d", Integer.valueOf(day));
                String dateTimeString = year+ "/" + month + "/" + leadingZeroDay + "/" + timeString;
                DatabaseReference refTime = database.getReference("posts/" + dateTimeString);

                String postKey = refTime.push().getKey(); //new empty post is created here
                DatabaseReference refPost = refTime.child(postKey);

                //insert data to new post
                refPost.child("category").setValue(category);
                refPost.child("game").setValue(game);
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                refPost.child("userID").setValue(userId);
                refPost.child("city").setValue(city);
                refPost.child("desiredNumPlayers").setValue(desiredNumPlayers);
                refPost.child("currentNumPlayers").setValue(currentNumPlayers);
                refPost.child("description").setValue(description);
                refPost.child("user_name").setValue(userName);
                refPost.child("host_age").setValue(age);

                //add post user as created
                DatabaseReference refUser = database.getReference("users/"+userId);
                DatabaseReference refCreatedTime = refUser.child("created/" + dateTimeString);
                DatabaseReference refCreatedPost = refCreatedTime.child(postKey);
                copyAtDB(refPost,refCreatedPost);

                //add post user as attending
                DatabaseReference refAttendingTime = refUser.child("attending/" + dateTimeString);
                DatabaseReference refAttendingPost = refAttendingTime.child(postKey);
                copyAtDB(refPost,refAttendingPost);

                Toast.makeText(createPost.this, "post created", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refName = database.getReference("users/" + userId);
        refName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                userName = dataSnapshot.getValue().toString();
                userName = dataSnapshot.child("user_name").getValue().toString();
                age = Integer.valueOf(dataSnapshot.child("age").getValue().toString());
//                Toast.makeText(createPost.this, "user name is " + userName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void copyAtDB(final DatabaseReference fromPath, final DatabaseReference toPath) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                        if (firebaseError != null) {
                            Toast.makeText(createPost.this, "Copy failed", Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(createPost.this, "Copy success", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}


