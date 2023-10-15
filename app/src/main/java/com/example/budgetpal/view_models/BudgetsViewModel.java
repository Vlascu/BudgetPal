package com.example.budgetpal.view_models;

import android.app.Application;

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
    private ArrayList<BudgetModel> budgetsList = new ArrayList<>();
    private MutableLiveData<ArrayList<BudgetModel>> mutableBudgetsList = new MutableLiveData<>();
    private BigDecimal allSpendingsSum = BigDecimal.ZERO, maxBudget = BigDecimal.ZERO;

    public BudgetsViewModel(@NonNull Application application) {
        super(application);
        databaseRepository = new DatabaseRepository(application);
    }

    public void insertBudget(int user_id, String month, int year, String category, BigDecimal value) {
        databaseRepository.insertBudget(new BudgetTable(user_id, category, value, month, year));
    }

    public void getAllBudgets(int user_id, String month, int year, LifecycleOwner lifecycleOwner) {
        LiveData<List<BudgetTable>> budgets = databaseRepository.getAllBudgetsFromDate(user_id, month, year);
        budgets.observe(lifecycleOwner, new Observer<List<BudgetTable>>() {
            @Override
            public void onChanged(List<BudgetTable> budgetTables) {
                buildBudgetsList(budgetTables, user_id, month, year, lifecycleOwner);
            }
        });
    }

    public LiveData<ArrayList<BudgetModel>> getTheBudgetList() {

        return mutableBudgetsList;
    }

    private void buildBudgetsList(List<BudgetTable> budgetTables, int user_id, String month, int year, LifecycleOwner lifecycleOwner) {
        budgetsList.clear();
        for (BudgetTable budget : budgetTables) {
            switch (budget.getCategory()) {
                case "Food": {
                    getAllSpendings(user_id, month, year, "Food", lifecycleOwner);
                    initializeMaxBudget(user_id, month, year, "Food", lifecycleOwner);
                    budgetsList.add(new BudgetModel(R.drawable.food, allSpendingsSum, "Food", maxBudget));
                    break;
                }
                case "Transport": {
                    getAllSpendings(user_id, month, year, "Transport", lifecycleOwner);
                    initializeMaxBudget(user_id, month, year, "Transport", lifecycleOwner);
                    budgetsList.add(new BudgetModel(R.drawable.transport, allSpendingsSum, "Transport", maxBudget));
                    break;
                }
                case "Shopping": {
                    getAllSpendings(user_id, month, year, "Shopping", lifecycleOwner);
                    initializeMaxBudget(user_id, month, year, "Shopping", lifecycleOwner);
                    budgetsList.add(new BudgetModel(R.drawable.shopping, allSpendingsSum, "Shopping", maxBudget));
                    break;
                }
                case "Services": {
                    getAllSpendings(user_id, month, year, "Services", lifecycleOwner);
                    initializeMaxBudget(user_id, month, year, "Services", lifecycleOwner);
                    budgetsList.add(new BudgetModel(R.drawable.services, allSpendingsSum, "Services", maxBudget));
                    break;
                }
                case "Restaurants": {
                    getAllSpendings(user_id, month, year, "Restaurants", lifecycleOwner);
                    initializeMaxBudget(user_id, month, year, "Restaurants", lifecycleOwner);
                    budgetsList.add(new BudgetModel(R.drawable.restaurants, allSpendingsSum, "Restaurants", maxBudget));
                    break;
                }
                case "Investments": {
                    getAllSpendings(user_id, month, year, "Investments", lifecycleOwner);
                    initializeMaxBudget(user_id, month, year, "Investments", lifecycleOwner);
                    budgetsList.add(new BudgetModel(R.drawable.investements, allSpendingsSum, "Investments", maxBudget));
                    break;
                }
                case "Entertainment": {
                    getAllSpendings(user_id, month, year, "Entertainment", lifecycleOwner);
                    initializeMaxBudget(user_id, month, year, "Entertainment", lifecycleOwner);
                    budgetsList.add(new BudgetModel(R.drawable.entertaiment, allSpendingsSum, "Entertainment", maxBudget));
                    break;
                }
            }
        }
        mutableBudgetsList.setValue(budgetsList);
    }

    private void initializeMaxBudget(int userID, String month, int year, String category, LifecycleOwner lifecycleOwner) {
        LiveData<BigDecimal> maxBudgetObservable = databaseRepository.getBudgetValue(userID, month, year, category);
        maxBudgetObservable.observe(lifecycleOwner, new Observer<BigDecimal>() {
            @Override
            public void onChanged(BigDecimal value) {
                maxBudgetObservable.removeObserver(this);
                maxBudget = value;
            }
        });
    }

    private void getAllSpendings(int user_id, String month, int year, String category, LifecycleOwner lifecycleOwner) {
        LiveData<List<BigDecimal>> allSpendings = databaseRepository.getAllSpendingsFromMonth(user_id, month, year, category);
        allSpendings.observe(lifecycleOwner, new Observer<List<BigDecimal>>() {
            @Override
            public void onChanged(List<BigDecimal> values) {
                allSpendings.removeObserver(this);
                calculateSum(values);
            }
        });
    }

    private void calculateSum(List<BigDecimal> values) {
        allSpendingsSum = BigDecimal.ZERO;
        for (BigDecimal number : values) {
            allSpendingsSum = allSpendingsSum.add(number);
        }
    }
}
