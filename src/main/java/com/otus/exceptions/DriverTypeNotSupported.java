package com.otus.exceptions;

public class DriverTypeNotSupported extends Exception {

    public DriverTypeNotSupported(String browserType) {
        super(String.format("Browser %s not supported", browserType));
    }
}
