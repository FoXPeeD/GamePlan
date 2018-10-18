package com.example.foxtz.gameplan;

public class Post {

    private int hour,minutes;
    private int day, year;
    private String time, date;
    private String month;
    private String category,game;
    private String user;
    private String city;
    private String description;

    //TODO: real constractor
    public Post(String category, String game, String time) {
        this.category = category;
        this.game = game;
        this.time = time;

    }


    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public int getHour() {
        return hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getDay() {
        return day;
    }

    public int getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getCategory() {
        return category;
    }

    public String getGame() {
        return game;
    }

    public String getUser() {
        return user;
    }

    public String getCity() {
        return city;
    }

    public String getDescription() {
        return description;
    }


}
