package com.erictoader.fooddelivery.bll.validators.customExceptions;

public class PasswordTooShortException extends Exception{
    public PasswordTooShortException(String message) {
        super(message);
    }
}
