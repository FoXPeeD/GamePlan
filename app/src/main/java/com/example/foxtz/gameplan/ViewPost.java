package com.example.foxtz.gameplan;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ViewPost extends AppCompatActivity {

    TextView organizerText;
    TextView gameText;
    TextView categoryText;
    TextView dateText;
    TextView timeText;
    TextView cityText;
    TextView playersText;
    TextView descriptionText;
    Button joinButton;
    TextView goingText;

    boolean canJoin;
    boolean isHistory;
    boolean isHosting;
    String dateTimePath;
    String postID;
    int desiredNumPlayers;
    String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        organizerText = findViewById(R.id.organizerTextView);
        gameText = findViewById(R.id.gameTextView);
        categoryText = findViewById(R.id.categoryTextView);
        dateText = findViewById(R.id.dateTextView);
        timeText = findViewById(R.id.timeTextView);
        cityText = findViewById(R.id.cityTextView);
        playersText = findViewById(R.id.playersTextView);
        descriptionText = findViewById(R.id.descriptionTextView);
        goingText = findViewById(R.id.goingText);
        goingText.setTextSize(32);

        joinButton = findViewById(R.id.joinButton);
        joinButton.setText("loading");
        joinButton.setEnabled(false);
        joinButton.setVisibility(View.GONE);
        goingText.setVisibility(View.GONE);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //updating user's attending posts with new post
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference refPostAll = database.getReference("posts/" + dateTimePath + "/" + postID);
                DatabaseReference refPostUser = database.getReference("users/" + userID + "/attending/" + dateTimePath + "/" + postID);
                copyAtDB(refPostAll,refPostUser);

                //add userID to list of attending users
                //refPostAll.child("joined").child(userID).setValue(true);

                //update number of current players in original post (at DB) and on this activity
                final DatabaseReference refPlayersNum = refPostAll.child("currentNumPlayers");
                refPlayersNum.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int currentPlayers = Integer.valueOf(dataSnapshot.getValue().toString());
                        currentPlayers++;
                        refPlayersNum.setValue(currentPlayers);
                        joinButton.setEnabled(false);
                        joinButton.setText("going");
                        playersText.setText(currentPlayers + " out of " + desiredNumPlayers);
                        joinButton.setBackground(getDrawable(R.drawable.custom_button_pressed));
                        joinButton.setTextColor(getResources().getColor(R.color.pressedGrey));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });

        Post post = (Post) getIntent().getSerializableExtra("Post");
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        isHosting = post.getUserID().equals(userID);
        desiredNumPlayers = post.getDesiredNumPlayers();
        canJoin = getIntent().getBooleanExtra("canJoin",false);
        isHistory = getIntent().getBooleanExtra("isHistory",false);

        String date = String.valueOf(post.getYear()) + "/" + post.getMonth() + "/" +  String.format("%02d", post.getDay());
        String time = post.getTime();
        dateTimePath = date + "/" + time;
        postID = post.getId();
        showPostDataFromDB(post);
        setJoinButtonStatus(canJoin);
    }

    public void showPostDataFromDB(final Post post){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refPost = database.getReference("posts/" + dateTimePath + "/" + postID);
        refPost.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> postMap = (HashMap<String, Object>) dataSnapshot.getValue();
                String postID = dataSnapshot.getKey();
                Post fullPost = new Post(postMap.get("category").toString(), postMap.get("game").toString(), post.getHour(), post.getMinutes(),
                        post.getDay(), post.getMonth(), post.getYear(), postMap.get("city").toString(), postMap.get("userID").toString(),
                        postMap.get("currentNumPlayers").toString(), postMap.get("desiredNumPlayers").toString(),
                        postMap.get("description").toString(), postMap.get("user_name").toString(), postID);

                organizerText.setText(fullPost.getUserName());
                gameText.setText(fullPost.getGame());
                categoryText.setText(fullPost.getCategory());
                dateText.setText(fullPost.getDate());
                timeText.setText(fullPost.getTime());
                cityText.setText(fullPost.getCity());
                playersText.setText(fullPost.getCurrNumPlayers() + " out of " + fullPost.getDesiredNumPlayers());
                descriptionText.setText(fullPost.getDescription());
                showBackgroundImage(fullPost.getGame());

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void setJoinButtonStatus(final Boolean canJoin){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refPost = database.getReference("users/" + userID + "/attending/" + dateTimePath + "/" + postID);
        refPost.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(canJoin && !dataSnapshot.exists()){
                    joinButton.setText("Join");
                    joinButton.setEnabled(true);
                    joinButton.setVisibility(View.VISIBLE);
                } else {
                    joinButton.setEnabled(false);
                    joinButton.setVisibility(View.GONE); // View.INVISIBLE reserves space for the item

                    goingText.setVisibility(View.VISIBLE);
                    if(isHosting){
                        if(isHistory){
                            goingText.setText("You hosted this event");
                        } else {
                            goingText.setText("You are hosting this event");
                        }
                    } else {
                        if(isHistory){
                            goingText.setText("You attended this event");
                        } else {
                            goingText.setText("Attending");
                        }
                    }
                }
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
                            Toast.makeText(ViewPost.this, "Copy failed", Toast.LENGTH_SHORT).show();
//                            System.out.println("Copy failed");
                        } else {
//                            Toast.makeText(ViewPost.this, "Copy success", Toast.LENGTH_SHORT).show();
//                            System.out.println("Success");

                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void showBackgroundImage(String game){
        int drawable = R.drawable.bg;
        switch (game){
            case "Soccer":
                drawable = R.drawable.soccer_back;
                break;
            case "Football":
                drawable = R.drawable.football_back;
                break;
            case "Basketball":
                drawable = R.drawable.basketball_back;
                break;
            case "Foosball":
                drawable = R.drawable.foosball_back;
                break;
            case "Tennis":
                drawable = R.drawable.tennis_back;
                break;
            case "Catan":
                drawable = R.drawable.catan_back;
                break;
            case "Clue":
                drawable = R.drawable.clue_back;
                break;
            case "Risk":
                drawable = R.drawable.risk_back;
                break;
            case "Talisman":
                drawable = R.drawable.talisman_back;
                break;
            case "Monopoly":
                drawable = R.drawable.monopoly_back;
                break;
            case "Spinning":
                drawable = R.drawable.spinning_back;
                break;
            case "Zumba":
                drawable = R.drawable.zumba_back;
                break;
            case "Cycling":
                drawable = R.drawable.cycling_back;
                break;
            case "Yoga":
                drawable = R.drawable.yoga_back;
                break;
            case "Running":
                drawable = R.drawable.running_back;
                break;
            case "Overwatch":
                drawable = R.drawable.overwatch_back;
                break;
            case "League Of Legends":
                drawable = R.drawable.lol_back;
                break;
            case "Call Of Duty":
                drawable = R.drawable.cod_back;
                break;
            case "Fortnite":
                drawable = R.drawable.fortnite_back;
                break;
            case "Mario Kart":
                drawable = R.drawable.mario_back;
                break;
        }
        ImageView image = findViewById(R.id.backImage);
        image.setImageResource(drawable);
    }


}
