package com.example.budgetpal.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.budgetpal.R;
import com.example.budgetpal.adapters.BudgetsRecyclerAdapter;
import com.example.budgetpal.data_models.BudgetModel;
import com.example.budgetpal.dialogs.AddDialog;
import com.example.budgetpal.view_models.BudgetsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Budgets extends AppCompatActivity implements AddDialog.AddDialogListener {

    private BudgetsViewModel budgetsViewModel;

    private String currentMonth;
    private int currentYear, userId;

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
        initializeDataAndUpdateRecycler();

        //Todo: check if a budget exists, fix recycler view not updating when adding
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddDialog();
            }
        });
    }
    private void initializeDataAndUpdateRecycler()
    {
        budgetsViewModel.getAllBudgets(userId, currentMonth, currentYear, this);
        budgetsViewModel.getTheBudgetList().observe(this, new Observer<ArrayList<BudgetModel>>() {
            @Override
            public void onChanged(ArrayList<BudgetModel> budgets) {
                updateRecyclerView(budgets);
            }
        });
    }
    private void openAddDialog() {
        AddDialog dialog = new AddDialog(userId,3);
        dialog.show(getSupportFragmentManager(),"Add dialog");
    }

    private void updateRecyclerView(ArrayList<BudgetModel> theBudgetList) {
        BudgetsRecyclerAdapter budgetsRecyclerAdapter = new BudgetsRecyclerAdapter(theBudgetList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(budgetsRecyclerAdapter);
    }

    @Override
    public void onPositiveButtonClick(int user_id, String name, BigDecimal value, String category) {
        budgetsViewModel.insertBudget(user_id, currentMonth, currentYear,category,value);
        initializeDataAndUpdateRecycler();
    }

    private void getIdFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(Register.SHARED_PREF, MODE_PRIVATE);
        userId = sharedPreferences.getInt(Register.USER_ID, 0);
    }

    private void getInformationFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            currentMonth = intent.getStringExtra("month");
            currentYear = intent.getIntExtra("year", 0);
        }
    }
    private void findGraphicalElements() {
        recyclerView = findViewById(R.id.budget_recyclerView);
        fab = findViewById(R.id.floatingActionButton2);

    }
}