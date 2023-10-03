package com.example.budgetpal.model;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.budgetpal.model.daos.RevenueDAO;
import com.example.budgetpal.model.daos.SpendingsDAO;
import com.example.budgetpal.model.daos.UserDAO;
import com.example.budgetpal.model.return_models.MonthYear;
import com.example.budgetpal.model.daos.BudgetDAO;
import com.example.budgetpal.model.daos.DatesDAO;
import com.example.budgetpal.model.tables.BudgetTable;
import com.example.budgetpal.model.tables.Dates;
import com.example.budgetpal.model.tables.Revenue;
import com.example.budgetpal.model.tables.SpendingsTable;
import com.example.budgetpal.model.tables.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseRepository {

    final private BudgetDAO budgetDAO;
    final private RevenueDAO revenueDAO;
    final private SpendingsDAO spendingsDAO;
    final private UserDAO userDAO;

    final private DatesDAO datesDAO;

    public DatabaseRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        budgetDAO = appDatabase.budgetDAO();
        revenueDAO = appDatabase.revenueDAO();
        spendingsDAO = appDatabase.spendingsDAO();
        userDAO = appDatabase.userDAO();
        datesDAO = appDatabase.datesDAO();
    }

    public void insertBudget(BudgetTable budgetTable) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                budgetDAO.insert(budgetTable);
            }
        });

    }

    public void updateBudget(BudgetTable budgetTable) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                budgetDAO.update(budgetTable);
            }
        });
    }

    public void deleteBudget(BudgetTable budgetTable) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                budgetDAO.delete(budgetTable);
            }
        });
    }

    public LiveData<List<MonthYear>> getBudgetDates(int user_id) {
        return budgetDAO.getAllDates(user_id);
    }

    public LiveData<List<BudgetTable>> getAllBudgetsFromDate(int user_id, String month, int year) {
        return budgetDAO.getAllBudgetsFromDate(user_id, month, year);
    }

    public void insertRevenue(Revenue revenue) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                revenueDAO.insert(revenue);
            }
        });

    }

    public void updateRevenue(Revenue revenue) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                revenueDAO.update(revenue);
            }
        });
    }

    public void deleteRevenue(Revenue revenue) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                revenueDAO.delete(revenue);
            }
        });
    }

    public LiveData<List<Revenue>> getAllRevenuesFromUser(int user_id) {
        return revenueDAO.getAllRevenuesFromUser(user_id);
    }

    public LiveData<List<Revenue>> getAllRevenueFromDate(int user_id, String month, int year) {
        return revenueDAO.getAllRevenuesFromDate(user_id, month, year);
    }

    public LiveData<List<MonthYear>> getAllRevenueDates(int user_id) {
        return revenueDAO.getAllDates(user_id);
    }


    public void insertSpending(SpendingsTable spendingsTable) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                spendingsDAO.insert(spendingsTable);
            }
        });
    }

    public void updateSpending(SpendingsTable spendingsTable) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                spendingsDAO.update(spendingsTable);
            }
        });
    }


    public LiveData<List<SpendingsTable>> getAllSpendingsFromDate(int user_id, String month, int day, int year) {
        return spendingsDAO.getAllSpendingsFromDate(user_id, month, day, year);
    }

    public void insertUser(User user) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                userDAO.insert(user);
            }
        });
    }

    public void updateUser(User user) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                userDAO.update(user);
            }
        });
    }

    public LiveData<User> getUser(String email, String password) {
        return userDAO.getUser(email, password);
    }

    public void deleteUsersWithEmptyCredentials() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                userDAO.deleteUsersWithEmptyCredentials();
            }
        });
    }

    public void deleteUser(int id) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                userDAO.deleteUser(id);
            }
        });
    }

    public void deleteRevenueByDate(int user_id, int year, String month, String account_name) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                revenueDAO.removeRevenue(user_id, month, year, account_name);
            }
        });
    }

    public LiveData<BigDecimal> getTotalMoneyFromDB(int user_id) {
        return userDAO.getTotalMoneyFromDB(user_id);
    }

    public void updateTotalMoney(BigDecimal value, int user_id) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                userDAO.updateTotalMoney(value, user_id);
            }
        });
    }

    public LiveData<Revenue> getTopAccount(int user_id, String month, int year) {
        return revenueDAO.getTopAccount(user_id, month, year);
    }

    public void insertNewDate(Dates dates) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                datesDAO.insert(dates);
            }
        });
    }

    public LiveData<String> doesDateExist(int user_id, String monthYear) {
        return datesDAO.doesDateExist(user_id, monthYear);
    }

    public LiveData<List<String>> getAllDatesFromAnUser(int user_id) {
        return datesDAO.getAllDatesFromAnUser(user_id);
    }

    public LiveData<List<BigDecimal>> getAllValuesBasedOnCategory(int user_id, String category) {
        return spendingsDAO.getAllValuesBasedOnCategory(user_id, category);
    }

    public void deleteSpending(int user_id, int day, String month, int year, String product_name) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                spendingsDAO.deleteSpending(user_id, day, month, year, product_name);
            }
        });
    }
    public LiveData<List<BigDecimal>> getAllSpendingsFromMonth(int user_id, String month, int year)
    {
        return spendingsDAO.getAllSpendingFromMonth(user_id,month,year);
    }
    public LiveData<List<BigDecimal>> getAllRevenuesValuesByMonth(int user_id, String month, int year)
    {
        return revenueDAO.getAllRevenuesValuesByMonth(user_id,month,year);
    }
}
