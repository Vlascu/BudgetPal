package com.example.budgetpal.model.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.budgetpal.model.return_models.MonthYear;
import com.example.budgetpal.model.tables.Revenue;

import java.math.BigDecimal;
import java.util.List;

@Dao
public interface RevenueDAO {

    @Insert
    void insert(Revenue revenue);

    @Update
    void update(Revenue revenue);

    @Delete
    void delete(Revenue revenue);

    @Query("SELECT * FROM Revenue WHERE user_id==:userID")
    LiveData<List<Revenue>> getAllRevenuesFromUser(int userID);

    @Query("SELECT * FROM Revenue WHERE user_id==:userID AND month==:revenueMonth AND year==:revenueYear")
    LiveData<List<Revenue>> getAllRevenuesFromDate(int userID, String revenueMonth, int revenueYear);

    @Query("SELECT month,year FROM Revenue WHERE user_id==:userID")
    LiveData<List<MonthYear>> getAllDates(int userID);

    @Query("DELETE FROM Revenue WHERE user_id==:userID AND month==:removed_month AND year==:removed_year " +
            "AND account==:account_name")
    void removeRevenue(int userID, String removed_month, int removed_year, String account_name);

    @Query("SELECT * FROM Revenue WHERE user_id==:userID AND month==:searched_month AND year==:searched_year " +
            "ORDER BY account_amount DESC LIMIT 1")
    LiveData<Revenue> getTopAccount(int userID, String searched_month, int searched_year);

    @Query("SELECT account_amount FROM Revenue WHERE user_id==:userID AND month==:searchedMonth AND year==:searchedYear")
    LiveData<List<BigDecimal>> getAllRevenuesValuesByMonth(int userID, String searchedMonth, int searchedYear);
}
