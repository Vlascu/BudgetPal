package com.example.budgetpal.model.tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity (tableName = "dates", foreignKeys = @ForeignKey(entity = User.class,parentColumns = "ID",childColumns = "user_id",onDelete = 5))
public class Dates {

    @ColumnInfo(name = "user_id")
    private int user_id;
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "monthYear")
    private String monthYear;

    public Dates(String monthYear, int user_id) {
        this.monthYear = monthYear;
        this.user_id = user_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }
}
