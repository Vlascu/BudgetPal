package com.example.budgetpal.data_models.model.return_models;

public class MonthYear {

    private String month;
    private int year;

    public MonthYear(String month, int year) {
        this.month = month;
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
}