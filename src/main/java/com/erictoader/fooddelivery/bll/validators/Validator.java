package com.erictoader.fooddelivery.bll.validators;

public interface Validator<T> {
    void validate(T t) throws Exception;
}
