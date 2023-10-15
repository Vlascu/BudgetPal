package com.example.budgetpal.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.budgetpal.R;
import com.example.budgetpal.model.tables.User;
import com.example.budgetpal.view_models.RegisterViewModel;

import java.security.NoSuchAlgorithmException;

public class Register extends AppCompatActivity {

    private Button loginButton, registerButton, showPassword;
    private EditText userEmail, userPassword;
    private RegisterViewModel registerViewModel;

    private boolean isPasswordVisible = false;

    public static final String SHARED_PREF = "sharedPref";

    public static final String USER_ID = "user_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        viewsFinding();

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userEmail.getText().length() == 0 || userPassword.getText().length() == 0)
                    Toast.makeText(Register.this, "Please fill up all the information!", Toast.LENGTH_LONG).show();
                else {

                    LiveData<User> existingUserLiveData = registerViewModel.getUser(userEmail.getText().toString(), userPassword.getText().toString());
                    existingUserLiveData.observe(Register.this, new Observer<User>() {
                        @Override
                        public void onChanged(User existingUser) {
                            existingUserLiveData.removeObserver(this);

                            if (existingUser == null) {
                                try {

                                    registerViewModel.insertUser(new User(userEmail.getText().toString(), userPassword.getText().toString()));
                                    LiveData<User> getIdFromUser = registerViewModel.getUser(userEmail.getText().toString(), userPassword.getText().toString());
                                    getIdFromUser.observe(Register.this, new Observer<User>() {
                                        @Override
                                        public void onChanged(User user) {
                                            if (user != null) {
                                                getIdFromUser.removeObserver(this);
                                                saveUserId(user.getID());
                                            }
                                        }
                                    });
                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(i);
                                    finish();
                                } catch (NoSuchAlgorithmException e) {
                                    throw new RuntimeException(e);
                                }
                            } else
                                Toast.makeText(Register.this, "User already exists!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPasswordVisible = !isPasswordVisible;
                int inputType;
                if (isPasswordVisible)
                    inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
                else
                    inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD;
                userPassword.setInputType(InputType.TYPE_CLASS_TEXT | inputType);
                userPassword.setSelection(userPassword.getText().length());
            }
        });

    }

    private void viewsFinding() {
        loginButton = findViewById(R.id.register_login_button);
        registerButton = findViewById(R.id.register_registerButton);
        userEmail = findViewById(R.id.emailRegister);
        userPassword = findViewById(R.id.passwordRegister);
        showPassword = findViewById(R.id.register_show_password);
    }

    private void saveUserId(int id) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        sharedPreferences.edit().putInt(USER_ID, id).apply();
    }
}