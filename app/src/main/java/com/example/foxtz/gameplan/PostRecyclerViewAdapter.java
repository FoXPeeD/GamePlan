package com.example.foxtz.gameplan;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.MyViewHolder> {

    private List<Post> postsList;   //add final?


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView game, time, category, date;

        public MyViewHolder(View view) {
            super(view);
            game = view.findViewById(R.id.game);
            category = view.findViewById(R.id.category);
            time = view.findViewById(R.id.time);
            date = view.findViewById(R.id.date);
        }
    }

    public PostRecyclerViewAdapter(List<Post> postsList) {
        this.postsList = postsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Post post = postsList.get(position);
        holder.game.setText(post.getGame());
        holder.category.setText(post.getCategory());
        holder.time.setText(String.valueOf(post.getTime()));
        holder.date.setText(String.valueOf(post.getDate()));
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }
}
