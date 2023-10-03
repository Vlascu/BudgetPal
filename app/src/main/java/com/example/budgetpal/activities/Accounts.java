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

import com.example.budgetpal.R;
import com.example.budgetpal.adapters.AccountsRecyclerAdapter;
import com.example.budgetpal.dialogs.AddDialog;
import com.example.budgetpal.model.tables.Revenue;
import com.example.budgetpal.view_models.AccountsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Accounts extends AppCompatActivity implements AddDialog.AddDialogListener {


    private FloatingActionButton add_account_fab;
    private RecyclerView recyclerView;

    private AccountsViewModel accountsViewModel;

    private String current_month;
    private int current_year, user_id;

    private AccountsRecyclerAdapter accountsRecyclerAdapter;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findGraphicalElements();
        getInformationFromIntent();
        getUserId();

        accountsViewModel = new ViewModelProvider(this).get(AccountsViewModel.class);


        accountsViewModel.getAllDataFromDate(user_id, current_month, current_year).observe(this, new Observer<List<Revenue>>() {
            @Override
            public void onChanged(List<Revenue> revenues) {
                updateRecyclerView(revenues);
            }
        });

        add_account_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddDialog();
            }
        });

    }

    private void openAddDialog() {
        AddDialog dialog = new AddDialog(user_id, 1);
        dialog.show(getSupportFragmentManager(), "Add Dialog");
    }

    private void updateRecyclerView(List<Revenue> revenues) {
        ArrayList<Revenue> arrayListRevenues = new ArrayList<>(revenues);
        accountsRecyclerAdapter = new AccountsRecyclerAdapter(arrayListRevenues, user_id, accountsViewModel, current_month, current_year, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(accountsRecyclerAdapter);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private void findGraphicalElements() {
        add_account_fab = findViewById(R.id.accounts_floatingActionButton);
        recyclerView = findViewById(R.id.accounts_recyclerView);
    }

    @Override
    public void onPositiveButtonClick(int user_id, String name, BigDecimal value, String category) {

        accountsViewModel.insertRevenue(new Revenue(user_id, name, value, current_month, current_year));
        LiveData<BigDecimal> dataReturned = accountsViewModel.getTotalMoney(user_id);
       dataReturned.observe(this, new Observer<BigDecimal>() {
            @Override
            public void onChanged(BigDecimal bigDecimal) {
                dataReturned.removeObserver(this);
                if (!bigDecimal.equals(BigDecimal.ZERO)) {
                    bigDecimal = bigDecimal.add(value);
                    accountsViewModel.updateTotalMoneyInDB(user_id, bigDecimal);
                }
                else
                    accountsViewModel.updateTotalMoneyInDB(user_id,value);
            }
        });

    }

    private void getUserId() {
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

}