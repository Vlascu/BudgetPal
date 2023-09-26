package com.example.budgetpal.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.budgetpal.model.DatabaseRepository;
import com.example.budgetpal.model.tables.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class LoginViewModel extends AndroidViewModel {

    private final DatabaseRepository databaseRepository;


    public LoginViewModel(@NonNull Application application) {
        super(application);
        databaseRepository = new DatabaseRepository(application);
    }
    public LiveData<User> getUser(String email, String password)
    {
        try
        {
           return databaseRepository.getUser(email, passwordHashing(password));
        }catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
    }
    public String passwordHashing(String password) throws NoSuchAlgorithmException {
        MessageDigest digester = MessageDigest.getInstance("SHA-256");

        byte[] passwordBytes = password.getBytes();
        byte[] hashedBytes = digester.digest(passwordBytes);

        return Base64.getEncoder().encodeToString(hashedBytes);
    }
}
