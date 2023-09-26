package com.example.budgetpal.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.budgetpal.R;

public class Budgets extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgets);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}