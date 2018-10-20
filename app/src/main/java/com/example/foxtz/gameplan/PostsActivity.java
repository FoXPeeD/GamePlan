package com.example.foxtz.gameplan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.List;

public class PostsActivity extends AppCompatActivity {

    private List<Post> postsList = new ArrayList<>();

//    private FirebaseAuth mAuth;
    FirebaseUser user;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        Button create = findViewById(R.id.createPostButton);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostsActivity.this, createPost.class);
                intent.putExtra("key", "value");
                startActivity(intent);
            }

        });
        mTextMessage = findViewById(R.id.message);
        mTextMessage.setText("...NOT!");

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //temp manual list
        Post post1 = new Post("birds","cock","11:33");
        Post post2 = new Post("birds","booby","17:83");
        Post post3 = new Post("mammals","ass","71:00");
        postsList.add(post1);
        postsList.add(post2);
        postsList.add(post3);

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
            public void onClick(View view, int position) {
                Post post = postsList.get(position);
                Toast.makeText(getApplicationContext(), post.getGame() + " is selected!", Toast.LENGTH_SHORT).show();
            }

//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
        }));
    }

    @Override
    public void onStart() {
        super.onStart();
        getSupportActionBar().setTitle("Posts");


//        user = FirebaseAuth.getInstance().getCurrentUser();
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("users/"+user.getUid());
//        DatabaseReference myName = myRef.child("name");
//        myName.setValue("Ali G");
//        myName.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d("read DB", "Value is: " + value);
//                mTextMessage.setText(value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w("read DB", "Failed to read value.", error.toException());
//            }
//        });
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
}
