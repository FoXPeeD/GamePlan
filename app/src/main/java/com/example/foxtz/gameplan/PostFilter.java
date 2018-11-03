package com.example.foxtz.gameplan;


import org.joda.time.DateTime;

public class PostFilter implements java.lang.Cloneable{

    final int MIN_MINUTES = 0;
    final int MAX_MINUTES = 45;
    final int MIN_HOUR = 0;
    final int MAX_HOUR = 23;



    public PostFilter(){//TODO: create real c'tor
        this.fromYear = 2018;
        this.fromMonth = 1;//valid values are 1 to 12
        this.fromDay = 1;

        this.toYear = 2018;
        this.toMonth = 3;//valid values are 1 to 12
        this.toDay = 2;

        this.fromHour = MIN_HOUR;
        this.toHour = MAX_HOUR;
        this.fromMinutes = MIN_MINUTES;
        this.toMinutes = MAX_MINUTES;

        this.filterFull = false;
        this.filterCategory = false;
        this.filterGame = false;
        this.filterCity = false;
    }

    public void setCurrentDateAsStart(){
        this.fromDay = getCurrDay();
        this.fromMonth = getCurrMonth();
        this.fromYear = getCurrYear();
    }
    public void setCurrentDateAsEnd(){
        this.toDay = getCurrDay();
        this.toMonth = getCurrMonth();
        this.toYear = getCurrYear();
    }
    public void setDateAsStart(int day, int month, int year){
        this.fromDay = day;
        this.fromMonth = month;
        this.fromYear = year;
    }
    public void setDateAsEnd(int day, int month, int year){
        this.toDay = day;
        this.toMonth = month;
        this.toYear = year;
    }

    public void setStartDateEarlierThanNowByMonths(int months){
        DateTime now = new org.joda.time.DateTime();
        DateTime past = now.minusMonths(months);
        this.fromYear = past.getYear();
        this.fromMonth = past.getMonthOfYear();
        this.fromDay = past.getDayOfMonth();
    }

    public void setEndDateLaterThanNowByMonths(int months){
        DateTime now = new org.joda.time.DateTime();
        DateTime future = now.plusMonths(months);
        this.toYear = future.getYear();
        this.toMonth = future.getMonthOfYear();
        this.toDay = future.getDayOfMonth();
    }

    public void setTimeAsAllDay(){
        this.fromHour = MIN_HOUR;
        this.toHour = MAX_HOUR;
        this.fromMinutes = MIN_MINUTES;
        this.toMinutes = MAX_MINUTES;
    }

    public void setTimeInterval(int startHour, int startMinute, int endHour, int endMinutes){
        this.fromHour = startHour;
        this.toHour = endHour;
        this.fromMinutes = startMinute;
        this.toMinutes = endMinutes;
    }

    public void setCategoryFilter(String category){
        this.filterCategory = true;
        this.category = category;
    }

    public void setGameFilter(String category, String game){
        setCategoryFilter(category);
        this.filterGame = true;
        this.game = game;
    }

    public void setFilterFull(){
        this.filterFull = true;
    }

    public void setCityFilter(String city){
        this.filterCity = true;
        this.city = city;
    }

    private int getCurrDay(){
        DateTime now = new org.joda.time.DateTime();
        return now.getDayOfMonth();
    }
    private int getCurrMonth(){
        DateTime now = new org.joda.time.DateTime();
        return now.getMonthOfYear();
    }
    private int getCurrYear(){
        DateTime now = new org.joda.time.DateTime();
        return now.getYear();
    }
    int fromYear, fromMonth, fromDay;
    int toYear, toMonth, toDay;
    int fromHour, fromMinutes;
    int toHour, toMinutes;
    boolean filterFull;
    boolean filterCategory;
    boolean filterGame;
    boolean filterCity;
    String category;
    String game;
    String city;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
