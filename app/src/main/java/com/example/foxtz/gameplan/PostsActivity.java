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

    PostFilter dummyFilter;

    TextView loadingText;
    MenuItem filterActionButton;
    int FILTER_REQ = 1;

    String userId;

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
                int toYear = filter.toYear;
                int toMonth = 12;
                int toDay = 31;
                int fromHour = filter.fromHour;
                int toHour = filter.toHour;
                int fromMinutes = filter.fromMinutes;
                int toMinutes = 45;
                for(int year = fromYear; year <= toYear ; year++) {
                    if(year == toYear){
                        toMonth = filter.toMonth;
                    }
                    for (int month = fromMonth; month <= toMonth; month++) {
                        if(month == toMonth && year == toYear){
                            toDay = filter.toDay;
                        }
                        for (int day = fromDay; day <= toDay; day++) {
                            for (int hour = fromHour; hour <= toHour; hour++) {
                                if(hour == toHour){
                                    toMinutes = filter.toMinutes;
                                }
                                for (int minute = fromMinutes; minute <= toMinutes; minute += 15) {
                                    String timeString = String.format("%02d", hour) + ":" + String.format("%02d", minute);
                                    String monthString = strings.getMonths()[month-1];
                                    Iterable<DataSnapshot> items = dataSnapshot
                                            .child(String.valueOf(year))
                                            .child(monthString)
                                            .child(String.format("%02d", day))
                                            .child(timeString)
                                            .getChildren();
                                    while(items.iterator().hasNext()) {
                                        DataSnapshot item = items.iterator().next();
                                        HashMap<String, Object> postMap = (HashMap<String, Object>) item.getValue();
                                        String postID = item.getKey();
                                        if(filter.filterFull &&
                                                (postMap.get("desiredNumPlayers").toString().equals(postMap.get("currentNumPlayers").toString()))){
                                            continue;
                                        }
                                        if(filter.filterCategory &&
                                                (!postMap.get("category").toString().equals(filter.category))){
                                            continue;
                                        }
                                        if(filter.filterGame &&
                                                (!postMap.get("game").toString().equals(filter.game))){
                                            continue;
                                        }
                                        if(filter.filterCity &&
                                                (!postMap.get("city").toString().equals(filter.city))){
                                            continue;
                                        }

                                        postsList.add(new Post(postMap.get("category").toString(), postMap.get("game").toString(), hour, minute,
                                                day,monthString,year,postMap.get("city").toString(),postMap.get("userID").toString(),
                                                postMap.get("currentNumPlayers").toString(),postMap.get("desiredNumPlayers").toString(),
                                                postMap.get("description").toString(),postMap.get("user_name").toString(),postID));

                                    }
                                }
                                fromMinutes = 0;
                            }
                        }
                        fromDay = 1;
                    }
                    fromMonth = 1;
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
    private void loadPostsAll(PostFilter originalFilter){
        try{
            PostFilter filter = (PostFilter) originalFilter.clone();
            filter.setFilterFull();
            loadPosts_internal("posts",filter);
        }
        catch (CloneNotSupportedException e) {}
    }
    private void loadPostsAttending(PostFilter filter){
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        loadPosts_internal("users/"+userId+"/attending",filter);
    }
    private void loadPostsCreated(PostFilter filter){
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        loadPosts_internal("users/"+userId+"/created",filter);
    }
    private void loadPostsHistory(PostFilter originalFilter){
        PostFilter filter = new PostFilter();
        if(originalFilter != null){
            try{
                filter = (PostFilter) originalFilter.clone();
            }
            catch (CloneNotSupportedException e) {}
        }
        filter.setCurrentDateAsEnd();
        filter.setStartDateEarlierThanNowByMonths(3);
        filter.setTimeAsAllDay();
        loadPosts_internal("users/"+userId+"/attending",filter);
    }
    private void loadPostsRecommended(PostFilter originalFilter){
        try{
            PostFilter filter = (PostFilter) originalFilter.clone();
            filter.setFilterFull();
            loadPosts_internal("users/"+userId+"/recommended",filter);
        }
        catch (CloneNotSupportedException e) {}
    }
    private void loadPostsCurrentTab(PostFilter filter){
        switch (currentTab){
            case R.id.navigation_all:
                loadPostsAll(filter);
                return;
            case R.id.navigation_attending:
                loadPostsAttending(filter);
                return;
            case R.id.navigation_created:
                loadPostsCreated(filter);
                return;
            case R.id.navigation_history:
                loadPostsHistory(null);
                return;
            case R.id.navigation_recommended:
                loadPostsRecommended(filter);
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
                    invalidateOptionsMenu();
                    loadPostsAll(dummyFilter);
                    return true;
                case R.id.navigation_attending:
                    currentTab = R.id.navigation_attending;
                    invalidateOptionsMenu();
                    loadPostsAttending(dummyFilter);
                    return true;
                case R.id.navigation_created:
                    currentTab = R.id.navigation_created;
                    invalidateOptionsMenu();
                    loadPostsCreated(dummyFilter);
                    return true;
                case R.id.navigation_history:
                    currentTab = R.id.navigation_history;
                    invalidateOptionsMenu();
                    loadPostsHistory(dummyFilter);
                    return true;
                case R.id.navigation_recommended:
                    currentTab = R.id.navigation_recommended;
                    invalidateOptionsMenu();
                    loadPostsRecommended(dummyFilter);
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
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostsActivity.this, createPost.class);
                startActivity(intent);
            }
        });


        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        filterActionButton = findViewById(R.id.action_filter);

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

                if(currentTab == R.id.navigation_all && !post.getUserID().equals(userId)){
                    intent.putExtra("canJoin", true);
                } else {
                    intent.putExtra("canJoin", false);
                }
                if(currentTab == R.id.navigation_history ){
                    intent.putExtra("isHistory", true);
                } else {
                    intent.putExtra("isHistory", false);
                }
                startActivity(intent);
            }
        }));

        //TODO: change dummy filter
        dummyFilter = new PostFilter();
        dummyFilter.setCurrentDateAsStart();
        dummyFilter.setEndDateLaterThanNowByMonths(3);
        dummyFilter.setTimeAsAllDay();

    }

    @Override
    public void onStart() {
        super.onStart();
        getSupportActionBar().setTitle("Posts");
        loadPostsCurrentTab(dummyFilter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILTER_REQ) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                PostFilter filter =
                        (PostFilter)bundle.getSerializable("filter");
                loadPostsAll(filter);
                return;
            }
        }
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
        if(currentTab == R.id.navigation_all){
            menu.findItem(R.id.action_filter).setVisible(true);
        } else {
            menu.findItem(R.id.action_filter).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                Intent intent = new Intent(PostsActivity.this,filterActivity.class);
                startActivityForResult(intent,FILTER_REQ);
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
