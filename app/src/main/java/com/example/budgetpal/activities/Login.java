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
import com.example.budgetpal.view_models.LoginViewModel;

public class Login extends AppCompatActivity {

    private Button loginButton, goToRegister, showPassword;
    private EditText userEmail, userPassword;

    private LoginViewModel loginViewModel;

    private boolean isPasswordVisible=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Register.class);
                startActivity(i);
                finish();
            }
        });
        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPasswordVisible = !isPasswordVisible;
                int inputType;
                if(isPasswordVisible)
                    inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
                else
                    inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD;
                userPassword.setInputType(InputType.TYPE_CLASS_TEXT | inputType);
                userPassword.setSelection(userPassword.getText().length());
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userEmail.getText().length()==0|| userPassword.getText().length()==0)
                    Toast.makeText(Login.this, "Insert all data!", Toast.LENGTH_SHORT).show();
                else
                {
                    LiveData<User> checkedUser = loginViewModel.getUser(userEmail.getText().toString(), userPassword.getText().toString());
                    checkedUser.observe(Login.this, new Observer<User>() {
                        @Override
                        public void onChanged(User user) {
                            checkedUser.removeObserver(this);
                            if(user!=null)
                            {
                                saveUserId(user.getID());
                                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                            else
                                Toast.makeText(Login.this, "User doesn't exist", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private void findViews()
    {
        loginButton = findViewById(R.id.login_loginButton);
        goToRegister = findViewById(R.id.login_register_button);
        showPassword = findViewById(R.id.login_show_password);
        userEmail = findViewById(R.id.emailLogin);
        userPassword =findViewById(R.id.passwordLogin);
    }
    private void saveUserId(int id) {
        SharedPreferences sharedPreferences = getSharedPreferences(Register.SHARED_PREF, MODE_PRIVATE);
        sharedPreferences.edit().putInt(Register.USER_ID, id).apply();
    }
}