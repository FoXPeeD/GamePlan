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

    PostFilter dummyFilter = new PostFilter();

    TextView loadingText;

    private List<Post> postsList = new ArrayList<>();
    private int currentTab = R.string.title_all;

//    private FirebaseAuth mAuth;
    FirebaseUser user;

    private void loadPosts_internal(String pathToPosts, final PostFilter filter){
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
                int fromYear = filter.fromYear;
                int fromMonth = filter.fromMonth;
                int fromDay = filter.fromDay;
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

                                        postsList.add(new Post(postMap.get("category").toString(), postMap.get("game").toString(), hour, minute,
                                                day,strings.getMonths()[month],year,postMap.get("city").toString(),postMap.get("userID").toString(),
                                                postMap.get("currentNumPlayers").toString(),postMap.get("desiredNumPlayers").toString(),
                                                postMap.get("description").toString(),postMap.get("user_name").toString()));

                                    }
                                }
                            }
                        }
                    }
                }
                postsRecyclerViewAdapter = new PostRecyclerViewAdapter(postsList);
                postsRecyclerView.setAdapter(postsRecyclerViewAdapter);
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
    private void loadPostsAll(PostFilter filter){
        loadPosts_internal("posts",dummyFilter);
    }
    private void loadPostsAttending(PostFilter filter){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        loadPosts_internal("users/"+userId+"/attending",dummyFilter);
    }
    private void loadPostsCreated(PostFilter filter){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        loadPosts_internal("users/"+userId+"/created",dummyFilter);
    }
    private void loadPostsHistory(PostFilter filter){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        loadPosts_internal("users/"+userId+"/history",dummyFilter);
    }
    private void loadPostsRecommended(PostFilter filter){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        loadPosts_internal("users/"+userId+"/recommended",dummyFilter);
    }
    private void loadPostsCurrentTab(PostFilter filter){
        switch (currentTab){
            case R.id.navigation_all:
                loadPostsAll(dummyFilter);
                return;
            case R.id.navigation_attending:
                loadPostsAttending(dummyFilter);
                return;
            case R.id.navigation_created:
                loadPostsCreated(dummyFilter);
                return;
            case R.id.navigation_history:
                loadPostsHistory(dummyFilter);
                return;
            case R.id.navigation_recommended:
                loadPostsRecommended(dummyFilter);
                return;
            default:
                Toast.makeText(getApplicationContext(), "no current tab set", Toast.LENGTH_SHORT).show();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_all:
                    currentTab = R.id.navigation_all;
                    loadPostsAll(dummyFilter);
                    return true;
                case R.id.navigation_attending:
                    currentTab = R.id.navigation_attending;
                    loadPostsAttending(dummyFilter);
                    return true;
                case R.id.navigation_created:
                    currentTab = R.id.navigation_created;
                    loadPostsCreated(dummyFilter);
                    return true;
                case R.id.navigation_history:
                    currentTab = R.id.navigation_history;
                    loadPostsHistory(dummyFilter);
                    return true;
                case R.id.navigation_recommended:
                    currentTab = R.id.navigation_recommended;
                    loadPostsHistory(dummyFilter);
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
            public void onClick(View view, int position) {
                Post post = postsList.get(position);
                Intent intent = new Intent(PostsActivity.this, ViewPost.class);
                intent.putExtra("Post", post);
                startActivity(intent);
//                        Toast.makeText(getApplicationContext(), post.getGame() + " is selected!", Toast.LENGTH_SHORT).show();
            }
        }));

    }

    @Override
    public void onStart() {
        super.onStart();
        getSupportActionBar().setTitle("Posts");
        loadPostsCurrentTab(dummyFilter);
    }



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
                loadPostsCurrentTab(dummyFilter);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


}
