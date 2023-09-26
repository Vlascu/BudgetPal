package com.example.budgetpal.model.tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;

@Entity(tableName = "Revenue", foreignKeys = @ForeignKey(entity = User.class,parentColumns = "ID",childColumns = "user_id",onDelete = 5))
public class Revenue {

    @PrimaryKey(autoGenerate = true)
    private int revenue_id;
    @ColumnInfo(name = "user_id")
    private int user_id;
    @ColumnInfo(name = "account")
    private String account;
    @ColumnInfo(name = "account_amount")
    private BigDecimal account_amount;
    @ColumnInfo(name = "month")
    private String month;
    @ColumnInfo(name = "year")
    private int year;

    public Revenue(int user_id, String account, BigDecimal account_amount, String month, int year) {
        this.user_id = user_id;
        this.account = account;
        this.account_amount = new BigDecimal(account_amount.toString());
        this.month = month;
        this.year = year;
    }

    public int getRevenue_id() {
        return revenue_id;
    }

    public void setRevenue_id(int revenue_id) {
        this.revenue_id = revenue_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public BigDecimal getAccount_amount() {
        return account_amount;
    }

    public void setAccount_amount(BigDecimal account_amount) {
        this.account_amount = new BigDecimal(account_amount.toString());
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
