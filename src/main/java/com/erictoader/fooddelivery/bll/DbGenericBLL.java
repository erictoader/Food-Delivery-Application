package com.erictoader.fooddelivery.bll;

import com.erictoader.fooddelivery.dao.GenericDAO;

import java.util.List;

public class DbGenericBLL<T> {
    private final GenericDAO<T> dao;

    public DbGenericBLL(Class<T> classType) {
        this.dao = new GenericDAO<>(classType);
    }

    /*
     *   Business logic method to get all entries in a table
     *   @return A list of all table entries
     */
    public List<T> findAll() {
        return dao.findAll();
    }

    /*
     *   Business logic method to insert a given object into a table
     */
    public void insert(T t) {
        dao.insert(t);
    }

    /*
     *   Business logic method to update the values of an entry
     */
    public void update(T t) {
        dao.update(t);
    }

    /*
     *   Business logic method to delete a table entry
     */
    public void delete(int id) {
        dao.delete(id);
    }
}
