package com.example.budgetpal.data_models.model.tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;

@Entity(tableName = "SpendingsTable", foreignKeys = @ForeignKey(entity = User.class,parentColumns = "ID",childColumns = "user_id",onDelete = 5))
public class SpendingsTable {

    @PrimaryKey(autoGenerate = true)
    private int spendings_id;

    @ColumnInfo(name="user_id")
    private int user_id;

    @ColumnInfo(name="product_name")
    private String product_name;

    @ColumnInfo(name="product_value")
    private BigDecimal product_value;

    @ColumnInfo(name="month")
    private String month;

    @ColumnInfo(name="day")
    private int day;

    @ColumnInfo(name="year")
    private int year;

    @ColumnInfo(name="category")
    private String category;

    public SpendingsTable(int user_id, String product_name, BigDecimal product_value, String month, int day, int year, String category) {
        this.user_id = user_id;
        this.product_name = product_name;
        this.product_value = new BigDecimal(product_value.toString());
        this.month = month;
        this.day = day;
        this.year = year;
        this.category = category;
    }

    public int getSpendings_id() {
        return spendings_id;
    }

    public void setSpendings_id(int spendings_id) {
        this.spendings_id = spendings_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public BigDecimal getProduct_value() {
        return product_value;
    }

    public void setProduct_value(BigDecimal product_value) {
        this.product_value = new BigDecimal(product_value.toString());
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
