package com.example.budgetpal.model.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.budgetpal.model.tables.User;

import java.math.BigDecimal;
import java.util.Stack;

@Dao
public interface UserDAO {

    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Query("SELECT * FROM User WHERE user_email==:searchedEmail AND user_password==:searchedPassword")
    LiveData<User> getUser(String searchedEmail, String searchedPassword);

    @Query("DELETE FROM User")
    void deleteUsersWithEmptyCredentials();

    @Query("DELETE FROM User WHERE ID==:user_id")
    void deleteUser(int user_id);

    @Query("SELECT total_money FROM User WHERE ID==:user_id")
    LiveData<BigDecimal> getTotalMoneyFromDB(int user_id);

    @Query("UPDATE User SET total_money=:value WHERE ID==:user_id")
    void updateTotalMoney(BigDecimal value, int user_id );

}
