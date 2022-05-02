package com.erictoader.fooddelivery.bll.validators.customExceptions;

public class PasswordTooSimpleException extends Exception{
    public PasswordTooSimpleException(String message) {
        super(message);
    }
}
