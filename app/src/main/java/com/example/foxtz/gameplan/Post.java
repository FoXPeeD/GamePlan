package com.example.foxtz.gameplan;

import java.io.Serializable;

public class Post implements Serializable {

    private int hour,minutes;
    private int day, year;
    private String month;
    private String time, date;
    private String category,game;
    private String userID;
    private String city;
    private String description;
    private String userName;
    private int currNumPlayers, DesiredNumPlayers;

    public Post(String category, String game, int hour, int minutes, int day, String month,
                int year, String city, String userID, String currNumPlayers, String DesiredNumPlayers,
                String description, String userName) {
        this.category = category;
        this.game = game;

        this.hour = hour;
        this.minutes = minutes;
        this.time = String.format("%02d", hour) + ":" + String.format("%02d", minutes);

        this.day = day;
        this.month = month;
        this.year = Integer.valueOf(year);
        this.date = String.format("%02d", day) + "/" + month + "/" + year;

        this.currNumPlayers = Integer.valueOf(currNumPlayers);
        this.DesiredNumPlayers = Integer.valueOf(DesiredNumPlayers);
        this.city = city;
        this.userID = userID;
        this.description = description;
        this.userName = userName;

    }

    //temporary c'tor
    public Post(String category, String game, int hour, int minutes) {
        this.category = category;
        this.game = game;

        this.hour = hour;
        this.minutes = minutes;
        this.time = String.format("%02d", hour) + ":" + String.format("%02d", minutes);
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

    public String getUserID() {
        return userID;
    }

    public String getCity() {
        return city;
    }

    public String getDescription() {
        return description;
    }

    public int getCurrNumPlayers() {
        return currNumPlayers;
    }

    public int getDesiredNumPlayers() {
        return DesiredNumPlayers;
    }
}
