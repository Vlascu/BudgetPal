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
}
