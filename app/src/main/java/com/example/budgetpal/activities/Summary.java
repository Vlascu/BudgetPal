package com.example.budgetpal.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.budgetpal.R;
import com.example.budgetpal.adapters.SummaryRecyclerAdapter;
import com.example.budgetpal.view_models.SummaryViewModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Summary extends AppCompatActivity {

    private SummaryViewModel summaryViewModel;
    private int user_id;
    private BigDecimal spendings_sum, revenues_sum;

    private ArrayList<BigDecimal> allRevenuesTotals, allSpendingsTotal;

    private RecyclerView recyclerView;

    private SummaryRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findAllGraphicalElements();
        summaryViewModel = new ViewModelProvider(this).get(SummaryViewModel.class);
        getUserID();

        allRevenuesTotals=new ArrayList<>();
        allSpendingsTotal=new ArrayList<>();

        LiveData<List<String>> allDates = summaryViewModel.getAllDatesFromUser(user_id);
        allDates.observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> dates) {
                allDates.removeObserver(this);
                if (dates.size() != 0) {

                    for (String monthYear : dates) {
                        revenues_sum = BigDecimal.ZERO;
                        spendings_sum = BigDecimal.ZERO;
                        String[] splitedDate = monthYear.split(" ");
                        LiveData<List<BigDecimal>> currentRevenues = summaryViewModel.getAllRevenuesValuesByMonth(user_id, splitedDate[0], Integer.parseInt(splitedDate[1]));
                        currentRevenues.observe(Summary.this, new Observer<List<BigDecimal>>() {
                            @Override
                            public void onChanged(List<BigDecimal> values) {
                                currentRevenues.removeObserver(this);
                                if (values != null) {
                                    revenues_sum = calculateValuesSum(values);
                                    allRevenuesTotals.add(revenues_sum);
                                }
                            }
                        });

                        LiveData<List<BigDecimal>> currentSpendings = summaryViewModel.getAllSpendingsValuesFromMonth(user_id, splitedDate[0], Integer.parseInt(splitedDate[1]));
                        currentSpendings.observe(Summary.this, new Observer<List<BigDecimal>>() {
                            @Override
                            public void onChanged(List<BigDecimal> values) {
                                currentSpendings.removeObserver(this);
                                if (values != null) {
                                    spendings_sum = calculateValuesSum(values);
                                    allSpendingsTotal.add(spendings_sum);
                                }

                            }
                        });
                    }

                    ArrayList<String> datesList = new ArrayList<>(dates);

                    updateRecyclerView(datesList);

                }
            }
        });

    }

    private void findAllGraphicalElements()
    {
        recyclerView = findViewById(R.id.summary_recyclerView);
    }

    private void updateRecyclerView(ArrayList<String> datesList) {
        checkDataListsSizes(datesList);
        adapter = new SummaryRecyclerAdapter(datesList, allRevenuesTotals, allSpendingsTotal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void checkDataListsSizes(ArrayList<String> dates) {
        if(allRevenuesTotals.size()==0)
            for (int index=0;index<dates.size();index++)
                allRevenuesTotals.add(BigDecimal.ZERO);
        if(allSpendingsTotal.size()==0)
            for (int index=0;index<dates.size();index++)
                allSpendingsTotal.add(BigDecimal.ZERO);
    }

    private void getUserID() {
        SharedPreferences sharedPreferences = getSharedPreferences(Register.SHARED_PREF, MODE_PRIVATE);
        user_id = sharedPreferences.getInt(Register.USER_ID, 0);
    }

    private BigDecimal calculateValuesSum(List<BigDecimal> values) {
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal number : values)
            sum = sum.add(number);
        return sum;
    }
}