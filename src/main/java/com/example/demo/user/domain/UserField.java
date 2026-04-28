package com.example.demo.user.domain;

public enum UserField {
    EMAIL("email"),
    USERNAME("username"),
    PHONE("phone");


    private final String value;
    UserField(String value) { this.value = value; }
    public String value() { return value; }
}
