package com.example.budgetpal.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.budgetpal.model.daos.RevenueDAO;
import com.example.budgetpal.model.daos.SpendingsDAO;
import com.example.budgetpal.model.daos.UserDAO;
import com.example.budgetpal.model.daos.BudgetDAO;
import com.example.budgetpal.model.daos.DatesDAO;
import com.example.budgetpal.model.tables.BudgetTable;
import com.example.budgetpal.model.tables.Dates;
import com.example.budgetpal.model.tables.Revenue;
import com.example.budgetpal.model.tables.SpendingsTable;
import com.example.budgetpal.model.tables.User;

@Database(entities = {BudgetTable.class, Revenue.class, SpendingsTable.class, User.class, Dates.class},version = 4)
@TypeConverters(BigDecimalDoubleTypeConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    public abstract BudgetDAO budgetDAO();
    public abstract RevenueDAO revenueDAO();
    public abstract SpendingsDAO spendingsDAO();
    public abstract UserDAO userDAO();

    public abstract DatesDAO datesDAO();

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context)
    {
        if(instance==null)
        {
            instance= Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,"app_database")
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
