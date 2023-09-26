package com.example.budgetpal.model.tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;

@Entity(tableName = "User")
public class User {

    @PrimaryKey(autoGenerate = true)
    private int ID;
    @ColumnInfo(name = "user_email")
    private String email;

    @ColumnInfo(name = "user_password")
    private String password;

    @ColumnInfo(name="total_money")
    private BigDecimal total_money = new BigDecimal(0);

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public BigDecimal getTotal_money() {
        return total_money;
    }

    public void setTotal_money(BigDecimal total_money) {
        this.total_money = this.total_money.add(total_money);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
