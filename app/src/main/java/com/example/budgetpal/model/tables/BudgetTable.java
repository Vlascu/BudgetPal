package com.example.budgetpal.model.tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;

@Entity(tableName = "BudgetTable", foreignKeys = @ForeignKey(entity = User.class,parentColumns = "ID",childColumns = "user_id",onDelete = 5))
public class BudgetTable {

    @PrimaryKey(autoGenerate = true)
    private int budget_id;

    @ColumnInfo(name="user_id")
    private int user_id;
    @ColumnInfo(name="category")
    private int category;
    @ColumnInfo(name="value")
    private BigDecimal value;
    @ColumnInfo(name="month")
    private String month;

    @ColumnInfo(name="year")
    private int year;

    public BudgetTable(int budget_id, int user_id, int category, BigDecimal value, String month, int year) {
        this.budget_id = budget_id;
        this.user_id = user_id;
        this.category = category;
        this.value = new BigDecimal(value.toString());
        this.month = month;
        this.year = year;
    }

    public int getBudget_id() {
        return budget_id;
    }

    public void setBudget_id(int budget_id) {
        this.budget_id = budget_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = new BigDecimal(value.toString());
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
