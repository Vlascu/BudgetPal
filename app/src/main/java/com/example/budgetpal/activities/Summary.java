package com.example.budgetpal.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.budgetpal.R;

public class Summary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}