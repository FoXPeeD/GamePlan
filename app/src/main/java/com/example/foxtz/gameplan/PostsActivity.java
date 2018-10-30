package com.example.foxtz.gameplan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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

//    private FirebaseAuth mAuth;
    FirebaseUser user;

    private void loadPosts(){
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
        DatabaseReference refPosts = database.getReference("posts");
        refPosts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String time = dataSnapshot.getKey();
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
                                        postsList.add(new Post(postMap.get("category").toString(), postMap.get("game").toString(), time));

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
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
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

        Button create = findViewById(R.id.createPostButton);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostsActivity.this, createPost.class);
                intent.putExtra("key", "value");
                startActivity(intent);
            }

        });

//        Toolbar myToolbar = findViewById(R.id.posts_action_bar);
//        setSupportActionBar(myToolbar);

        mTextMessage = findViewById(R.id.message);
        mTextMessage.setText("...NOT!");

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //temp manual list
//        Post post1 = new Post("birds","cock","11:33");
//        Post post2 = new Post("birds","booby","17:83");
//        Post post3 = new Post("mammals","ass","71:00");
//        postsList.add(post1);
//        postsList.add(post2);
//        postsList.add(post3);
//        StringsClass strings = new StringsClass();
//        for(int year = 2018; year < 2020 ; year++) {
//            for (int month = 0; month < 12; month++) {
//                for (int day = 1; day<=31; day++) {
////                    String day = "1";
////                    String month = "Jan";
////                    String year = "2018";
//                    final String hour = "00";
//                    final String minute = "00";
//
//                    String dateString = day + "-" + strings.getMonths()[month] + "-" + year;
//                    final String timeString = hour + ":" + minute;
//                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
//                    DatabaseReference refTime = database.getReference("posts/" + dateString + "/" + timeString);
//                    refTime.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();
//                            while (items.hasNext()) {
//                                DataSnapshot item = items.next();
//                                HashMap<String, Object> postMap = (HashMap<String, Object>) item.getValue();
//                                postsList.add(new Post(postMap.get("category").toString(), postMap.get("game").toString(), timeString));
//
//                                postsRecyclerView = findViewById(R.id.postsRecyclerView);
//                                postsRecyclerView.setHasFixedSize(true);
//                                postsLayoutManager = new LinearLayoutManager(PostsActivity.this);
//                                postsRecyclerView.setLayoutManager(postsLayoutManager);
//                                postsRecyclerViewAdapter = new PostRecyclerViewAdapter(postsList);
//                                postsRecyclerView.addItemDecoration(new DividerItemDecoration(PostsActivity.this, LinearLayoutManager.VERTICAL));
//                                postsRecyclerView.setAdapter(postsRecyclerViewAdapter);
//                                postsRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), postsRecyclerView, new RecyclerTouchListener.ClickListener() {
//                                    @Override
//                                    public void onClick(View view, int position) {
//                                        Post post = postsList.get(position);
//                                        Toast.makeText(getApplicationContext(), post.getGame() + " is selected!", Toast.LENGTH_SHORT).show();
//                                    }
//                                }));
//                            }
//
////                String str = dataSnapshot.getValue().getClass().toString();
////                StringsClass strings = new StringsClass();
////                mTextMessage.setText(strings.getMonths()[1]);//HashMap<String, Object>
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//            }
//        }


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

        refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPosts();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getSupportActionBar().setTitle("Posts");
        loadPosts();
    }

//
//    public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.MyViewHolder> {
//        private String[] mDataset;
//
//        // Provide a reference to the views for each data item
//        // Complex data items may need more than one view per item, and
//        // you provide access to all the views for a data item in a view holder
//        public static class MyViewHolder extends RecyclerView.ViewHolder {
//            // each data item is just a string in this case
//            public TextView mTextView;
//            public MyViewHolder(TextView v) {
//                super(v);
//                mTextView = v;
//            }
//        }
//
//        // Provide a suitable constructor (depends on the kind of dataset)
//        public PostsListAdapter(String[] myDataset) {
//            mDataset = myDataset;
//        }
//
//        // Create new views (invoked by the layout manager)
//        @Override
//        public PostsListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
//                                                         int viewType) {
//            // create a new view
//            TextView v = (TextView) LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.my_text_view, parent, false);
//
//            MyViewHolder vh = new MyViewHolder(v);
//            return vh;
//        }
//
//        // Replace the contents of a view (invoked by the layout manager)
//        @Override
//        public void onBindViewHolder(MyViewHolder holder, int position) {
//            // - get element from your dataset at this position
//            // - replace the contents of the view with that element
//            holder.mTextView.setText(mDataset[position]);
//
//        }
//
//        // Return the size of your dataset (invoked by the layout manager)
//        @Override
//        public int getItemCount() {
//            return mDataset.length;
//        }
//    }


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
        return true;
    }
}
