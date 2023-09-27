package com.example.budgetpal.model.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.budgetpal.model.return_models.MonthDayYear;
import com.example.budgetpal.model.tables.Revenue;
import com.example.budgetpal.model.tables.SpendingsTable;

import java.math.BigDecimal;
import java.util.List;

@Dao
public interface SpendingsDAO {

    @Insert
    void insert(SpendingsTable spendingsTable);

    @Update
    void update(SpendingsTable spendingsTable);

    @Delete
    void delete(SpendingsTable spendingsTable);


    @Query("SELECT * FROM SpendingsTable WHERE user_id=:userID AND month=:spendingMonth AND day=:spendingDay AND year=:spendingYear")
    LiveData<List<SpendingsTable>> getAllSpendingsFromDate(int userID, String spendingMonth, int spendingDay, int spendingYear);

    @Query("SELECT product_value FROM SpendingsTable WHERE user_id==:userID AND category==:productCategory")
    LiveData<List<BigDecimal>> getAllValuesBasedOnCategory(int userID, String productCategory);

    @Query("DELETE FROM SpendingsTable WHERE user_id==:userID AND day==:current_day " +
            "AND month==:current_month AND year==:current_year AND product_name==:productName")
    void deleteSpending(int userID, int current_day, String current_month, int current_year, String productName);

}
