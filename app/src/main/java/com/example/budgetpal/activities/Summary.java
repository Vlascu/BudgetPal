package com.example.budgetpal.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.budgetpal.DataReadyCallback;
import com.example.budgetpal.R;
import com.example.budgetpal.adapters.SummaryRecyclerAdapter;
import com.example.budgetpal.view_models.SummaryViewModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Summary extends AppCompatActivity implements DataReadyCallback {

    private SummaryViewModel summaryViewModel;
    private int user_id;
    private BigDecimal spendings_sum, revenues_sum;

    private RecyclerView recyclerView;

    private SummaryRecyclerAdapter adapter;

    private ArrayList<BigDecimal> allRevenuesTotals = new ArrayList<>(), allSpendingsTotal = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findAllGraphicalElements();
        summaryViewModel = new ViewModelProvider(this).get(SummaryViewModel.class);
        getUserID();

        LiveData<List<String>> allDates = summaryViewModel.getAllDatesFromUser(user_id);
        allDates.observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> dates) {
                allDates.removeObserver(this);
                if (dates.size() != 0) {
                    for (String monthYear : dates) {
                        String[] splitedDate = monthYear.split(" ");
                        getTotalRevenuesAndSpendingData(splitedDate[0], Integer.parseInt(splitedDate[1]), dates);
                    }
                }
            }
        });
    }

    private void getTotalRevenuesAndSpendingData(String month, int year, List<String> dates) {
        summaryViewModel.getDataForMonth(user_id, month, year, this, dates);
    }

    private void findAllGraphicalElements() {
        recyclerView = findViewById(R.id.summary_recyclerView);
    }

    private void updateRecyclerView(ArrayList<String> datesList) {
        //Todo: fix piechart
        checkDataListsSizes(datesList);
        adapter = new SummaryRecyclerAdapter(datesList, allRevenuesTotals, allSpendingsTotal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void checkDataListsSizes(ArrayList<String> dates) {
        if (allRevenuesTotals.size() == 0)
            for (int index = 0; index < dates.size(); index++)
                allRevenuesTotals.add(BigDecimal.ZERO);
        if (allSpendingsTotal.size() == 0)
            for (int index = 0; index < dates.size(); index++)
                allSpendingsTotal.add(BigDecimal.ZERO);
    }

    private void getUserID() {
        SharedPreferences sharedPreferences = getSharedPreferences(Register.SHARED_PREF, MODE_PRIVATE);
        user_id = sharedPreferences.getInt(Register.USER_ID, 0);
    }

    @Override
    public void onDataReady(BigDecimal revenuesSum, BigDecimal spendingsSum, List<String> dates) {
        allRevenuesTotals.add(revenuesSum);
        allSpendingsTotal.add(spendingsSum);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateRecyclerView(new ArrayList<>(dates));
            }
        });
    }
}