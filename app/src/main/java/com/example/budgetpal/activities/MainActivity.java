package com.example.budgetpal.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.budgetpal.R;
import com.example.budgetpal.dialogs.ConfirmationDialog;
import com.example.budgetpal.data_models.model.tables.Revenue;
import com.example.budgetpal.view_models.MainActivityViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ConfirmationDialog.DialogListener {

    private Spinner date_spinner;
    private ConstraintLayout summaryTab, spendingTab, budgetTab, accountsTab;

    private MainActivityViewModel mainActivityViewModel;

    private TextView total_money_text, top_account, top_account_value;

    private String current_month;

    private int current_year;

    private BarChart spendingsBarChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findGraphicalElements();
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        getDate();

        setTotalMoneyText();

        setTopAccountAndValue();

        summaryTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCustomAnimation(v);
                Intent i = new Intent(getApplicationContext(), Summary.class);
                startActivity(i);
            }
        });

        spendingTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCustomAnimation(v);
                Intent i = new Intent(getApplicationContext(), Spendings.class);
                putExtraMonthAndYear(i);
                startActivity(i);
            }
        });
        budgetTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCustomAnimation(v);
                Intent i = new Intent(getApplicationContext(), Budgets.class);
                putExtraMonthAndYear(i);
                startActivity(i);
            }
        });
        accountsTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCustomAnimation(v);
                Intent i = new Intent(getApplicationContext(), Accounts.class);
                putExtraMonthAndYear(i);
                startActivity(i);
            }
        });

        date_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String date = (String) parent.getItemAtPosition(position);
                getMonthAndYear(date);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //setSpendingsChartData();
    }

    private void getMonthAndYear(String date) {

        String[] separated = date.split(" ");
        current_month=separated[0];
        current_year=Integer.parseInt(separated[1]);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_delete) {
            openConfirmationDialog();
        } else if (item.getItemId() == R.id.menu_logout) {
            removeIdFromSharedPref();
            Toast.makeText(this, "You've been logged out", Toast.LENGTH_LONG).show();
            Intent i = new Intent(getApplicationContext(), Login.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void findGraphicalElements() {
        date_spinner = findViewById(R.id.date_spinner);
        summaryTab = findViewById(R.id.summaryTab);
        spendingTab = findViewById(R.id.spendingTab);
        budgetTab = findViewById(R.id.budgetTab);
        accountsTab = findViewById(R.id.accountsTab);
        total_money_text = findViewById(R.id.current_balance_value);
        top_account = findViewById(R.id.accounts_tab_account_name);
        top_account_value = findViewById(R.id.accounts_tab_account_value);
        spendingsBarChart = findViewById(R.id.spending_tab_chart);
    }

    private void updateSpinner(ArrayList<String> strings) {

        ArrayAdapter<String> dateSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                strings);
        dateSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        date_spinner.setAdapter(dateSpinnerAdapter);
    }

    private void startCustomAnimation(View v) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(200);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        scaleAnimation.setRepeatCount(1);
        v.startAnimation(scaleAnimation);
    }

    private void getDate() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

        current_month = monthFormat.format(currentDate);

        current_year = Integer.parseInt(yearFormat.format(currentDate));

        String current_date = current_month + " " + current_year;
        LiveData<String> checkedDate = mainActivityViewModel.getDates(getIdFromPreferences(),current_date);

        checkedDate.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                checkedDate.removeObserver(this);
                if(s==null)
                    mainActivityViewModel.setDates(current_date,getIdFromPreferences());
            }
        });

        LiveData<List<String>> allDates = mainActivityViewModel.getAllDatesFromAnUser(getIdFromPreferences());
        allDates.observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                if(strings!=null)
                {
                    ArrayList<String> arrayStrings = new ArrayList<>(strings);
                    updateSpinner(arrayStrings);
                }
            }
        });
    }

    private void openConfirmationDialog() {
        ConfirmationDialog dialog = new ConfirmationDialog(getIdFromPreferences());
        dialog.show(getSupportFragmentManager(), "Confirmation Dialog");
    }

    private int getIdFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(Register.SHARED_PREF, MODE_PRIVATE);
        return sharedPreferences.getInt(Register.USER_ID, 0);
    }

    @Override
    public void onPositiveButtonClick(int result, int user_id) {
        if (result == 1) {
            mainActivityViewModel.deleteUser(user_id);
            removeIdFromSharedPref();
            Intent i = new Intent(getApplicationContext(), Register.class);
            startActivity(i);
            finish();
        }
    }

    private void removeIdFromSharedPref() {
        SharedPreferences sharedPreferences = getSharedPreferences(Register.SHARED_PREF, MODE_PRIVATE);
        sharedPreferences.edit().remove(Register.USER_ID).apply();
    }

    private void putExtraMonthAndYear(Intent i) {
        i.putExtra("month", current_month);
        i.putExtra("year", current_year);
    }

    private void setTotalMoneyText() {
        mainActivityViewModel.getTotalMoney(getIdFromPreferences()).observe(this, new Observer<BigDecimal>() {
            @Override
            public void onChanged(BigDecimal bigDecimal) {
                if (!bigDecimal.equals(BigDecimal.ZERO)) {
                    mainActivityViewModel.getTotalMoney(getIdFromPreferences()).removeObserver(this);
                    total_money_text.setText(String.format(Locale.getDefault(), "%.2f", bigDecimal));
                } else
                    total_money_text.setText(String.format(Locale.getDefault(), "%.2f", BigDecimal.ZERO));

            }
        });
    }

    private void setTopAccountAndValue() {
        LiveData<Revenue> returnedData = mainActivityViewModel.getTopAccount(getIdFromPreferences(), current_month, current_year);
        returnedData.observe(this, new Observer<Revenue>() {
            @Override
            public void onChanged(Revenue revenue) {
                if (revenue != null) {
                    top_account.setText(revenue.getAccount());
                    top_account_value.setText(String.format(Locale.getDefault(), "%.2f", revenue.getAccount_amount()));
                }
                else
                {
                    top_account.setText(R.string.no_account_in_tab);
                    top_account_value.setText("0.0");
                }
            }
        });
    }
    private Float calculateTotalSpendingsCategoryValues(List<BigDecimal> values)
    {
        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal value : values)
        {
            total = total.add(value);
        }
        return Float.parseFloat(total.toString());
    }
    private void setSpendingsChartData()
    {
        List<BarEntry> entries = new ArrayList<>();
        final String[] categories = {"Restaurants", "Food", "Entertainment","Transport", "Services","Shopping","Investments"};
        for(int index=0;index<categories.length;index++)
        {
            final int finalIndex = index;
            LiveData<List<BigDecimal>> values = mainActivityViewModel.getAllValuesBasedOnCategory(getIdFromPreferences(),categories[index]);
            values.observe(this, new Observer<List<BigDecimal>>() {
                @Override
                public void onChanged(List<BigDecimal> allValues) {
                    values.removeObserver(this);
                    if(allValues!=null)
                    {
                        entries.add(new BarEntry(finalIndex,calculateTotalSpendingsCategoryValues(allValues)));
                    }
                    else
                    {
                        entries.add(new BarEntry(finalIndex,0));
                    }
                }
            });
        }


        if (entries.size() == categories.length) {
            BarDataSet dataSet = new BarDataSet(entries, "Expenses");
            dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

            BarData barData = new BarData(dataSet);

            spendingsBarChart.getAxisRight().setDrawLabels(false);

            YAxis yAxis = spendingsBarChart.getAxisLeft();
            yAxis.setAxisMinimum(0);
            yAxis.setAxisLineColor(Color.BLACK);
            yAxis.setLabelCount(25);

            spendingsBarChart.setData(barData);
            spendingsBarChart.getDescription().setEnabled(false);

            spendingsBarChart.invalidate();
            spendingsBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(categories));
            spendingsBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            spendingsBarChart.getXAxis().setGranularity(1f);
            spendingsBarChart.getXAxis().setGranularityEnabled(true);
        }
    }


}