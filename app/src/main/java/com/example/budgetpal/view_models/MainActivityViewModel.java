package com.example.budgetpal.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.budgetpal.model.DatabaseRepository;
import com.example.budgetpal.model.tables.Dates;
import com.example.budgetpal.model.tables.Revenue;

import java.math.BigDecimal;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    final private DatabaseRepository databaseRepository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        databaseRepository = new DatabaseRepository(application);
    }
    public void setDates(String date, int user_id)
    {
       databaseRepository.insertNewDate(new Dates(date,user_id));
    }
    public LiveData<String> getDates(int user_id, String date)
    {
        return databaseRepository.doesDateExist(user_id, date);
    }
    public LiveData<List<String>> getAllDatesFromAnUser(int user_id)
    {
        return databaseRepository.getAllDatesFromAnUser(user_id);
    }
    public void deleteUser(int user_id)
    {
        databaseRepository.deleteUser(user_id);
    }

    public LiveData<BigDecimal> getTotalMoney(int user_id) {
        return databaseRepository.getTotalMoneyFromDB(user_id);
    }

    public LiveData<Revenue> getTopAccount(int user_id, String month, int year)
    {
        return databaseRepository.getTopAccount(user_id,month,year);
    }
    public LiveData<List<BigDecimal>> getAllValuesBasedOnCategory(int user_id, String category)
    {
        return databaseRepository.getAllValuesBasedOnCategory(user_id,category);
    }

}
