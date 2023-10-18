package com.example.budgetpal.view_models;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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
    private MutableLiveData<ArrayList<BudgetModel>> mutableBudgetsList;
    private BigDecimal allSpendingsSum , maxBudget;

    public BudgetsViewModel(@NonNull Application application) {
        super(application);
        databaseRepository = new DatabaseRepository(application);
    }

    public void insertBudget(int userId, String month, int year, String category, BigDecimal value) {
        databaseRepository.insertBudget(new BudgetTable(userId, category, value, month, year));
    }

    public void getAllBudgets(int userId, String month, int year, LifecycleOwner lifecycleOwner) {
        budgetsList = new ArrayList<>();
        mutableBudgetsList = new MutableLiveData<>();
        LiveData<List<BudgetTable>> budgets = databaseRepository.getAllBudgetsFromDate(userId, month, year);
        budgets.observe(lifecycleOwner, new Observer<List<BudgetTable>>() {
            @Override
            public void onChanged(List<BudgetTable> budgetTables) {
                buildBudgetsList(budgetTables, userId, month, year, lifecycleOwner);
            }
        });
    }

    public LiveData<ArrayList<BudgetModel>> getTheBudgetList() {

        return mutableBudgetsList;
    }

    private void buildBudgetsList(List<BudgetTable> budgetTables, int userId, String month, int year, LifecycleOwner lifecycleOwner) {
        budgetsList.clear();
        for (BudgetTable budget : budgetTables) {
            allSpendingsSum = BigDecimal.ZERO;
            maxBudget = BigDecimal.ZERO;
            switch (budget.getCategory()) {
                case "Food": {
                    initializeMaxBudgetAndAllSpendings(userId,month,year,"Food",lifecycleOwner);
                    budgetsList.add(new BudgetModel(R.drawable.food, allSpendingsSum, "Food", maxBudget));
                    break;
                }
                case "Transport": {
                    initializeMaxBudgetAndAllSpendings(userId,month,year,"Transport",lifecycleOwner);
                    budgetsList.add(new BudgetModel(R.drawable.transport, allSpendingsSum, "Transport", maxBudget));
                    break;
                }
                case "Shopping": {
                    initializeMaxBudgetAndAllSpendings(userId,month,year,"Shopping",lifecycleOwner);
                    budgetsList.add(new BudgetModel(R.drawable.shopping, allSpendingsSum, "Shopping", maxBudget));
                    break;
                }
                case "Services": {
                    initializeMaxBudgetAndAllSpendings(userId,month,year,"Services",lifecycleOwner);
                    budgetsList.add(new BudgetModel(R.drawable.services, allSpendingsSum, "Services", maxBudget));
                    break;
                }
                case "Restaurants": {
                    initializeMaxBudgetAndAllSpendings(userId,month,year,"Restaurants",lifecycleOwner);
                    budgetsList.add(new BudgetModel(R.drawable.restaurants, allSpendingsSum, "Restaurants", maxBudget));
                    //Todo: maxBudget resets to 0 after asigning value to it
                    Log.i("Max budget after insert is: ",maxBudget.toString());
                    break;
                }
                case "Investments": {
                    initializeMaxBudgetAndAllSpendings(userId,month,year,"Investments",lifecycleOwner);
                    budgetsList.add(new BudgetModel(R.drawable.investements, allSpendingsSum, "Investments", maxBudget));
                    break;
                }
                case "Entertainment": {
                    initializeMaxBudgetAndAllSpendings(userId,month,year,"Entertainment",lifecycleOwner);
                    budgetsList.add(new BudgetModel(R.drawable.entertaiment, allSpendingsSum, "Entertainment", maxBudget));
                    break;
                }
            }
        }
        for (BudgetModel model : budgetsList)
        {
            Log.i("Budget value: ",model.getMaxBudget().toString());
        }
        mutableBudgetsList.setValue(budgetsList);
    }
    private void initializeMaxBudgetAndAllSpendings(int userId, String month, int year, String category, LifecycleOwner lifecycleOwner)
    {
        getAllSpendings(userId, month, year, category, lifecycleOwner);
        Log.i("Sum is: ", allSpendingsSum.toString());
        initializeMaxBudget(userId, month, year, category, lifecycleOwner);
    }
    private void initializeMaxBudget(int userId, String month, int year, String category, LifecycleOwner lifecycleOwner) {
        LiveData<BigDecimal> maxBudgetObservable = databaseRepository.getBudgetValue(userId, month, year, category);
        maxBudgetObservable.observe(lifecycleOwner, new Observer<BigDecimal>() {
            @Override
            public void onChanged(BigDecimal value) {
                maxBudgetObservable.removeObserver(this);
                maxBudget = value;
                Log.i("Max budget for " + category + " is: ", maxBudget.toString());
            }
        });
    }

    private void getAllSpendings(int userId, String month, int year, String category, LifecycleOwner lifecycleOwner) {
        databaseRepository.getAllSpendingsFromMonth(userId, month, year, category).observe(lifecycleOwner, new Observer<List<BigDecimal>>() {
            @Override
            public void onChanged(List<BigDecimal> values) {
                calculateSum(values);
            }
        });
    }

    private void calculateSum(List<BigDecimal> values) {
        for (BigDecimal number : values) {
            allSpendingsSum = allSpendingsSum.add(number);
        }
    }
}
