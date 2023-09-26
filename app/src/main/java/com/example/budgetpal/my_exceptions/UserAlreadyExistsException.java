package com.example.budgetpal.my_exceptions;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException()
    {
        super("User already exists.");
    }
}
