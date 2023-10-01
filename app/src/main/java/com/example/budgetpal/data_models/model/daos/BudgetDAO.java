package com.example.budgetpal.data_models.model.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.budgetpal.data_models.model.return_models.MonthYear;
import com.example.budgetpal.data_models.model.tables.BudgetTable;

import java.util.List;

@Dao
public interface BudgetDAO {

    @Insert
    void insert(BudgetTable budgetTable);

    @Update
    void update(BudgetTable budgetTable);

    @Delete
    void delete(BudgetTable budgetTable);

    @Query("SELECT month,year FROM BudgetTable WHERE user_id==:userID")
    LiveData<List<MonthYear>> getAllDates(int userID);

    @Query("SELECT * FROM BudgetTable WHERE user_id==:userID AND month==:budgetMonth AND year==:budgetYear")
    LiveData<List<BudgetTable>> getAllBudgetsFromDate(int userID, String budgetMonth, int budgetYear);
}
