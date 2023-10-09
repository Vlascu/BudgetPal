package com.example.budgetpal.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.budgetpal.R;
import com.example.budgetpal.activities.Budgets;
import com.example.budgetpal.data_models.BudgetModel;
import com.example.budgetpal.model.DatabaseRepository;
import com.example.budgetpal.model.tables.BudgetTable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BudgetsViewModel extends AndroidViewModel {

    final private DatabaseRepository databaseRepository;
    private ArrayList<BudgetModel> budgetsList;
    private BigDecimal allSpendingsSum;
    public BudgetsViewModel(@NonNull Application application) {
        super(application);
        databaseRepository = new DatabaseRepository(application);
    }
    public void insertBudget(int user_id, String month, int year, String category, BigDecimal value)
    {
        databaseRepository.insertBudget(new BudgetTable(user_id,category,value,month,year));
    }
    public void getAllBudgets(int user_id, String month, int year)
    {
        LiveData<List<BudgetTable>> budgets = databaseRepository.getAllBudgetsFromDate(user_id, month, year);
        budgets.observe(getApplication(), new Observer<List<BudgetTable>>() {
            @Override
            public void onChanged(List<BudgetTable> budgetTables) {
                budgets.removeObserver(this);
                buildBudgetsList(budgetTables, user_id, month, year);
            }
        });
    }

    public ArrayList<BudgetModel> getTheBudgetList()
    {
        return budgetsList;
    }

    private void buildBudgetsList(List<BudgetTable> budgetTables, int user_id, String month, int year) {
        budgetsList = new ArrayList<>();
        for(BudgetTable budget : budgetTables)
        {
            switch (budget.getCategory())
            {
                case "Food":
                {
                    //Todo: create getBudgetValue dao query and methods for the constructor
                    getAllSpendings(user_id,month,year,"Food");
                    //budgetsList.add(new BudgetModel(R.drawable.food,allSpendingsSum,"Food",))
                }
            }
        }
    }
    private void getAllSpendings(int user_id, String month, int year, String category)
    {
        LiveData<List<BigDecimal>> allSpendings = databaseRepository.getAllSpendingsFromMonth(user_id, month, year, category);
        allSpendings.observe(getApplication(), new Observer<List<BigDecimal>>() {
            @Override
            public void onChanged(List<BigDecimal> bigDecimals) {
                allSpendings.removeObserver(this);
                calculateSum(bigDecimals);
            }
        });
    }

    private void calculateSum(List<BigDecimal> bigDecimals) {
        allSpendingsSum=BigDecimal.ZERO;
        for(BigDecimal number : bigDecimals)
        {
            allSpendingsSum = allSpendingsSum.add(number);
        }
    }
}
