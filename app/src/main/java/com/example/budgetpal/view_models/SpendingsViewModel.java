package com.example.budgetpal.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.budgetpal.dialogs.AddDialog;
import com.example.budgetpal.model.DatabaseRepository;
import com.example.budgetpal.model.tables.SpendingsTable;

import java.math.BigDecimal;
import java.util.List;

public class SpendingsViewModel extends AndroidViewModel {

    DatabaseRepository databaseRepository;
    public SpendingsViewModel(@NonNull Application application) {
        super(application);
        databaseRepository = new DatabaseRepository(application);
    }
    public void insertSpending(int user_id, String product, BigDecimal value, String month, int day, int year, String category)
    {
        databaseRepository.insertSpending(new SpendingsTable(user_id,product,value,month,day,year,category));
    }

    public LiveData<List<SpendingsTable>> getAllSpendingFromDate(int user_id,String month, int day, int year)
    {
        return databaseRepository.getAllSpendingsFromDate(user_id,month,day,year);
    }
    public void updateTotalMoneyInDB(int user_id, BigDecimal value)
    {
        databaseRepository.updateTotalMoney(value,user_id);
    }
    public LiveData<BigDecimal> getTotalMoney(int user_id)
    {
        return databaseRepository.getTotalMoneyFromDB(user_id);
    }
    public void deleteSpending(int user_id, int day, String month, int year, String product_name)
    {
        databaseRepository.deleteSpending(user_id,day,month,year, product_name);
    }
    public void updateTotalMoney(int user_id, BigDecimal value)
    {
        databaseRepository.updateTotalMoney(value,user_id);
    }


}
