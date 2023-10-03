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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.budgetpal.R;
import com.example.budgetpal.adapters.SpendingsRecyclerAdapter;
import com.example.budgetpal.dialogs.AddDialog;
import com.example.budgetpal.model.tables.SpendingsTable;
import com.example.budgetpal.view_models.SpendingsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Spendings extends AppCompatActivity implements AddDialog.AddDialogListener {

    private RecyclerView recyclerView;
    private SpendingsRecyclerAdapter spendingsRecyclerAdapter;

    private SpendingsViewModel spendingsViewModel;

    private FloatingActionButton add_fab;
    private int user_id, current_year, current_day;

    private String current_month;

    private Spinner days_spinner;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spendings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findGraphicalElements();
        getIdFromPreferences();
        getInformationFromIntent();
        createDaysArray();
        spendingsViewModel = new ViewModelProvider(this).get(SpendingsViewModel.class);
        add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddDialog();
            }
        });
        days_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                current_day = (int) parent.getItemAtPosition(position);
                observeNewData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void observeNewData() {
        spendingsViewModel.getAllSpendingFromDate(user_id, current_month, current_day, current_year).observe(this, new Observer<List<SpendingsTable>>() {
            @Override
            public void onChanged(List<SpendingsTable> spendingsTables) {
                if (spendingsTables != null) {
                    ArrayList<SpendingsTable> spendings = new ArrayList<>(spendingsTables);
                    updateRecyclerView(spendings);
                }
            }
        });
    }

    private void createDaysArray() {
        ArrayList<Integer> days = new ArrayList<Integer>();
        LocalDate currentDate = LocalDate.now();
        for (int index = 1; index <= currentDate.getMonth().length(currentDate.isLeapYear()); index++) {
            days.add(index);
        }
        updateDaysSpinner(days);
    }

    private void updateDaysSpinner(ArrayList<Integer> days) {
        ArrayAdapter<Integer> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, days);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        days_spinner.setAdapter(spinnerAdapter);
    }

    private void findGraphicalElements() {
        recyclerView = findViewById(R.id.spendings_recyclerView);
        add_fab = findViewById(R.id.spendings_floatingActionButton);
        days_spinner = findViewById(R.id.spendings_date_spinner);
    }

    private void updateRecyclerView(ArrayList<SpendingsTable> data) {
        spendingsRecyclerAdapter = new SpendingsRecyclerAdapter(data, spendingsViewModel, current_year,current_day,current_month, user_id, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(spendingsRecyclerAdapter);
    }

    @Override
    public void onPositiveButtonClick(int user_id, String name, BigDecimal value, String category) {

        LiveData<BigDecimal> totalMoney = spendingsViewModel.getTotalMoney(user_id);
        totalMoney.observe(this, new Observer<BigDecimal>() {
            @Override
            public void onChanged(BigDecimal total_money_value) {
                totalMoney.removeObserver(this);
                if (total_money_value.subtract(value).compareTo(BigDecimal.ZERO) >= 0) {
                    spendingsViewModel.updateTotalMoneyInDB(user_id, total_money_value.subtract(value));
                    if (current_day <= getCurrentDay())
                    {
                        spendingsViewModel.insertSpending(user_id, name, value, current_month, current_day, current_year, category);
                        observeNewData();
                    }

                    else
                        Toast.makeText(Spendings.this, "You can't add a spending for future days", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Spendings.this, "You don't have enough money to add this spending", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getIdFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(Register.SHARED_PREF, MODE_PRIVATE);
        user_id = sharedPreferences.getInt(Register.USER_ID, 0);
    }

    private int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void getInformationFromIntent() {
        intent = getIntent();
        if (intent != null) {
            current_month = intent.getStringExtra("month");
            current_year = intent.getIntExtra("year", 0);
        }
    }

    private void openAddDialog() {
        AddDialog addDialog = new AddDialog(user_id, 2);
        addDialog.show(getSupportFragmentManager(), "Add dialog");
    }
}