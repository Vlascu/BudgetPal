package com.example.budgetpal.model.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.budgetpal.model.tables.Dates;

import java.util.List;

@Dao
public interface DatesDAO {

    @Insert
    void insert(Dates dates);

    @Query("SELECT monthYear FROM dates WHERE user_id==:userID AND monthYear==:monthYearSearched")
    LiveData<String> doesDateExist(int userID, String monthYearSearched);

    @Query("SELECT monthYear FROM dates WHERE user_id==:userID ")
    LiveData<List<String>> getAllDatesFromAnUser(int userID);
}
