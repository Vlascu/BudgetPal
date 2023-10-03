package com.example.budgetpal.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.budgetpal.model.DatabaseRepository;
import com.example.budgetpal.model.tables.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class RegisterViewModel extends AndroidViewModel{

    final private DatabaseRepository databaseRepository;

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        databaseRepository = new DatabaseRepository(application);
    }

    public void insertUser(User user) throws NoSuchAlgorithmException {

        user.setPassword(passwordHashing(user.getPassword()));
        databaseRepository.insertUser(user);
    }

    public LiveData<User> getUser(String email, String password) {
        try {
            return databaseRepository.getUser(email, passwordHashing(password));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String passwordHashing(String password) throws NoSuchAlgorithmException {
        MessageDigest digester = MessageDigest.getInstance("SHA-256");

        byte[] passwordBytes = password.getBytes();
        byte[] hashedBytes = digester.digest(passwordBytes);

        return Base64.getEncoder().encodeToString(hashedBytes);
    }

    public void emptyUserTable() {
        databaseRepository.deleteUsersWithEmptyCredentials();
    }
}
