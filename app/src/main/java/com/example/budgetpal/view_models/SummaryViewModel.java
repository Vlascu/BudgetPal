package com.example.budgetpal.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.budgetpal.data_models.model.DatabaseRepository;

import java.math.BigDecimal;
import java.util.List;

public class SummaryViewModel extends AndroidViewModel {
    final private DatabaseRepository databaseRepository;
    public SummaryViewModel(@NonNull Application application) {
        super(application);
        databaseRepository = new DatabaseRepository(application);
    }
    public LiveData<List<BigDecimal>> getAllSpendingsValuesFromMonth ( int user_id, String month, int year)
    {
        return databaseRepository.getAllSpendingsFromMonth(user_id,month,year);
    }
    public LiveData<List<BigDecimal>> getAllRevenuesValuesByMonth(int user_id, String month, int year)
    {
        return databaseRepository.getAllRevenuesValuesByMonth(user_id,month,year);
    }
}
