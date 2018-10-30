package com.example.foxtz.gameplan;

public class Post {

    private int hour,minutes;
    private int day, year;
    private String month;
    private String time, date;
    private String category,game;
    private String user;
    private String city;
    private String description;
    private int currNumPlayers, DesiredNumPlayers;

    public Post(String category, String game, int hour, int minutes, String day, String month,
                String year, String city, String user, String currNumPlayers, String DesiredNumPlayers, String description) {
        this.category = category;
        this.game = game;

        this.hour = hour;
        this.minutes = minutes;
        this.time = String.format("%02d", hour) + ":" + String.format("%02d", minutes);

        this.day = Integer.valueOf(day);
        this.month = month;
        this.year = Integer.valueOf(year);
        this.date = day + "/" + month + "/" + year;

        this.currNumPlayers = Integer.valueOf(currNumPlayers);
        this.DesiredNumPlayers = Integer.valueOf(DesiredNumPlayers);
        this.city = city;
        this.user = user;
        this.description = description;

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

    public String getUser() {
        return user;
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
