package com.erictoader.fooddelivery.bll.validators;

import com.erictoader.fooddelivery.bll.validators.customExceptions.PasswordTooShortException;
import com.erictoader.fooddelivery.bll.validators.customExceptions.PasswordTooSimpleException;
import com.erictoader.fooddelivery.model.Users;

public class UserPasswordValidator implements Validator<Users> {

    @Override
    public void validate(Users users) throws PasswordTooShortException, PasswordTooSimpleException {
        if(users.getPass().length() < 8) {
            throw new PasswordTooShortException("Password needs to be 8 or more characters long");
        }
        if(!users.getPass().contains("0") &&
                !users.getPass().contains("1") &&
                !users.getPass().contains("2") &&
                !users.getPass().contains("3") &&
                !users.getPass().contains("4") &&
                !users.getPass().contains("5") &&
                !users.getPass().contains("6") &&
                !users.getPass().contains("7") &&
                !users.getPass().contains("8") &&
                !users.getPass().contains("9")) {
            throw new PasswordTooSimpleException("Password needs to have at least one number in it");
        }
    }
}
