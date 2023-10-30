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
    private interface Callback {
        void onComplete(BigDecimal allSpendingsSum, BigDecimal maxBudget);
    }

    private interface MaxBudgetCallback {
        void onMaxBudget(BigDecimal maxBudget);
    }

    private interface SpendingsCallback {
        void onSpendingsSum(BigDecimal allSpendingsSum);
    }

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
        mutableBudgetsList.setValue(budgetsList);
        for (BudgetTable budget : budgetTables) {
            switch (budget.getCategory()) {
                case "Food": {
                    processCategory(userId,month,year,"Food",lifecycleOwner, R.drawable.food);
                    break;
                }
                case "Transport": {
                    processCategory(userId,month,year,"Transport",lifecycleOwner, R.drawable.transport);
                    break;
                }
                case "Shopping": {
                    processCategory(userId,month,year,"Shopping",lifecycleOwner, R.drawable.shopping);
                    break;
                }
                case "Services": {
                    processCategory(userId,month,year,"Services",lifecycleOwner,R.drawable.services);
                    break;
                }
                case "Restaurants": {
                    processCategory(userId,month,year,"Restaurants",lifecycleOwner, R.drawable.restaurants);
                    break;
                }
                case "Investments": {
                    processCategory(userId,month,year,"Investments",lifecycleOwner, R.drawable.investements);
                    break;
                }
                case "Entertainment": {
                    processCategory(userId,month,year,"Entertainment",lifecycleOwner, R.drawable.entertaiment);
                    break;
                }
            }
        }
        mutableBudgetsList.setValue(budgetsList);
    }
    private void processCategory(int userId, String month, int year, String category, LifecycleOwner lifecycleOwner, int categoryImageResource) {
        initializeMaxBudgetAndAllSpendings(userId, month, year, category, lifecycleOwner, new Callback() {
            //Todo: insereaza noul model intr-o baza de date si extrage-l de acolo pentru a evite duplicara de date
            // ( un buget model pt fiecare buget )
            @Override
            public void onComplete(BigDecimal allSpendingsSum, BigDecimal maxBudget) {
                budgetsList.add(new BudgetModel(categoryImageResource, allSpendingsSum, category, maxBudget));
            }
        });
    }
    private void initializeMaxBudgetAndAllSpendings(int userId, String month, int year, String category, LifecycleOwner lifecycleOwner, Callback callback) {
        initializeMaxBudget(userId, month, year, category, lifecycleOwner, new MaxBudgetCallback() {
            @Override
            public void onMaxBudget(BigDecimal maxBudget) {
                getAllSpendings(userId, month, year, category, lifecycleOwner, new SpendingsCallback() {
                    @Override
                    public void onSpendingsSum(BigDecimal allSpendingsSum) {
                        callback.onComplete(allSpendingsSum, maxBudget);
                    }
                });
            }
        });
    }
    private void initializeMaxBudget(int userId, String month, int year, String category, LifecycleOwner lifecycleOwner, MaxBudgetCallback callback) {
        LiveData<BigDecimal> maxBudgetObservable = databaseRepository.getBudgetValue(userId, month, year, category);
        maxBudgetObservable.observe(lifecycleOwner, new Observer<BigDecimal>() {
            @Override
            public void onChanged(BigDecimal value) {
                maxBudgetObservable.removeObserver(this);
                callback.onMaxBudget(value);
            }
        });
    }

    private void getAllSpendings(int userId, String month, int year, String category, LifecycleOwner lifecycleOwner, SpendingsCallback callback) {
        databaseRepository.getAllSpendingsFromMonth(userId, month, year, category).observe(lifecycleOwner, new Observer<List<BigDecimal>>() {
            @Override
            public void onChanged(List<BigDecimal> values) {
                callback.onSpendingsSum(calculateSum(values));
            }
        });
    }

    private BigDecimal calculateSum(List<BigDecimal> values) {
        BigDecimal allSpendingsSum = BigDecimal.ZERO;
        for (BigDecimal number : values) {
            allSpendingsSum = allSpendingsSum.add(number);
        }
        return allSpendingsSum;
    }
}
