package com.example.budgetpal.data_models.model.return_models;

public class MonthDayYear {
    private String month;
    private int day, year;

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
