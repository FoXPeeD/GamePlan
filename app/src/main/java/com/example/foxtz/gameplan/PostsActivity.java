package com.example.foxtz.gameplan;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import com.example.foxtz.gameplan.StringsClass;

public class PostsActivity extends AppCompatActivity {

    TextView loadingText;
    private List<Post> postsList = new ArrayList<>();
    private int currentTab = R.string.title_all;

//    private FirebaseAuth mAuth;
    FirebaseUser user;

    private void loadPosts_internal(String pathToPosts){
        loadingText.setText("Loading...");

        postsList.clear();
        postsRecyclerView = findViewById(R.id.postsRecyclerView);
        postsRecyclerView.setHasFixedSize(true);
        postsLayoutManager = new LinearLayoutManager(this);
        postsRecyclerView.setLayoutManager(postsLayoutManager);
        postsRecyclerViewAdapter = new PostRecyclerViewAdapter(postsList);
        postsRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        postsRecyclerView.setAdapter(postsRecyclerViewAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refPosts = database.getReference(pathToPosts);
        refPosts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StringsClass strings = new StringsClass();
                for(int year = 2018; year <= 2018 ; year++) {
                    for (int month = 0; month < 3; month++) {
                        for (int day = 1; day <= 2; day++) {
                            for (int hour = 0; hour <= 23; hour++) {
                                for (int minute = 0; minute <= 45; minute += 15) {
                                    String timeString = String.format("%02d", hour) + ":" + String.format("%02d", minute);
                                    Iterable<DataSnapshot> items = dataSnapshot.child(String.valueOf(year)).child(strings.getMonths()[month]).child(String.valueOf(day)).child(timeString).getChildren();
                                    while(items.iterator().hasNext()) {
                                        DataSnapshot item = items.iterator().next();
                                        HashMap<String, Object> postMap = (HashMap<String, Object>) item.getValue();
                                        postsList.add(new Post(postMap.get("category").toString(), postMap.get("game").toString(), timeString));

                                    }
                                }
                            }
                        }
                    }
                }
                postsRecyclerView = findViewById(R.id.postsRecyclerView);
                postsRecyclerView.setHasFixedSize(true);
                postsLayoutManager = new LinearLayoutManager(PostsActivity.this);
                postsRecyclerView.setLayoutManager(postsLayoutManager);
                postsRecyclerViewAdapter = new PostRecyclerViewAdapter(postsList);
                postsRecyclerView.addItemDecoration(new DividerItemDecoration(PostsActivity.this, LinearLayoutManager.VERTICAL));
                postsRecyclerView.setAdapter(postsRecyclerViewAdapter);
                postsRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), postsRecyclerView, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Post post = postsList.get(position);
                        Toast.makeText(getApplicationContext(), post.getGame() + " is selected!", Toast.LENGTH_SHORT).show();
                    }
                }));
                if(postsList.size() == 0){
                    loadingText.setText("Nothing to show :(");
                } else {
                    loadingText.setText("");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void loadPostsAll(){
        loadPosts_internal("posts");
    }
    private void loadPostsAttending(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        loadPosts_internal("users/"+userId+"/attending");
    }
    private void loadPostsCreated(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        loadPosts_internal("users/"+userId+"/created");
    }
    private void loadPostsHistory(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        loadPosts_internal("users/"+userId+"/history");
    }
    private void loadPostsRecommended(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        loadPosts_internal("users/"+userId+"/recommended");
    }
    private void loadPostsCurrentTab(){
        switch (currentTab){
            case R.id.navigation_all:
                loadPostsAll();
                return;
            case R.id.navigation_attending:
                loadPostsAttending();
                return;
            case R.id.navigation_created:
                loadPostsCreated();
                return;
            case R.id.navigation_history:
                loadPostsHistory();
                return;
            case R.id.navigation_recommended:
                loadPostsRecommended();
                return;
            default:
                Toast.makeText(getApplicationContext(), "no current tab set", Toast.LENGTH_SHORT).show();
        }
    }
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_all:
                    mTextMessage.setText(R.string.title_all);
                    currentTab = R.id.navigation_all;
                    loadPostsAll();
                    return true;
                case R.id.navigation_attending:
                    mTextMessage.setText(R.string.title_attending);
                    currentTab = R.id.navigation_attending;
                    loadPostsAttending();
                    return true;
                case R.id.navigation_created:
                    mTextMessage.setText(R.string.title_created_events);
                    currentTab = R.id.navigation_created;
                    loadPostsCreated();
                    return true;
                case R.id.navigation_history:
                    mTextMessage.setText(R.string.title_history);
                    currentTab = R.id.navigation_history;
                    loadPostsHistory();
                    return true;
                case R.id.navigation_recommended:
                    mTextMessage.setText(R.string.title_history);
                    currentTab = R.id.navigation_recommended;
                    loadPostsHistory();
                    return true;
                default:
                    Toast.makeText(getApplicationContext(), "unexpected tab", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    };

    private RecyclerView postsRecyclerView;
    private RecyclerView.Adapter postsRecyclerViewAdapter;
    private RecyclerView.LayoutManager postsLayoutManager;

    private Button refreshButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        loadingText = findViewById(R.id.loadingTextView);
        loadingText.setTextSize(24);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostsActivity.this, createPost.class);
                startActivity(intent);
            }
        });

        mTextMessage = findViewById(R.id.message);
        mTextMessage.setText("...NOT!");

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        currentTab = R.id.navigation_all;

        //recyclerView
        postsRecyclerView = findViewById(R.id.postsRecyclerView);
        postsRecyclerView.setHasFixedSize(true);
        postsLayoutManager = new LinearLayoutManager(this);
        postsRecyclerView.setLayoutManager(postsLayoutManager);
        postsRecyclerViewAdapter = new PostRecyclerViewAdapter(postsList);
        postsRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        postsRecyclerView.setAdapter(postsRecyclerViewAdapter);

        postsRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), postsRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick (View view, int position) {
                Post post = postsList.get(position);
                Toast.makeText(getApplicationContext(), post.getGame() + " is selected!", Toast.LENGTH_SHORT).show();
            }

//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
        }));

//        refreshButton = findViewById(R.id.refreshButton);
//        refreshButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                loadPostsCurrentTab();
//            }
//        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getSupportActionBar().setTitle("Posts");
        loadPostsCurrentTab();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.posts_action_bar, menu);
//        return true;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.posts_action_bar, menu);
        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.action_bar_icons), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                //TODO: call intent and update posts accordingly

                return true;

            case R.id.action_reload:
                loadPostsCurrentTab();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


}
