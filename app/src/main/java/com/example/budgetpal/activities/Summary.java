package com.example.budgetpal.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.budgetpal.R;
import com.example.budgetpal.view_models.SummaryViewModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Summary extends AppCompatActivity {

    private SummaryViewModel summaryViewModel;
    private int current_year, user_id;
    private String current_month;
    private Intent intent;
    private BigDecimal spendings_sum , revenues_sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        summaryViewModel = new ViewModelProvider(this).get(SummaryViewModel.class);
        getInformationFromIntent();
        getUserID();
        //Todo: set up recycler view and the adapter
        LiveData<List<BigDecimal>> currentRevenues = summaryViewModel.getAllRevenuesValuesByMonth(user_id,current_month,current_year);
        currentRevenues.observe(this, new Observer<List<BigDecimal>>() {
            @Override
            public void onChanged(List<BigDecimal> values) {
                currentRevenues.removeObserver(this);
                if(values!=null)
                {
                    revenues_sum = calculateValuesSum(values);
                }
            }
        });
        LiveData<List<BigDecimal>> currentSpendings = summaryViewModel.getAllSpendingsValuesFromMonth(user_id,current_month,current_year);
        currentSpendings.observe(this, new Observer<List<BigDecimal>>() {
            @Override
            public void onChanged(List<BigDecimal> values) {
                currentSpendings.removeObserver(this);
                if(values!=null)
                {
                    spendings_sum = calculateValuesSum(values);
                }

            }
        });
    }
    private void getInformationFromIntent() {
        intent = getIntent();
        if (intent != null) {
            current_month = intent.getStringExtra("month");
            current_year = intent.getIntExtra("year", 0);
        }
    }
    private void getUserID()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(Register.SHARED_PREF,MODE_PRIVATE);
        user_id = sharedPreferences.getInt(Register.USER_ID,0);
    }
    private BigDecimal calculateValuesSum(List<BigDecimal> values)
    {
        BigDecimal sum = BigDecimal.ZERO;
        for(BigDecimal number : values)
            sum = sum.add(number);
        return sum;
    }
}