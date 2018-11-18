package com.example.foxtz.gameplan;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.MyViewHolder> {

    private List<Post> postsList;   //add final?


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView game, time, category, date;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            game = view.findViewById(R.id.game);
            category = view.findViewById(R.id.category);
            time = view.findViewById(R.id.time);
            date = view.findViewById(R.id.date);
            image = view.findViewById(R.id.image);
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
        int drawable = R.drawable.lol;
        switch (post.getGame()){
            case "Soccer":
                drawable = R.drawable.soccer;
                break;
            case "Football":
                drawable = R.drawable.football;
                break;
            case "Basketball":
                drawable = R.drawable.basketball;
                break;
            case "Foosball":
                drawable = R.drawable.foosball;
                break;
            case "Tennis":
                drawable = R.drawable.tennis;
                break;
            case "Catan":
                drawable = R.drawable.catan;
                break;
            case "Clue":
                drawable = R.drawable.clue;
                break;
            case "Risk":
                drawable = R.drawable.risk;
                break;
            case "Talisman":
                drawable = R.drawable.talisman;
                break;
            case "Monopoly":
                drawable = R.drawable.monopoly;
                break;
            case "Spinning":
                drawable = R.drawable.spinning;
                break;
            case "Zumba":
                drawable = R.drawable.zumba;
                break;
            case "Cycling":
                drawable = R.drawable.cycling;
                break;
            case "Yoga":
                drawable = R.drawable.yoga;
                break;
            case "Running":
                drawable = R.drawable.run;
                break;
            case "Overwatch":
                drawable = R.drawable.overwatch;
                break;
            case "League Of Legends":
                drawable = R.drawable.lol;
                break;
            case "Call Of Duty":
                drawable = R.drawable.cod;
                break;
            case "Fortnite":
                drawable = R.drawable.fortnite;
                break;
            case "Mario Kart":
                drawable = R.drawable.mario;
                break;
            default:
                drawable = R.drawable.bg;
                break;
        }
        holder.image.setImageResource(drawable);
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }
}
