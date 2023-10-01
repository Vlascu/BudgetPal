package com.example.budgetpal.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.budgetpal.data_models.model.DatabaseRepository;
import com.example.budgetpal.data_models.model.tables.Revenue;

import java.math.BigDecimal;
import java.util.List;

public class AccountsViewModel extends AndroidViewModel {

    DatabaseRepository databaseRepository;


    public AccountsViewModel(@NonNull Application application) {
        super(application);
        databaseRepository = new DatabaseRepository(application);

    }
    public void insertRevenue(Revenue revenue)
    {
        databaseRepository.insertRevenue(revenue);
    }
    public LiveData<List<Revenue>> getAllDataFromDate(int user_id, String month, int year)
    {
        return databaseRepository.getAllRevenueFromDate(user_id,month,year);
    }
    public void deleteRevenue(int user_id, String month, int year, String account_name)
    {
        databaseRepository.deleteRevenueByDate(user_id,year,month, account_name);
    }
    public void updateTotalMoneyInDB(int user_id, BigDecimal value)
    {
        databaseRepository.updateTotalMoney(value,user_id);
    }
    public LiveData<BigDecimal> getTotalMoney(int user_id)
    {
       return databaseRepository.getTotalMoneyFromDB(user_id);
    }
}
