package com.example.budgetpal.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.budgetpal.R;
import com.example.budgetpal.dialogs.AddDialog;
import com.example.budgetpal.view_models.BudgetsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;

public class Budgets extends AppCompatActivity implements AddDialog.AddDialogListener {

    private BudgetsViewModel budgetsViewModel;

    private String current_month;
    private int current_year, user_id;
    private Intent intent;

    private FloatingActionButton fab;

    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgets);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        budgetsViewModel = new ViewModelProvider(this).get(BudgetsViewModel.class);
        findGraphicalElements();
        getIdFromPreferences();
        getInformationFromIntent();

        //Todo: update recycler view and progress bar from each item view

    }

    @Override
    public void onPositiveButtonClick(int user_id, String name, BigDecimal value, String category) {
        budgetsViewModel.insertBudget(user_id,current_month,current_year,category,value);
    }

    private void getIdFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(Register.SHARED_PREF, MODE_PRIVATE);
        user_id = sharedPreferences.getInt(Register.USER_ID, 0);
    }

    private void getInformationFromIntent() {
        intent = getIntent();
        if (intent != null) {
            current_month = intent.getStringExtra("month");
            current_year = intent.getIntExtra("year", 0);
        }
    }
    private void findGraphicalElements() {
        recyclerView = findViewById(R.id.budget_recyclerView);
        fab = findViewById(R.id.floatingActionButton2);

    }
}