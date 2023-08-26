package com.example.demo.utils;

public class UserNotFoundException extends DataNotFoundException {
    public UserNotFoundException() {
        super("User not found");
    }
}