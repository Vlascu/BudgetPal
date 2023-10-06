package com.example.budgetpal.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.budgetpal.DataReadyCallback;
import com.example.budgetpal.model.DatabaseRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SummaryViewModel extends AndroidViewModel {
    final private DatabaseRepository databaseRepository;

    private ArrayList<BigDecimal> allRevenues = new ArrayList<>(), allSpendings = new ArrayList<>();

    public SummaryViewModel(@NonNull Application application) {
        super(application);
        databaseRepository = new DatabaseRepository(application);
    }
    public void getDataForMonth(int user_id, String month, int year, DataReadyCallback callback, List<String> dates) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<BigDecimal> revenues = new ArrayList<>(databaseRepository.getAllRevenuesValuesByMonth(user_id, month, year));
                ArrayList<BigDecimal> spendings = new ArrayList<>(databaseRepository.getAllSpendingsFromMonth(user_id, month, year));

                BigDecimal revenuesSum = calculateValuesSum(revenues);
                BigDecimal spendingsSum = calculateValuesSum(spendings);

                callback.onDataReady(revenuesSum, spendingsSum, dates);
            }
        });
    }
    public LiveData<List<String>> getAllDatesFromUser(int user_id)
    {
        return databaseRepository.getAllDatesFromAnUser(user_id);
    }
    private BigDecimal calculateValuesSum(ArrayList<BigDecimal> values) {
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal number : values)
            sum = sum.add(number);
        return sum;
    }
}
