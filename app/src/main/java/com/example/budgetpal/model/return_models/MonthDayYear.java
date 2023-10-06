package com.example.budgetpal.model.return_models;

public class MonthDayYear {
    private final String month;
    private final int day;
    private final int year;

    public MonthDayYear(String month, int day, int year) {
        this.month = month;
        this.day = day;
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getYear() {
        return year;
    }
}
