package com.example.budgetpal.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.budgetpal.model.DatabaseRepository;
import com.example.budgetpal.model.tables.BudgetTable;

import java.math.BigDecimal;

public class BudgetsViewModel extends AndroidViewModel {

    final private DatabaseRepository databaseRepository;
    public BudgetsViewModel(@NonNull Application application) {
        super(application);
        databaseRepository = new DatabaseRepository(application);
    }
    public void insertBudget(int user_id, String month, int year, String category, BigDecimal value)
    {
        databaseRepository.insertBudget(new BudgetTable(user_id,category,value,month,year));
    }
}
